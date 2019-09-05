package br.com.sdum.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import br.com.sdum.dao.GradeAmostralDAO;
import br.com.sdum.dao.PontoAmostralDAO;
import br.com.sdum.enums.Roles;
import br.com.sdum.exception.PPATException;
import br.com.sdum.model.Amostra;
import br.com.sdum.model.Area;
import br.com.sdum.model.Atributo;
import br.com.sdum.model.GradeAmostral;
import br.com.sdum.model.MensagemRetorno;
import br.com.sdum.model.PixelAmostra;
import br.com.sdum.model.PontoAmostral;
import br.com.sdum.model.TipoSolo;
import br.com.sdum.model.Usuario;
import br.com.sdum.session.UsuarioSession;
import br.com.sdum.ws.client.PPATWSClient;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.jersey.api.client.ClientResponse;
import com.vividsolutions.jts.io.ParseException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.geotools.factory.FactoryRegistryException;

@Resource
@Path("gradeamostral")
public class GradeAmostralController implements Serializable {

    private static final long serialVersionUID = 2694475780215992675L;

    private static int projection = 4326;

    @Inject
    private UsuarioSession usuarioSession;

    @Inject
    private Result result;

    @Inject
    private AreaDAO areaDAO;

    @Inject
    private AmostraDAO amostraDAO;

    @Inject
    private GradeAmostralDAO gradeAmostralDAO;

    @Inject
    private PontoAmostralDAO pontoAmostralDAO;

    @Inject
    private AtributoDAO atributoDAO;

    @Inject
    private PPATWSClient client;

    @Inject
    private UsuarioSession userInfo;

    private Gson gson;

    private Gson gsonAtributo;

    private Gson gsonPontoAmostral;

    private Gson gsonGradeAmostral;

    private Gson gsonPixelAmostra;

