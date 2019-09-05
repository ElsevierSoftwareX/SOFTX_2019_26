package br.com.sdum.controller;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.RequestInfo;
import br.com.caelum.vraptor.view.Results;
import br.com.sdum.annotation.Permissao;
import br.com.sdum.controller.base.WebConfig;
import br.com.sdum.dao.AmostraDAO;
import br.com.sdum.dao.AreaDAO;
import br.com.sdum.dao.ClassificacaoDAO;
import br.com.sdum.dao.MapaDAO;
import br.com.sdum.dao.PixelAmostraDAO;
import br.com.sdum.dao.ProjetoDAO;
import br.com.sdum.dao.UsuarioDAO;
import br.com.sdum.enums.Roles;
import br.com.sdum.exception.PPATException;
import br.com.sdum.model.Amostra;
import br.com.sdum.model.Area;
import br.com.sdum.model.Classificacao;
import br.com.sdum.model.Mapa;
import br.com.sdum.model.MensagemRetorno;
import br.com.sdum.model.PixelAmostra;
import br.com.sdum.model.PixelMapa;
import br.com.sdum.model.Projeto;
import br.com.sdum.model.Usuario;
import br.com.sdum.session.UsuarioSession;
import br.com.sdum.ws.client.GeoestatisticaClient;
import br.com.sdum.ws.client.InterpolaClient;
import br.com.sdum.ws.client.PPATWSClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.ClientResponse;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.json.JSONObject;

@Resource
@Path("interpolacao")
public class InterpolacaoController extends WebConfig implements Serializable {

    private static final long serialVersionUID = -1747435946222314539L;

    @Inject
    private UsuarioSession usuarioSession;

    @Inject
    private Result result;

    @Inject
    private ProjetoDAO projetoDAO;

    @Inject
    private PixelAmostraDAO pixelAmostraDAO;

    @Inject
    private AreaDAO areaDAO;

    @Inject
    private AmostraDAO amostraDAO;

    @Inject
    private MapaDAO mapaDAO;

    @Inject
    private UsuarioDAO usuarioDAO;

    @Inject
    private ClassificacaoDAO classificacaoDAO;

    @Inject
    private PPATWSClient client;

    @Inject
    private InterpolaClient clientInterpolador;

    @Inject
    private GeoestatisticaClient geoestatisticaClient;

    private Gson gson, gsonArea, gsonAmostra, gsonMapa, gsonPixelMapa;

    private static int projection = 4326;

    public InterpolacaoController(Result result, RequestInfo request) {
        super(result, request);
    }

