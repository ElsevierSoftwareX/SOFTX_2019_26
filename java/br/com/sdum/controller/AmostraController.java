package br.com.sdum.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.DecimalFormat;

import javax.inject.Inject;

import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTSFactoryFinder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTReader;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.sdum.annotation.Permissao;
import br.com.sdum.dao.AmostraDAO;
import br.com.sdum.dao.AreaDAO;
import br.com.sdum.dao.AtributoDAO;
import br.com.sdum.dao.PixelAmostraDAO;
import br.com.sdum.dao.TipoSoloDAO;
import br.com.sdum.dao.UnidadeMedidaDAO;
import br.com.sdum.dao.UsuarioDAO;
import br.com.sdum.enums.Roles;
import br.com.sdum.exception.PPATException;
import br.com.sdum.model.Amostra;
import br.com.sdum.model.Area;
import br.com.sdum.model.Atributo;
import br.com.sdum.model.MensagemRetorno;
import br.com.sdum.model.PixelAmostra;
import br.com.sdum.model.TipoSolo;
import br.com.sdum.model.UnidadeMedida;
import br.com.sdum.model.Usuario;
import br.com.sdum.session.UsuarioSession;
import br.com.sdum.ws.client.PPATWSClient;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.ClientResponse;
import com.vividsolutions.jts.io.ParseException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

@Resource
@Path("amostra")
public class AmostraController implements Serializable {

    private static final long serialVersionUID = 1846035799563404394L;

    private static int projection = 4326;

    @Inject
    private UsuarioSession usuarioSession;

    @Inject
    private Result result;

    @Inject
    private AmostraDAO amostraDAO;

    @Inject
    private AreaDAO areaDAO;

    @Inject
    private TipoSoloDAO tipoSoloDAO;

    @Inject
    private PixelAmostraDAO pixelAmostraDAO;

    @Inject
    private AtributoDAO atributoDAO;

    @Inject
    private UnidadeMedidaDAO unidadeMedidaDAO;

    @Inject
    private UsuarioDAO usuarioDAO;

    @Inject
    private PPATWSClient client;

    @Inject
    private UsuarioSession userInfo;

    private Gson gson;

    private Gson gsonunidadeMedida;

    private Gson gsonArea;

    private Gson gsonSolo;

    private Gson gsonAmostra;

    private Gson gsonPixelAmostra;

    @Path("lista")
    @Permissao(Roles.ON)
    public void lista(Long id) {
        /*try {
            Integer idProjeto;
            ClientResponse response = client.getResource("amostra", userInfo.getUsuario().getToken());
            String json = response.getEntity(String.class);
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            List<Amostra> todasAmostras = new ArrayList<>();
            List<Amostra> amostrasDaArea = new ArrayList<>();

            switch (response.getStatus()) {
                case 200:
                    todasAmostras = (List<Amostra>) gson.fromJson(json, new TypeToken<ArrayList<Amostra>>() {
                    }.getType());
                    for (int i = 0; i < todasAmostras.size(); i++) {
                        if (todasAmostras.get(i).getArea().getCodigo().equals(codigo)) {
                            amostrasDaArea.add(todasAmostras.get(i));
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
            Area areameubanco = areaDAO.buscaAreaByCodigo(codigo);
            if (areameubanco == null) {
                Area area = null;
                ClientResponse responseArea = client.getResource("area/" + codigo, userInfo.getUsuario().getToken());
                String jsonArea = responseArea.getEntity(String.class);

                gsonArea = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create();

                area = gsonArea.fromJson(jsonArea, Area.class);

                switch (response.getStatus()) {
                    case 200:
                        int iniciocoord = json.indexOf("MULTIPOLYGON");
                        int finalcoord = json.lastIndexOf(")))");
                        String poligono = json.substring(iniciocoord, finalcoord);
                        poligono += ")))";
                        TipoSolo tipoSolo = new TipoSolo();
                        areameubanco = area;
                        tipoSolo = tipoSoloDAO.buscaSoloByCodigo(area.getTipoSolo().getCodigo());
                        if (tipoSolo == null) {
                            ClientResponse responseSolo = client.getResource("solo/" + area.getTipoSolo().getCodigo(), userInfo.getUsuario().getToken());
                            String jsonSolo = responseSolo.getEntity(String.class);

                            gsonSolo = new GsonBuilder()
                                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                                    .create();

                            tipoSolo = gsonSolo.fromJson(jsonSolo, TipoSolo.class);
                            List<Usuario> usuarios = new ArrayList<Usuario>();
                            usuarios.add(usuarioSession.getUsuario());
                            tipoSolo.getUsuarios().addAll(usuarios);
                            tipoSoloDAO.adicionar(tipoSolo);
                            TipoSolo solosalvo = tipoSoloDAO.buscaSoloByCodigo(tipoSolo.getCodigo());
                            areameubanco.setTipoSolo(solosalvo);
                        } else {
                            areameubanco.setTipoSolo(tipoSolo);
                        }
                        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
                        WKTReader reader = new WKTReader(geometryFactory);

                         {
                            try {
                                areameubanco.setGeometry(reader.read(poligono));
                            } catch (ParseException ex) {
                                Logger.getLogger(AmostraController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        areameubanco.getGeometry().setSRID(projection);
                        areameubanco.setUsuario(usuarioSession.getUsuario());
                        areaDAO.adicionar(areameubanco);
                        break;

                    case 403:
                        break;
                    default:
                        MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                        throw new PPATException(mensagem.getMessage());
                }

            }

            result.include("idUsuario", usuarioSession.getUsuario().getId()).include("idArea", codigo).include("listaAmostra", amostrasDaArea).include("idProjeto", areameubanco.getProjeto().getId());

        } catch (PPATException ex) {

            result.redirectTo(MapaController.class).mapa();
            result.include("mensagem", ex.getMessage());
        }*/
        Area area = areaDAO.buscarPorId(id);
        try {
            //Area area = areaDAO.buscarPorId(id);

            Long idArea = area.getId();

            result
                    .include("idArea", id)
                    .include("idAreaAPI", area.getCodigo())
                    .include("idProjeto", area.getProjeto().getId())
                    .include("listaAmostra", amostraDAO.listaAmostraByArea(idArea));
        } catch (Exception e) {
            result.redirectTo(MapaController.class).mapa(area.getProjeto().getId());
        }
    }

