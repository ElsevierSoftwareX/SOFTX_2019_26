package br.com.sdum.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.RollbackException;

import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.hibernate.exception.ConstraintViolationException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import static br.com.caelum.vraptor.view.Results.json;
import br.com.sdum.annotation.Permissao;
import br.com.sdum.dao.AmostraDAO;
import br.com.sdum.dao.AreaDAO;
import br.com.sdum.dao.AtributoDAO;
import br.com.sdum.dao.ClassificacaoDAO;
import br.com.sdum.dao.EntidadeDAO;
import br.com.sdum.dao.GradeAmostralDAO;
import br.com.sdum.dao.PixelAmostraDAO;
import br.com.sdum.dao.PontoAmostralDAO;
import br.com.sdum.dao.ProjetoDAO;
import br.com.sdum.dao.TipoSoloDAO;
import br.com.sdum.dao.UnidadeMedidaDAO;
import br.com.sdum.enums.Roles;
import br.com.sdum.exception.PPATException;
import br.com.sdum.model.Amostra;
import br.com.sdum.model.Area;
import br.com.sdum.model.Atributo;
import br.com.sdum.model.Classe;
import br.com.sdum.model.Classificacao;
import br.com.sdum.model.Entidade;
import br.com.sdum.model.GradeAmostral;
import br.com.sdum.model.MensagemRetorno;
import br.com.sdum.model.PixelAmostra;
import br.com.sdum.model.PontoAmostral;
import br.com.sdum.model.TipoSolo;
import br.com.sdum.model.UnidadeMedida;
import br.com.sdum.model.Usuario;
import br.com.sdum.service.ConsultaService;
import br.com.sdum.session.UsuarioSession;
import br.com.sdum.ws.client.PPATWSClient;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.ClientResponse;
import com.vividsolutions.jts.geom.MultiPolygon;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

@Resource
@Path("projeto/area")
public class AreaController implements Serializable {

    private static final long serialVersionUID = 6770785282469692484L;

    private static int projection = 4326;

    @Inject
    private UsuarioSession usuarioSession;

    @Inject
    private Result result;

    @Inject
    private AreaDAO areaDAO;

    @Inject
    private ProjetoDAO projetoDAO;

    @Inject
    private UnidadeMedidaDAO unidadeMedidaDAO;

    @Inject
    private AtributoDAO atributoDAO;

    @Inject
    private AmostraDAO amostraDAO;

    @Inject
    private GradeAmostralDAO gradeAmostralDAO;

    @Inject
    private ClassificacaoDAO classificacaoDAO;

    @Inject
    private TipoSoloDAO tipoSoloDAO;

    @Inject
    private PixelAmostraDAO pixelamostraDAO;

    @Inject
    private PontoAmostralDAO pontoamostralDAO;

    @Inject
    private EntidadeDAO entidadeDAO;

    @Inject
    private PPATWSClient client;

    @Inject
    private UsuarioSession userInfo;

    private Gson gson, gsonAmostra, gsonAtributo, gsonGradeAmostral, gsonUnidadeMedida, gsonPixelAmostra, gsonPontoAmostral;

    private Gson gsonSolo, gsonSolo2, gsonarea, gsonentidade, gsonclassificacao, gsonclasse;