    @Get("lista")
    @Permissao(Roles.ON)
    public void lista(Long idProjeto) {
        Projeto projetoaux = projetoDAO.buscarPorId(idProjeto);
        Usuario usuario = usuarioDAO.buscarPorId(projetoaux.getUsuario().getId());
        if (Objects.equals(usuario.getId(), usuarioSession.getUsuario().getId())) {
            /*try {

            ClientResponse response = client.getResource("mapas", usuarioSession.getUsuario().getToken());
            String json = response.getEntity(String.class);

            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            List<Mapa> mapa = new ArrayList<>();

            switch (response.getStatus()) {
                case 200:
                    mapa = (List<Mapa>) gson.fromJson(json, new TypeToken<ArrayList<Mapa>>() {
                    }.getType());
                    break;
                case 404:
                    mapa = new ArrayList<>();
                    break;
                default:
                    MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                    throw new PPATException(mensagem.getMessage());
            }

            ClientResponse responseArea = client.getResource("area", usuarioSession.getUsuario().getToken());
            String jsonArea = responseArea.getEntity(String.class);
            gsonArea = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

            List<Area> area = new ArrayList<>();

            switch (responseArea.getStatus()) {
                case 200:
                    area = (List<Area>) gson.fromJson(json, new TypeToken<ArrayList<Area>>() {
                    }.getType());
                    for (int i = 0; i < area.size(); i++) {
                        Integer idProjeto;
                        ClientResponse responseAmostra = client.getResource("amostra", usuarioSession.getUsuario().getToken());
                        String jsonAmostra = responseAmostra.getEntity(String.class);
                        gsonAmostra = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                        List<Amostra> todasAmostras = new ArrayList<>();
                        List<Amostra> amostrasDaArea = new ArrayList<>();

                        switch (responseAmostra.getStatus()) {
                            case 200:
                                todasAmostras = (List<Amostra>) gsonAmostra.fromJson(jsonAmostra, new TypeToken<ArrayList<Amostra>>() {
                                }.getType());
                                for (int j = 0; j < todasAmostras.size(); j++) {
                                    if (todasAmostras.get(j).getArea().getCodigo().equals(area.get(i).getCodigo())) {
                                        amostrasDaArea.add(todasAmostras.get(j));
                                    }
                                }
                                break;
                            case 404:
                                todasAmostras = new ArrayList<>();
                                break;
                            default:
                                MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                                throw new PPATException(mensagem.getMessage());
                        }
                    }
                    break;

                case 404:
                    area = new ArrayList<>();
                    break;
                default:
                    MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                    throw new PPATException(mensagem.getMessage());
            }

            result.include("interpolacao", mapa);
            //result.include("interpolacao", mapaDAO.listaMapaByUsuarioAll(usuarioSession.getUsuario()));

        } catch (PPATException ex) {

            result.include("interpolacao", new ArrayList());
            result.include("mensagem", ex.getMessage());
        }
             */
            System.out.println("ID PROJETO NO CONTROLLER INTERPOLAÇÃO" + idProjeto);
            List<Mapa> mapasprojeto = mapaDAO.listaMapaByProjeto(idProjeto);
            System.out.println("MAPAS PROJETO " + mapasprojeto.size());
            List<Mapa> mapasIDWeMM = new ArrayList<>();
            List<Mapa> mapasKRI = new ArrayList<>();
            //List<Mapa> todosMapas = new ArrayList<>();
            // todosMapas = mapaDAO.listaMapaByUsuarioAll(usuarioSession.getUsuario());
            for (int i = 0; i < mapasprojeto.size(); i++) {
                if (mapasprojeto.get(i).getTipoInterpolador().equals("KRI")) {
                    mapasKRI.add(mapasprojeto.get(i));
                } else {
                    mapasIDWeMM.add(mapasprojeto.get(i));
                }
            }
            result.include("interpolacaoidw", mapasIDWeMM).include("interpolacaokri", mapasKRI).include("interpolacao", mapaDAO.listaMapaByUsuarioAll(usuarioSession.getUsuario()))
                    .include("idProjeto", idProjeto);
        } else {
            result.redirectTo(ProjetoController.class).lista();

        }

    }