    @Path("exportartxt")
    @Permissao(Roles.ON)
    public void exportartxt(Long id, HttpServletResponse response) {
        Amostra amostra = amostraDAO.buscarPorId(id);
        try {
            String send = "Long\tLat\t" + amostra.getDescricao() + System.getProperty("line.separator");
            List<PixelAmostra> pixelamostra = amostra.getPixelAmostra();
            for (int i = 0; i < pixelamostra.size(); i++) {
                String coordenadaspixel = pixelamostra.get(i).getGeometry().toString();
                coordenadaspixel = coordenadaspixel.replace("POINT (", "");
                coordenadaspixel = coordenadaspixel.replace(")", "");
                //System.out.println("pixel.tostring() " + coordenadaspixel);
                String arraypixel[] = coordenadaspixel.split(" ");
                send += arraypixel[0] + "\t" + arraypixel[1] + "\t" + pixelamostra.get(i).getValor() + System.getProperty("line.separator");
            }
            byte[] report = send.getBytes();
            response.setContentType("application/save");
            response.setContentLength(report.length);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + amostra.getDescricao() + ".txt\"");
            response.addHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            try (ServletOutputStream outputStream = response.getOutputStream()) {
                outputStream.write(report, 0, report.length);
                outputStream.flush();
            }
            result.include("idProjeto", amostra.getArea().getProjeto().getId()).redirectTo(this).lista(amostra.getArea().getId());
        } catch (Exception ex) {
            System.out.println("Exception" + ex);
        }
    }

    @Path("formulario")
    @Permissao(Roles.ON)
    public void formulario(Long id) {

        Area area = areaDAO.buscarPorId(id);
        try {
            ClientResponse response = client.getResource("atributo", userInfo.getUsuario().getToken());
            String json = response.getEntity(String.class);

            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            List<Atributo> atributos = new ArrayList<>();

            switch (response.getStatus()) {
                case 200:
                    atributos = (List<Atributo>) gson.fromJson(json, new TypeToken<ArrayList<Atributo>>() {
                    }.getType());
                    for (int i = 0; i < atributos.size(); i++) {
                        Atributo atributoAux = atributos.get(i);
                        try {
                            UnidadeMedida um = unidadeMedidaDAO.buscaUnidadeByCodigo(atributoAux.getUnidadeMedidaPT().getCodigo());
                            if (um == null) {
                                //get unidademedida pra salvar no banco sdum
                                ClientResponse responseUM = client.getResource("unidademedida/" + atributoAux.getUnidadeMedidaPT().getCodigo(), userInfo.getUsuario().getToken());
                                String jsonUM = responseUM.getEntity(String.class);

                                gsonunidadeMedida = new GsonBuilder()
                                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                                        .create();

                                switch (responseUM.getStatus()) {
                                    case 200:
                                        //get deu sucesso, salva unidademedida no banco sdum
                                        um = gsonunidadeMedida.fromJson(jsonUM, UnidadeMedida.class);
                                        Usuario u = usuarioDAO.buscarPorId(userInfo.getUsuario().getId());
                                        um.getUsuario().setId(u.getId());
                                        unidadeMedidaDAO.adicionar(um);
                                        break;
                                    default:
                                        System.out.println("NÃO SALVOU UNIDADE DE MEDIDA NO BANCO!");
                                    //MensagemRetorno mensagem2 = gsonunidadeMedida.fromJson(jsonUM, MensagemRetorno.class);
                                    //throw new PPATException(mensagem2.getMessage());
                                }

                            }
                            Atributo atributomeubanco = atributoDAO.buscaPorCodigo(atributos.get(i).getCodigo());
                            if (atributomeubanco == null) {
                                // salva atributo banco sdum
                                atributomeubanco = atributoAux;
                                atributoAux.setUnidadeMedidaPT(um);
                                atributoAux.setUnidadeMedidaEN(um);
                                atributoAux.setUnidadeMedidaES(um);
                                atributoDAO.adicionar(atributoAux);
                            }

                        } catch (PPATException ex) {
                            System.out.println("Exception formulario: " + ex);
                            result.include("listaAtributo", atributoDAO.listaAtributoByUsuarioAll(usuarioSession.getUsuario())).include("idProjeto", area.getProjeto().getId());
                            //result.include("mensagem", ex.getMessage());
                        }
                    }
                    break;
                case 404:
                    atributos = new ArrayList<>();
                    break;
                default:
                    System.out.println("Exception formulario!");
                //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                //throw new PPATException(mensagem.getMessage());
            }
            result.include("idArea", id).include("idProjeto", area.getProjeto().getId()).include("nomeArea", area.getDescricao()).include("listaAtributo", atributoDAO.listaAtributoByUsuarioAll(usuarioSession.getUsuario())).include("idAreaAPI", area.getCodigo());

        } catch (PPATException ex) {
            System.out.println("Exception formulario: " + ex);
            result.include("listaAtributo", new ArrayList());
            // result.include("mensagem", ex.getMessage());
        }
    }

    @Path("sincronizar")
    @Permissao(Roles.ON)
    public void sincronizar(Long id) {
        Amostra amostrasincr = amostraDAO.buscarPorId(id);
        Area area = areaDAO.buscarPorId(amostrasincr.getArea().getId());
        if (area.getCodigo() == null) { //se a área referente a amostra não está na api, entra no if para criá-la
            Area areaAPI = new Area();
            areaAPI.setDescricao(area.getDescricao());
            areaAPI.setTamanho(area.getTamanho());
            TipoSolo tiposolo = new TipoSolo();
            tiposolo.setCodigo(area.getTipoSolo().getCodigo());
            areaAPI.setTipoSolo(tiposolo);
            areaAPI.setUsuario(area.getUsuario());
            GeometryJSON g = new GeometryJSON();
            area.setGeom("{" + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + "," + '"' + "geometry" + '"' + ":" + g.toString(area.getGeometry()) + "}");
            String auxgeom = area.getGeom();
            int iniciogeom = auxgeom.lastIndexOf("\"coordinates\":[[");
            auxgeom = auxgeom.substring(iniciogeom);
            auxgeom = auxgeom.replace("\"coordinates\":[[", "");
            String poligono = "SRID=4326;MULTIPOLYGON(((";
            String auxgeom2 = auxgeom.replace("[", "");
            auxgeom2 = auxgeom2.replace("]", "");
            auxgeom2 = auxgeom2.replace("}}", "");
            String array[] = auxgeom2.split(",");
            for (int a = 0; a < array.length; a += 2) {
                if (a == array.length - 2) {
                    poligono += array[a] + " " + array[a + 1] + ")))";
                } else {
                    poligono += array[a] + " " + array[a + 1] + ",";
                }
            }

            areaAPI.setGeom(poligono);

            gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

            ClientResponse response;
            try {
                response = client.postResource("area", userInfo.getUsuario().getToken(), gson.toJson(areaAPI));
                String json = response.getEntity(String.class);
                System.out.println("json" + json + " response " + response.getStatus());

                //fim parte que salva na api
                if (response.getStatus() != 201) {
                    result.include("danger", "warning.nao.sincronizado").redirectTo(this).lista(amostrasincr.getArea().getId());
                    //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                    // System.out.println("mensagem json: " + mensagem.toString() + " \n response status: " + response.getStatus());
                } else {
                    int fim = json.indexOf("\"descricao");
                    Long aux = Long.parseLong(json.substring(10, fim - 1));
                    area.setCodigo(aux);
                    areaDAO.alterar(area);
                    List<Amostra> listAmostras = new ArrayList<>();
                    listAmostras = amostraDAO.listaAmostraByArea(area.getId());
                }
            } catch (PPATException ex) {
                result.include("danger", "warning.nao.sincronizado").include("idProjeto", area.getProjeto().getId()).redirectTo(this).lista(amostrasincr.getArea().getId());
                System.out.println("Exception sincronizar: " + ex);
            }

        } else { //caso área já está na api - vai para POST ou PUT de amostra
            try {
                if (amostrasincr.getCodigo() == null) { //Nova amostra, POST API
                    Amostra amostra = new Amostra();
                    amostra.setDescricao(amostrasincr.getDescricao());
                    amostra.setNomeTabela(amostrasincr.getDescricao());
                    Atributo atrAPI = new Atributo();
                    atrAPI.setCodigo(amostrasincr.getAtributo().getCodigo());
                    amostra.setAtributo(atrAPI);
                    Area areamostra = new Area();
                    areamostra.setCodigo(area.getCodigo());
                    amostra.setArea(areamostra);
                    Usuario usuario = new Usuario();
                    usuario.setCodigo(usuarioSession.getUsuario().getCodigo());
                    amostra.setUsuario(usuario);
                    gsonAmostra = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                    ClientResponse responseAmostra = client.postResource("amostra", userInfo.getUsuario().getToken(), gsonAmostra.toJson(amostra));
                    String jsonAmostra = responseAmostra.getEntity(String.class);
                    System.out.println("json" + jsonAmostra + " response " + responseAmostra.getStatus());
                    if (responseAmostra.getStatus() != 201) {
                        //MensagemRetorno mensagem = gsonAmostra.fromJson(jsonAmostra, MensagemRetorno.class);
                        //System.out.println("mensagem de erro = " + mensagem.toString());
                        //throw new PPATException(mensagem.getMessage());
                        result.include("danger", "warning.nao.sincronizado").include("idProjeto", area.getProjeto().getId()).redirectTo(this).lista(amostrasincr.getArea().getId());
                    } else {
                        int fimamostra = jsonAmostra.indexOf("\"descricao");
                        Long codamostra = Long.parseLong(jsonAmostra.substring(10, fimamostra - 1));
                        amostrasincr.setCodigo(codamostra);
                        amostraDAO.alterar(amostrasincr);
                        List<PixelAmostra> pixelamostra = amostrasincr.getPixelAmostra();
                        for (int k = 0; k < pixelamostra.size(); k++) {//cria pixel de cada amostra na api
                            GeometryJSON gamostra = new GeometryJSON();
                            pixelamostra.get(k).setGeom("{" + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + "," + '"' + "geometry" + '"' + ":" + gamostra.toString(pixelamostra.get(k).getGeometry()) + "}");
                            String geompa = pixelamostra.get(k).getGeom();
                            int iniciogeompa = geompa.lastIndexOf("\"coordinates\":[");
                            geompa = geompa.substring(iniciogeompa);
                            geompa = geompa.replace("\"coordinates\":[", "");
                            String pa = "POINT(";
                            String geompa2 = geompa.replace("[", "");
                            geompa2 = geompa2.replace("]", "");
                            geompa2 = geompa2.replace("}}", "");
                            String arraypa[] = geompa2.split(",");
                            pa += arraypa[0] + " " + arraypa[1] + ")";
                            //pixelamostra.get(k).setGeom(pa);
                            PixelAmostra pixela = new PixelAmostra();
                            pixela.setGeom(pa);
                            amostra.setCodigo(codamostra);
                            pixela.setAmostra(amostra);
                            pixela.setValor(pixelamostra.get(k).getValor());
                            gsonPixelAmostra = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                            ClientResponse responsePixelAmostra = client.postResource("pixelamostra", userInfo.getUsuario().getToken(), gsonPixelAmostra.toJson(pixela));
                            String jsonPixelAmostra = responsePixelAmostra.getEntity(String.class);
                            System.out.println("json" + jsonPixelAmostra + " response " + responsePixelAmostra.getStatus());
                            if (responsePixelAmostra.getStatus() != 201) {
                                result.include("danger", "warning.nao.sincronizado").include("idProjeto", area.getProjeto().getId()).redirectTo(this).lista(amostrasincr.getArea().getId());
                                // MensagemRetorno mensagemPixelAmostra = gsonPixelAmostra.fromJson(jsonPixelAmostra, MensagemRetorno.class);
                                // System.out.println("mensagem de erro = " + mensagemPixelAmostra.toString());
                                // throw new PPATException(mensagemPixelAmostra.getMessage());
                            } else {
                                int fimcod = jsonPixelAmostra.indexOf("\"geom\"");
                                System.out.println("auxcod-----------: " + jsonPixelAmostra.substring(10, fimcod - 1));
                                Long auxcod = Long.parseLong(jsonPixelAmostra.substring(10, fimcod - 1));
                                pixelamostra.get(k).setCodigo(auxcod);
                                pixelAmostraDAO.alterar(pixelamostra.get(k));
                                //fim parte que salva na api

                            }
                        }
                    }
                } else { //já está na API, da PUT
                    Amostra amostra = new Amostra();
                    amostra.setDescricao(amostrasincr.getDescricao());
                    amostra.setNomeTabela(amostrasincr.getDescricao());
                    Atributo atrAPI = new Atributo();
                    atrAPI.setCodigo(amostrasincr.getAtributo().getCodigo());
                    amostra.setAtributo(atrAPI);
                    Area areamostra = new Area();
                    areamostra.setCodigo(area.getCodigo());
                    amostra.setArea(areamostra);
                    amostra.setCodigo(amostrasincr.getCodigo());
                    Usuario usuario = new Usuario();
                    usuario.setCodigo(usuarioSession.getUsuario().getCodigo());
                    amostra.setUsuario(usuario);
                    gsonAmostra = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                    ClientResponse responseAmostra = client.putResource("amostra", userInfo.getUsuario().getToken(), gsonAmostra.toJson(amostra));
                    String jsonAmostra = responseAmostra.getEntity(String.class);
                    System.out.println("json" + jsonAmostra + " response " + responseAmostra.getStatus());
                    if (responseAmostra.getStatus() != 200) {
                        result.include("danger", "warning.nao.sincronizado").include("idProjeto", area.getProjeto().getId()).redirectTo(this).lista(amostrasincr.getArea().getId());
                        //MensagemRetorno mensagem = gsonAmostra.fromJson(jsonAmostra, MensagemRetorno.class);
                        //System.out.println("mensagem de erro = " + mensagem.toString());
                    } else {
                        List<PixelAmostra> pixelamostra = amostrasincr.getPixelAmostra();
                        for (int k = 0; k < pixelamostra.size(); k++) {//post ou put do pixel amostra
                            GeometryJSON gamostra = new GeometryJSON();
                            pixelamostra.get(k).setGeom("{" + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + "," + '"' + "geometry" + '"' + ":" + gamostra.toString(pixelamostra.get(k).getGeometry()) + "}");
                            String geompa = pixelamostra.get(k).getGeom();
                            int iniciogeompa = geompa.lastIndexOf("\"coordinates\":[");
                            geompa = geompa.substring(iniciogeompa);
                            geompa = geompa.replace("\"coordinates\":[", "");
                            String pa = "POINT(";
                            String geompa2 = geompa.replace("[", "");
                            geompa2 = geompa2.replace("]", "");
                            geompa2 = geompa2.replace("}}", "");
                            String arraypa[] = geompa2.split(",");
                            pa += arraypa[0] + " " + arraypa[1] + ")";
                            PixelAmostra pixela = new PixelAmostra();
                            pixela.setGeom(pa);
                            pixela.setAmostra(amostra);
                            pixela.setValor(pixelamostra.get(k).getValor());
                            gsonPixelAmostra = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                            if (pixelamostra.get(k).getCodigo() == null) {//cria novo pixel na api caso nao tenha la
                                ClientResponse responsePixelAmostra = client.postResource("pixelamostra", userInfo.getUsuario().getToken(), gsonPixelAmostra.toJson(pixela));
                                String jsonPixelAmostra = responsePixelAmostra.getEntity(String.class);
                                System.out.println("json" + jsonPixelAmostra + " response " + responsePixelAmostra.getStatus());
                                if (responsePixelAmostra.getStatus() != 201) {
                                    MensagemRetorno mensagemPixelAmostra = gsonPixelAmostra.fromJson(jsonPixelAmostra, MensagemRetorno.class);
                                    System.out.println("mensagem de erro = " + mensagemPixelAmostra.toString());
                                } else {
                                    int fimcod = jsonPixelAmostra.indexOf("\"geom\"");
                                    System.out.println("auxcod-----------: " + jsonPixelAmostra.substring(10, fimcod - 1));
                                    Long auxcod = Long.parseLong(jsonPixelAmostra.substring(10, fimcod - 1));
                                    pixelamostra.get(k).setCodigo(auxcod);
                                    pixelAmostraDAO.alterar(pixelamostra.get(k));
                                }
                            } else { //atualiza pixel que já está na api
                                pixela.setCodigo(pixelamostra.get(k).getCodigo());
                                ClientResponse responsePA = client.putResource("pixelamostra", userInfo.getUsuario().getToken(), gsonPixelAmostra.toJson(pixela));
                                String jsonPA = responsePA.getEntity(String.class);
                                System.out.println("ENTROU NO PUT PIXEL " + responsePA + " codigo pxiel: " + pixela.getCodigo());
                                if (responsePA.getStatus() != 200) {
                                    result.include("danger", "warning.nao.sincronizado").include("idProjeto", area.getProjeto().getId()).redirectTo(this).lista(amostrasincr.getArea().getId());
                                    //MensagemRetorno mensagemPA = gsonPixelAmostra.fromJson(jsonPA, MensagemRetorno.class);
                                    //throw new PPATException(mensagemPA.getMessage());
                                }

                            }
                        }
                    }
                }
                result.include("success", "success.cadastrar.amostra.sincronizada").include("idProjeto", area.getProjeto().getId()).redirectTo(this).lista(amostrasincr.getArea().getId());
            } catch (Exception ex) {
                System.out.println("**********ERRO: " + ex);
                result.include("danger", "warning.nao.sincronizado").include("idProjeto", area.getProjeto().getId()).redirectTo(this).lista(amostrasincr.getArea().getId());
            }
        }
    }

    @Path("editar")
    @Permissao(Roles.ON)
    public void editar(Long id) {
        Amostra amostra = amostraDAO.buscarPorId(id);
        try {
            GeometryJSON g = new GeometryJSON();

            List<String> foo = new ArrayList<String>();
            for (int i = 0; i < amostra.getPixelAmostra().size(); i++) {
                foo.add("{"
                        + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + ","
                        + '"' + "id" + '"' + ":" + '"' + amostra.getPixelAmostra().get(i).getId() + '"' + ","
                        //+ '"' + "codigo" + '"' + ":" + '"' + amostra.getPixelAmostra().get(i).getCodigo() + '"' + ","
                        + '"' + "valor" + '"' + ":" + '"' + amostra.getPixelAmostra().get(i).getValor() + '"' + ","
                        + '"' + "geometry" + '"' + ":" + g.toString(amostra.getPixelAmostra().get(i).getGeometry()) + "}");
            }

            amostra.getArea().setGeom("{" + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + "," + '"' + "geometry" + '"' + ":" + g.toString(amostra.getArea().getGeometry()) + "}");

            String jsonPontoAmostra = new Gson().toJson(foo);

            result
                    .include("amostra", amostra)
                    .include("pontoAmostra", jsonPontoAmostra)
                    .include("idProjeto", amostra.getArea().getProjeto().getId())
                    .include("codigoAmostraAPI", amostra.getCodigo())
                    .include("listaAtributo", atributoDAO.listaAtributoByUsuarioAll(usuarioSession.getUsuario()));

        } catch (Exception ex) {
            System.out.println("Exception editar amostra: " + ex);
            result.include("idProjeto", amostra.getArea().getProjeto().getId()).redirectTo(this).lista(amostraDAO.buscarPorId(id).getArea().getId());
        }
    }

    @Post("persiste")
    @Permissao(Roles.ON)
    public void cadastrarAmostraTXT(Amostra amostra, UploadedFile uploadedFile,
            String latlong, Long idProjeto,
            boolean formGrade, String salvarAPI,
            Long idAreaAPI
    ) {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
        WKTReader reader = new WKTReader(geometryFactory);
        System.out.println("ID AREA API ------------ " + idAreaAPI + " salvar api " + salvarAPI + " id projeto " + idProjeto);
        if ((salvarAPI.equals("sim")) && !(Objects.equals(idAreaAPI, ""))) {
            try {
                Amostra amostraAPI = new Amostra();
                amostraAPI.setDescricao(amostra.getDescricao());
                amostraAPI.setNomeTabela(amostra.getDescricao());
                Atributo atrAPI = new Atributo();
                atrAPI.setCodigo(amostra.getAtributo().getCodigo());
                amostraAPI.setAtributo(atrAPI);
                Area areamostra = new Area();
                areamostra.setCodigo(idAreaAPI);
                amostraAPI.setArea(areamostra);
                Usuario usuario = new Usuario();
                usuario.setCodigo(usuarioSession.getUsuario().getCodigo());
                amostraAPI.setUsuario(usuario);
                amostraAPI.setNomeTabela(amostra.getDescricao());
                //salvando amostra na api
                gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                ClientResponse response = client.postResource("amostra", userInfo.getUsuario().getToken(), gson.toJson(amostraAPI));
                String json = response.getEntity(String.class);
                int fim = json.indexOf("\"descricao");
                Long aux = Long.parseLong(json.substring(10, fim - 1));
                amostra.setCodigo(aux);
                amostraAPI.setCodigo(aux);

                if (response.getStatus() != 201) {
                    result.include("danger", "danger.amostra.cadastrada.nao.foi.possivel").include("idProjeto",amostra.getArea().getProjeto().getId()).redirectTo(this).lista(amostra.getArea().getId());
                    //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                    //System.out.println("mensagem de erro = " + mensagem.toString());
                    //throw new PPATException(mensagem.getMessage());
                }

                List<PixelAmostra> pixelAmostras = new ArrayList<PixelAmostra>();

                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(uploadedFile.getFile(), "UTF8"));

                    in.readLine();

                    String ponto = "";
                    while ((ponto = in.readLine()) != null) {

                        String[] valores = ponto.split("\t");

                        if (valores.length == 3) {

                            PixelAmostra pa = new PixelAmostra();
                            PixelAmostra paMeubanco = new PixelAmostra();
                            pa.setAmostra(amostraAPI);
                            paMeubanco.setAmostra(amostra);

                            if (latlong.equals("longlat")) {
                                paMeubanco.setGeometry(reader.read("POINT(" + valores[0] + " " + valores[1] + ")"));
                                pa.setGeom("POINT(" + valores[0] + " " + valores[1] + ")");
                            } else {
                                paMeubanco.setGeometry(reader.read("POINT(" + valores[1] + " " + valores[0] + ")"));
                                pa.setGeom("POINT(" + valores[1] + " " + valores[0] + ")");
                            }
                            if (valores[2] != null || (!valores[2].equals("null"))) {
                                paMeubanco.setValor(Float.parseFloat(valores[2]));
                                pa.setValor(Float.parseFloat(valores[2]));
                            } else {
                                paMeubanco.setValor(0);
                            }

                            paMeubanco.getGeometry().setSRID(projection);

                            //salvando pixelamostra na api
                            gsonPixelAmostra = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                            ClientResponse responsePixelAmostra = client.postResource("pixelamostra", userInfo.getUsuario().getToken(), gsonPixelAmostra.toJson(pa));
                            String jsonPixelAmostra = responsePixelAmostra.getEntity(String.class);

                            if (responsePixelAmostra.getStatus() != 201) {
                                result.include("danger", "danger.amostra.cadastrada.nao.foi.possivel").include("idProjeto",amostra.getArea().getProjeto().getId()).redirectTo(this).lista(amostra.getArea().getId());
                                //MensagemRetorno mensagemPixelAmostra = gsonPixelAmostra.fromJson(jsonPixelAmostra, MensagemRetorno.class);
                                // System.out.println("mensagem de erro = " + mensagemPixelAmostra.toString());
                                // throw new PPATException(mensagemPixelAmostra.getMessage());
                            }
                            int fimcod = jsonPixelAmostra.indexOf("\"geom\"");
                            System.out.println("auxcod-----------: " + jsonPixelAmostra.substring(10, fimcod - 1));
                            Long auxcod = Long.parseLong(jsonPixelAmostra.substring(10, fimcod - 1));
                            paMeubanco.setCodigo(auxcod);
                            //fim parte que salva na api
                            pixelAmostras.add(paMeubanco);
                        }
                    }

                    in.close();
                } catch (Exception e) {
                    System.out.println("Exception persiste amostra: " + e);
                }
                amostra.setDataCadastro(new Date());
                amostra.setUsuario(usuarioSession.getUsuario());
                amostra.setArea(areaDAO.buscarPorId(amostra.getArea().getId()));
                amostra.setAtributo(atributoDAO.buscaPorCodigo(amostra.getAtributo().getCodigo()));
                amostra.setPixelAmostra(pixelAmostras);
                amostra.setNomeTabela(amostra.getDescricao());
                amostraDAO.adicionar(amostra);
                // List<PixelAmostra> pixelAmo = new ArrayList<PixelAmostra>();
                //pixelAmo = pixelAmostras;
                //for (int i = 0; i < pixelAmo.size(); i++) {
                //    pixelAmo.get(i).setCodigo(aux);
                // }
                //amostra.setPixelAmostra(pixelAmostras);
                // amostraDAO.alterar(amostra);
                result.include("success", "success.amostra.cadastrada").include("idProjeto",amostra.getArea().getProjeto().getId()).redirectTo(this).lista(amostra.getArea().getId());
            } catch (PPATException ex) {
                //result.include("amostra", amostra);
                System.out.println("Exception persiste amostra: " + ex);
                result.include("danger", "danger.amostra.cadastrada.nao.foi.possivel").include("idProjeto",amostra.getArea().getProjeto().getId()).redirectTo(this).lista(amostra.getArea().getId());
                //result.include("mensagem", ex.getMessage());
            }
        } else {
            try {
                amostra.setNomeTabela(amostra.getDescricao());
                List<PixelAmostra> pixelAmostras = new ArrayList<PixelAmostra>();
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(uploadedFile.getFile(), "UTF8"));

                    in.readLine();

                    String ponto = "";
                    while ((ponto = in.readLine()) != null) {

                        String[] valores = ponto.split("\t");

                        if (valores.length == 3) {
                            PixelAmostra paMeubanco = new PixelAmostra();
                            paMeubanco.setAmostra(amostra);

                            if (latlong.equals("longlat")) {
                                paMeubanco.setGeometry(reader.read("POINT(" + valores[0] + " " + valores[1] + ")"));
                            } else {
                                paMeubanco.setGeometry(reader.read("POINT(" + valores[1] + " " + valores[0] + ")"));
                            }
                            if (valores[2] != null || (!valores[2].equals("null"))) {
                                paMeubanco.setValor(Float.parseFloat(valores[2]));
                            } else {
                                paMeubanco.setValor(0);
                            }

                            paMeubanco.getGeometry().setSRID(projection);
                            pixelAmostras.add(paMeubanco);
                        }
                    }

                    in.close();
                } catch (Exception e) {
                    System.out.println("Exception persiste amostra: " + e);
                    result.include("danger", "danger.amostra.cadastrada.nao.foi.possivel").include("idProjeto",amostra.getArea().getProjeto().getId()).redirectTo(this).lista(amostra.getArea().getId());

                }
                amostra.setDataCadastro(new Date());
                amostra.setUsuario(usuarioSession.getUsuario());
                amostra.setArea(areaDAO.buscarPorId(amostra.getArea().getId()));
                amostra.setAtributo(atributoDAO.buscaPorCodigo(amostra.getAtributo().getCodigo()));
                amostra.setNomeTabela(amostra.getDescricao());
                amostra.setPixelAmostra(pixelAmostras);
                amostraDAO.adicionar(amostra);
                result.include("success", "success.amostra.cadastrada").include("idProjeto",amostra.getArea().getProjeto().getId()).redirectTo(this).lista(amostra.getArea().getId());
            } catch (Exception ex) {
                System.out.println("Exception persiste amostra: " + ex);
                result.include("danger", "danger.amostra.cadastrada.nao.foi.possivel").include("idProjeto",amostra.getArea().getProjeto().getId()).redirectTo(this).lista(amostra.getArea().getId());
                //result.include("amostra", amostra);
                //result.include("mensagem", ex.getMessage());
            }
        }

    }