    @Path("lista")
    @Permissao(Roles.ON)
    public void lista(Long id) {
        System.out.println("entrou no lista --------  id area: " + id);
        try {
//            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
//            WKTReader reader = new WKTReader(geometryFactory);
//            ClientResponse response = client.getResource("gradeamostral", userInfo.getUsuario().getToken());
//            String json = response.getEntity(String.class);
//            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
//            List<GradeAmostral> todasGradesAmostrais = new ArrayList<>();
//            List<GradeAmostral> gradesAmostraisDaArea = new ArrayList<>();
//            GradeAmostral gradeAmostral = new GradeAmostral();
//
//            switch (response.getStatus()) {
//                case 200:
//                    todasGradesAmostrais = (List<GradeAmostral>) gson.fromJson(json, new TypeToken<ArrayList<GradeAmostral>>() {
//                    }.getType());
//                    for (int i = 0; i < todasGradesAmostrais.size(); i++) {
//                        if (todasGradesAmostrais.get(i).getArea().getCodigo().equals(codigo)) {
//                            gradesAmostraisDaArea.add(todasGradesAmostrais.get(i));
//                            if (gradeAmostralDAO.buscaGradeAmostralByCodigo(todasGradesAmostrais.get(i).getCodigo()) == null) {
//                                todasGradesAmostrais.get(i).setFlagsensor(false);
//                                todasGradesAmostrais.get(i).setUsuario(userInfo.getUsuario());
//                                todasGradesAmostrais.get(i).setArea(areaDAO.buscaAreaByCodigo(codigo));
//                                ClientResponse responsePontoAmostral = client.getResource("pontoamostral", userInfo.getUsuario().getToken());
//                                String jsonPontoAmostral = responsePontoAmostral.getEntity(String.class);
//
//                                gsonPontoAmostral = new GsonBuilder()
//                                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
//                                        .create();
//
//                                List<PontoAmostral> pontosAmostrais = new ArrayList<>();
//                                List<PontoAmostral> pontosAmostraisMeuBanco = new ArrayList<>();
//
//                                switch (responsePontoAmostral.getStatus()) {
//                                    case 200:
//                                        pontosAmostrais = (List<PontoAmostral>) gsonPontoAmostral.fromJson(jsonPontoAmostral, new TypeToken<ArrayList<PontoAmostral>>() {
//                                        }.getType());
//                                        for (int j = 0; j < pontosAmostrais.size(); j++) {
//                                            if (pontosAmostrais.get(j).getGradeAmostral().getCodigo().equals(todasGradesAmostrais.get(i).getCodigo())) {
//                                                pontosAmostrais.get(j).setThe_geom(reader.read(pontosAmostrais.get(j).getGeom()));
//                                                pontosAmostrais.get(j).setGradeAmostral(todasGradesAmostrais.get(i));
//                                                pontosAmostraisMeuBanco.add(pontosAmostrais.get(j));
//                                            }
//                                        }
//                                        gradeAmostral = todasGradesAmostrais.get(i);
//                                        gradeAmostral.setPontoAmostral(pontosAmostraisMeuBanco);
//                                        break;
//                                    case 404:
//                                        pontosAmostrais = new ArrayList<>();
//                                        break;
//                                    default:
//                                        MensagemRetorno mensagemPontoAmostral = gsonPontoAmostral.fromJson(jsonPontoAmostral, MensagemRetorno.class);
//                                        throw new PPATException(mensagemPontoAmostral.getMessage());
//                                }
//                                gradeAmostralDAO.adicionar(gradeAmostral);
//                            }
//                        }
//                    }
//                    break;
//                case 404:
//                    todasGradesAmostrais = new ArrayList<>();
//                    break;
//                default:
//                    MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
//                    throw new PPATException(mensagem.getMessage());
//            }

            Area area = areaDAO.buscarPorId(id);

            ClientResponse responseAtributo = client.getResource("atributo", userInfo.getUsuario().getToken());
            String jsonAtributo = responseAtributo.getEntity(String.class);

            gsonAtributo = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            List<Atributo> atributo = new ArrayList<>();

            switch (responseAtributo.getStatus()) {
                case 200:
                    atributo = (List<Atributo>) gsonAtributo.fromJson(jsonAtributo, new TypeToken<ArrayList<Atributo>>() {
                    }.getType());
                    break;
                case 404:
                    atributo = new ArrayList<>();
                    break;
                default:
                    System.out.println("Erro get atributo api na lista grade amostral");
                //MensagemRetorno mensagemAtributo = gsonAtributo.fromJson(jsonAtributo, MensagemRetorno.class);
                //throw new PPATException(mensagemAtributo.getMessage());
            }
            System.out.println("listar -------- " + id + "  projeto " + area.getProjeto().getId() + "  listagrades " + gradeAmostralDAO.listaGradeAmostralByArea(id) + " atributo: " + atributo);
            result
                    .include("idArea", id)
                    .include("idProjeto", area.getProjeto().getId())
                    .include("listaGradeAmostral", gradeAmostralDAO.listaGradeAmostralByArea(id))
                    //.include("listaGradeAmostral", gradesAmostraisDaArea)
                    .include("idAreaAPI", area.getCodigo())
                    .include("listaAtributo", atributo);
        } catch (Exception e) {
            System.out.println("**********************************erro: " + e);
            result.redirectTo(MapaController.class).mapa(areaDAO.buscarPorId(id).getProjeto().getId());
        }
    }

    
    @Path("exportartxt")
    @Permissao(Roles.ON)
    public void exportartxt(Long id, HttpServletResponse response) {
        GradeAmostral gradeAmostral = gradeAmostralDAO.buscarPorId(id);
        try {
            String send = "Long\tLat" + System.getProperty("line.separator");
            List<PontoAmostral> pontoamostral = gradeAmostral.getPontoAmostral();
            for (int i = 0; i < pontoamostral.size(); i ++) {
                String coordenadaspixel = pontoamostral.get(i).getThe_geom().toString();
                coordenadaspixel = coordenadaspixel.replace("POINT (", "");
                coordenadaspixel = coordenadaspixel.replace(")", "");
                //System.out.println("pixel.tostring() " + coordenadaspixel);
                String arraypixel[] = coordenadaspixel.split(" ");
                send += arraypixel[0] + "\t" + arraypixel[1] + "\t"+ System.getProperty("line.separator");
            }
            byte[] report = send.getBytes();
            response.setContentType("application/save");
            response.setContentLength(report.length);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + gradeAmostral.getDescricao() + ".txt\"");
            response.addHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            try (ServletOutputStream outputStream = response.getOutputStream()) {
                outputStream.write(report, 0, report.length);
                outputStream.flush();
            }
            result.include("idProjeto", gradeAmostral.getArea().getProjeto().getId()).redirectTo(this).lista(gradeAmostral.getArea().getId());
        } catch (Exception ex) {
            System.out.println("Exception" + ex);
        }
    }
    
    @Path("formulario")
    @Permissao(Roles.ON)
    public void formulario(Long id) {

        // Area area = areaDAO.buscaAreaByCodigo(codigo);
        Area area = areaDAO.buscarPorId(id);

        result
                .include("idArea", id)
                .include("idProjeto", area.getProjeto().getId())
                .include("idAreaAPI", area.getCodigo())
                .include("nomeProjeto", area.getDescricao());
    }

    @Path("sincronizar")
    @Permissao(Roles.ON)
    public void sincronizar(Long id) {
        GradeAmostral gradesincr = gradeAmostralDAO.buscarPorId(id);
        Area area = areaDAO.buscarPorId(gradesincr.getArea().getId());
        if (area.getCodigo() == null) { //se a área referente a grade amostral não está na api, entra no if para criá-la
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
                    System.out.println("Erro post area api no sincronizar grade amostral");
                    //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                    //System.out.println("mensagem json: " + mensagem.toString() + " \n response status: " + response.getStatus());
                } else {
                    int fim = json.indexOf("\"descricao");
                    Long aux = Long.parseLong(json.substring(10, fim - 1));
                    area.setCodigo(aux);
                    areaDAO.alterar(area);
                }
            } catch (PPATException ex) {
                System.out.println("Exception post area api no sincronizar grade amostral: " + ex);
            }

        } else { //caso área já está na api - vai para POST ou PUT de grade amostral
            try {
                if (gradesincr.getCodigo() == null) { //Nova grade amostral, POST API
                    GradeAmostral gradeamostral = new GradeAmostral();
                    gradeamostral.setDescricao(gradesincr.getDescricao());
                    gradeamostral.setNomeTabela(gradesincr.getDescricao());
                    gradeamostral.setFlagsensor(gradesincr.isFlagsensor());
                    Area areamostra = new Area();
                    areamostra.setCodigo(area.getCodigo());
                    gradeamostral.setArea(areamostra);
                    Usuario usuario = new Usuario();
                    usuario.setCodigo(usuarioSession.getUsuario().getCodigo());
                    gradeamostral.setUsuario(usuario);
                    gsonGradeAmostral = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                    ClientResponse responseAmostra = client.postResource("gradeamostral", userInfo.getUsuario().getToken(), gsonGradeAmostral.toJson(gradeamostral));
                    String jsonAmostra = responseAmostra.getEntity(String.class);
                    System.out.println("json" + jsonAmostra + " response " + responseAmostra.getStatus());
                    if (responseAmostra.getStatus() != 201) {
                        System.out.println("Erro post grade amostral api no sincronizar grade amostral");
                        //MensagemRetorno mensagem = gsonGradeAmostral.fromJson(jsonAmostra, MensagemRetorno.class);
                        //System.out.println("mensagem de erro = " + mensagem.toString());
                        //throw new PPATException(mensagem.getMessage());
                    } else {
                        int fimamostra = jsonAmostra.indexOf("\"descricao");
                        Long codamostra = Long.parseLong(jsonAmostra.substring(10, fimamostra - 1));
                        gradesincr.setCodigo(codamostra);
                        gradeAmostralDAO.alterar(gradesincr);
                        List<PontoAmostral> pontoamostral = gradesincr.getPontoAmostral();
                        for (int k = 0; k < pontoamostral.size(); k++) {//cria pixel de cada amostra na api
                            GeometryJSON gamostra = new GeometryJSON();
                            pontoamostral.get(k).setGeom("{" + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + "," + '"' + "geometry" + '"' + ":" + gamostra.toString(pontoamostral.get(k).getThe_geom()) + "}");
                            String geompa = pontoamostral.get(k).getGeom();
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
                            PontoAmostral pontoa = new PontoAmostral();
                            pontoa.setGeom(pa);
                            gradeamostral.setCodigo(codamostra);
                            pontoa.setGradeAmostral(gradeamostral);
                            gsonPontoAmostral = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                            ClientResponse responsePontoAmostral = client.postResource("pontoamostral", userInfo.getUsuario().getToken(), gsonPontoAmostral.toJson(pontoa));
                            String jsonPixelAmostra = responsePontoAmostral.getEntity(String.class);
                            System.out.println("json" + jsonPixelAmostra + " response " + responsePontoAmostral.getStatus());
                            if (responsePontoAmostral.getStatus() != 201) {
                                System.out.println("Erro post ponto amostral api no sincronizar grade amostral");
                                //MensagemRetorno mensagemPixelAmostra = gsonPontoAmostral.fromJson(jsonPixelAmostra, MensagemRetorno.class);
                                //System.out.println("mensagem de erro = " + mensagemPixelAmostra.toString());
                                //throw new PPATException(mensagemPixelAmostra.getMessage());
                            } else {
                                int fimcod = jsonPixelAmostra.indexOf("\"geom\"");
                                System.out.println("auxcod-----------: " + jsonPixelAmostra.substring(10, fimcod - 1));
                                Long auxcod = Long.parseLong(jsonPixelAmostra.substring(10, fimcod - 1));
                                pontoamostral.get(k).setCodigo(auxcod);
                                pontoAmostralDAO.alterar(pontoamostral.get(k));
                                //fim parte que salva na api

                            }
                        }
                    }
                } else { //já está na API, da PUT
                    GradeAmostral gradeamostral = new GradeAmostral();
                    gradeamostral.setDescricao(gradesincr.getDescricao());
                    gradeamostral.setNomeTabela(gradesincr.getDescricao());
                    gradeamostral.setFlagsensor(gradesincr.isFlagsensor());
                    Area areamostra = new Area();
                    areamostra.setCodigo(area.getCodigo());
                    gradeamostral.setArea(areamostra);
                    gradeamostral.setCodigo(gradesincr.getCodigo());
                    Usuario usuario = new Usuario();
                    usuario.setCodigo(usuarioSession.getUsuario().getCodigo());
                    gradeamostral.setUsuario(usuario);
                    gsonGradeAmostral = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                    ClientResponse responseAmostra = client.putResource("gradeamostral", userInfo.getUsuario().getToken(), gsonGradeAmostral.toJson(gradeamostral));
                    String jsonAmostra = responseAmostra.getEntity(String.class);
                    System.out.println("json" + jsonAmostra + " response " + responseAmostra.getStatus());
                    if (responseAmostra.getStatus() != 200) {
                        System.out.println("Erro put grade amostral na api no sincronizar grade amostral");
                        //MensagemRetorno mensagem = gsonGradeAmostral.fromJson(jsonAmostra, MensagemRetorno.class);
                        //System.out.println("mensagem de erro = " + mensagem.toString());
                    } else {
                        List<PontoAmostral> pontoamostral = gradesincr.getPontoAmostral();
                        for (int k = 0; k < pontoamostral.size(); k++) {//post ou put do pixel amostra
                            GeometryJSON gamostra = new GeometryJSON();
                            pontoamostral.get(k).setGeom("{" + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + "," + '"' + "geometry" + '"' + ":" + gamostra.toString(pontoamostral.get(k).getThe_geom()) + "}");
                            String geompa = pontoamostral.get(k).getGeom();
                            int iniciogeompa = geompa.lastIndexOf("\"coordinates\":[");
                            geompa = geompa.substring(iniciogeompa);
                            geompa = geompa.replace("\"coordinates\":[", "");
                            String pa = "POINT(";
                            String geompa2 = geompa.replace("[", "");
                            geompa2 = geompa2.replace("]", "");
                            geompa2 = geompa2.replace("}}", "");
                            String arraypa[] = geompa2.split(",");
                            pa += arraypa[0] + " " + arraypa[1] + ")";
                            PontoAmostral pontoa = new PontoAmostral();
                            pontoa.setGeom(pa);
                            pontoa.setGradeAmostral(gradeamostral);
                            gsonPontoAmostral = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                            if (pontoamostral.get(k).getCodigo() == null) {//cria novo ponto na api caso nao tenha la
                                ClientResponse responsePixelAmostra = client.postResource("pontoamostral", userInfo.getUsuario().getToken(), gsonPontoAmostral.toJson(pontoa));
                                String jsonPixelAmostra = responsePixelAmostra.getEntity(String.class);
                                System.out.println("json" + jsonPixelAmostra + " response " + responsePixelAmostra.getStatus());
                                if (responsePixelAmostra.getStatus() != 201) {
                                    System.out.println("Erro post ponto amostral api no sincronizar grade amostral");
                                    //MensagemRetorno mensagemPixelAmostra = gsonPontoAmostral.fromJson(jsonPixelAmostra, MensagemRetorno.class);
                                    //System.out.println("mensagem de erro = " + mensagemPixelAmostra.toString());
                                } else {
                                    int fimcod = jsonPixelAmostra.indexOf("\"geom\"");
                                    System.out.println("auxcod-----------: " + jsonPixelAmostra.substring(10, fimcod - 1));
                                    Long auxcod = Long.parseLong(jsonPixelAmostra.substring(10, fimcod - 1));
                                    pontoamostral.get(k).setCodigo(auxcod);
                                    pontoAmostralDAO.alterar(pontoamostral.get(k));
                                }
                            } else { //atualiza pixel que já está na api
                                pontoa.setCodigo(pontoamostral.get(k).getCodigo());
                                ClientResponse responsePA = client.putResource("pontoamostral", userInfo.getUsuario().getToken(), gsonPontoAmostral.toJson(pontoa));
                                String jsonPA = responsePA.getEntity(String.class);
                                System.out.println("ENTROU NO PUT PIXEL " + responsePA + " codigo pxiel: " + pontoa.getCodigo());
                                if (responsePA.getStatus() != 200) {
                                    System.out.println("Erro put ponto amostral api no sincronizar grade amostral");
                                    // MensagemRetorno mensagemPA = gsonPixelAmostra.fromJson(jsonPA, MensagemRetorno.class);
                                    // throw new PPATException(mensagemPA.getMessage());
                                }

                            }
                        }
                    }
                }
                result.include("success", "success.cadastrar.grade.sincronizada").redirectTo(this).lista(gradesincr.getArea().getId());
            } catch (Exception ex) {
                System.out.println("**********ERRO: " + ex);
                result.include("danger", "warning.nao.sincronizado").redirectTo(this).lista(gradesincr.getArea().getId());
            }
        }
    }

    @Path("editar")
    @Permissao(Roles.ON)
    public void editar(Long id) {
        //GradeAmostral gradeAmostral = gradeAmostralDAO.buscaGradeAmostralByCodigo(codigo);
        try {
            GradeAmostral gradeAmostral = gradeAmostralDAO.buscarPorId(id);

            GeometryJSON g = new GeometryJSON();

            List<String> foo = new ArrayList<String>();
            for (int i = 0; i < gradeAmostral.getPontoAmostral().size(); i++) {
                String aux = "{\"type\":\"Point\",\"coordinates\":[";
                aux += gradeAmostral.getPontoAmostral().get(i).getThe_geom().toString();
                aux = aux.replace("POINT (", "");
                aux = aux.replace(" ", ",");
                aux = aux.replace(")", "]}");
                foo.add("{"
                        + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + ","
                        + '"' + "id" + '"' + ":" + '"' + gradeAmostral.getPontoAmostral().get(i).getId() + '"' + ","
                        //   + '"' + "codigo" + '"' + ":" + '"' + gradeAmostral.getPontoAmostral().get(i).getCodigo() + '"' + ","
                        + '"' + "geometry" + '"' + ":" + aux + "}");
            }

            gradeAmostral.getArea().setGeom("{" + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + "," + '"' + "geometry" + '"' + ":" + g.toString(gradeAmostral.getArea().getGeometry()) + "}");
            String jsonPontoAmostral = new Gson().toJson(foo);

            result
                    .include("gradeAmostral", gradeAmostral)
                    .include("idArea", gradeAmostral.getArea().getId())
                    .include("codigoGradeAmostralAPI", gradeAmostral.getCodigo())
                    .include("pontoAmostral", jsonPontoAmostral);
        } catch (Exception e) {
            System.out.println("Exception editar grade amostral: " + e);
            result.redirectTo(this).lista(gradeAmostralDAO.buscarPorId(id).getArea().getId());
        }
    }

    @Post("persiste")
    @Permissao(Roles.ON)
    //salva uma grade amostral - o nome está errado, seria cadastrarGradeAmostral tanto txt como tamx e tamy
    public void cadastrarAmostraTXT(GradeAmostral gradeAmostral, UploadedFile uploadedFile, String latlong, Long idProjeto, boolean formGrade, String salvarAPI, Long idAreaAPI) {
        try {
            System.out.println("ID AREA API ------------ " + idAreaAPI + " salvar api " + salvarAPI + " id projeto " + idProjeto);
            GradeAmostral gradeAmostralMeuBanco = gradeAmostral;
            gradeAmostralMeuBanco.setUsuario(usuarioSession.getUsuario());
            gradeAmostralMeuBanco.setArea(areaDAO.buscarPorId(gradeAmostral.getArea().getId()));
            gradeAmostralMeuBanco.setDataCadastro(new Date());
            //}

            if (!formGrade) {
                gradeAmostralDAO.adicionar(gradeAmostralMeuBanco);
                gradeAmostralDAO.gerarGradeAmostral(gradeAmostralMeuBanco.getArea().getId(), gradeAmostralMeuBanco.getTamx(), gradeAmostralMeuBanco.getTamy(), gradeAmostralMeuBanco.getId());

                if ((salvarAPI.equals("sim")) && !(Objects.equals(idAreaAPI, ""))) {
                    GradeAmostral gradeAmostralAPI = new GradeAmostral();
                    Area area = new Area();
                    area.setCodigo(areaDAO.buscarPorId(gradeAmostral.getArea().getId()).getCodigo());
                    gradeAmostralAPI.setArea(area);
                    gradeAmostralAPI.setDescricao(gradeAmostral.getDescricao());
                    gradeAmostralAPI.setNomeTabela(gradeAmostral.getDescricao());
                    gradeAmostralAPI.setTamx(gradeAmostral.getTamx());
                    gradeAmostralAPI.setTamy(gradeAmostral.getTamy());
                    Usuario user = new Usuario();
                    user.setCodigo(usuarioSession.getUsuario().getCodigo());
                    gradeAmostralAPI.setUsuario(user);
                    //salvando GradeAmostral na api
                    gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                    ClientResponse response;
                    try {
                        response = client.postResource("gradeamostral", userInfo.getUsuario().getToken(), gson.toJson(gradeAmostralAPI));
                        String json = response.getEntity(String.class);

                        if (response.getStatus() != 201) {
                            System.out.println("Erro persiste grade amostral na api");
                            //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                            //System.out.println("mensagem de erro = " + mensagem.toString());
                            //throw new PPATException(mensagem.getMessage());
                        } else {
                            int fim = json.indexOf("\"descricao");
                            Long aux = Long.parseLong(json.substring(10, fim - 1));
                            gradeAmostralMeuBanco.setCodigo(aux);
                            gradeAmostralAPI.setCodigo(aux);
                            gradeAmostralDAO.alterar(gradeAmostralMeuBanco);
                        }
                    } catch (PPATException ex) {
                        System.out.println("PPATEXCEPTION ------ " + ex);
                    }
                    List<PontoAmostral> pontoAmostral = pontoAmostralDAO.buscaPorIdPontoAmostral(gradeAmostralMeuBanco.getId());
                    //salvando pontoAmostral na api
                    GeometryJSON g = new GeometryJSON();
                    for (int i = 0; i < pontoAmostral.size(); i++) {
                        PontoAmostral pontoAmostralAPI = new PontoAmostral();
                        int finalcoord = g.toString(pontoAmostral.get(i).getThe_geom()).indexOf("]");
                        int iniciocoord = g.toString(pontoAmostral.get(i).getThe_geom()).indexOf("[");
                        String geom = "POINT(" + (g.toString(pontoAmostral.get(i).getThe_geom())).substring(iniciocoord + 1, finalcoord - 1) + ")";
                        geom = geom.replace(",", " ");
                        pontoAmostralAPI.setGeom(geom);
                        pontoAmostralAPI.setGradeAmostral(gradeAmostralAPI);
                        gsonPontoAmostral = new GsonBuilder().create();
                        ClientResponse responsePontoAmostral = client.postResource("pontoamostral", userInfo.getUsuario().getToken(), gsonPontoAmostral.toJson(pontoAmostralAPI));
                        String jsonPontoAmostral = responsePontoAmostral.getEntity(String.class);
                        if (responsePontoAmostral.getStatus() != 201) {
                            System.out.println("Erro persiste ponto amostral na api");
                            //MensagemRetorno mensagemPontoAmostral = gsonPontoAmostral.fromJson(jsonPontoAmostral, MensagemRetorno.class);
                            //throw new PPATException(mensagemPontoAmostral.getMessage());
                        } else {
                            int fimcod = jsonPontoAmostral.indexOf("\"geom");
                            Long auxcod = Long.parseLong(jsonPontoAmostral.substring(10, fimcod - 1));
                            pontoAmostral.get(i).setCodigo(auxcod);
                            pontoAmostralDAO.alterar(pontoAmostral.get(i));
                        }
                    }
                    //fim parte que salva na api

                }
                result.include("success", "success.grade.cadastrada").redirectTo(this).lista(gradeAmostralMeuBanco.getArea().getId());
            } else {

                if ((salvarAPI.equals("sim")) && !(Objects.equals(idAreaAPI, ""))) {
                    GradeAmostral gradeAmostralAPI = new GradeAmostral();
                    Area area = new Area();
                    area.setCodigo(areaDAO.buscarPorId(gradeAmostral.getArea().getId()).getCodigo());
                    gradeAmostralAPI.setArea(area);
                    gradeAmostralAPI.setDescricao(gradeAmostral.getDescricao());
                    gradeAmostralAPI.setNomeTabela(gradeAmostral.getDescricao());
                    gradeAmostralAPI.setTamx(gradeAmostral.getTamx());
                    gradeAmostralAPI.setTamy(gradeAmostral.getTamy());
                    Usuario user = new Usuario();
                    user.setCodigo(usuarioSession.getUsuario().getCodigo());
                    gradeAmostralAPI.setUsuario(user);
                    //salvando GradeAmostral na api
                    gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                    ClientResponse response;
                    try {
                        response = client.postResource("gradeamostral", userInfo.getUsuario().getToken(), gson.toJson(gradeAmostralAPI));
                        String json = response.getEntity(String.class);

                        if (response.getStatus() != 201) {
                            System.out.println("Erro persiste grade amostral na api");
                            //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                            //System.out.println("mensagem de erro = " + mensagem.toString());
                            //throw new PPATException(mensagem.getMessage());
                        } else {
                            int fim = json.indexOf("\"descricao");
                            Long aux = Long.parseLong(json.substring(10, fim - 1));
                            gradeAmostralMeuBanco.setCodigo(aux);
                            gradeAmostralAPI.setCodigo(aux);
                            //gradeAmostralDAO.alterar(gradeAmostralMeuBanco);
                        }
                    } catch (PPATException ex) {
                        System.out.println("PPATEXCEPTION ------ " + ex);
                    }

                    GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
                    WKTReader reader = new WKTReader(geometryFactory);

                    List<PontoAmostral> pontoAmostral = new ArrayList<>();

                    try {

                        BufferedReader in = new BufferedReader(new InputStreamReader(uploadedFile.getFile(), "UTF8"));

                        in.readLine();

                        String ponto = "";
                        while ((ponto = in.readLine()) != null) {

                            String[] valores = ponto.split("\t");

                            if (valores.length == 2) {
                                PontoAmostral pa = new PontoAmostral();
                                pa.setGradeAmostral(gradeAmostralMeuBanco);
                                if (latlong.equals("longlat")) {
                                    pa.setThe_geom(reader.read("POINT(" + valores[0] + " " + valores[1] + ")"));
                                } else {
                                    pa.setThe_geom(reader.read("POINT(" + valores[1] + " " + valores[0] + ")"));
                                }
                                pa.getThe_geom().setSRID(projection);
                                pontoAmostral.add(pa);
                            }
                        }
                        in.close();
                    } catch (Exception e) {
                        System.out.println("Exception persiste grade amostral na api: " + e);
                    }
                    gradeAmostralMeuBanco.setPontoAmostral(pontoAmostral);
                    gradeAmostralMeuBanco.setDataCadastro(new Date());
                    gradeAmostralDAO.adicionar(gradeAmostralMeuBanco);
                    List<PontoAmostral> pontosAmostrais = pontoAmostralDAO.buscaPorIdPontoAmostral(gradeAmostralMeuBanco.getId());

                    //salvando pontoAmostral na api
                    GeometryJSON g = new GeometryJSON();
                    for (int i = 0; i < pontosAmostrais.size(); i++) {
                        PontoAmostral pontoAmostralAPI = new PontoAmostral();
                        int finalcoord = g.toString(pontosAmostrais.get(i).getThe_geom()).indexOf("]");
                        int iniciocoord = g.toString(pontosAmostrais.get(i).getThe_geom()).indexOf("[");
                        String geom = "POINT(" + (g.toString(pontosAmostrais.get(i).getThe_geom())).substring(iniciocoord + 1, finalcoord - 1) + ")";
                        geom = geom.replace(",", " ");
                        pontoAmostralAPI.setGeom(geom);
                        pontoAmostralAPI.setGradeAmostral(gradeAmostralAPI);
                        gsonPontoAmostral = new GsonBuilder().create();
                        ClientResponse responsePontoAmostral = client.postResource("pontoamostral", userInfo.getUsuario().getToken(), gsonPontoAmostral.toJson(pontoAmostralAPI));
                        String jsonPontoAmostral = responsePontoAmostral.getEntity(String.class);
                        if (responsePontoAmostral.getStatus() != 201) {
                            System.out.println("Erro post ponto amostral na api");
                            //MensagemRetorno mensagemPontoAmostral = gsonPontoAmostral.fromJson(jsonPontoAmostral, MensagemRetorno.class);
                            //throw new PPATException(mensagemPontoAmostral.getMessage());
                        } else {
                            int fimcod = jsonPontoAmostral.indexOf("\"geom");
                            Long auxcod = Long.parseLong(jsonPontoAmostral.substring(10, fimcod - 1));
                            pontosAmostrais.get(i).setCodigo(auxcod);
                            pontoAmostralDAO.alterar(pontosAmostrais.get(i));
                        }
                    }
                    //fim parte que salva na api
                    result.include("success", "success.grade.cadastrada").redirectTo(this).lista(gradeAmostral.getArea().getId());
                } else {
                    GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
                    WKTReader reader = new WKTReader(geometryFactory);

                    List<PontoAmostral> pontoAmostral = new ArrayList<>();

                    try {

                        BufferedReader in = new BufferedReader(new InputStreamReader(uploadedFile.getFile(), "UTF8"));

                        in.readLine();

                        String ponto = "";
                        while ((ponto = in.readLine()) != null) {

                            String[] valores = ponto.split("\t");

                            if (valores.length == 2) {
                                PontoAmostral pa = new PontoAmostral();
                                pa.setGradeAmostral(gradeAmostralMeuBanco);
                                if (latlong.equals("longlat")) {
                                    pa.setThe_geom(reader.read("POINT(" + valores[0] + " " + valores[1] + ")"));
                                } else {
                                    pa.setThe_geom(reader.read("POINT(" + valores[1] + " " + valores[0] + ")"));
                                }
                                pa.getThe_geom().setSRID(projection);
                                pontoAmostral.add(pa);
                            }
                        }
                        in.close();
                    } catch (Exception e) {
                        System.out.println("Exception persiste grade amostral na api: "+e);
                    }
                    gradeAmostralMeuBanco.setPontoAmostral(pontoAmostral);
                    gradeAmostralMeuBanco.setDataCadastro(new Date());
                    gradeAmostralDAO.adicionar(gradeAmostralMeuBanco);
                    result.include("success", "success.grade.cadastrada").redirectTo(this).lista(gradeAmostral.getArea().getId());
                }

            }
        } catch (Exception ex) {
            System.out.println("Último catch Exception no persiste grade amostral na api: "+ex);
            result.include("danger", "danger.grade.cadastrada").include("success", "success.grade.cadastrada").redirectTo(this).lista(gradeAmostral.getArea().getId());
            //result.include("mensagem", ex.getMessage());
        }
    }

    @Post("editar/persiste")
    @Permissao(Roles.ON)
    public void editarGradeAmostral(GradeAmostral gradeAmostral, String pontosAmostrais, String salvarAPI) {

        if (salvarAPI.equals("nao")) {
            try {

                GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
                WKTReader reader = new WKTReader(geometryFactory);

                if (!(gradeAmostral.getDescricao().equals(gradeAmostralDAO.buscarPorId(gradeAmostral.getId()).getDescricao()))) {
                    gradeAmostral = gradeAmostralDAO.buscarPorId(gradeAmostral.getId());
                    gradeAmostralDAO.alterar(gradeAmostral);
                } else {
                    gradeAmostral = gradeAmostralDAO.buscarPorId(gradeAmostral.getId());
                }

                List<PontoAmostral> pontoGradeAmostral = new ArrayList<>();
                List<PontoAmostral> pontosAmostraisList = new Gson().fromJson(pontosAmostrais, new TypeToken<ArrayList<PontoAmostral>>() {
                }.getType());

                for (int i = 0; i < pontosAmostraisList.size(); i++) {

                    pontosAmostraisList.get(i).setThe_geom(reader.read(pontosAmostraisList.get(i).getGeom()));
                    pontosAmostraisList.get(i).getThe_geom().setSRID(projection);
                    pontosAmostraisList.get(i).setGradeAmostral(gradeAmostral);
                    PontoAmostral pontoaux = pontoAmostralDAO.buscarPorId(pontosAmostraisList.get(i).getId());
                    pontosAmostraisList.get(i).setCodigo(pontoaux.getCodigo());
                    pontoAmostralDAO.alterar(pontosAmostraisList.get(i));
                    pontoAmostralDAO.alterar(pontosAmostraisList.get(i));
                    pontoGradeAmostral.add(pontosAmostraisList.get(i));
                }

                gradeAmostral.setPontoAmostral(pontoGradeAmostral);
                List<PontoAmostral> pAmostral = pontoAmostralDAO.buscaPorIdPontoAmostral(gradeAmostral.getId());
                pontosAmostraisList = new ArrayList<>();

                boolean ajuda = true;

                for (int i = 0; i < pAmostral.size(); i++) {
                    for (int j = 0; j < pontoGradeAmostral.size(); j++) {

                        if (pontoGradeAmostral.get(j).getId().equals(pAmostral.get(i).getId())) {
                            ajuda = false;
                            System.out.println("ajuda: " + ajuda);
                        }
                    }

                    if (ajuda == true) {
                        System.out.println("------ENTROU NO IF DE AJUDA=TRUE");
                        pontosAmostraisList.add(pAmostral.get(i));
                    } else {
                        ajuda = true;
                    }
                }

                for (int i = 0; i < pontosAmostraisList.size(); i++) {
                    gradeAmostralDAO.alterar(gradeAmostral);
                    pontoAmostralDAO.excluir(pontosAmostraisList.get(i));

                }
                result.include("success", "success.gradeamostral.editada").redirectTo(this).lista(gradeAmostral.getArea().getId());

            } catch (JsonSyntaxException | ParseException | FactoryRegistryException e) {
                System.out.println("CATCH AMOSTRA NÃO EDITADA: " + e);
                result.include("danger", "danger.gradeamostral.editada.nao.foi.possivel").redirectTo(this).lista(gradeAmostral.getArea().getId());
            }
        } else {
            try {

                GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
                WKTReader reader = new WKTReader(geometryFactory);
                GradeAmostral gradeAmostralAPI = new GradeAmostral();
                gradeAmostralAPI.setCodigo(gradeAmostral.getCodigo());
                gradeAmostralAPI.setDescricao(gradeAmostral.getDescricao());
                gradeAmostralAPI.setTamx(gradeAmostral.getTamx());
                gradeAmostralAPI.setTamy(gradeAmostral.getTamy());
                gradeAmostralAPI.setNomeTabela(gradeAmostral.getDescricao());
                Area area = new Area();
                area.setCodigo(gradeAmostral.getArea().getCodigo());
                gradeAmostralAPI.setArea(area);
                Usuario usuario = new Usuario();
                usuario.setCodigo(usuarioSession.getUsuario().getCodigo());
                //if (!(gradeAmostral.getDescricao().equals(gradeAmostralDAO.buscaGradeAmostralByCodigo(gradeAmostral.getCodigo()).getDescricao()))) {
                if (!(gradeAmostral.getDescricao().equals(gradeAmostralDAO.buscarPorId(gradeAmostral.getId()).getDescricao()))) {
                    gsonGradeAmostral = new GsonBuilder().create();
                    ClientResponse responseGA;
                    try {
                        responseGA = client.putResource("gradeamostral", userInfo.getUsuario().getToken(), gsonGradeAmostral.toJson(gradeAmostralAPI));
                        String jsonGA = responseGA.getEntity(String.class);

                        if (responseGA.getStatus() != 200) {
                            System.out.println("Erro put grade amostral api");
                            //MensagemRetorno mensagemGA = gsonGradeAmostral.fromJson(jsonGA, MensagemRetorno.class);
                            //System.out.println("mensagem resposta put grade amostral na api " + mensagemGA);
                        } else {
                            gradeAmostralDAO.alterar(gradeAmostral);
                            gradeAmostral = gradeAmostralDAO.buscarPorId(gradeAmostral.getId());
                        }
                    } catch (PPATException ex) {
                        System.out.println("ENTROU NO PPAT EXCEPTION PUT GRADE ---" + ex);
                    }
                }
                List<PontoAmostral> pontoGradeAmostral = new ArrayList<>();
                List<PontoAmostral> pontosAmostraisList = new Gson().fromJson(pontosAmostrais, new TypeToken<ArrayList<PontoAmostral>>() {
                }.getType());

                for (int i = 0; i < pontosAmostraisList.size(); i++) {

                    try {
                        pontosAmostraisList.get(i).setThe_geom(reader.read(pontosAmostraisList.get(i).getGeom()));
                    } catch (ParseException ex) {
                        Logger.getLogger(GradeAmostralController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    pontosAmostraisList.get(i).getThe_geom().setSRID(projection);
                    pontosAmostraisList.get(i).setGradeAmostral(gradeAmostral);
                    PontoAmostral pontoaux = pontoAmostralDAO.buscarPorId(pontosAmostraisList.get(i).getId());
                    pontosAmostraisList.get(i).setCodigo(pontoaux.getCodigo());
                    pontoAmostralDAO.alterar(pontosAmostraisList.get(i));
                    pontoGradeAmostral.add(pontosAmostraisList.get(i));
                    PontoAmostral paAPI = new PontoAmostral();
                    paAPI.setCodigo(pontosAmostraisList.get(i).getCodigo());
                    paAPI.setGeom(pontosAmostraisList.get(i).getGeom());
                    paAPI.setGradeAmostral(gradeAmostralAPI);
                    gsonPontoAmostral = new GsonBuilder().create();
                    ClientResponse responsePA = client.putResource("pontoamostral", userInfo.getUsuario().getToken(), gsonPontoAmostral.toJson(paAPI));
                    String jsonPA = responsePA.getEntity(String.class);

                    if (responsePA.getStatus() != 200) {
                          System.out.println("Erro put ponto amostral api");
                        //MensagemRetorno mensagemPA = gsonPontoAmostral.fromJson(jsonPA, MensagemRetorno.class);
                        //throw new PPATException(mensagemPA.getMessage());
                    }
                }

                //GradeAmostral grade = gradeAmostralDAO.buscaGradeAmostralByCodigo(gradeAmostralAPI.getCodigo());
                gradeAmostral.setPontoAmostral(pontoGradeAmostral);
                List<PontoAmostral> pAmostral = pontoAmostralDAO.buscaPorIdPontoAmostral(gradeAmostral.getId());
                pontosAmostraisList = new ArrayList<>();

                boolean ajuda = true;

                for (int i = 0; i < pAmostral.size(); i++) {
                    for (int j = 0; j < pontoGradeAmostral.size(); j++) {

                        if (pontoGradeAmostral.get(j).getId().equals(pAmostral.get(i).getId())) {
                            ajuda = false;
                            System.out.println("ajuda: " + ajuda);
                        }
                    }

                    if (ajuda == true) {
                        System.out.println("------ENTROU NO IF DE AJUDA=TRUE");
                        pontosAmostraisList.add(pAmostral.get(i));
                    } else {
                        ajuda = true;
                    }
                }

                for (int i = 0; i < pontosAmostraisList.size(); i++) {
                    gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                            .create();

                    ClientResponse response = client.deleteResource("pontoamostral/" + pontosAmostraisList.get(i).getCodigo(), userInfo.getUsuario().getToken());
                    String json = response.getEntity(String.class);
                    if (response.getStatus() != 200) {
                        MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                        throw new PPATException(mensagem.getMessage());
                    } else {
                        gradeAmostralDAO.alterar(gradeAmostral);
                        pontoAmostralDAO.excluir(pontosAmostraisList.get(i));
                    }

                }
                result.include("success", "success.gradeamostral.editada").redirectTo(this).lista(gradeAmostral.getArea().getId());

            } catch (Exception e) {
                System.out.println("CATCH AMOSTRA NÃO EDITADA: " + e);
                result.include("danger", "danger.gradeamostral.editada.nao.foi.possivel").redirectTo(this).lista(gradeAmostral.getArea().getId());
            }

        }
    }

    @Get("excluir")
    @Permissao(Roles.ON)
    public void excluir(Long id, String excluirapi) {

        GradeAmostral gradeAmostral = gradeAmostralDAO.buscarPorId(id);
        Area area = areaDAO.buscarPorId(gradeAmostral.getArea().getId());
        if (excluirapi.equals("nao")) {
            try {
                gradeAmostralDAO.excluir(gradeAmostral);
                result.include("success", "success.excluir.gradeamostral").redirectTo(this).lista(area.getId());
            } catch (Exception e) {
                System.out.println("CATCH AMOSTRA NÃO EXCLUÍDA: " + e);
                result.include("danger", "danger.excluir.gradeamostral").redirectTo(this).lista(area.getId());

            }
        } else {
            try {
                ClientResponse responseGetPontoA = client.getResource("pontoamostral", userInfo.getUsuario().getToken());
                String jsonGetPontoA = responseGetPontoA.getEntity(String.class);

                gsonPontoAmostral = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create();
                List<PontoAmostral> pontoAmostral = new ArrayList<>();
                int tamArray = 0;
                switch (responseGetPontoA.getStatus()) {
                    case 200:
                        pontoAmostral = (List<PontoAmostral>) gsonPontoAmostral.fromJson(jsonGetPontoA, new TypeToken<ArrayList<PontoAmostral>>() {
                        }.getType());
                        tamArray = pontoAmostral.size();
                        break;
                    case 404:
                        pontoAmostral = new ArrayList<>();
                        break;
                    default:
                          System.out.println("Erro get ponto da grade amostral api");
                       // MensagemRetorno mensagemGetPontoA = gsonPontoAmostral.fromJson(jsonGetPontoA, MensagemRetorno.class);
                        //throw new PPATException(mensagemGetPontoA.getMessage());
                }
                for (int i = 0; i < tamArray; i++) {
                    Long codGradeAmostral = pontoAmostral.get(i).getGradeAmostral().getCodigo();
                    if (codGradeAmostral.equals(gradeAmostral.getCodigo())) {
                        gson = new GsonBuilder()
                                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                                .create();
                        ClientResponse responseDelPontoA = client.deleteResource("pontoamostral/" + pontoAmostral.get(i).getCodigo(), userInfo.getUsuario().getToken());
                        String jsonDelPontoA = responseDelPontoA.getEntity(String.class);
                        if (responseDelPontoA.getStatus() != 200) {
                           System.out.println("Erro excluir ponto da grade amostral api");
                           // MensagemRetorno mensagemDelPontoA = gson.fromJson(jsonDelPontoA, MensagemRetorno.class);
                           // throw new PPATException(mensagemDelPontoA.getMessage());
                        }
                    }
                }
                gsonGradeAmostral = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create();
                ClientResponse response = client.deleteResource("gradeamostral/" + gradeAmostral.getCodigo(), userInfo.getUsuario().getToken());
                String json = response.getEntity(String.class);
                if (response.getStatus() != 200) {
                    System.out.println("Erro excluir grade amostral api");
                    //MensagemRetorno mensagem = gsonGradeAmostral.fromJson(json, MensagemRetorno.class);
                    //throw new PPATException(mensagem.getMessage());
                } else {
                    gradeAmostralDAO.excluir(gradeAmostral);
                }
                result.include("success", "success.excluir.gradeamostral").redirectTo(this).lista(area.getId());
            } catch (Exception e) {
                System.out.println("CATCH AMOSTRA NÃO EXCLUÍDA: " + e);
                result.include("danger", "danger.excluir.gradeamostral").redirectTo(this).lista(area.getId());

            }
        }
    }

    /*
    @Post("/cadastrar/gradeamostral/txt")
    @Permissao(Roles.ON)
    public void cadastrarGradeAmostralTXT(GradeAmostral gradeAmostral, UploadedFile uploadedFile, String latlong, Long idProjeto) {

        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
        WKTReader reader = new WKTReader(geometryFactory);

        if (gradeAmostral.getId() == null) {

            gradeAmostral.setDataCadastro(new Date());
            gradeAmostral.setUsuario(usuarioSession.getUsuario());
            gradeAmostral.setArea(areaDAO.buscarPorId(gradeAmostral.getArea().getId()));

            List<PontoAmostral> pontoAmostral = new ArrayList<PontoAmostral>();

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(uploadedFile.getFile(), "UTF8"));

                in.readLine();

                String ponto = "";
                while ((ponto = in.readLine()) != null) {

                    String[] valores = ponto.split("\t");

                    if (valores.length == 3) {

                        PontoAmostral pa = new PontoAmostral();
                        pa.setGradeAmostral(gradeAmostral);

                        if (latlong.equals("longlat")) {
                            pa.setThe_geom(reader.read("POINT(" + valores[1] + " " + valores[0] + ")"));
                        } else {
                            pa.setThe_geom(reader.read("POINT(" + valores[0] + " " + valores[1] + ")"));
                        }

                        pa.getThe_geom().setSRID(projection);

                        pontoAmostral.add(pa);
                    }
                }

                in.close();

            } catch (Exception e) {
            }
            gradeAmostral.setPontoAmostral(pontoAmostral);
            gradeAmostralDAO.adicionar(gradeAmostral);

        }
    }*/
    @Post("adicionaramostra")
    @Permissao(Roles.ON)
    public void formularioAmostraApartirGrade(Amostra amostra, GradeAmostral gradeAmostral,
            boolean formCriaAmostra
    ) {

        if (formCriaAmostra) {
            try {
                GradeAmostral gradeAmostralAux = gradeAmostralDAO.buscarPorId(gradeAmostral.getId());
                Area area = areaDAO.buscarPorId(gradeAmostral.getArea().getId());
                //area.setCodigo(gradeAmostralAux.getArea().getCodigo());
                //amostra.setArea(area);
                amostra.setNomeTabela(amostra.getDescricao());
                //gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                // ClientResponse response = client.postResource("amostra", userInfo.getUsuario().getToken(), gson.toJson(amostra));
                // String json = response.getEntity(String.class);
                // int fim = json.indexOf("\"descricao");
                // Long aux = Long.parseLong(json.substring(10, fim - 1));
                // amostra.setCodigo(aux);
                Amostra amostraMeuBanco = amostra;
                //if (response.getStatus() != 201) {
                //     MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                //     System.out.println("mensagem de erro = " + mensagem.toString());
                //     throw new PPATException(mensagem.getMessage());
                //  } else {
                amostraMeuBanco.setDataCadastro(new Date());
                amostraMeuBanco.setUsuario(usuarioSession.getUsuario());
                amostraMeuBanco.setArea(area);
                amostraMeuBanco.setAtributo(atributoDAO.buscaPorCodigo(amostra.getAtributo().getCodigo()));
                amostraDAO.adicionar(amostraMeuBanco);
                //  }

                //PixelAmostra pixelamostra = new PixelAmostra();
                List<PixelAmostra> pixelAmostras = new ArrayList<>();
                List<PontoAmostral> pontoAmostral = new ArrayList<>();
                pontoAmostral = gradeAmostralAux.getPontoAmostral();

                for (int i = 0; i < pontoAmostral.size(); i++) {

                    PixelAmostra pixelamostra = new PixelAmostra();
                    //PixelAmostra pixelamostraAPI = new PixelAmostra();
                    pixelamostra.setValor(0);
                    //pixelamostraAPI.setValor(0);
                    pixelamostra.setGeometry(pontoAmostral.get(i).getThe_geom());
                    //pixelamostraAPI.setGeom((pontoAmostral.get(i).getThe_geom().toString()).replace("POINT ", "POINT"));
                    pixelamostra.getGeometry().setSRID(projection);
                    //Amostra amostraAPI = new Amostra();
                    //amostraAPI.setCodigo(amostra.getCodigo());
                    //pixelamostraAPI.setAmostra(amostraAPI);
                    pixelamostra.setAmostra(amostraMeuBanco);
                    //salvando pixelamostra na api
                    //gsonPixelAmostra = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                    // ClientResponse responsePixelAmostra = client.postResource("pixelamostra", userInfo.getUsuario().getToken(), gsonPixelAmostra.toJson(pixelamostraAPI));
                    //String jsonPixelAmostra = responsePixelAmostra.getEntity(String.class);

                    //if (responsePixelAmostra.getStatus() != 201) {
                    //    MensagemRetorno mensagemPixelAmostra = gsonPixelAmostra.fromJson(jsonPixelAmostra, MensagemRetorno.class);
                    //    System.out.println("mensagem de erro = " + mensagemPixelAmostra.toString());
                    //    throw new PPATException(mensagemPixelAmostra.getMessage());
                    // } else {
                    //    int fimcod = jsonPixelAmostra.indexOf("\"geom");
                    //    Long auxcod = Long.parseLong(jsonPixelAmostra.substring(10, fimcod - 1));
                    //     pixelamostra.setCodigo(auxcod);
                    pixelAmostras.add(pixelamostra);
                    // }

                }

                amostraMeuBanco.setPixelAmostra(pixelAmostras);
                amostraDAO.alterar(amostraMeuBanco);
                result
                        .include("success", "success.amostra.cadastrada").redirectTo(AmostraController.class
                ).lista(area.getId());
            } catch (Exception e) {
                System.out.println("CATCH EXCEPTION ---- " + e);
                result.redirectTo(this).lista(gradeAmostral.getArea().getId());
            }

        }

    }

    /*
    @Post("modal/gradeamostral")
    public void novaGradeAmostral(GradeAmostral grade) {

        grade.setDataCadastro(new Date());
        grade.setUsuario(usuarioSession.getUsuario());
        grade.setArea(areaDAO.buscarPorId(grade.getArea().getId()));

        gradeAmostralDAO.gerarGradeAmostral(grade.getArea().getId(), grade.getTamx(), grade.getTamy(), grade.getId());

        result.include("success", "success.grade.nova");
    }*/
}