    @Get("lista")
    @Permissao(Roles.ON)
    public void lista(Long id) {
        try {
            Usuario user = usuarioSession.getUsuario();
            ClientResponse response = client.getResource("area", userInfo.getUsuario().getToken());
            String json = response.getEntity(String.class);
            ClientResponse responseSolo = client.getResource("solo", userInfo.getUsuario().getToken());
            String jsonSolo = responseSolo.getEntity(String.class);
            /* ClientResponse responseAmostra = client.getResource("amostra", userInfo.getUsuario().getToken());
            String jsonAmostra = responseAmostra.getEntity(String.class);
            ClientResponse responseAtributo = client.getResource("atributo", userInfo.getUsuario().getToken());
            String jsonAtributo = responseAtributo.getEntity(String.class);
            ClientResponse responseGradeAmostral = client.getResource("gradeamostral", userInfo.getUsuario().getToken());
            String jsonGradeAmostral = responseGradeAmostral.getEntity(String.class);
            ClientResponse responseUnidadeMedida = client.getResource("unidademedida", userInfo.getUsuario().getToken());
            String jsonUnidadeMedida = responseUnidadeMedida.getEntity(String.class);
            ClientResponse responsePixelAmostra = client.getResource("pixelamostra", userInfo.getUsuario().getToken());
            String jsonPixelAmostra = responsePixelAmostra.getEntity(String.class);
            ClientResponse responsePontoAmostral = client.getResource("pontoamostral", userInfo.getUsuario().getToken());
            String jsonPontoAmostral = responsePontoAmostral.getEntity(String.class);
            ClientResponse responseEntidade = client.getResource("entidade", userInfo.getUsuario().getToken());
            String jsonEntidade = responseEntidade.getEntity(String.class);
            ClientResponse responseClassificacao = client.getResource("classificacao", userInfo.getUsuario().getToken());
            String jsonClassificacao = responseClassificacao.getEntity(String.class);
            ClientResponse responseClasse = client.getResource("classe", userInfo.getUsuario().getToken());
            String jsonClasse = responseClasse.getEntity(String.class);
             */
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            gsonSolo = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            /*gsonAmostra = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            gsonAtributo = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            gsonGradeAmostral = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            gsonUnidadeMedida = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            gsonPixelAmostra = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            gsonPontoAmostral = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            gsonentidade = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            gsonclassificacao = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            gsonclasse = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
             */
            List<Area> area = new ArrayList<>();
            List<TipoSolo> tipoSolo = new ArrayList<>();
            /*List<Amostra> amostra = new ArrayList<>();
            List<Atributo> atributo = new ArrayList<>();
            List<GradeAmostral> gradeAmostral = new ArrayList<>();
            List<UnidadeMedida> unidadeMedida = new ArrayList<>();
            List<PixelAmostra> pixelAmostra = new ArrayList<>();
            List<PontoAmostral> pontoAmostral = new ArrayList<>();
            List<Entidade> entidade = new ArrayList<>();
            List<Classificacao> classificacao = new ArrayList<>();
            List<Classe> classe = new ArrayList<>();
             */
            switch (response.getStatus()) {
                case 200:
                    area = (List<Area>) gson.fromJson(json, new TypeToken<ArrayList<Area>>() {
                    }.getType());
                    if (responseSolo.getStatus() == 200) {
                        tipoSolo = (List<TipoSolo>) gsonSolo.fromJson(jsonSolo, new TypeToken<ArrayList<TipoSolo>>() {
                        }.getType());
                    }
                    /*  if (responseAmostra.getStatus() == 200) {
                        amostra = (List<Amostra>) gsonAmostra.fromJson(jsonAmostra, new TypeToken<ArrayList<Amostra>>() {
                        }.getType());
                    }
                    if (responseAtributo.getStatus() == 200) {
                        atributo = (List<Atributo>) gsonAtributo.fromJson(jsonAtributo, new TypeToken<ArrayList<Atributo>>() {
                        }.getType());
                    }
                    if (responseGradeAmostral.getStatus() == 200) {
                        gradeAmostral = (List<GradeAmostral>) gsonGradeAmostral.fromJson(jsonGradeAmostral, new TypeToken<ArrayList<GradeAmostral>>() {
                        }.getType());
                    }
                    if (responseUnidadeMedida.getStatus() == 200) {
                        unidadeMedida = (List<UnidadeMedida>) gsonUnidadeMedida.fromJson(jsonUnidadeMedida, new TypeToken<ArrayList<UnidadeMedida>>() {
                        }.getType());
                    }
                    if (responsePixelAmostra.getStatus() == 200) {
                        pixelAmostra = (List<PixelAmostra>) gsonPixelAmostra.fromJson(jsonPixelAmostra, new TypeToken<ArrayList<PixelAmostra>>() {
                        }.getType());
                    }
                    if (responsePontoAmostral.getStatus() == 200) {
                        pontoAmostral = (List<PontoAmostral>) gsonPontoAmostral.fromJson(jsonPontoAmostral, new TypeToken<ArrayList<PontoAmostral>>() {
                        }.getType());
                    }
                    if (responseEntidade.getStatus() == 200) {
                        entidade = (List<Entidade>) gsonentidade.fromJson(jsonEntidade, new TypeToken<ArrayList<Entidade>>() {
                        }.getType());
                    }
                    if (responseClassificacao.getStatus() == 200) {
                        classificacao = (List<Classificacao>) gsonclassificacao.fromJson(jsonClassificacao, new TypeToken<ArrayList<Classificacao>>() {
                        }.getType());
                    }
                    if (responseClasse.getStatus() == 200) {
                        classe = (List<Classe>) gsonclasse.fromJson(jsonClasse, new TypeToken<ArrayList<Classe>>() {
                        }.getType());
                    }
                    for (int j = 0; j < unidadeMedida.size(); j++) {
                        UnidadeMedida um = unidadeMedidaDAO.buscaUnidadeByCodigo(unidadeMedida.get(j).getCodigo());
                        if (um == null) {
                            um = unidadeMedida.get(j);
                            um.getUsuario().setId(user.getId());
                            unidadeMedidaDAO.adicionar(um);
                        }
                    }
                    for (int j = 0; j < entidade.size(); j++) {
                        Entidade ent = entidadeDAO.buscaEntidadeByCodigo(entidade.get(j).getCodigo());
                        if (ent == null) {
                            ent = entidade.get(j);
                            ent.getUsuario().setId(user.getId());
                            entidadeDAO.adicionar(ent);
                        }
                    }
                    for (int j = 0; j < atributo.size(); j++) {
                        Atributo atr = atributoDAO.buscaPorCodigo(atributo.get(j).getCodigo());
                        if (atr == null) {
                            atr = atributo.get(j);
                            UnidadeMedida umPT = unidadeMedidaDAO.buscaUnidadeByCodigo(atr.getUnidadeMedidaPT().getCodigo());
                            atr.setUnidadeMedidaPT(umPT);
                            UnidadeMedida umEN = unidadeMedidaDAO.buscaUnidadeByCodigo(atr.getUnidadeMedidaEN().getCodigo());
                            atr.setUnidadeMedidaEN(umEN);
                            UnidadeMedida umES = unidadeMedidaDAO.buscaUnidadeByCodigo(atr.getUnidadeMedidaES().getCodigo());
                            atr.setUnidadeMedidaES(umES);
                            atributoDAO.adicionar(atr);
                        }
                    }*/

                    //for (int i = 0; i < area.size(); i++) {
                    //    Area areameubanco = areaDAO.buscaAreaByCodigo(area.get(i).getCodigo());
                    //    if (areameubanco == null) {
                    //        Long idSoloAux = area.get(i).getTipoSolo().getCodigo();
                    //        ClientResponse responseArea = client.getResource("area/" + area.get(i).getCodigo(), userInfo.getUsuario().getToken());
                    //        String jsonArea = responseArea.getEntity(String.class);
//
                    //        gsonarea = new GsonBuilder()
                    //                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    //                .create();
//
                    //      Area areafor = gsonarea.fromJson(jsonArea, Area.class);
                    // int iniciocoord = jsonArea.indexOf("MULTIPOLYGON");
                    // int finalcoord = jsonArea.lastIndexOf(")))");
                    // String poligonoAux = jsonArea.substring(iniciocoord, finalcoord);
                    // poligonoAux += ")))";
                    // poligonoAux = poligonoAux.replace("MULTIPOLYGON(((", "POLYGON((");
                    // poligonoAux = poligonoAux.replace(")))", "))");
                    // areameubanco = area.get(i);
                    //  GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
                    //  WKTReader reader = new WKTReader(geometryFactory);
//
                    //    try {
                    //        areameubanco.setGeometry(reader.read(poligonoAux));
                    //    } catch (ParseException ex) {
                    //        Logger.getLogger(AreaController.class.getName()).log(Level.SEVERE, null, ex);
                    //     }
                    //    areameubanco.getGeometry().setSRID(projection);
                    //     areameubanco.setUsuario(usuarioSession.getUsuario());
//
                    //    TipoSolo tiposolomeubanco = tipoSoloDAO.buscaSoloByCodigo(idSoloAux);
                    //     if (tiposolomeubanco == null) {
                    //         ClientResponse responseSolo2 = client.getResource("solo/" + idSoloAux, userInfo.getUsuario().getToken());
                    //         String jsonSolo2 = responseSolo2.getEntity(String.class);
//
                    //         gsonSolo2 = new GsonBuilder()
                    //                 .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
//                                        .create();
//
//                                tiposolomeubanco = gsonSolo2.fromJson(jsonSolo2, TipoSolo.class);
//                                List<Usuario> usuarios = new ArrayList<Usuario>();
//                                usuarios.add(usuarioSession.getUsuario());
//                                tiposolomeubanco.getUsuarios().addAll(usuarios);
//                                tiposolomeubanco.setDataCadastro(new Date());
//                                tipoSoloDAO.adicionar(tiposolomeubanco);
//                                TipoSolo solosalvo = tipoSoloDAO.buscaSoloByCodigo(tiposolomeubanco.getCodigo());
//                                areameubanco.setTipoSolo(solosalvo);
//                            } else {
//                                areameubanco.setTipoSolo(tiposolomeubanco);
//                            }
//                            areameubanco.setProjeto(projetoDAO.buscaProjetoById(id));
//                            areaDAO.adicionar(areameubanco);
//                        }
//                    }

                    /* for (int i = 0; i < amostra.size(); i++) {
                        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
                        WKTReader reader = new WKTReader(geometryFactory);
                        List<PixelAmostra> pixelsAmostraisMeuBanco = new ArrayList<>();
                        if (amostraDAO.buscaAmostraByCodigo(amostra.get(i).getCodigo()) == null) {
                            amostra.get(i).setUsuario(userInfo.getUsuario());
                            amostra.get(i).setArea(areaDAO.buscaAreaByCodigo(amostra.get(i).getArea().getCodigo()));
                            amostra.get(i).setAtributo(atributoDAO.buscaPorCodigo(amostra.get(i).getAtributo().getCodigo()));
                            amostraDAO.adicionar(amostra.get(i));
                            Amostra amo = amostraDAO.buscaAmostraByCodigo(amostra.get(i).getCodigo());
                            for (int j = 0; j < pixelAmostra.size(); j++) {
                                if (pixelAmostra.get(j).getAmostra().getCodigo().equals(amostra.get(i).getCodigo())) {
                                    PixelAmostra paAux = pixelAmostra.get(j);
                                    try {
                                        paAux.setGeometry(reader.read(pixelAmostra.get(j).getGeom()));
                                    } catch (ParseException ex) {
                                        Logger.getLogger(AreaController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    paAux.setAmostra(amo);
                                    paAux.getGeometry().setSRID(projection);
                                    pixelsAmostraisMeuBanco.add(paAux);
                                }
                            }
                            amo.setPixelAmostra(pixelsAmostraisMeuBanco);
                            amostraDAO.alterar(amo);
                        }

                    }

                    for (int i = 0; i < gradeAmostral.size(); i++) {
                        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
                        WKTReader reader = new WKTReader(geometryFactory);
                        List<PontoAmostral> pontosAmostraisMeuBanco = new ArrayList<>();
                        if (gradeAmostralDAO.buscaGradeAmostralByCodigo(gradeAmostral.get(i).getCodigo()) == null) {
                            gradeAmostral.get(i).setFlagsensor(false);
                            gradeAmostral.get(i).setUsuario(userInfo.getUsuario());
                            gradeAmostral.get(i).setArea(areaDAO.buscaAreaByCodigo(gradeAmostral.get(i).getArea().getCodigo()));
                            gradeAmostralDAO.adicionar(gradeAmostral.get(i));
                            GradeAmostral ga = gradeAmostralDAO.buscaGradeAmostralByCodigo(gradeAmostral.get(i).getCodigo());
                            for (int j = 0; j < pontoAmostral.size(); j++) {
                                if (pontoAmostral.get(j).getGradeAmostral().getCodigo().equals(gradeAmostral.get(i).getCodigo())) {
                                    PontoAmostral paAux = pontoAmostral.get(j);
                                    try {
                                        paAux.setThe_geom(reader.read(pontoAmostral.get(j).getGeom()));
                                        paAux.getThe_geom().setSRID(projection);
                                    } catch (ParseException ex) {
                                        Logger.getLogger(AreaController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    paAux.setGradeAmostral(ga);
                                    pontosAmostraisMeuBanco.add(paAux);
                                }
                            }
                            ga.setPontoAmostral(pontosAmostraisMeuBanco);
                            gradeAmostralDAO.alterar(ga);
                        }

                    }

                    for (int j = 0; j < classificacao.size(); j++) {
                        Classificacao classif = classificacaoDAO.buscaPorCodigo(classificacao.get(j).getCodigo());
                        List<Classe> classes = new ArrayList<Classe>();
                        if (classif == null) {
                            classif = classificacao.get(j);
                            classif.setUsuario(usuarioSession.getUsuario());
                            classif.setAtributo(atributoDAO.buscaPorCodigo(classificacao.get(j).getAtributo().getCodigo()));
                            classif.setTipoSolo(tipoSoloDAO.buscaSoloByCodigo(classificacao.get(j).getTipoSolo().getCodigo()));
                            classif.setEntidade(entidadeDAO.buscaEntidadeByCodigo(classificacao.get(j).getEntidade().getCodigo()));
                            classificacaoDAO.adicionar(classif);
                            for (int i = 0; i < classe.size(); i++) {
                                if (classe.get(i).getClassificacao().getCodigo().equals(classificacao.get(j).getCodigo())) {
                                    Classe classemeubanco = new Classe();
                                    classemeubanco.setCor(classe.get(i).getCor());
                                    classemeubanco.setNivel(classe.get(i).getNivel());
                                    classemeubanco.setValorMaximo(classe.get(i).getValorMaximo());
                                    classemeubanco.setValorMinimo(classe.get(i).getValorMinimo());
                                    classemeubanco.setClassificacao(classificacaoDAO.buscaPorCodigo(classificacao.get(j).getCodigo()));
                                    classemeubanco.setCodigo(classe.get(i).getCodigo());
                                    classes.add(classemeubanco);
                                }
                            }
                            classif.setClasses(classes);
                            classificacaoDAO.alterar(classif);
                        }
                    }*/
                    break;
                case 404:
                    area = new ArrayList<>();
                    tipoSolo = tipoSoloDAO.listaSolosByUsuario(usuarioSession.getUsuario().getId());
                    break;
                default:
                    result.include("listaAreaProjeto", new ArrayList());
                    result.include("idProjeto", id);
                //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                //throw new PPATException(mensagem.getMessage());
            }

            result.include("idProjeto", id).include("idUsuario", userInfo.getUsuario().getCodigo()).include("listaAreaAPI", area).include("listaAreaProjeto", areaDAO.listaAreaByProjeto(id)).include("listaSolos", tipoSoloDAO.listaSolosByUsuario(usuarioSession.getUsuario().getId()));

        } catch (PPATException ex) {

            System.out.println("-------------------------PPATEXCEPTION " + ex);
            result.include("listaAreaProjeto", new ArrayList());
            //result.include("mensagem", ex.getMessage());
            result.include("idProjeto", id);
        }

    }