//}
    @Post("editar/persiste")
    @Permissao(Roles.ON)
    public void editarAmostra(Amostra amostra, String pontosDasAmostras, String salvarAPI
    ) {
        if (salvarAPI.equals("nao")) {
            try {
                GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
                WKTReader reader = new WKTReader(geometryFactory);

                amostra.setDataCadastro(new Date());
                amostra.setUsuario(usuarioSession.getUsuario());
                amostra.setArea(areaDAO.buscarPorId(amostra.getArea().getId()));
                amostra.setAtributo(atributoDAO.buscaPorCodigo(amostra.getAtributo().getCodigo()));
                amostra.setNomeTabela(amostra.getDescricao());
                if ((!(amostra.getDescricao().equals(amostraDAO.buscarPorId(amostra.getId()).getDescricao()))) || (!(amostra.getAtributo().getCodigo().equals(amostraDAO.buscarPorId(amostra.getId()).getAtributo().getCodigo())))) {
                    Amostra amostraAux = amostraDAO.buscarPorId(amostra.getId());
                    amostraAux.setDescricao(amostra.getDescricao());
                    amostraAux.setAtributo(atributoDAO.buscaPorCodigo(amostra.getAtributo().getCodigo()));
                    amostraAux.setNomeTabela(amostra.getDescricao());
                    amostraDAO.alterar(amostraAux);

                } else {

                }
                amostra = amostraDAO.buscarPorId(amostra.getId());
                List<PixelAmostra> pixelAmostras = new ArrayList<>();
                List<PixelAmostra> pixelAmostraList = new Gson().fromJson(pontosDasAmostras, new TypeToken<ArrayList<PixelAmostra>>() {
                }.getType());

                for (int i = 0; i < pixelAmostraList.size(); i++) {

                    pixelAmostraList.get(i).setGeometry(reader.read(pixelAmostraList.get(i).getGeom().toString()));
                    pixelAmostraList.get(i).getGeometry().setSRID(projection);
                    pixelAmostraList.get(i).setAmostra(amostra);
                    PixelAmostra pixelaux = pixelAmostraDAO.buscarPorId(pixelAmostraList.get(i).getId());
                    pixelAmostraList.get(i).setCodigo(pixelaux.getCodigo());
                    pixelAmostras.add(pixelAmostraList.get(i));
                }

                amostra.setPixelAmostra(pixelAmostras);

                List<PixelAmostra> pAmostras = pixelAmostraDAO.buscaPorIdAmostra(amostraDAO.buscarPorId(amostra.getId()).getId());
                pixelAmostraList = new ArrayList<>();

                boolean ajuda = true;

                for (int i = 0; i < pAmostras.size(); i++) {

                    for (int j = 0; j < pixelAmostras.size(); j++) {

                        if (pixelAmostras.get(j).getId().equals(pAmostras.get(i).getId())) {
                            ajuda = false;
                            System.out.println("ajuda: " + ajuda);
                        }
                    }

                    if (ajuda == true) {
                        System.out.println("------ENTROU NO IF DE AJUDA=TRUE");
                        pixelAmostraList.add(pAmostras.get(i));
                    } else {
                        ajuda = true;
                    }
                }

                for (int i = 0; i < pixelAmostraList.size(); i++) {
                    pixelAmostraDAO.excluir(pixelAmostraList.get(i));

                }
                amostraDAO.alterar(amostra);
                result.include("success", "success.amostra.editada").include("idProjeto",amostra.getArea().getProjeto().getId()).redirectTo(this).lista(amostra.getArea().getId());

            } catch (Exception e) {
                System.out.println("EXCEÇÃO: ********** " + e);
                result.include("danger", "danger.amostra.editada.nao.foi.possivel").include("idProjeto",amostra.getArea().getProjeto().getId()).redirectTo(this).lista(amostra.getArea().getId());
            }
        } else {
            try {
                GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
                WKTReader reader = new WKTReader(geometryFactory);
                amostra.setDataCadastro(new Date());
                amostra.setUsuario(usuarioSession.getUsuario());
                amostra.setArea(areaDAO.buscarPorId(amostra.getArea().getId()));
                amostra.setAtributo(atributoDAO.buscaPorCodigo(amostra.getAtributo().getCodigo()));
                amostra.setNomeTabela(amostra.getDescricao());
                Amostra amostraAPI = new Amostra();
                amostraAPI.setCodigo(amostraDAO.buscarPorId(amostra.getId()).getCodigo());
                amostraAPI.setDescricao(amostra.getDescricao());
                amostraAPI.setNomeTabela(amostraAPI.getDescricao());
                Atributo atrAPI = new Atributo();
                atrAPI.setCodigo(amostra.getAtributo().getCodigo());
                amostraAPI.setAtributo(atrAPI);
                Area area = new Area();
                area.setCodigo(amostra.getArea().getCodigo());
                amostraAPI.setArea(area);
                if ((!(amostra.getDescricao().equals(amostraDAO.buscarPorId(amostra.getId()).getDescricao()))) || (!(amostra.getAtributo().getCodigo().equals(amostraDAO.buscarPorId(amostra.getId()).getAtributo().getCodigo())))) {
                    gsonAmostra = new GsonBuilder().create();
                    ClientResponse responseA = client.putResource("amostra", userInfo.getUsuario().getToken(), gsonAmostra.toJson(amostraAPI));
                    String jsonA = responseA.getEntity(String.class);
                    System.out.println("JSON AMOSTRA----------: " + jsonA);
                    if (responseA.getStatus() != 200) {
                        result.include("danger", "danger.amostra.editada.nao.foi.possivel").include("idProjeto",amostra.getArea().getProjeto().getId()).redirectTo(this).lista(amostra.getArea().getId());
                        //MensagemRetorno mensagemA = gsonAmostra.fromJson(jsonA, MensagemRetorno.class);
                        //throw new PPATException(mensagemA.getMessage());
                    } else {
                        Amostra amostraAux = amostraDAO.buscaPorIdAmostra(amostra.getId());
                        amostraAux.setDescricao(amostraAPI.getDescricao());
                        amostraAux.setAtributo(atributoDAO.buscaPorCodigo(amostra.getAtributo().getCodigo()));
                        amostraAux.setNomeTabela(amostra.getDescricao());
                        amostraDAO.alterar(amostraAux);
                    }
                } else {

                }
                amostra = amostraDAO.buscarPorId(amostra.getId());
                //pixelAmostraDAO.excluirAllPixelAmostra( amostra.getId() );
                List<PixelAmostra> pixelAmostras = new ArrayList<PixelAmostra>();
                List<PixelAmostra> pixelAmostraList = new Gson().fromJson(pontosDasAmostras, new TypeToken<ArrayList<PixelAmostra>>() {
                }.getType());

                for (int i = 0; i < pixelAmostraList.size(); i++) {

                    //pixelAmostraList.get(i).setId(null);
                    pixelAmostraList.get(i).setGeometry(reader.read(pixelAmostraList.get(i).getGeom()));
                    pixelAmostraList.get(i).getGeometry().setSRID(projection);
                    pixelAmostraList.get(i).setAmostra(amostra);
                    pixelAmostraList.get(i).setCodigo(pixelAmostraDAO.buscarPorId(pixelAmostraList.get(i).getId()).getCodigo());
                    pixelAmostras.add(pixelAmostraList.get(i));

                    PixelAmostra paAPI = new PixelAmostra();
                    paAPI.setCodigo(pixelAmostraDAO.buscarPorId(pixelAmostraList.get(i).getId()).getCodigo());
                    paAPI.setGeom(pixelAmostraList.get(i).getGeom());
                    paAPI.setAmostra(amostraAPI);
                    paAPI.setValor(pixelAmostraList.get(i).getValor());
                    gsonPixelAmostra = new GsonBuilder().create();
                    ClientResponse responsePA = client.putResource("pixelamostra", userInfo.getUsuario().getToken(), gsonPixelAmostra.toJson(paAPI));
                    String jsonPA = responsePA.getEntity(String.class);

                    if (responsePA.getStatus() != 200) {
                        result.include("danger", "danger.amostra.editada.nao.foi.possivel").include("idProjeto",amostra.getArea().getProjeto().getId()).redirectTo(this).lista(amostra.getArea().getId());
                        //MensagemRetorno mensagemPA = gsonPixelAmostra.fromJson(jsonPA, MensagemRetorno.class);
                        //throw new PPATException(mensagemPA.getMessage());
                    }
                }

                amostra.setPixelAmostra(pixelAmostras);

                List<PixelAmostra> pAmostras = pixelAmostraDAO.buscaPorIdAmostra(amostraDAO.buscaAmostraByCodigo(amostra.getCodigo()).getId());
                pixelAmostraList = new ArrayList<PixelAmostra>();

                boolean ajuda = true;

                for (int i = 0; i < pAmostras.size(); i++) {

                    for (int j = 0; j < pixelAmostras.size(); j++) {

                        if (pixelAmostras.get(j).getId().equals(pAmostras.get(i).getId())) {
                            ajuda = false;
                            System.out.println("ajuda: " + ajuda);
                        }
                    }

                    if (ajuda == true) {
                        System.out.println("------ENTROU NO IF DE AJUDA=TRUE");
                        pixelAmostraList.add(pAmostras.get(i));
                    } else {
                        ajuda = true;
                    }
                }

                for (int i = 0; i < pixelAmostraList.size(); i++) {
                    gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                            .create();
                    System.out.println("DELETAR: " + pixelAmostraDAO.buscarPorId(pixelAmostraList.get(i).getId()).getCodigo().toString());
                    ClientResponse response = client.deleteResource("pixelamostra/" + pixelAmostraDAO.buscarPorId(pixelAmostraList.get(i).getId()).getCodigo(), userInfo.getUsuario().getToken());
                    String json = response.getEntity(String.class);

                    if (response.getStatus() != 200) {
                        result.include("danger", "danger.amostra.editada.nao.foi.possivel").redirectTo(this).lista(amostra.getArea().getId());
                        //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                        //throw new PPATException(mensagem.getMessage());
                    } else {
                        System.out.println("entrou no else DELETAR meu banco!!!!!!");
                        amostraDAO.alterar(amostra);
                        pixelAmostraDAO.excluir(pixelAmostraList.get(i));
                    }

                }
                amostraDAO.alterar(amostra);
                result.include("success", "success.amostra.editada").include("idProjeto",amostra.getArea().getProjeto().getId()).redirectTo(this).lista(amostra.getArea().getId());

            } catch (Exception e) {
                System.out.println("EXCEÇÃO: ********** " + e);
                result.include("danger", "danger.amostra.editada.nao.foi.possivel").include("idProjeto",amostra.getArea().getProjeto().getId()).redirectTo(this).lista(amostra.getArea().getId());
            }
        }
    }

    @Get("excluir")
    @Permissao(Roles.ON)
    public void excluir(Long id, String excluirapi) {
        Amostra amostra = amostraDAO.buscarPorId(id);
        Long idProjeto = amostra.getArea().getProjeto().getId();
        if (excluirapi.equals("nao")) {
            try {
                amostraDAO.excluir(amostra);
                result.include("success", "success.excluir.amostra").include("idProjeto", idProjeto).redirectTo(this).lista(amostra.getArea().getId());
            } catch (Exception e) {
                result.include("danger", "danger.excluir.amostra").include("idProjeto", idProjeto).redirectTo(this).lista(amostra.getArea().getId());
            }
        } else {
            //Amostra amostra = amostraDAO.buscaAmostraByCodigo(codigo);

            try {
                ClientResponse responseGetPixelA = client.getResource("pixelamostra", userInfo.getUsuario().getToken());
                String jsonGetPixelA = responseGetPixelA.getEntity(String.class);

                gsonPixelAmostra = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create();

                List<PixelAmostra> pixelAmostra = new ArrayList<>();
                int tamArray = 0;

                switch (responseGetPixelA.getStatus()) {
                    case 200:
                        pixelAmostra = (List<PixelAmostra>) gsonPixelAmostra.fromJson(jsonGetPixelA, new TypeToken<ArrayList<PixelAmostra>>() {
                        }.getType());
                        tamArray = pixelAmostra.size();
                        break;
                    case 404:
                        pixelAmostra = new ArrayList<>();
                        break;
                    default:
                        result.include("danger", "danger.excluir.amostra").redirectTo(this).lista(amostra.getArea().getId());
                    //MensagemRetorno mensagemGetPixelA = gsonPixelAmostra.fromJson(jsonGetPixelA, MensagemRetorno.class);
                    //throw new PPATException(mensagemGetPixelA.getMessage());
                }
                for (int i = 0; i < tamArray; i++) {
                    Long codAmostra = pixelAmostra.get(i).getAmostra().getCodigo();
                    if (codAmostra.equals(amostra.getCodigo())) {
                        gson = new GsonBuilder()
                                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                                .create();

                        ClientResponse responseDelPixelA = client.deleteResource("pixelamostra/" + pixelAmostra.get(i).getCodigo(), userInfo.getUsuario().getToken());
                        String jsonDelPixelA = responseDelPixelA.getEntity(String.class);

                        if (responseDelPixelA.getStatus() != 200) {
                            result.include("danger", "danger.excluir.amostra").include("idProjeto", idProjeto).redirectTo(this).lista(amostra.getArea().getId());
                            //MensagemRetorno mensagemDelPixelA = gson.fromJson(jsonDelPixelA, MensagemRetorno.class);
                            //throw new PPATException(mensagemDelPixelA.getMessage());
                        }
                    }
                }

                gsonAmostra = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create();

                ClientResponse response = client.deleteResource("amostra/" + amostra.getCodigo(), userInfo.getUsuario().getToken());
                String json = response.getEntity(String.class);

                if (response.getStatus() != 200) {
                    result.include("danger", "danger.excluir.amostra").include("idProjeto", idProjeto).redirectTo(this).lista(amostra.getArea().getId());
                    //MensagemRetorno mensagem = gsonAmostra.fromJson(json, MensagemRetorno.class);
                    //throw new PPATException(mensagem.getMessage());
                } else {

                    amostraDAO.excluir(amostra);

                }

                result.include("success", "success.excluir.amostra").include("idProjeto", idProjeto).redirectTo(this).lista(amostra.getArea().getId());
            } catch (Exception e) {
                System.out.println("Exception excluir amostra: " + e);
                result.include("danger", "danger.excluir.amostra").include("idProjeto", idProjeto).redirectTo(this).lista(amostra.getArea().getId());
            }
        }
    }

    /*@Path("modal/novaAmostra")
    @Permissao(Roles.ON)
    public void novaAmostra() {
    }

    @Post("/cadastrar/amostra/txt")
    @Permissao(Roles.ON)
    public void cadastrarAmostraTXT(Amostra amostra, UploadedFile uploadedFile,
            String latlong, Long idProjeto
    ) {

        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
        WKTReader reader = new WKTReader(geometryFactory);

        if (amostra.getId() == null) {

            amostra.setDataCadastro(new Date());
            amostra.setUsuario(usuarioSession.getUsuario());
            amostra.setArea(areaDAO.buscarPorId(amostra.getArea().getId()));

            List<PixelAmostra> pixelAmostras = new ArrayList<PixelAmostra>();

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(uploadedFile.getFile(), "UTF8"));

                in.readLine();

                String ponto = "";
                while ((ponto = in.readLine()) != null) {

                    String[] valores = ponto.split("\t");

                    if (valores.length == 3) {

                        PixelAmostra pa = new PixelAmostra();
                        pa.setAmostra(amostra);

                        if (latlong.equals("longlat")) {
                            pa.setGeometry(reader.read("POINT(" + valores[1] + " " + valores[0] + ")"));
                        } else {
                            pa.setGeometry(reader.read("POINT(" + valores[0] + " " + valores[1] + ")"));
                        }

                        pa.setValor(valores[2]);
                        pa.getGeometry().setSRID(projection);

                        pixelAmostras.add(pa);
                    }
                }

                in.close();

            } catch (Exception e) {
            }

            amostraDAO.adicionar(amostra);
        }
    }*/
}