    @Path("exportartxt")
    @Permissao(Roles.ON)
    public void exportartxt(Long id, HttpServletResponse response) {
        Mapa mapa = mapaDAO.buscarPorId(id);
        List<PixelMapa> px = mapa.getPixelMapa();
        try {
            String send = "Long\tLat\t" + mapa.getDescricao() + System.getProperty("line.separator");
            List<String> centroid = (mapaDAO.CentroidPixelMapa(mapa.getId()));
            for (int k = 0; k < centroid.size(); k++) {
                String pixelmapa = centroid.get(k).replace("POINT(", "");
                pixelmapa = pixelmapa.replace(")", "");
                pixelmapa = pixelmapa.replace("[", "");
                pixelmapa = pixelmapa.replace("]", "");
                String arraypa[] = pixelmapa.split(" ");
                System.out.println("Ponto: " + k + " x: " + arraypa[0] + " y: " + arraypa[1]);
                String pa = arraypa[0] + "\t" + arraypa[1] + "\t" + px.get(k).getValor();
                send += pa + System.getProperty("line.separator");
            }
            byte[] report = send.getBytes();
            response.setContentType("application/save");
            response.setContentLength(report.length);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + mapa.getDescricao() + ".txt\"");
            response.addHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(report, 0, report.length);
            outputStream.flush();
            outputStream.close();
            result.include("idProjeto", mapa.getArea().getProjeto().getId()).redirectTo(this).lista(mapa.getArea().getProjeto().getId());
        } catch (Exception ex) {
            System.out.println("Exception" + ex);
        }
    }

    @Path("formulario")
    @Permissao(Roles.ON)
    public void formulario(Long idProjeto) {

        List<Area> areasprojeto = areaDAO.listaAreaByProjeto(idProjeto);
        List<Amostra> amostrasprojeto = amostraDAO.listaAmostraByProjeto(idProjeto);
        result.include("areasprojeto", areasprojeto).include("amostrasprojeto", amostrasprojeto).include("idProjeto", idProjeto);
        //result.include("projetos", projetoDAO.listaProjetoByUsuarioAll(usuarioSession.getUsuario()));
    }

    @Get("lista/area")
    @Permissao(Roles.ON)
    public void projetoArea(Long id) {
        List<Area> areas = areaDAO.listaAreaByProjeto(id);
        result.use(Results.json()).withoutRoot().from(areas).serialize();
    }

    @Get("lista/amostra")
    @Permissao(Roles.ON)
    public void areaAmostra(Long id) {
        List<Amostra> amostras = amostraDAO.listaAmostraByArea(id);
        result.use(Results.json()).withoutRoot().from(amostras).serialize();
    }

    @Get("lista/classificacao")
    @Permissao(Roles.ON)
    public void projetoClassificador(Long idArea, Long idAmostra) {
        List<Classificacao> classificadores = classificacaoDAO.listaProjetoByClassificador(idArea, idAmostra);
        System.out.println("-------------------------------" + idArea.toString() + idAmostra.toString());
        result.use(Results.json()).withoutRoot().from(classificadores).serialize();
    }

    @Get("imagem")
    @Permissao(Roles.ON)
    public File imgSemivariograma(String imagem) {
        HttpServletRequest request = null;
        String caminho = request.getServletContext().getRealPath("/file/" + usuarioSession.getUsuario().getId().toString()); //"C:\\projetos\\sdum-web\\src\\main\\webapp\\resources\\image\\" + imagem;
        if (!caminho.contains("jpg")) {
            return null;
        } else {
            File arquivo = new File(caminho);
            return arquivo;
        }
    }

    @Post("persiste")
    @Permissao(Roles.ON)
    public void cadastrarEditar(Mapa mapa, Long idProjeto) throws IOException {
        try {
            System.out.println("MAPA " + mapa.toString());
            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
            WKTReader reader = new WKTReader(geometryFactory);
            Area areabanco = areaDAO.buscarPorId(mapa.getArea().getId());
            Amostra amostrabanco = amostraDAO.buscaPorIdAmostra(mapa.getAmostra().getId());
            //Classificacao classificacaobanco = classificacaoDAO.buscaPorCodigo(mapa.getClassificacao().getCodigo());
            GeometryJSON g = new GeometryJSON();
            String interpola = null;
            List<PixelAmostra> pixelsamostra = new ArrayList<>();
            List<PixelMapa> pixelsMapa = new ArrayList<>();
            ClientResponse responseInterpola = null;
            pixelsamostra = pixelAmostraDAO.buscaPorIdAmostra(amostrabanco.getId());
            String resultadoConversaoArea = mapaDAO.ConversaoGrausUTMArea(areabanco.getId());
            List<String> resultadoConversaoAmostra = mapaDAO.ConversaoGrausUTMPixelAmostra(amostrabanco.getId());
            resultadoConversaoArea = resultadoConversaoArea.replace("[POLYGON((", "");
            resultadoConversaoArea = resultadoConversaoArea.replace("))]", "");
            resultadoConversaoArea = resultadoConversaoArea.replace(",", " ");
            String[] coord = resultadoConversaoArea.split(" ");
            String contour = "";
            String todossemivariogramas = null;
            InputStream imageMelhorSemivariograma = null;
            InputStream imageTodosSemivariogramas = null;
            String descricaoamostra = amostrabanco.getDescricao().replace("-", "_");
            URL url = null;
            URL urltodos = null;
            for (int x = 0; x < coord.length; x += 2) {
                if (x != coord.length - 2) {
                    contour += "{ \n \"x\": " + coord[x] + ", \n \"y\": " + coord[x + 1] + " \n }, \n";
                } else {
                    contour += "{ \n \"x\": " + coord[x] + ", \n \"y\": " + coord[x + 1] + " \n } \n ] \n }";
                }
            }

            if ("IDP".equals(mapa.getTipoInterpolador())) {
                interpola = "{ \n \"idw\":{ \n \"exponenent\": " + mapa.getExpoente() + ", \n \"neighbors\": " + mapa.getNumeroPontos() + ", \n \"sizePixelX\": " + mapa.getTamanhoX() + ", \n \"sizePixelX\": " + mapa.getTamanhoY() + ", \n \"samples\": [ \n";

                for (int i = 0; i < resultadoConversaoAmostra.size(); i++) {
                    interpola += "{ \n \"coordinate\": { \n  \"x\": ";
                    String geom = resultadoConversaoAmostra.get(i);
                    geom = geom.replace("POINT(", "");
                    geom = geom.replace(")", "");
                    geom = geom.replace(" ", ",\n \"y\": ");
                    if (i == pixelsamostra.size() - 1) {
                        interpola += geom + " \n }, \n \"data\": " + pixelsamostra.get(i).getValor() + " \n } \n";
                    } else {
                        interpola += geom + " \n }, \n \"data\": " + pixelsamostra.get(i).getValor() + " \n }, \n";
                    }
                }
                interpola += "], \n \"contour\": [ \n" + contour + "\n}";
                System.out.println("DADOS " + interpola);
                gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                //JsonObject bodyPost = new JsonObject();
                responseInterpola = clientInterpolador.postResource("idw", interpola);
                String json = responseInterpola.getEntity(String.class
                );

                if (responseInterpola.getStatus() != 200) {
                    //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                    //System.out.println("mensagem de erro = " + mensagem.toString());
                    System.out.println("mensagem de erro = " + responseInterpola.getStatus());
                    result.include("danger", "danger.cadastrar.interpolacao").include("idProjeto", idProjeto).redirectTo(this).formulario(idProjeto);
                    //throw new PPATException(mensagem.getMessage());
                } else {
                    Usuario user = new Usuario();
                    Area area = new Area();
                    Amostra amostra = new Amostra();
                    area.setCodigo(areabanco.getCodigo());
                    amostra.setCodigo(amostrabanco.getCodigo());
                    user.setCodigo(usuarioSession.getUsuario().getCodigo());
                    mapa.setTipoGeometria("POLYGON");
                    mapa.setCompPrincipal("s");
                    mapa.setArea(area);
                    mapa.setAmostra(amostra);
                    mapa.setUsuario(usuarioSession.getUsuario());
                    mapa.setAmostra(amostrabanco);
                    mapa.setArea(areabanco);
                    mapa.setData(new Date());
                    mapa.setDataCriacao(new Date());
                    mapaDAO.adicionar(mapa);
                    JSONObject bodyPost = new JSONObject(json);
                    org.json.JSONArray arraypoints = bodyPost.getJSONArray("points");
                    String arrayconverterx = "";
                    String arrayconvertey = "";
                    List<Double> objdata = new ArrayList<>();
                    String pixvalor = "";
                    System.out.println("QUANTIDADE PONTOS IDP: " + arraypoints.length());

                    for (int i = 0; i < arraypoints.length(); i++) {
                        //recupera coordenada de índice "i" no array 
                        JSONObject objpoint = arraypoints.getJSONObject(i);
                        JSONObject objcoordinate = objpoint.getJSONObject("coordinate");
                        Double objx = objcoordinate.getDouble("x");
                        Double objy = objcoordinate.getDouble("y");
                        Double data = objpoint.getDouble("data");
                        //objdata.add(objpoint.getDouble("data"));
                        

                        if (i != (arraypoints.length() - 1)) {
                            pixvalor +=  data.toString() + ",";
                            arrayconverterx += objx.toString() + ",";
                            arrayconverterx += objy.toString() + ",";
                        } else {
                            pixvalor +=  data.toString();
                            arrayconverterx += objx.toString()+ ",";
                            arrayconverterx += objy.toString();
                        }

                    }
                    mapaDAO.gerarPixelsInterpolacaoR(mapa, arrayconverterx, pixvalor);
                    // String resultadoUTMGraus = mapaDAO.ConversaoUTMGrausPixelMapa(arrayconverterx, arrayconvertey);
                    //resultadoUTMGraus = resultadoUTMGraus.replace("{\"", "");
                    //resultadoUTMGraus = resultadoUTMGraus.replace("[", "");
                    // resultadoUTMGraus = resultadoUTMGraus.replace("]", "");
                    //resultadoUTMGraus = resultadoUTMGraus.replace("\"}", "");
                    //  String[] pontosEmGraus = resultadoUTMGraus.split(",");
                    // for (int j = 0; j < pontosEmGraus.length; j++) {
                    //    PixelMapa pixelMapa = new PixelMapa();
                    //    try {
                    //         pixelMapa.setGeometry(reader.read(pontosEmGraus[j]));
                    //         pixelMapa.setGeom(resultadoUTMGraus);
                    //         pixelMapa.setValor(Float.valueOf(objdata.get(j).toString()));
                    //         pixelMapa.getGeometry().setSRID(projection);

                    //     } catch (ParseException ex) {
                    //         Logger.getLogger(InterpolacaoController.class
                    //                 .getName()).log(Level.SEVERE, null, ex);
                    //    }
                    //     pixelsMapa.add(pixelMapa);
                    // }
                    result.include("success", "success.cadastrar.interpolacao").include("idProjeto", idProjeto).redirectTo(this).lista(mapa.getArea().getProjeto().getId());
                }
            } else {
                if ("KRI".equals(mapa.getTipoInterpolador())) {
                    String isi = "{ \"autoLags\": true,  \"amountLags\": 11,  \"estimator\": \"classical\",  \"cutOff\": 50,  \"pairs\": 30,  \"amountRangeIntervals\": 5, \"amountContributionIntervals\": 5, \"attributeName\": \"" + descricaoamostra.replace(" ", "") + "\",  \"generateGraphics\": true,  ";
                    String samples = "\"dataAsSampleCollection\": [";
                    String samples2 = "\"samples\": [";
                    for (int i = 0; i < resultadoConversaoAmostra.size(); i++) {
                        samples += "{ \n \"coordinates\": [ ";
                        samples2 += "{ \n \"coordinate\": { \n  \"x\": ";
                        String geom = resultadoConversaoAmostra.get(i);
                        geom = geom.replace("POINT(", "");
                        geom = geom.replace(")", "");
                        geom = geom.replace(" ", ",");
                        String geom2 = resultadoConversaoAmostra.get(i);
                        geom2 = geom2.replace("POINT(", "");
                        geom2 = geom2.replace(")", "");
                        geom2 = geom2.replace(" ", ",\n \"y\": ");
                        if (i == pixelsamostra.size() - 1) {
                            samples2 += geom2 + " \n }, \n \"data\": " + pixelsamostra.get(i).getValor() + " \n } \n";
                            samples += geom + "], \n \"data\": " + pixelsamostra.get(i).getValor() + " \n } \n";
                        } else {
                            samples2 += geom2 + " \n }, \n \"data\": " + pixelsamostra.get(i).getValor() + " \n }, \n";
                            samples += geom + "], \n \"data\": " + pixelsamostra.get(i).getValor() + " \n }, \n";
                        }
                    }
                    samples += "] \n";
                    samples2 += "],";
                    isi += samples + "}";
                    gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                    //JSONObject bodyPost = new JSONObject();
                    System.out.println("AMOSTRA***** " + amostrabanco.getDescricao().replace(" ", ""));
                    ClientResponse responseISI = geoestatisticaClient.postResource("isi", isi);
                    System.out.println("JSON PARA MANDAR PARA API ISI " + isi);
                    String json = responseISI.getEntity(String.class
                    );

                    if (responseISI.getStatus() != 200) {
                        //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                        //System.out.println("mensagem de erro = " + mensagem.toString());
                        System.out.println("mensagem de erro = " + responseISI.getStatus());
                        result.include("danger", "danger.cadastrar.interpolacao").include("idProjeto", idProjeto).redirectTo(this).formulario(idProjeto);
                        //throw new PPATException(mensagem.getMessage());
                    } else {
                        JSONObject analysis = new JSONObject(json);
                        System.out.println("RETORNO JSON API ISI " + json);
                        org.json.JSONArray arrayanalysis = analysis.getJSONArray("analysis");
                        org.json.JSONArray resourcesanalysis = analysis.getJSONArray("resources");
                        todossemivariogramas = resourcesanalysis.getString(1);
                        
                        //JSONObject objanalysis = analysis.getJSONObject("analysis");
                        // bodyPost = (JSONObject) gson.fromJson(json, new TypeToken<JSONObject>() {
                        // }.getType());
                        Double menorisi = null;
                        int posicaoMenorISI = 0;
                        for (int i = 0; i < arrayanalysis.length(); i++) {
                            //recupera filme de índice "i" no array 
                            JSONObject objanalysis = arrayanalysis.getJSONObject(i);
                            if (i == 0) {
                                menorisi = objanalysis.getDouble("isi");
                            } else {
                                Double objisi = objanalysis.getDouble("isi");
                                if (objisi < menorisi) {
                                    menorisi = objisi;
                                    posicaoMenorISI = i;
                                }
                            }
                        }
                        String model = arrayanalysis.getJSONObject(posicaoMenorISI).getString("model");
                        String method = arrayanalysis.getJSONObject(posicaoMenorISI).getString("method");
                        float contribution = arrayanalysis.getJSONObject(posicaoMenorISI).getFloat("contribution");
                        float range = arrayanalysis.getJSONObject(posicaoMenorISI).getFloat("range");
                        float kappa = arrayanalysis.getJSONObject(posicaoMenorISI).getFloat("kappa");
                        float ice = arrayanalysis.getJSONObject(posicaoMenorISI).getFloat("ice");
                        float isibanco = arrayanalysis.getJSONObject(posicaoMenorISI).getFloat("isi");
                        float meanError = arrayanalysis.getJSONObject(posicaoMenorISI).getFloat("meanError");
                        float standardDeviationdMeanError = arrayanalysis.getJSONObject(posicaoMenorISI).getFloat("standardDeviationdMeanError");
                        mapa.setModelo(model.toLowerCase());
                        mapa.setMetodo(method);
                        mapa.setContribuicao(contribution);
                        mapa.setAlcance(range);
                        mapa.setKappa(kappa);
                        mapa.setIce(ice);
                        mapa.setIsi(isibanco);
                        mapa.setMeanError(meanError);
                        mapa.setStandardDeviationdMeanError(standardDeviationdMeanError);
                        String jsonKrigagem = "{\n"
                                + "  \"variogram\": {\n"
                                + "    \"estimator\": \"classical\",\n"
                                + "    \"model\": \"" + model.toLowerCase() + "\",\n"
                                + "    \"method\": \"" + method + "\",\n"
                                + "    \"contribution\": " + contribution + ",\n"
                                + "    \"range\": " + range + ",\n"
                                + "    \"kappa\": " + kappa + ",\n"
                                + "    \"lambda\": 1,\n"
                                + "    \"autoLags\": true,\n"
                                + "    \"amountLags\": 11,\n"
                                + "    \"cutoff\": 50,\n"
                                + "    \"pairs\": 30\n"
                                + "  },"
                                + "\"sizePixelX\": " + mapa.getTamanhoX() + ","
                                + "\"sizePixelY\": " + mapa.getTamanhoY() + ","
                                + "\"attributeName\": \"" + descricaoamostra.replace(" ", "") + "\","
                                + "\"resultDataAsJson\": true,"
                                + samples2 + " \n\"contour\": ["
                                + contour;
                        gsonMapa = new GsonBuilder()
                                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                                .create();
                        System.out.println("DADOS " + jsonKrigagem);
                        responseInterpola = clientInterpolador.postResource("kriging", jsonKrigagem);
                        String jsonResposta = responseInterpola.getEntity(String.class
                        );

                        if (responseInterpola.getStatus() != 200) {
                            //MensagemRetorno mensagemMapa = gsonMapa.fromJson(jsonResposta, MensagemRetorno.class);
                            result.include("danger", "danger.cadastrar.interpolacao").include("idProjeto", idProjeto).redirectTo(this).formulario(idProjeto);
                            //throw new PPATException(mensagemMapa.getMessage());
                        } else {
                            JSONObject points = new JSONObject(jsonResposta);
                            org.json.JSONArray arraypoints = points.getJSONArray("points");
                            org.json.JSONArray arrayresources = points.getJSONArray("resources");
                            Usuario user = new Usuario();
                            Area area = new Area();
                            Amostra amostra = new Amostra();
                            area.setCodigo(areabanco.getCodigo());
                            amostra.setCodigo(amostrabanco.getCodigo());
                            user.setCodigo(usuarioSession.getUsuario().getCodigo());
                            mapa.setTipoGeometria("POLYGON");
                            mapa.setCompPrincipal("s");
                            mapa.setArea(area);
                            mapa.setAmostra(amostra);
                            mapa.setUsuario(usuarioSession.getUsuario());
                            mapa.setAmostra(amostrabanco);
                            mapa.setArea(areabanco);
                            mapa.setData(new Date());
                            mapa.setDataCriacao(new Date());
                            mapaDAO.adicionar(mapa);
                            Long mapaID = mapa.getId();
                            String melhorvariograma = arrayresources.getString(2);

                            try {
                                urltodos = new URL(todossemivariogramas);
                                url = new URL(melhorvariograma);

                            } catch (MalformedURLException ex) {
                                Logger.getLogger(InterpolacaoController.class
                                        .getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            if (mapa.getTipoInterpolador().equals("KRI")) {
                                InputStream is = url.openStream();

                                //File diretorio = new File("C:\\projetos\\sdum-web\\src\\main\\webapp\\file\\"+usuarioSession.getUsuario().getId().toString());
                                File diretorio = new File(this.dirPath + usuarioSession.getUsuario().getId());
                                this.validaCriaDiretorio(diretorio);
                                //System.out.println(diretorio + System.getProperty("file.separator") + "melhor" + mapaID + ".jpg");
                                //OutputStream os = new FileOutputStream("C:\\projetos\\sdum-web\\src\\semivariogramas\\melhor" + mapaID + ".jpg");
                                OutputStream os = new FileOutputStream(diretorio + System.getProperty("file.separator") + "melhor" + mapaID + ".jpg");
                                byte[] b = new byte[2048];
                                int length;
                                while ((length = is.read(b)) != -1) {
                                    os.write(b, 0, length);
                                }

                                is.close();
                                os.close();
                                //todos semivariogramas
                                InputStream istodos = urltodos.openStream();
                                OutputStream ostodos = new FileOutputStream(diretorio + System.getProperty("file.separator") + "todos" + mapaID + ".jpg");
                                byte[] btodos = new byte[2048];
                                int lengthtodos;
                                while ((lengthtodos = istodos.read(btodos)) != -1) {
                                    ostodos.write(btodos, 0, lengthtodos);
                                }

                                istodos.close();
                                ostodos.close();

                                mapa.setMelhorsemivariograma("melhor" + mapaID + ".jpg");
                                mapa.setTodossemivariogramas("todos" + mapaID + ".jpg");
                            }

                            String arrayconverterx = "";
                            String arrayconvertey = "";
                            List<Double> objdata = new ArrayList<>();
                            String pixvalor = "";
                            System.out.println("QUANTIDADE PONTOS KRIGAGEM: " + arraypoints.length());
                            for (int i = 0; i < arraypoints.length(); i++) {
                                //recupera coordenada de índice "i" no array 
                                JSONObject objpoint = arraypoints.getJSONObject(i);
                                JSONObject objcoordinate = objpoint.getJSONObject("coordinate");
                                Double objx = objcoordinate.getDouble("x");
                                Double objy = objcoordinate.getDouble("y");
                                Double data = objpoint.getDouble("data");
                                //objdata.add(objpoint.getDouble("data"));

                                if (i != (arraypoints.length() - 1)) {
                                    pixvalor += data.toString() + ",";
                                    arrayconverterx += objx.toString() + ",";
                                    arrayconverterx += objy.toString() + ",";
                                } else {
                                    pixvalor += data.toString();
                                    arrayconverterx += objx.toString() + ",";
                                    arrayconverterx += objy.toString();
                                }
                            }
                            mapaDAO.gerarPixelsInterpolacaoR(mapa, arrayconverterx, pixvalor);
                            //String resultadoUTMGraus = mapaDAO.ConversaoUTMGrausPixelMapa(arrayconverterx, arrayconvertey);
                            //resultadoUTMGraus = resultadoUTMGraus.replace("[", "");
                            //resultadoUTMGraus = resultadoUTMGraus.replace("]", "");
                            //String[] pontosEmGraus = resultadoUTMGraus.split(",");
                            //for (int j = 0; j < pontosEmGraus.length; j++) {
                            //  PixelMapa pixelMapa = new PixelMapa();
                            //try {
                            //  pixelMapa.setGeometry(reader.read(pontosEmGraus[j]));
                            // pixelMapa.setGeom(resultadoUTMGraus);
                            // pixelMapa.setValor(Float.valueOf(objdata.get(j).toString()));
                            // pixelMapa.getGeometry().setSRID(projection);

                            //} catch (ParseException ex) {
                            //  Logger.getLogger(InterpolacaoController.class
                            //        .getName()).log(Level.SEVERE, null, ex);
                            //}
                            //pixelsMapa.add(pixelMapa);
                            //}
                            
                        }
                        result.include("success", "success.cadastrar.interpolacao").include("idProjeto", idProjeto).redirectTo(this).lista(mapa.getArea().getProjeto().getId());
                    }
                } else {
                    if ("MM".equals(mapa.getTipoInterpolador())) {
                        try {
                            mapaDAO.gerarMapaIntepolacao(mapa.getDescricao(), areabanco.getId(), amostrabanco.getId(), mapa.getTipoInterpolador(), mapa.getTamanhoX(), mapa.getTamanhoY(), 1, mapa.getNumeroPontos(), mapa.getRaio(), usuarioSession.getUsuario());
                            result.include("success", "success.cadastrar.interpolacao").include("idProjeto", idProjeto).redirectTo(this).lista(areabanco.getProjeto().getId());
                        } catch (Exception e) {
                            result.include("danger", "danger.cadastrar.interpolacao").include("idProjeto", idProjeto).redirectTo(this).formulario(idProjeto);
                        }
                    }

                }
            }

        } catch (PPATException e) {
            result.include("danger", "danger.cadastrar.interpolacao").include("idProjeto", idProjeto).redirectTo(this).formulario(idProjeto);
            System.out.println("ERRO: " + e);
            result.include("mensagem", e.getMessage());
        }
        /*try {
            Area areabanco = areaDAO.buscarPorId(mapa.getArea().getCodigo());
            Amostra amostrabanco = amostraDAO.buscaPorIdAmostra(mapa.getAmostra().getCodigo());
            Classificacao classificacaobanco = classificacaoDAO.buscarPorId(mapa.getClassificacao().getCodigo());
            mapaDAO.gerarMapaIntepolacao(mapa.getDescricao(), areabanco.getId(), amostrabanco.getId(), mapa.getTipoInterpolador(), mapa.getTamanhoX(), mapa.getTamanhoY(), mapa.getExpoente(), mapa.getNumeroPontos(), mapa.getRaio(), usuarioSession.getUsuario(), classificacaobanco);
            result.include("success", "success.cadastrar.interpolacao").redirectTo(this).lista();
        } catch (Exception e) {
            result.include("danger", "danger.cadastrar.interpolacao").redirectTo(this).formulario();
        }*/
    }

    @Get("excluir")
    @Permissao(Roles.ON)
    public void excluir(Long id, Long idProjeto
    ) {
        Long idproj = mapaDAO.buscarPorId(id).getArea().getProjeto().getId();
        mapaDAO.excluir(mapaDAO.buscarPorId(id));
        result.include("success", "success.excluir.interpolacao").include("idProjeto", idProjeto).redirectTo(this).lista(idproj);
    }

    @Get("teste")
    public void teste() {
        // http://localhost:8080/sdum/interpolacao/teste
        File diretorio = new File(this.dirPath + "gabriela1");//usuarioSession.getUsuario().getId());
        this.validaCriaDiretorio(diretorio);

    }
}