    @Path("exportartxt")
    @Permissao(Roles.ON)
    public void exportartxt(Long id, HttpServletResponse response) {
        Area area = areaDAO.buscarPorId(id);
        try {
            String send = "Long\tLat\t" + System.getProperty("line.separator");
            String coordenadasArea = area.getGeometry().toString();
            coordenadasArea = coordenadasArea.replace("POLYGON ((", "");
            coordenadasArea = coordenadasArea.replace("))", "");
            coordenadasArea = coordenadasArea.replace(",", "");
            System.out.println("area.tostring() " + coordenadasArea);
            String arrayarea[] = coordenadasArea.split(" ");
            for (int i = 0; i < arrayarea.length; i += 2) {
                send += arrayarea[i] + "\t" + arrayarea[i + 1] + System.getProperty("line.separator");
            }
            byte[] report = send.getBytes();
            response.setContentType("application/save");
            response.setContentLength(report.length);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + area.getDescricao() + ".txt\"");
            response.addHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            try (ServletOutputStream outputStream = response.getOutputStream()) {
                outputStream.write(report, 0, report.length);
                outputStream.flush();
            }
            result.include("idProjeto", area.getProjeto().getId()).redirectTo(this).lista(area.getProjeto().getId());
        } catch (Exception ex) {
            System.out.println("Exception" + ex);
        }
    }

    @Get("excluir")
    @Permissao(Roles.ON)
    public void excluir(Long id, String excluirapi) {
        if (excluirapi.equals("nao")) {
            Area area = areaDAO.buscarPorId(id);
            Long idProjetoAux = area.getProjeto().getId();
            try {
                areaDAO.excluir(area);
                result.include("success", "success.excluir.area").include("idProjeto", idProjetoAux).redirectTo(this).lista(idProjetoAux);
            } catch (Exception e) {
                result.include("danger", "danger.excluir.area").include("idProjeto", idProjetoAux).redirectTo(this).lista(idProjetoAux);
            }
        } else {
            Long idProjetoAux = areaDAO.buscarPorId(id).getProjeto().getId();
            Long codigo = areaDAO.buscarPorId(id).getCodigo();
            try {

                if (id == null) {
                    throw new PPATException("Código da área não informado.");
                }

                gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create();

                ClientResponse response = client.deleteResource("area/" + codigo, userInfo.getUsuario().getToken());
                String json = response.getEntity(String.class
                );
                System.out.println("json *****" + json);
                if (response.getStatus() != 200) {
                    System.out.println("entrou no status != 200 *****");
                    result.include("danger", "danger.excluir.area").include("idProjeto", idProjetoAux).redirectTo(this).lista(idProjetoAux);
                    //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                    // throw new PPATException(mensagem.getMessage());
                } else {
                    Area area = areaDAO.buscaAreaByCodigo(codigo);
                    if (area == null) {

                    } else {
                        areaDAO.excluir(area);
                        result.include("success", "success.excluir.area").include("idProjeto", idProjetoAux).redirectTo(this).lista(idProjetoAux);
                    }
                }

            } catch (Exception ex) {
                result.include("danger", "danger.excluir.area.api").include("idProjeto", idProjetoAux).redirectTo(this).lista(idProjetoAux);
            }

        }
    }

    // public List<Area> buscaAreaServidor() {
    //    String stringArea = consultaService.buscarAreaUsuario(usuarioSession.getUsuario());
    //    try {
    //        return new Gson().fromJson(stringArea, new TypeToken<List<Area>>() {
    //        }.getType());
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //    }
    //    return null;
    //}
    @Post("persiste/areaTxt")
    @Permissao(Roles.ON)
    public void cadastrarAreaTxt(Area area, UploadedFile uploadedFile, String latlong, String pontos, Long idProjeto, String salvarAPI) {

        try {
            //System.out.println("-----------------------------Valor variável salvar API: " + salvarAPI);
            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
            WKTReader reader = new WKTReader(geometryFactory);
            Area areaAPI = new Area();

            if (area.getCodigo() == null) {
                areaAPI = area;
                areaAPI.setUsuario(usuarioSession.getUsuario());
                areaAPI.setProjeto(projetoDAO.buscarPorId(idProjeto));
                String poligono = "POLYGON((";

                try {

                    BufferedReader s = new BufferedReader(new InputStreamReader(uploadedFile.getFile(), "UTF8"));

                    s.readLine();

                    String[] parts = s.readLine().split("\t");

                    if (parts.length >= 2) {

                        String first = parts[0] + " " + parts[1];
                        if (latlong.equals("latlong")) {
                            first = parts[1] + " " + parts[0];
                        }

                        poligono += first + ", ";

                        String contornoP = null;
                        while ((contornoP = s.readLine()) != null) {

                            parts = contornoP.split("\t");

                            if (parts.length >= 2) {
                                if (latlong.equals("latlong")) {
                                    poligono += parts[1] + " " + parts[0];
                                } else {
                                    poligono += parts[0] + " " + parts[1];
                                }
                                poligono += ", ";
                            }
                        }

                        poligono += first;
                        poligono += "))";

                        areaAPI.setGeometry(reader.read(poligono));
                        areaAPI.getGeometry().setSRID(projection);
                        areaAPI.setDescricao(area.getDescricao());

                        //salvando na api
                        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                        //gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                        System.out.println("gson= " + gson.toString());

                        poligono = poligono.replace("POLYGON((", "SRID=4326;MULTIPOLYGON(((");
                        poligono = poligono.replace("))", ")))");
                        area.setGeom(poligono);

                        TipoSolo tipoSolo = new TipoSolo();
                        tipoSolo = tipoSoloDAO.buscaSoloByCodigo(area.getTipoSolo().getCodigo());

                        if (tipoSolo == null) {
                            ClientResponse responseSolo = client.getResource("solo/" + area.getTipoSolo().getCodigo(), userInfo.getUsuario().getToken());
                            String jsonSolo = responseSolo.getEntity(String.class
                            );

                            gsonSolo = new GsonBuilder()
                                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                                    .create();

                            tipoSolo
                                    = gsonSolo.fromJson(jsonSolo, TipoSolo.class
                                    );
                            List<Usuario> usuarios = new ArrayList<Usuario>();
                            usuarios.add(usuarioSession.getUsuario());
                            tipoSolo.getUsuarios().addAll(usuarios);
                            tipoSoloDAO.adicionar(tipoSolo);
                            TipoSolo solosalvo = tipoSoloDAO.buscaSoloByCodigo(tipoSolo.getCodigo());
                            areaAPI.setTipoSolo(solosalvo);
                        } else {
                            areaAPI.setTipoSolo(tipoSolo);
                        }
                        if ("sim".equals(salvarAPI)) {
                            ClientResponse response = client.postResource("area", userInfo.getUsuario().getToken(), gson.toJson(area));
                            String json = response.getEntity(String.class
                            );
                            int fim = json.indexOf("\"descricao");
                            Long aux = Long.parseLong(json.substring(10, fim - 1));
                            areaAPI.setCodigo(aux);
                            //fim parte que salva na api  

                            if (response.getStatus() != 201) {
                                //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                                //System.out.println("mensagem de erro = " + mensagem.toString());
                                //throw new PPATException(mensagem.getMessage());
                                result.include("danger", "danger.id.area.nao.selecionada").include("idProjeto", idProjeto).redirectTo(this).lista(idProjeto);
                            } else {
                                areaAPI.setDataCadastro(new Date());
                                areaAPI.setProjeto(projetoDAO.buscarPorId(idProjeto));
                                areaDAO.adicionar(areaAPI); //salvando banco da aplicação
                            }
                        } else {
                            areaAPI.setDataCadastro(new Date());
                            areaAPI.setProjeto(projetoDAO.buscarPorId(idProjeto));
                            areaDAO.adicionar(areaAPI); //salvando banco da aplicação
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                result.include("success", "success.cadastrar.area.txt").include("idProjeto", idProjeto).redirectTo(this).lista(idProjeto);

            } else {

                result.include("danger", "danger.id.area.nao.selecionada").include("idProjeto", idProjeto).redirectTo(this).lista(idProjeto);

            }
        } catch (Exception ex) {
            System.out.println("Exception cadastrar txt: " + ex);
            result.include("danger", "danger.id.area.nao.selecionada").include("idProjeto", idProjeto).redirectTo(this).lista(idProjeto);
        }
    }

    @Get("formulario")
    @Permissao(Roles.ON)
    public void formulario(Long id, String editar) {
        try {
            System.out.println("----------EDITAR " + editar);
            if ("editar".equals(editar)) {
                Long idProjeto = areaDAO.buscarPorId(id).getProjeto().getId();
                result
                        .include("idProjeto", idProjeto)
                        .include("idArea", id)
                        .include("editar", editar)
                        .include("listaSolos", tipoSoloDAO.listaSolosByUsuario(usuarioSession.getUsuario().getId()));
            } else {
                result
                        .include("idProjeto", id)
                        .include("editar", editar)
                        .include("listaSolos", tipoSoloDAO.listaSolosByUsuario(usuarioSession.getUsuario().getId()));
            }
        } catch (Exception ex) {
            System.out.println("Exception formulario: " + ex);
            result.include("idProjeto", areaDAO.buscarPorId(id).getProjeto().getId())
                    .include("editar", editar)
                    .include("listaSolos", tipoSoloDAO.listaSolosByUsuario(usuarioSession.getUsuario().getId()));

        }
    }

    @Get("editar")
    @Permissao(Roles.ON)
    public void editar(Long id) throws ParseException {
        Area area = areaDAO.buscarPorId(id);
        try {
            //Area area = areaDAO.buscarPorId(id);
            /*ClientResponse response = client.getResource("area/" + codigo, userInfo.getUsuario().getToken());
            String json = response.getEntity(String.class);

           gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

            area = gson.fromJson(json, Area.class);

            switch (response.getStatus()) {
                case 200:
                    int iniciocoord = json.indexOf("MULTIPOLYGON");
                    int finalcoord = json.lastIndexOf(")))");
                    String poligono = json.substring(iniciocoord, finalcoord);
                    poligono += ")))";
                    Area areaAux = areaDAO.buscaAreaByCodigo(codigo);
                    TipoSolo tipoSolo = new TipoSolo();
                    if (areaAux == null) {
                        areaAux = area;
                        tipoSolo = tipoSoloDAO.buscaSoloByCodigo(area.getTipoSolo().getCodigo());
                        if (tipoSolo == null) {
                            ClientResponse responseSolo = client.getResource("solo/" + area.getTipoSolo().getCodigo(), userInfo.getUsuario().getToken());
                            String jsonSolo = responseSolo.getEntity(String.class
                            );

                            gsonSolo = new GsonBuilder()
                                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                                    .create();

                            tipoSolo
                                    = gsonSolo.fromJson(jsonSolo, TipoSolo.class
                                    );
                            List<Usuario> usuarios = new ArrayList<Usuario>();
                            usuarios.add(usuarioSession.getUsuario());
                            tipoSolo.getUsuarios().addAll(usuarios);
                            tipoSoloDAO.adicionar(tipoSolo);
                            TipoSolo solosalvo = tipoSoloDAO.buscaSoloByCodigo(tipoSolo.getCodigo());
                            areaAux.setTipoSolo(solosalvo);
                        } else {
                            areaAux.setTipoSolo(tipoSolo);
                        }
                        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
                        WKTReader reader = new WKTReader(geometryFactory);

                        areaAux.setGeometry(reader.read(poligono));
                        areaAux.getGeometry().setSRID(projection);
                        areaAux.setUsuario(usuarioSession.getUsuario());
                        areaDAO.adicionar(areaAux);
                    }
                    // GeometryJSON g = new GeometryJSON();
                    String aux = "{\"type\":\"Feature\",\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[";
                    String auxgeom = area.getGeom();
                    auxgeom = auxgeom.replace("MULTIPOLYGON(((", "");
                    auxgeom = auxgeom.replace(")))", "");
                    auxgeom = auxgeom.replace(",", "],[");
                    auxgeom = auxgeom.replace(" ", ",");
                    String finalgeom = "]]]}}";
                    aux += auxgeom + finalgeom;
                    areaAux.setGeom(aux);
                    //areaAux.setGeom("{" + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + "," + '"' + "geometry" + '"' + ":" + g.toString(areaAux.getGeometry()) + "}");
                    result.include("area", areaAux).include("idProjeto", areaAux.getProjeto().getId()).redirectTo("formulario?codigo=" + area.getCodigo());
             */
            GeometryJSON g = new GeometryJSON();
            area.setGeom("{" + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + "," + '"' + "geometry" + '"' + ":" + g.toString(area.getGeometry()) + "}");
            String editar = "editar";
            result.include("area", area).include("editar", editar).include("idProjeto", area.getProjeto().getId()).redirectTo(this).formulario(area.getId(), editar);
            // break;

            //case 403:
            //    break;
            //default:
            //   MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class
            //   );
            //   throw new PPATException(mensagem.getMessage());
            //}
            // } catch (PPATException ex) {
        } catch (Exception ex) {
            System.out.println("Exception editar area: " + ex);
            result.include("idProjeto", area.getProjeto().getId());
            result.redirectTo(this).lista(userInfo.getUsuario().getId());
        }

        //GeometryJSON g = new GeometryJSON();
        //area.setGeom("{" + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + "," + '"' + "geometry" + '"' + ":" + g.toString(area.getGeometry()) + "}");
        // result.include("area", area).redirectTo("formulario?id=" + area.getProjeto().getId());
    }

    @Post("sincronizar/server")
    @Permissao(Roles.ON)
    public void sincronizarServer(String[] camposMarcados, Long idProjeto) {
        Area areaprojeto = areaDAO.buscarPorId(Long.parseLong(camposMarcados[0]));
        Usuario usuarioprojeto = new Usuario();
        usuarioprojeto.setCodigo(usuarioSession.getUsuario().getCodigo());
        try {
            for (int i = 0; i < camposMarcados.length; i++) { //para cada área que for marcada para sincronizar
                Area area = areaDAO.buscarPorId(Long.parseLong(camposMarcados[i]));
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
                if (area.getCodigo() == null) {//se a área não está na api, entra no if para criá-la
                    gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

                    ClientResponse response = client.postResource("area", userInfo.getUsuario().getToken(), gson.toJson(areaAPI));
                    String json = response.getEntity(String.class);
                    System.out.println("json" + json + " response " + response.getStatus());

                    //fim parte que salva na api
                    if (response.getStatus() != 201) {
                        //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                        result.include("danger", "warning.nao.sincronizado").include("idProjeto", areaprojeto.getProjeto().getId()).redirectTo(this).lista(areaprojeto.getProjeto().getId());
                        //System.out.println("mensagem json: " + mensagem.toString() + " \n response status: " + response.getStatus());
                    } else {
                        int fim = json.indexOf("\"descricao");
                        Long aux = Long.parseLong(json.substring(10, fim - 1));
                        area.setCodigo(aux);
                        areaDAO.alterar(area);
                        /*  List<Amostra> listAmostras = new ArrayList<>();
                        listAmostras = amostraDAO.listaAmostraByArea(area.getId());
                        for (int j = 0; j < listAmostras.size(); j++) {//para cada amostra da área, salva na api (pois entra nesse for somente se a área não estava na api 
                            Amostra amostra = new Amostra();
                            amostra.setDescricao(listAmostras.get(j).getDescricao());
                            amostra.setNomeTabela(listAmostras.get(j).getDescricao());
                            Atributo atrAPI = new Atributo();
                            atrAPI.setCodigo(listAmostras.get(j).getAtributo().getCodigo());
                            amostra.setAtributo(atrAPI);
                            Area areamostra = new Area();
                            areamostra.setCodigo(area.getCodigo());
                            amostra.setArea(areamostra);
                            Usuario usuario = new Usuario();
                            usuario.setCodigo(usuarioSession.getUsuario().getCodigo());
                            amostra.setUsuario(usuarioprojeto);
                            gsonAmostra = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                            ClientResponse responseAmostra = client.postResource("amostra", userInfo.getUsuario().getToken(), gsonAmostra.toJson(amostra));
                            String jsonAmostra = responseAmostra.getEntity(String.class);
                            System.out.println("json" + jsonAmostra + " response " + responseAmostra.getStatus());
                            if (responseAmostra.getStatus() != 201) {
                                MensagemRetorno mensagem = gsonAmostra.fromJson(jsonAmostra, MensagemRetorno.class);
                                System.out.println("mensagem de erro = " + mensagem.toString());
                                throw new PPATException(mensagem.getMessage());
                            } else {
                                int fimamostra = jsonAmostra.indexOf("\"descricao");
                                Long codamostra = Long.parseLong(jsonAmostra.substring(10, fimamostra - 1));
                                listAmostras.get(j).setCodigo(codamostra);
                                amostraDAO.alterar(listAmostras.get(j));
                                List<PixelAmostra> pixelamostra = listAmostras.get(j).getPixelAmostra();
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
                                        MensagemRetorno mensagemPixelAmostra = gsonPixelAmostra.fromJson(jsonPixelAmostra, MensagemRetorno.class);
                                        System.out.println("mensagem de erro = " + mensagemPixelAmostra.toString());
                                        throw new PPATException(mensagemPixelAmostra.getMessage());
                                    } else {
                                        int fimcod = jsonPixelAmostra.indexOf("\"geom\"");
                                        System.out.println("auxcod-----------: " + jsonPixelAmostra.substring(10, fimcod - 1));
                                        Long auxcod = Long.parseLong(jsonPixelAmostra.substring(10, fimcod - 1));
                                        pixelamostra.get(k).setCodigo(auxcod);
                                        pixelamostraDAO.alterar(pixelamostra.get(k));
                                        //fim parte que salva na api

                                    }
                                }
                            }
                        }
                        List<GradeAmostral> listGradesAmostrais = new ArrayList<>();
                        listGradesAmostrais = gradeAmostralDAO.listaGradeAmostralByArea(area.getId());
                        for (int j = 0; j < listGradesAmostrais.size(); j++) {//cria grade para cada grade da área nova na api
                            GradeAmostral gradeAmostral = listGradesAmostrais.get(j);
                            Usuario usuario = new Usuario();
                            usuario.setCodigo(usuarioSession.getUsuario().getCodigo());
                            gradeAmostral.setUsuario(usuarioprojeto);
                            gsonGradeAmostral = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                            ClientResponse responseGradeAmostral = client.postResource("gradeamostral", userInfo.getUsuario().getToken(), gsonGradeAmostral.toJson(gradeAmostral));
                            String jsonGradeAmostral = responseGradeAmostral.getEntity(String.class);
                            System.out.println("json" + jsonGradeAmostral + " response " + responseGradeAmostral.getStatus());
                            if (responseGradeAmostral.getStatus() != 201) {
                                MensagemRetorno mensagem = gsonGradeAmostral.fromJson(jsonGradeAmostral, MensagemRetorno.class);
                                System.out.println("mensagem de erro = " + mensagem.toString());
                                throw new PPATException(mensagem.getMessage());
                            } else {
                                int fimcodgrade = jsonGradeAmostral.indexOf("\"descricao");
                                Long codgrade = Long.parseLong(jsonGradeAmostral.substring(10, fimcodgrade - 1));
                                gradeAmostral.setCodigo(codgrade);
                                gradeAmostralDAO.alterar(gradeAmostral);
                                List<PontoAmostral> pontoamostral = gradeAmostral.getPontoAmostral();
                                for (int k = 0; k < pontoamostral.size(); k++) {//cria cada ponto amostral de cada grade nova na api
                                    GeometryJSON ggradeamostra = new GeometryJSON();
                                    pontoamostral.get(k).setGeom("{" + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + "," + '"' + "geometry" + '"' + ":" + ggradeamostra.toString(pontoamostral.get(k).getThe_geom()) + "}");
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
                                    //pontoamostral.get(k).setGeom(pa);
                                    PontoAmostral pontoa = new PontoAmostral();
                                    pontoa.setGeom(pa);
                                    pontoa.setGradeAmostral(gradeAmostral);
                                    gsonPontoAmostral = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                                    ClientResponse responsePontoAmostral = client.postResource("pontoamostral", userInfo.getUsuario().getToken(), gsonPontoAmostral.toJson(pontoa));
                                    String jsonPontoAmostral = responsePontoAmostral.getEntity(String.class);
                                    System.out.println("json" + jsonPontoAmostral + " response " + responsePontoAmostral.getStatus());
                                    if (responsePontoAmostral.getStatus() != 201) {
                                        MensagemRetorno mensagemPontoAmostral = gsonPontoAmostral.fromJson(jsonPontoAmostral, MensagemRetorno.class);
                                        System.out.println("mensagem de erro = " + mensagemPontoAmostral.toString());
                                        throw new PPATException(mensagemPontoAmostral.getMessage());
                                    } else {
                                        int fimcodponto = jsonPontoAmostral.indexOf("\"geom\"");
                                        System.out.println("auxcod-----------: " + jsonPontoAmostral.substring(10, fimcodponto - 1));
                                        Long auxcodponto = Long.parseLong(jsonPontoAmostral.substring(10, fimcodponto - 1));
                                        pontoamostral.get(k).setCodigo(auxcodponto);
                                        pontoamostralDAO.alterar(pontoamostral.get(k));
                                        //fim parte que salva na api

                                    }
                                }
                            }
                        }*/
                    }
                } else {
                    //PUT na API caso área já existe
                    areaAPI.setCodigo(area.getCodigo());
                    gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                    ClientResponse response = client.putResource("area", userInfo.getUsuario().getToken(), gson.toJson(areaAPI));
                    String json = response.getEntity(String.class
                    );
                    //fim do PUT API da área

                    if (response.getStatus() != 200) {
                        result.include("danger", "warning.nao.sincronizado").include("idProjeto", areaprojeto.getProjeto().getId()).redirectTo(this).lista(areaprojeto.getProjeto().getId());
                        //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                        //throw new PPATException(mensagem.getMessage());
                    } else {
                        int fim = json.indexOf("\"descricao");
                        Long aux = Long.parseLong(json.substring(10, fim - 1));
                        area.setCodigo(aux);
                        areaDAO.alterar(area);
                    }
                    /*else {
                        List<Amostra> listAmostras = amostraDAO.listaAmostraByArea(area.getId());
                        for (int j = 0; j < listAmostras.size(); j++) {
                            if (listAmostras.get(j).getCodigo() == null) { //post de nova amostra caso nao existe na api
                                Amostra amostra = new Amostra();
                                amostra.setDescricao(listAmostras.get(j).getDescricao());
                                amostra.setNomeTabela(listAmostras.get(j).getDescricao());
                                Atributo atrAPI = new Atributo();
                                atrAPI.setCodigo(listAmostras.get(j).getAtributo().getCodigo());
                                amostra.setAtributo(atrAPI);
                                Area areamostra = new Area();
                                areamostra.setCodigo(area.getCodigo());
                                amostra.setArea(areamostra);
                                Usuario usuario = new Usuario();
                                usuario.setCodigo(usuarioSession.getUsuario().getCodigo());
                                amostra.setUsuario(usuarioprojeto);
                                gsonAmostra = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                                ClientResponse responseAmostra = client.postResource("amostra", userInfo.getUsuario().getToken(), gsonAmostra.toJson(amostra));
                                String jsonAmostra = responseAmostra.getEntity(String.class);
                                System.out.println("json" + jsonAmostra + " response " + responseAmostra.getStatus());
                                if (responseAmostra.getStatus() != 201) {
                                    MensagemRetorno mensagem = gsonAmostra.fromJson(jsonAmostra, MensagemRetorno.class);
                                    System.out.println("mensagem de erro = " + mensagem.toString());
                                    throw new PPATException(mensagem.getMessage());
                                } else {
                                    int fimamostra = jsonAmostra.indexOf("\"descricao");
                                    Long codamostra = Long.parseLong(jsonAmostra.substring(10, fimamostra - 1));
                                    listAmostras.get(j).setCodigo(codamostra);
                                    amostra.setCodigo(codamostra);
                                    amostraDAO.alterar(listAmostras.get(j));
                                    usuario.setCodigo(usuarioSession.getUsuario().getCodigo());
                                    amostra.setUsuario(usuarioprojeto);
                                    List<PixelAmostra> pixelamostra = listAmostras.get(j).getPixelAmostra();
                                    for (int k = 0; k < pixelamostra.size(); k++) {//post pixel amostra da nova amostra
                                        GeometryJSON gamostra = new GeometryJSON();
                                        pixelamostra.get(k).setGeom("{" + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + "," + '"' + "geometry" + '"' + ":" + gamostra.toString(pixelamostra.get(k).getGeometry()) + "}");
                                        String geompa = pixelamostra.get(k).getGeom();
                                        int iniciogeompa = geompa.lastIndexOf("\"coordinates\":[");
                                        geompa = geompa.substring(iniciogeompa);
                                        geompa = geompa.replace("\"coordinates\":[", "");
                                        String pa = "POINT(";
                                        geompa = geompa.replace("]", "");
                                        geompa = geompa.replace("}}", "");
                                        String arraypa[] = geompa.split(",");
                                        pa += arraypa[0] + " " + arraypa[1] + ")";
                                        PixelAmostra pixela = new PixelAmostra();
                                        pixela.setGeom(pa);
                                        pixela.setAmostra(amostra);
                                        pixela.setValor(pixelamostra.get(k).getValor());
                                        gsonPixelAmostra = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                                        ClientResponse responsePixelAmostra = client.postResource("pixelamostra", userInfo.getUsuario().getToken(), gsonPixelAmostra.toJson(pixela));
                                        String jsonPixelAmostra = responsePixelAmostra.getEntity(String.class);
                                        System.out.println("json" + jsonPixelAmostra + " response " + responsePixelAmostra.getStatus());
                                        if (responsePixelAmostra.getStatus() != 201) {
                                            MensagemRetorno mensagemPixelAmostra = gsonPixelAmostra.fromJson(jsonPixelAmostra, MensagemRetorno.class);
                                            System.out.println("mensagem de erro = " + mensagemPixelAmostra.toString());
                                            throw new PPATException(mensagemPixelAmostra.getMessage());
                                        } else {
                                            int fimcod = jsonPixelAmostra.indexOf("\"geom\"");
                                            System.out.println("auxcod-----------: " + jsonPixelAmostra.substring(10, fimcod - 1));
                                            Long auxcod = Long.parseLong(jsonPixelAmostra.substring(10, fimcod - 1));
                                            pixelamostra.get(k).setCodigo(auxcod);
                                            pixelamostraDAO.alterar(pixelamostra.get(k));
                                            //fim parte que salva na api

                                        }
                                    }
                                }
                            } else { //amostra já existe na api então PUT
                                Amostra amostra = new Amostra();
                                amostra.setDescricao(listAmostras.get(j).getDescricao());
                                amostra.setNomeTabela(listAmostras.get(j).getDescricao());
                                Atributo atrAPI = new Atributo();
                                atrAPI.setCodigo(listAmostras.get(j).getAtributo().getCodigo());
                                amostra.setAtributo(atrAPI);
                                Area areamostra = new Area();
                                areamostra.setCodigo(area.getCodigo());
                                amostra.setArea(areamostra);
                                amostra.setCodigo(listAmostras.get(j).getCodigo());
                                Usuario usuario = new Usuario();
                                usuario.setCodigo(usuarioSession.getUsuario().getCodigo());
                                amostra.setUsuario(usuarioprojeto);
                                gsonAmostra = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                                ClientResponse responseAmostra = client.putResource("amostra", userInfo.getUsuario().getToken(), gsonAmostra.toJson(amostra));
                                String jsonAmostra = responseAmostra.getEntity(String.class);
                                System.out.println("json" + jsonAmostra + " response " + responseAmostra.getStatus());
                                if (responseAmostra.getStatus() != 200) {
                                    MensagemRetorno mensagem = gsonAmostra.fromJson(jsonAmostra, MensagemRetorno.class);
                                    System.out.println("mensagem de erro = " + mensagem.toString());
                                } else {
                                    List<PixelAmostra> pixelamostra = listAmostras.get(j).getPixelAmostra();
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
                                                pixelamostraDAO.alterar(pixelamostra.get(k));
                                            }
                                        } else { //atualiza pixel que já está na api
                                            pixela.setCodigo(pixelamostra.get(k).getCodigo());
                                            ClientResponse responsePA = client.putResource("pixelamostra", userInfo.getUsuario().getToken(), gsonPixelAmostra.toJson(pixela));
                                            String jsonPA = responsePA.getEntity(String.class);

                                            if (responsePA.getStatus() != 200) {
                                                MensagemRetorno mensagemPA = gsonPixelAmostra.fromJson(jsonPA, MensagemRetorno.class);
                                                throw new PPATException(mensagemPA.getMessage());
                                            }

                                        }
                                    }
                                }*/
                }
            }
            /* List<GradeAmostral> listgrades = new ArrayList<>();
                        listgrades = gradeAmostralDAO.listaGradeAmostralByArea(area.getId());
                        for (int j = 0; j < listgrades.size(); j++) {
                            if (listgrades.get(j).getCodigo() == null) { //post de nova grade amostral caso nao existe na api
                                GradeAmostral gradeamostral = new GradeAmostral();
                                gradeamostral.setDescricao(listgrades.get(j).getDescricao());
                                gradeamostral.setNomeTabela(listgrades.get(j).getDescricao());
                                Area areamostra = new Area();
                                areamostra.setCodigo(area.getCodigo());
                                gradeamostral.setArea(areamostra);
                                gradeamostral.setTamx(listgrades.get(j).getTamx());
                                Usuario usuario = new Usuario();
                                usuario.setCodigo(usuarioSession.getUsuario().getCodigo());
                                gradeamostral.setUsuario(usuarioprojeto);
                                gsonGradeAmostral = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                                ClientResponse responseGradeAmostral = client.postResource("gradeamostral", userInfo.getUsuario().getToken(), gsonGradeAmostral.toJson(gradeamostral));
                                String jsonGradeAmostral = responseGradeAmostral.getEntity(String.class);
                                System.out.println("json" + jsonGradeAmostral + " response " + responseGradeAmostral.getStatus());
                                if (responseGradeAmostral.getStatus() != 201) {
                                    MensagemRetorno mensagem = gsonGradeAmostral.fromJson(jsonGradeAmostral, MensagemRetorno.class);
                                    System.out.println("mensagem de erro = " + mensagem.toString());
                                    throw new PPATException(mensagem.getMessage());
                                } else {
                                    int fimamostra = jsonGradeAmostral.indexOf("\"descricao");
                                    Long codamostra = Long.parseLong(jsonGradeAmostral.substring(10, fimamostra - 1));
                                    listgrades.get(j).setCodigo(codamostra);
                                    gradeamostral.setCodigo(codamostra);
                                    gradeAmostralDAO.alterar(listgrades.get(j));
                                    List<PontoAmostral> pontoamostral = listgrades.get(j).getPontoAmostral();
                                    for (int k = 0; k < pontoamostral.size(); k++) {//post de cada ponto amostral na api
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
                                        pontoamostral.get(k).setGeom(pa);
                                        PontoAmostral pontoa = new PontoAmostral();
                                        pontoa.setGeom(pa);
                                        pontoa.setGradeAmostral(gradeamostral);
                                        gsonPontoAmostral = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                                        ClientResponse responsePontoAmostral = client.postResource("pontoamostral", userInfo.getUsuario().getToken(), gsonPontoAmostral.toJson(pontoa));
                                        String jsonPontoAmostral = responsePontoAmostral.getEntity(String.class);
                                        System.out.println("json" + jsonPontoAmostral + " response " + responsePontoAmostral.getStatus());
                                        if (responsePontoAmostral.getStatus() != 201) {
                                            MensagemRetorno mensagemPontoAmostral = gsonPontoAmostral.fromJson(jsonPontoAmostral, MensagemRetorno.class);
                                            System.out.println("mensagem de erro = " + mensagemPontoAmostral.toString());
                                            throw new PPATException(mensagemPontoAmostral.getMessage());
                                        } else {
                                            int fimcod = jsonPontoAmostral.indexOf("\"geom\"");
                                            System.out.println("auxcod-----------: " + jsonPontoAmostral.substring(10, fimcod - 1));
                                            Long auxcod = Long.parseLong(jsonPontoAmostral.substring(10, fimcod - 1));
                                            pontoamostral.get(k).setCodigo(auxcod);
                                            pontoamostralDAO.alterar(pontoamostral.get(k));
                                            //fim parte que salva grade e pontos amostrais na api

                                        }
                                    }
                                }
                            } else { // grade amostral já existe na api então PUT
                                GradeAmostral gradeamostral = new GradeAmostral();
                                gradeamostral.setDescricao(listgrades.get(j).getDescricao());
                                gradeamostral.setNomeTabela(listgrades.get(j).getDescricao());
                                Area areamostra = new Area();
                                areamostra.setCodigo(area.getCodigo());
                                gradeamostral.setArea(areamostra);
                                gradeamostral.setCodigo(listgrades.get(j).getCodigo());
                                Usuario usuario = new Usuario();
                                usuario.setCodigo(usuarioSession.getUsuario().getCodigo());
                                gradeamostral.setUsuario(usuarioprojeto);
                                gsonGradeAmostral = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                                ClientResponse responseGradeAmostral = client.putResource("gradeamostral", userInfo.getUsuario().getToken(), gsonGradeAmostral.toJson(gradeamostral));
                                String jsonGradeAmostral = responseGradeAmostral.getEntity(String.class);
                                System.out.println("json" + jsonGradeAmostral + " response " + responseGradeAmostral.getStatus());
                                if (responseGradeAmostral.getStatus() != 200) {
                                    MensagemRetorno mensagem = gsonGradeAmostral.fromJson(jsonGradeAmostral, MensagemRetorno.class);
                                    System.out.println("mensagem de erro = " + mensagem.toString());
                                } else {
                                    List<PontoAmostral> pontoamostral = listgrades.get(j).getPontoAmostral();
                                    for (int k = 0; k < pontoamostral.size(); k++) {//verifica se já tem ponto amostral-PUT, senão POST de ponto amostral
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
                                        pontoamostral.get(k).setGeom(pa);
                                        gsonPontoAmostral = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                                        if (pontoamostral.get(k).getCodigo() == null) {//cria novo ponto amostral na api caso nao tenha lá
                                            PontoAmostral pontoa = new PontoAmostral();
                                            pontoa.setGeom(pa);
                                            pontoa.setGradeAmostral(gradeamostral);
                                            ClientResponse responsePontoAmostral = client.postResource("pontoamostral", userInfo.getUsuario().getToken(), gsonPontoAmostral.toJson(pontoa));
                                            String jsonPontoAmostral = responsePontoAmostral.getEntity(String.class);
                                            System.out.println("json" + jsonPontoAmostral + " response " + responsePontoAmostral.getStatus());
                                            if (responsePontoAmostral.getStatus() != 201) {
                                                MensagemRetorno mensagemPontoAmostral = gsonPontoAmostral.fromJson(jsonPontoAmostral, MensagemRetorno.class);
                                                System.out.println("mensagem de erro = " + mensagemPontoAmostral.toString());
                                            } else {
                                                int fimcod = jsonPontoAmostral.indexOf("\"geom\"");
                                                System.out.println("auxcod-----------: " + jsonPontoAmostral.substring(10, fimcod - 1));
                                                Long auxcod = Long.parseLong(jsonPontoAmostral.substring(10, fimcod - 1));
                                                pontoamostral.get(k).setCodigo(auxcod);
                                                pontoamostralDAO.alterar(pontoamostral.get(k));
                                            }
                                        } else { //atualiza ponto amostral que já está na api
                                            PontoAmostral pontoa = new PontoAmostral();
                                            pontoa.setGeom(pa);
                                            pontoa.setCodigo(pontoamostral.get(k).getCodigo());
                                            pontoa.setGradeAmostral(gradeamostral);
                                            ClientResponse responsePA = client.putResource("pontoamostral", userInfo.getUsuario().getToken(), gsonPontoAmostral.toJson(pontoa));
                                            String jsonPA = responsePA.getEntity(String.class);

                                            if (responsePA.getStatus() != 200) {
                                                MensagemRetorno mensagemPA = gsonPontoAmostral.fromJson(jsonPA, MensagemRetorno.class);
                                                throw new PPATException(mensagemPA.getMessage());
                                            }

                                        }
                                    }
                                }
                            }
                        }

                    }
                }*/

            result.include("success", "success.cadastrar.area.sincronizada").include("idProjeto", areaprojeto.getProjeto().getId()).redirectTo(this).lista(areaprojeto.getProjeto().getId());
        } catch (Exception ex) {
            System.out.println("EXCECAO " + ex);
            result.include("danger", "warning.nao.sincronizado").include("idProjeto", areaprojeto.getProjeto().getId()).redirectTo(this).lista(areaprojeto.getProjeto().getId());
        }
    }

    @Post("persiste/areaServer")
    @Permissao(Roles.ON)
    public void importarAreaAPIProjeto(String[] camposMarcados, Long idProjeto
    ) {
        try {
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            List<Area> areasProjeto = areaDAO.listaAreaByProjeto(idProjeto);
            Area areameubanco = new Area();

            for (int i = 0; i < camposMarcados.length; i++) {
                Area areaAPI = areaDAO.buscaAreaByCodigo(Long.parseLong(camposMarcados[i]));
                if (areasProjeto.contains(areaAPI)) {
                    //a área já está no projeto então não é necessário salvá-la
                    System.out.println("--------------ENTROU NO IF DE JÁ EXISTE");
                } else {
                    //salvar área da API no banco com o cód do projeto corrente
                    System.out.println("--------------ENTROU NO IF DE NÃO EXISTE NO PROJETO CORRENTE");
                    ClientResponse responseArea = client.getResource("area/" + camposMarcados[i], userInfo.getUsuario().getToken());
                    String jsonArea = responseArea.getEntity(String.class);
                    gsonarea = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                            .create();
                    int iniciocoord = jsonArea.indexOf("MULTIPOLYGON");
                    int finalcoord = jsonArea.lastIndexOf(")))");
                    String poligonoAux = jsonArea.substring(iniciocoord, finalcoord);
                    poligonoAux += ")))";
                    poligonoAux = poligonoAux.replace("MULTIPOLYGON(((", "POLYGON((");
                    poligonoAux = poligonoAux.replace(")))", "))");
                    areaAPI = gsonarea.fromJson(jsonArea, Area.class);
                    areameubanco = areaAPI;
                    GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
                    WKTReader reader = new WKTReader(geometryFactory);

                    try {
                        areameubanco.setGeometry(reader.read(poligonoAux));
                    } catch (ParseException ex) {
                        Logger.getLogger(AreaController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    areameubanco.getGeometry().setSRID(projection);
                    areameubanco.setUsuario(usuarioSession.getUsuario());

                    TipoSolo tiposolomeubanco = tipoSoloDAO.buscaSoloByCodigo(areaAPI.getTipoSolo().getCodigo());
                    areameubanco.setTipoSolo(tiposolomeubanco);
                    areameubanco.setProjeto(projetoDAO.buscaProjetoById(idProjeto));
                    areaDAO.adicionar(areameubanco);

                    //add amostras e grades da área
                    ClientResponse responseAmostra = client.getResource("amostra", userInfo.getUsuario().getToken());
                    String jsonAmostra = responseAmostra.getEntity(String.class);
                    ClientResponse responseGradeAmostral = client.getResource("gradeamostral", userInfo.getUsuario().getToken());
                    String jsonGradeAmostral = responseGradeAmostral.getEntity(String.class);
                    ClientResponse responsePixelAmostra = client.getResource("pixelamostra", userInfo.getUsuario().getToken());
                    String jsonPixelAmostra = responsePixelAmostra.getEntity(String.class);
                    ClientResponse responsePontoAmostral = client.getResource("pontoamostral", userInfo.getUsuario().getToken());
                    String jsonPontoAmostral = responsePontoAmostral.getEntity(String.class);
                    gsonAmostra = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                    gsonGradeAmostral = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                    gsonPixelAmostra = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                    gsonPontoAmostral = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                    List<Amostra> amostra = new ArrayList<>();
                    List<GradeAmostral> gradeAmostral = new ArrayList<>();
                    List<PixelAmostra> pixelAmostra = new ArrayList<>();
                    List<PontoAmostral> pontoAmostral = new ArrayList<>();

                    if (responseAmostra.getStatus() == 200) {
                        amostra = (List<Amostra>) gsonAmostra.fromJson(jsonAmostra, new TypeToken<ArrayList<Amostra>>() {
                        }.getType());
                    }
                    if (responseGradeAmostral.getStatus() == 200) {
                        gradeAmostral = (List<GradeAmostral>) gsonGradeAmostral.fromJson(jsonGradeAmostral, new TypeToken<ArrayList<GradeAmostral>>() {
                        }.getType());
                    }
                    if (responsePixelAmostra.getStatus() == 200) {
                        pixelAmostra = (List<PixelAmostra>) gsonPixelAmostra.fromJson(jsonPixelAmostra, new TypeToken<ArrayList<PixelAmostra>>() {
                        }.getType());
                    }
                    if (responsePontoAmostral.getStatus() == 200) {
                        pontoAmostral = (List<PontoAmostral>) gsonPontoAmostral.fromJson(jsonPontoAmostral, new TypeToken<ArrayList<PontoAmostral>>() {
                        }.getType());
                    }

                    for (int k = 0; k < amostra.size(); k++) {
                        GeometryFactory geometryFactory2 = JTSFactoryFinder.getGeometryFactory(null);
                        WKTReader reader2 = new WKTReader(geometryFactory2);
                        List<PixelAmostra> pixelsAmostraisMeuBanco = new ArrayList<>();
                        if (amostraDAO.buscaAmostraByCodigo(amostra.get(k).getCodigo()) == null) {
                            amostra.get(k).setUsuario(userInfo.getUsuario());
                            amostra.get(k).setArea(areaDAO.buscaAreaByCodigo(amostra.get(k).getArea().getCodigo()));
                            amostra.get(k).setAtributo(atributoDAO.buscaPorCodigo(amostra.get(k).getAtributo().getCodigo()));
                            amostraDAO.adicionar(amostra.get(k));
                            Amostra amo = amostraDAO.buscaAmostraByCodigo(amostra.get(k).getCodigo());
                            for (int j = 0; j < pixelAmostra.size(); j++) {
                                if (pixelAmostra.get(j).getAmostra().getCodigo().equals(amostra.get(k).getCodigo())) {
                                    PixelAmostra paAux = pixelAmostra.get(j);
                                    try {
                                        paAux.setGeometry(reader2.read(pixelAmostra.get(j).getGeom()));
                                    } catch (ParseException ex) {
                                        Logger.getLogger(AreaController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    paAux.setAmostra(amo);
                                    paAux.getGeometry().setSRID(projection);
                                    pixelsAmostraisMeuBanco.add(paAux);
                                }
                            }
                            amo.setPixelAmostra(pixelsAmostraisMeuBanco);
                            amostraDAO.alterar(amo);
                        }

                    }

                    for (int l = 0; l < gradeAmostral.size(); l++) {
                        GeometryFactory geometryFactory3 = JTSFactoryFinder.getGeometryFactory(null);
                        WKTReader reader3 = new WKTReader(geometryFactory3);
                        List<PontoAmostral> pontosAmostraisMeuBanco = new ArrayList<>();
                        if (gradeAmostralDAO.buscaGradeAmostralByCodigo(gradeAmostral.get(l).getCodigo()) == null) {
                            gradeAmostral.get(l).setFlagsensor(false);
                            gradeAmostral.get(l).setUsuario(userInfo.getUsuario());
                            gradeAmostral.get(l).setArea(areaDAO.buscaAreaByCodigo(gradeAmostral.get(l).getArea().getCodigo()));
                            gradeAmostralDAO.adicionar(gradeAmostral.get(l));
                            GradeAmostral ga = gradeAmostralDAO.buscaGradeAmostralByCodigo(gradeAmostral.get(l).getCodigo());
                            for (int j = 0; j < pontoAmostral.size(); j++) {
                                if (pontoAmostral.get(j).getGradeAmostral().getCodigo().equals(gradeAmostral.get(l).getCodigo())) {
                                    PontoAmostral paAux = pontoAmostral.get(j);
                                    try {
                                        paAux.setThe_geom(reader3.read(pontoAmostral.get(j).getGeom()));
                                        paAux.getThe_geom().setSRID(projection);
                                    } catch (ParseException ex) {
                                        Logger.getLogger(AreaController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    paAux.setGradeAmostral(ga);
                                    pontosAmostraisMeuBanco.add(paAux);
                                }
                            }
                            ga.setPontoAmostral(pontosAmostraisMeuBanco);
                            gradeAmostralDAO.alterar(ga);
                        }

                    }

                }

            }

            result.include("success", "success.areasinseridas").include("idProjeto", idProjeto).include("idUsuario", userInfo.getUsuario().getCodigo()).include("listaAreaProjeto", areaDAO.listaAreaByProjeto(idProjeto)).include("listaSolos", tipoSoloDAO.listaSolosByUsuario(userInfo.getUsuario().getId())).redirectTo(this).lista(idProjeto);

        } catch (Exception ex) {
            System.out.println("EXCEPTION importar area do servidor para a aplicação" + ex);
            result.include("danger", "danger.areasnaoinseridas").include("listaAreaProjeto", areaDAO.listaAreaByProjeto(idProjeto)).include("listaSolos", tipoSoloDAO.listaSolosByUsuario(userInfo.getUsuario().getId())).include("mensagem", ex.getMessage()).include("idProjeto", idProjeto).redirectTo(this).lista(idProjeto);
        }

    }

    @Post("persiste/areaPoligono")
    @Permissao(Roles.ON)
    public void poligonoProjetoArea(Area area, String salvarAPI
    ) {
        try {
            System.out.println("PERSISTE AREA POLIGONO " + area.toString() + " Salvar API: " + salvarAPI);
            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
            WKTReader reader = new WKTReader(geometryFactory);
            if (area.getId() == null) {
                Area areaMeuBanco = area;
                TipoSolo tiposolo = new TipoSolo();
                tiposolo.setCodigo(area.getTipoSolo().getCodigo());
                area.setTipoSolo(tiposolo);
                areaMeuBanco.setTipoSolo(tipoSoloDAO.buscaSoloByCodigo(area.getTipoSolo().getCodigo()));
                areaMeuBanco.setUsuario(usuarioSession.getUsuario());
                areaMeuBanco.setProjeto(projetoDAO.buscarPorId(area.getProjeto().getId()));
                if (area.getGeom() != null) {

                    try {
                        areaMeuBanco.setGeometry(reader.read(area.getGeom()));

                    } catch (ParseException ex) {
                        Logger.getLogger(AreaController.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                    areaMeuBanco.getGeometry().setSRID(projection);
                    String poligono = area.getGeom().replace("POLYGON((", "SRID=4326;MULTIPOLYGON(((");
                    poligono = poligono.replace("))", ")))");
                    area.setGeom(poligono);
                    //salva na api
                    if ("sim".equals(salvarAPI)) {
                        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                        ClientResponse response = null;
                        String json = null;
                        try {
                            response = client.postResource("area", userInfo.getUsuario().getToken(), gson.toJson(area));
                            json = response.getEntity(String.class);
                        } catch (PPATException ex) {
                            Logger.getLogger(AreaController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        //fim parte que salva na api
                        if (response.getStatus() != 201) {
                            // MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                            //System.out.println("mensagem json: " + mensagem.toString() + " \n response status: " + response.getStatus());
                            result.include("danger", "warning.polygono.nao.cadastrado").redirectTo(this).lista(area.getProjeto().getId());
                        } else {
                            int fim = json.indexOf("\"descricao");
                            Long aux = Long.parseLong(json.substring(10, fim - 1));
                            areaMeuBanco.setCodigo(aux);
                            areaMeuBanco.setCodigo(area.getCodigo());
                            areaMeuBanco.setDataCadastro(new Date());
                            areaDAO.adicionar(areaMeuBanco);
                            result.include("success", "success.cadastrar.area.desenhada").include("idProjeto", areaMeuBanco.getProjeto().getId()).redirectTo(this).lista(areaMeuBanco.getProjeto().getId());
                        }
                    } else {
                        areaMeuBanco.setDataCadastro(new Date());
                        areaDAO.adicionar(areaMeuBanco);
                        result.include("success", "success.cadastrar.area.desenhada").include("idProjeto", areaMeuBanco.getProjeto().getId()).redirectTo(this).lista(areaMeuBanco.getProjeto().getId());
                    }

                }
            } else {

                Area areaAntiga = areaDAO.buscarPorId(area.getId());
                Area areaMeuBanco = areaAntiga;
                areaMeuBanco.setDescricao(area.getDescricao());

                if (area.getGeom() != null) {

                    try {
                        if (area.getGeom() != null) {
                            //if (area.getGeom().contains("POLYGON((")) {
                            //int inicio = area.getGeom().indexOf("[[") + 2;
                            //int fim = area.getGeom().indexOf("]]");
                            //String auxcoord = area.getGeom().substring(inicio, fim);
                            //auxcoord = auxcoord.replace("[", "");
                            //auxcoord = auxcoord.replace("]", "");
                            //String[] coord = auxcoord.split(",");
                            //String geomAux = "POLYGON((";
                            //for (int x = 0; x < coord.length; x += 2) {
                            //    if (x != coord.length - 2) {
                            //        geomAux += coord[x] + " " + coord[x + 1] + ",";
                            //    } else {
                            //        geomAux += coord[x] + " " + coord[x + 1] + "))";
                            //    }
                            //}
                            areaMeuBanco.setGeometry(reader.read(area.getGeom()));
                            areaMeuBanco.getGeometry().setSRID(projection);

                        }
                        TipoSolo ts = tipoSoloDAO.buscaSoloByCodigo(area.getTipoSolo().getCodigo());
                        areaMeuBanco.setTipoSolo(ts);
                        areaDAO.alterar(areaMeuBanco);
                        result.include("success", "success.editar.area.desenhada").include("idProjeto", areaMeuBanco.getProjeto().getId()).redirectTo(this).lista(areaMeuBanco.getProjeto().getId());

                    } catch (Exception e) {
                        result.include("danger", "warning.polygono.nao.cadastrado").include("idProjeto", areaMeuBanco.getProjeto().getId()).redirectTo(this).lista(areaMeuBanco.getProjeto().getId());
                    }
                } else {
                    result.include("danger", "warning.polygono.nao.cadastrado").include("idProjeto", areaMeuBanco.getProjeto().getId()).redirectTo(this).lista(areaMeuBanco.getProjeto().getId());
                }
            }
        } catch (Exception ex) {
            System.out.println("Exception persiste desenho area poligono: " + ex);
            result.include("danger", "warning.polygono.nao.cadastrado").include("idProjeto", area.getProjeto().getId()).redirectTo(this).lista(area.getProjeto().getId());
        }
    }
}
