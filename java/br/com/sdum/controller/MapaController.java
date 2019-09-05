package br.com.sdum.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.geotools.geojson.geom.GeometryJSON;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.sdum.annotation.Permissao;
import br.com.sdum.dao.AmostraDAO;
import br.com.sdum.dao.AreaDAO;
import br.com.sdum.dao.AtributoDAO;
import br.com.sdum.dao.ClasseDAO;
import br.com.sdum.dao.ClassificacaoDAO;
import br.com.sdum.dao.GradeAmostralDAO;
import br.com.sdum.dao.MapaDAO;
import br.com.sdum.dao.ProjetoDAO;
import br.com.sdum.dao.UsuarioDAO;
import br.com.sdum.enums.Roles;
import br.com.sdum.exception.PPATException;
import br.com.sdum.model.Amostra;
import br.com.sdum.model.Area;
import br.com.sdum.model.Atributo;
import br.com.sdum.model.Classe;
import br.com.sdum.model.Classificacao;
import br.com.sdum.model.GradeAmostral;
import br.com.sdum.model.Mapa;
import br.com.sdum.model.MensagemRetorno;
import br.com.sdum.model.PixelAmostra;
import br.com.sdum.model.PixelMapa;
import br.com.sdum.model.PontoAmostral;
import br.com.sdum.model.Projeto;
import br.com.sdum.model.Usuario;
import br.com.sdum.session.UsuarioSession;
import br.com.sdum.ws.client.PPATWSClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.ClientResponse;
import java.lang.reflect.Array;
import java.util.Objects;

@Resource
public class MapaController implements Serializable {

    private static final long serialVersionUID = -2965105069503932577L;

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
    private MapaDAO mapaDAO;

    @Inject
    private ClasseDAO classeDAO;

    @Inject
    private ClassificacaoDAO classificacaoDAO;

    @Inject
    private PPATWSClient client;

    @Inject
    private UsuarioDAO usuarioDAO;

    @Inject
    private AtributoDAO atributoDAO;

    @Inject
    private ProjetoDAO projetoDAO;

    private Gson gson;

    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

    @Path({"mapa", "/"})
    @Permissao(Roles.ON)
    public void mapa(Long id) {
        Projeto projetoaux = projetoDAO.buscarPorId(id);
        Usuario usuario = usuarioDAO.buscarPorId(projetoaux.getUsuario().getId());
        if (Objects.equals(usuario.getId(), usuarioSession.getUsuario().getId())) {
            //List<Projeto> projetos = projetoDAO.listaProjetoByUsuarioAll(usuarioSession.getUsuario());
            Projeto projet = projetoDAO.buscarPorId(id);
            String json = "";
            //for (int i = 0; i < projetos.size(); i++) {
            json += "{\"id\":\"projeto" + projet.getId() + "\",\"text\":\"" + projet.getNome() + "\",\"children\":[";
            List<Area> areas = areaDAO.listaAreaByProjeto(projet.getId());
            for (int j = 0; j < areas.size(); j++) {
                List<Amostra> amostras = amostraDAO.listaAmostraByArea(areas.get(j).getId());
                List<GradeAmostral> gradesamostrais = gradeAmostralDAO.listaGradeAmostralByArea(areas.get(j).getId());
                List<Mapa> mapas = mapaDAO.listaMapaByArea(areas.get(j).getId());
                if (gradesamostrais.size() > 0 && amostras.size() > 0 && mapas.size() > 0) {
                    json += "{\"id\":\"area" + areas.get(j).getId() + "\",\"text\":\"" + areas.get(j).getDescricao() + "\",\"children\":[";
                    json += "{\"id\":\"gradesamostraisarea" + areas.get(j).getId() + "\",\"text\":\"GradesAmostraissubstituir\",\"children\":[";
                    for (int l = 0; l < gradesamostrais.size(); l++) {
                        if (l != (gradesamostrais.size() - 1)) {
                            json += "{\"id\":\"grade" + gradesamostrais.get(l).getId() + "\",\"text\":\"" + gradesamostrais.get(l).getDescricao() + "\"},";
                        } else {
                            json += "{\"id\":\"grade" + gradesamostrais.get(l).getId() + "\",\"text\":\"" + gradesamostrais.get(l).getDescricao() + "\"}]},";
                        }
                    }
                    json += "{\"id\":\"amostrasarea" + areas.get(j).getId() + "\",\"text\":\"Amostrassubstituir\",\"children\":[";
                    for (int k = 0; k < amostras.size(); k++) {
                        if (k != (amostras.size() - 1)) {
                            json += "{\"id\":\"amostra" + amostras.get(k).getId() + "\",\"text\":\"" + amostras.get(k).getDescricao() + "\"},";
                        } else {
                            json += "{\"id\":\"amostra" + amostras.get(k).getId() + "\",\"text\":\"" + amostras.get(k).getDescricao() + "\"}]},";
                        }
                    }
                    json += "{\"id\":\"mapasarea" + areas.get(j).getId() + "\",\"text\":\"Mapassubstituir\",\"children\":[";
                    for (int k = 0; k < mapas.size(); k++) {
                        if (k != (mapas.size() - 1)) {
                            json += "{\"id\":\"mapa" + mapas.get(k).getId() + "\",\"text\":\"" + mapas.get(k).getDescricao() + "\"},";
                        } else {
                            json += "{\"id\":\"mapa" + mapas.get(k).getId() + "\",\"text\":\"" + mapas.get(k).getDescricao() + "\"}]}";
                        }
                    }
                    if (j != (areas.size() - 1)) {
                        json += "]},";
                    } else {
                        json += "]}";
                    }

                } else {
                    if (gradesamostrais.size() > 0 && amostras.isEmpty()) {
                        json += "{\"id\":\"area" + areas.get(j).getId() + "\",\"text\":\"" + areas.get(j).getDescricao() + "\",\"children\":[";
                        json += "{\"id\":\"gradesamostraisarea" + areas.get(j).getId() + "\",\"text\":\"GradesAmostraissubstituir\",\"children\":[";
                        for (int k = 0; k < gradesamostrais.size(); k++) {
                            if (k != (gradesamostrais.size() - 1)) {
                                json += "{\"id\":\"grade" + gradesamostrais.get(k).getId() + "\",\"text\":\"" + gradesamostrais.get(k).getDescricao() + "\"},";
                            } else {
                                json += "{\"id\":\"grade" + gradesamostrais.get(k).getId() + "\",\"text\":\"" + gradesamostrais.get(k).getDescricao() + "\"}]}";
                            }
                        }
                        if (mapas.size() > 0) {
                            json += ",{\"id\":\"mapasarea" + areas.get(j).getId() + "\",\"text\":\"Mapassubstituir\",\"children\":[";
                            for (int k = 0; k < mapas.size(); k++) {
                                if (k != (mapas.size() - 1)) {
                                    json += "{\"id\":\"mapa" + mapas.get(k).getId() + "\",\"text\":\"" + mapas.get(k).getDescricao() + "\"},";
                                } else {
                                    json += "{\"id\":\"mapa" + mapas.get(k).getId() + "\",\"text\":\"" + mapas.get(k).getDescricao() + "\"}]}";
                                }
                            }

                        }
                        if (j != (areas.size() - 1)) {
                            json += "]},";
                        } else {
                            json += "]}";
                        }

                    } else {
                        if (gradesamostrais.isEmpty() && amostras.size() > 0) {
                            json += "{\"id\":\"area" + areas.get(j).getId() + "\",\"text\":\"" + areas.get(j).getDescricao() + "\",\"children\":[";
                            json += "{\"id\":\"amostrasarea" + areas.get(j).getId() + "\",\"text\":\"Amostrassubstituir\",\"children\":[";
                            for (int k = 0; k < amostras.size(); k++) {
                                if (k != (amostras.size() - 1)) {
                                    json += "{\"id\":\"amostra" + amostras.get(k).getId() + "\",\"text\":\"" + amostras.get(k).getDescricao() + "\"},";
                                } else {
                                    json += "{\"id\":\"amostra" + amostras.get(k).getId() + "\",\"text\":\"" + amostras.get(k).getDescricao() + "\"}]}";
                                }
                            }
                            if (mapas.size() > 0) {
                                json += ",{\"id\":\"mapasarea" + areas.get(j).getId() + "\",\"text\":\"Mapassubstituir\",\"children\":[";
                                for (int k = 0; k < mapas.size(); k++) {
                                    if (k != (mapas.size() - 1)) {
                                        json += "{\"id\":\"mapa" + mapas.get(k).getId() + "\",\"text\":\"" + mapas.get(k).getDescricao() + "\"},";
                                    } else {
                                        json += "{\"id\":\"mapa" + mapas.get(k).getId() + "\",\"text\":\"" + mapas.get(k).getDescricao() + "\"}]}";
                                    }
                                }

                            }
                            if (j != (areas.size() - 1)) {
                                json += "]},";
                            } else {
                                json += "]}";
                            }

                        } else {
                            if (mapas.size() > 0 && amostras.isEmpty() && gradesamostrais.isEmpty()) {
                                json += "{\"id\":\"area" + areas.get(j).getId() + "\",\"text\":\"" + areas.get(j).getDescricao() + "\",\"children\":[";
                                json += "{\"id\":\"mapasarea" + areas.get(j).getId() + "\",\"text\":\"Mapassubstituir\",\"children\":[";
                                for (int k = 0; k < mapas.size(); k++) {
                                    if (k != (mapas.size() - 1)) {
                                        json += "{\"id\":\"mapa" + mapas.get(k).getId() + "\",\"text\":\"" + mapas.get(k).getDescricao() + "\"},";
                                    } else {
                                        json += "{\"id\":\"mapa" + mapas.get(k).getId() + "\",\"text\":\"" + mapas.get(k).getDescricao() + "\"}]}";
                                    }
                                }
                                if (j != (areas.size() - 1)) {
                                    json += "]},";
                                } else {
                                    json += "]}";
                                }

                            } else {
                                if (gradesamostrais.size() > 0 && amostras.size() > 0 && mapas.isEmpty()) {
                                    json += "{\"id\":\"area" + areas.get(j).getId() + "\",\"text\":\"" + areas.get(j).getDescricao() + "\",\"children\":[";
                                    json += "{\"id\":\"gradesamostraisarea" + areas.get(j).getId() + "\",\"text\":\"GradesAmostraissubstituir\",\"children\":[";
                                    for (int l = 0; l < gradesamostrais.size(); l++) {
                                        if (l != (gradesamostrais.size() - 1)) {
                                            json += "{\"id\":\"grade" + gradesamostrais.get(l).getId() + "\",\"text\":\"" + gradesamostrais.get(l).getDescricao() + "\"},";
                                        } else {
                                            json += "{\"id\":\"grade" + gradesamostrais.get(l).getId() + "\",\"text\":\"" + gradesamostrais.get(l).getDescricao() + "\"}]},";
                                        }
                                    }
                                    json += "{\"id\":\"amostrasarea" + areas.get(j).getId() + "\",\"text\":\"Amostrassubstituir\",\"children\":[";
                                    for (int k = 0; k < amostras.size(); k++) {
                                        if (k != (amostras.size() - 1)) {
                                            json += "{\"id\":\"amostra" + amostras.get(k).getId() + "\",\"text\":\"" + amostras.get(k).getDescricao() + "\"},";
                                        } else {
                                            json += "{\"id\":\"amostra" + amostras.get(k).getId() + "\",\"text\":\"" + amostras.get(k).getDescricao() + "\"}]}";
                                        }
                                    }
                                    if (j != (areas.size() - 1)) {
                                        json += "]},";
                                    } else {
                                        json += "]}";
                                    }

                                } else {

                                    if (gradesamostrais.isEmpty() && amostras.isEmpty() && mapas.isEmpty() && j != (areas.size() - 1)) {
                                        json += "{\"id\":\"area" + areas.get(j).getId() + "\",\"text\":\"" + areas.get(j).getDescricao() + "\"},";
                                    } else {
                                        if (gradesamostrais.isEmpty() && amostras.isEmpty() && mapas.isEmpty() && j == (areas.size() - 1)) {
                                            json += "{\"id\":\"area" + areas.get(j).getId() + "\",\"text\":\"" + areas.get(j).getDescricao() + "\"}";
                                        }
                                    }
                                }

                            }

                        }
                    }
                }
            }
            json += "]}";
            //if (i != (projetos.size() - 1)) {
            //    json += ",";
            //}
            // }
            List<Classificacao> classificacoes = classificacaoDAO.buscaPorUsuario(usuarioSession.getUsuario().getId());

            result.include(
                    "amostras", amostraDAO.listaAmostraByAreaByProjetoByUsuarioAll(usuarioSession.getUsuario()))
                    .include("projetos", projetoDAO.listaProjetoByUsuarioAll(usuarioSession.getUsuario()))
                    .include("areas", areaDAO.listaAreaByUsuario(usuarioSession.getUsuario().getId()))
                    .include("interpolacoes", mapaDAO.listaMapaByUsuarioAll(usuarioSession.getUsuario()))
                    .include("classificacoes", classificacoes)
                    .include("idProjeto", id)
                    .include("jsontree", json);
        }else{
            result.redirectTo(ProjetoController.class).lista();
        }
    }

    @Get({"mapa/projeto"})
    @Permissao(Roles.ON)
    public void projeto(Long idArea, Long idAmostra
    ) {

        GeometryJSON g = new GeometryJSON();

        Area area = areaDAO.buscarPorId(idArea);
        area.setGeom("{" + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + "," + '"' + "geometry" + '"' + ":"
                + g.toString(area.getGeometry()) + "}");

        Amostra amostra = amostraDAO.buscarPorId(idAmostra);
        amostra.setDataFormatada(format.format(amostra.getDataCadastro()));

        for (int k = 0; k < amostra.getPixelAmostra().size(); k++) {

            //if (amostra.getPixelAmostra().get(k).getValor() != null
            //       && (!amostra.getPixelAmostra().get(k).getValor().equals("null"))) {
            PixelAmostra pa = new PixelAmostra();
            pa.setId(amostra.getPixelAmostra().get(k).getId());
            pa.setAmostra(amostra.getPixelAmostra().get(k).getAmostra());
            pa.setValor(amostra.getPixelAmostra().get(k).getValor());
            pa.setGeom("{" + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + "," + '"' + "geometry" + '"' + ":"
                    + g.toString(amostra.getPixelAmostra().get(k).getGeometry()) + "}");

            amostra.getPixelAmostrasTrans().add(pa);
            //}
        }

        area.setAmostra(amostra);
        Atributo atr = atributoDAO.buscarPorId(amostra.getAtributo().getId());

        result.use(Results.json()).withoutRoot().from(area)
                .include("amostra", "amostra.pixelAmostrasTrans", "amostra.atributo", "amostra.atributo.unidadeMedidaPT").serialize();
        //result.use(Results.json()).withoutRoot().from(atr).include("atributo.unidadeMedidaPT.sigla").serialize();
    }

    @Get({"mapa/checkbox"})
    @Permissao(Roles.ON)
    public void checkbox(Long idArea, Long idAmostra,
            Long idGrade
    ) {
        try {
            GeometryJSON g = new GeometryJSON();
            Area area = new Area();
            Amostra amostra = new Amostra();
            GradeAmostral gradeAmostral = new GradeAmostral();
            if (idAmostra != null) {
                amostra = amostraDAO.buscarPorId(idAmostra);
                if (idArea == null) {
                    area = areaDAO.buscarPorId(amostra.getArea().getId());
                } else {
                    area = areaDAO.buscarPorId(idArea);
                }
                area.setGeom("{" + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + "," + '"' + "geometry" + '"' + ":"
                        + g.toString(area.getGeometry()) + "}");

                amostra.setDataFormatada(format.format(amostra.getDataCadastro()));

                for (int k = 0; k < amostra.getPixelAmostra().size(); k++) {

                    //if (amostra.getPixelAmostra().get(k).getValor() != null
                    //       && (!amostra.getPixelAmostra().get(k).getValor().equals("null"))) {
                    PixelAmostra pa = new PixelAmostra();
                    pa.setId(amostra.getPixelAmostra().get(k).getId());
                    pa.setAmostra(amostra.getPixelAmostra().get(k).getAmostra());
                    pa.setValor(amostra.getPixelAmostra().get(k).getValor());
                    pa.setGeom("{" + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + "," + '"' + "geometry" + '"' + ":"
                            + g.toString(amostra.getPixelAmostra().get(k).getGeometry()) + "}");

                    amostra.getPixelAmostrasTrans().add(pa);
                    //}
                }

                area.setAmostra(amostra);
                Atributo atr = atributoDAO.buscarPorId(amostra.getAtributo().getId());

                result.use(Results.json()).withoutRoot().from(area)
                        .include("amostra", "amostra.pixelAmostrasTrans", "amostra.atributo", "amostra.atributo.unidadeMedidaPT").serialize();
                //result.use(Results.json()).withoutRoot().from(atr).include("atributo.unidadeMedidaPT.sigla").serialize();
            } else {
            }
            if (idArea != null) {
                area = areaDAO.buscarPorId(idArea);
                area.setGeom("{" + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + "," + '"' + "geometry" + '"' + ":"
                        + g.toString(area.getGeometry()) + "}");
                result.use(Results.json()).withoutRoot().from(area).serialize();
            } else {
                if (idGrade != null) {
                    gradeAmostral = gradeAmostralDAO.buscarPorId(idGrade);
                    area = areaDAO.buscarPorId(gradeAmostral.getArea().getId());
                    area.setGeom("{" + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + "," + '"' + "geometry" + '"' + ":"
                            + g.toString(area.getGeometry()) + "}");

                    gradeAmostral.setDataFormatada(format.format(gradeAmostral.getDataCadastro()));

                    for (int k = 0; k < gradeAmostral.getPontoAmostral().size(); k++) {

                        //if (amostra.getPixelAmostra().get(k).getValor() != null
                        //       && (!amostra.getPixelAmostra().get(k).getValor().equals("null"))) {
                        PontoAmostral pa = new PontoAmostral();
                        pa.setId(gradeAmostral.getPontoAmostral().get(k).getId());
                        pa.setGradeAmostral(gradeAmostral.getPontoAmostral().get(k).getGradeAmostral());
                        pa.setGeom("{" + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + "," + '"' + "geometry" + '"' + ":"
                                + g.toString(gradeAmostral.getPontoAmostral().get(k).getThe_geom()) + "}");

                        gradeAmostral.getPontoAmostralTrans().add(pa);
                        //}
                    }

                    area.setGradeAmostral(gradeAmostral);

                    result.use(Results.json()).withoutRoot().from(area)
                            .include("gradeAmostral", "gradeAmostral.pontoAmostralTrans").serialize();
                    //result.use(Results.json()).withoutRoot().from(atr).include("atributo.unidadeMedidaPT.sigla").serialize();
                }
            }
        } catch (Exception e) {
            System.out.println("Exception mapa/checkbox " + e);
        }
    }

    @Get("interpolacao")
    @Permissao(Roles.ON)
    public void interpolacao(Long id
    ) {
        /*try {
            ClientResponse response = client.getResource("mapas/" + codigo, usuarioSession.getUsuario().getToken());
            String json = response.getEntity(String.class);

            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            Mapa mapa = null;

            switch (response.getStatus()) {
                case 200:
                    mapa = gson.fromJson(json, Mapa.class);
                    Mapa mapaAux = mapaDAO.buscaPorCodigo(codigo);
                    if (mapaAux != null) {
                        Usuario u = usuarioDAO.buscarPorId(usuarioSession.getUsuario().getId());
                        mapa.getUsuario().setId(u.getId());
                        if (mapaAux.getClassificacao() == null) {
                            mapaAux = mapaClassificadorGenerico(mapaAux);
                        } else {
                            mapaAux = mapaClassificadorEspecifico(mapaAux);
                        }
                        result.use(Results.json()).withoutRoot().from(mapaAux)
                                .include("pixelMapasTrans", "area", "classesTran", "amostra").serialize();

                    }
                    break;
                default:
                    MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                    throw new PPATException(mensagem.getMessage());
            }

        } catch (PPATException ex) {
            result.include("mensagem", ex.getMessage());
            result.redirectTo(this).mapa();
        }
         */
        Mapa mapa = mapaDAO.buscarUltimoMapaIntepolacao(id, usuarioSession.getUsuario());

        if (mapa.getClassificacao() == null) {
            mapa = mapaClassificadorGenerico(mapa);
        } else {
            mapa = mapaClassificadorEspecifico(mapa);
        }
        System.out.println("------------" + mapa.toString());
        //Atributo atr = atributoDAO.buscarPorId(mapa.getAmostra().getAtributo().getId());
        //result.use(Results.json()).withoutRoot().from(mapa)
        //		.include("pixelMapasTrans", "area", "classesTran", "amostra", "atributo", "unidadeMedida").serialize();
        result.use(Results.json()).withoutRoot().from(mapa)
                .include("id", "pixelMapasTrans", "area", "classesTran", "amostra").serialize();
        //result.use(Results.json()).withoutRoot().from(atr).include("atributo.unidadeMedidaPT.sigla").serialize();
    }

    public Mapa mapaClassificadorEspecifico(Mapa mapa) {

        GeometryJSON g = new GeometryJSON();

        List<Classe> intervalos = new ArrayList<Classe>();
        for (int i = 0; i < mapa.getClassificacao().getClasses().size(); i++) {

            Classe intervalo = new Classe();

            intervalo.setId(mapa.getClassificacao().getClasses().get(i).getId());
            intervalo.setValorMinimo(mapa.getClassificacao().getClasses().get(i).getValorMinimo());
            intervalo.setValorMaximo(mapa.getClassificacao().getClasses().get(i).getValorMaximo());
            intervalo.setCor(mapa.getClassificacao().getClasses().get(i).getCor());

            intervalos.add(intervalo);
        }

        mapa.setClassesTran(intervalos);

        List<PixelMapa> pixelMapas = new ArrayList<PixelMapa>();
        for (int j = 0; j < mapa.getPixelMapa().size(); j++) {

            PixelMapa pixelMapa = new PixelMapa();
            Float auxiliar = mapa.getPixelMapa().get(j).getValor();
            //Classe intervalo = classeDAO.intervaloByValor(mapa.getClassificacao().getId(),
            //        Double.valueOf(auxiliar));
            Double valorMinimo;
            Double valorMaximo;
            Classe intervalo = new Classe();
            String cor = "";
            for (int k = 0; k < intervalos.size(); k++) {
                if (auxiliar >= intervalos.get(k).getValorMinimo() && auxiliar <= intervalos.get(k).getValorMaximo()) {
                    intervalo = intervalos.get(k);
                    valorMinimo = intervalos.get(k).getValorMinimo();
                    valorMaximo = intervalos.get(k).getValorMaximo();
                    cor = intervalos.get(k).getCor();
                }
            }

            pixelMapa.setId(mapa.getPixelMapa().get(j).getId());
            pixelMapa.setValor(mapa.getPixelMapa().get(j).getValor());

            pixelMapa.setGeom("{" + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + "," + '"' + "properties" + '"'
                    + ":" + "{" + '"' + "valor" + '"' + ":" + mapa.getPixelMapa().get(j).getValor() + "," + '"'
                    + "intervalo" + '"' + ": 0," + '"' + "minimo" + '"' + ": 0," + '"' + "maximo" + '"' + ": 0," + '"'
                    + "cor" + '"' + ":" + '"' + intervalo.getCor() + '"' + "}" + "," + '"' + "geometry" + '"' + ":"
                    + g.toString(mapa.getPixelMapa().get(j).getGeometry()) + "}");

            pixelMapas.add(pixelMapa);
        }

        mapa.setPixelMapasTrans(pixelMapas);

        return mapa;
    }

    @Get("trocaclassificacaogenerica")
    @Permissao(Roles.ON)
    public void trocaclassificacaogenerica(String[] arraymin, String[] arraymax, String[] arraycor, Long idmapa) {
        System.out.println("______________min array tamanho " + arraymin.length);
        for (int i = 0; i < arraymin.length; i++) {
            System.out.println("min posicao " + i + " = " + arraymin[i]);
            System.out.println("max posicao " + i + " = " + arraymax[i]);
            System.out.println("cor posicao " + i + " = " + arraycor[i]);
        }

        GeometryJSON g = new GeometryJSON();
        Mapa mapa = mapaDAO.buscarPorId(idmapa);
        List<Classe> intervalos = new ArrayList<Classe>();
        for (int i = 0; i < arraymin.length; i++) {

            Classe intervalo = new Classe();

            //intervalo.setId(Long.parseLong());
            intervalo.setValorMinimo(Double.parseDouble(arraymin[i]));
            intervalo.setValorMaximo(Double.parseDouble(arraymax[i]));
            intervalo.setCor(arraycor[i]);

            intervalos.add(intervalo);
        }

        mapa.setClassesTran(intervalos);

        List<PixelMapa> pixelMapas = new ArrayList<>();
        for (int j = 0; j < mapa.getPixelMapa().size(); j++) {

            PixelMapa pixelMapa = new PixelMapa();
            float auxiliar = mapa.getPixelMapa().get(j).getValor();
            int intervalo = 0;
            float valorMinimo = 0;
            float valorMaximo = 0;
            String cor = "";
            for (int k = 0; k < arraymin.length; k++) {
                if (auxiliar >= Float.parseFloat(arraymin[k]) && auxiliar <= Float.parseFloat(arraymax[k])) {
                    intervalo = k;
                    valorMinimo = Float.parseFloat(arraymin[k]);
                    valorMaximo = Float.parseFloat(arraymax[k]);
                    cor = arraycor[k];
                }
            }

            pixelMapa.setId(mapa.getPixelMapa().get(j).getId());
            pixelMapa.setValor(mapa.getPixelMapa().get(j).getValor());

            pixelMapa.setGeom("{" + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + "," + '"' + "properties" + '"'
                    + ":" + "{" + '"' + "valor" + '"' + ":" + mapa.getPixelMapa().get(j).getValor() + "," + '"'
                    + "intervalo" + '"' + ": " + intervalo + "," + '"' + "minimo" + '"' + ": " + valorMinimo + "," + '"' + "maximo" + '"' + ": " + valorMaximo + "," + '"'
                    + "cor" + '"' + ":" + '"' + cor + '"' + "}" + "," + '"' + "geometry" + '"' + ":"
                    + g.toString(mapa.getPixelMapa().get(j).getGeometry()) + "}");

            pixelMapas.add(pixelMapa);
        }

        mapa.setPixelMapasTrans(pixelMapas);
        result.use(Results.json()).withoutRoot().from(mapa)
                .include("pixelMapasTrans", "area", "classesTran", "amostra").serialize();

    }

    @Get("trocaclassificacao")
    @Permissao(Roles.ON)
    public void trocaclassificacao(Long id, Long idmapa) {
        System.out.println("**********ENTROU NO TEESTACHAMADO SCRIPT" + id);
        GeometryJSON g = new GeometryJSON();
        Classificacao classificacao = classificacaoDAO.buscarPorId(id);
        List<Classe> intervalos = new ArrayList<Classe>();
        for (int i = 0; i < classificacao.getClasses().size(); i++) {

            Classe intervalo = new Classe();

            intervalo.setId(classificacao.getClasses().get(i).getId());
            intervalo.setValorMinimo(classificacao.getClasses().get(i).getValorMinimo());
            intervalo.setValorMaximo(classificacao.getClasses().get(i).getValorMaximo());
            intervalo.setCor(classificacao.getClasses().get(i).getCor());

            intervalos.add(intervalo);
        }
        System.out.println("-----------------------------idmapa " + idmapa);
        // Mapa mapa = mapaDAO.buscarPorId(idmapa);
        Mapa mapa = mapaDAO.buscarPorId(idmapa);
        mapa.setClassificacao(classificacao);
        mapaDAO.alterar(mapa);
        mapa.setClassesTran(intervalos);

        List<PixelMapa> pixelMapas = new ArrayList<PixelMapa>();
        for (int j = 0; j < mapa.getPixelMapa().size(); j++) {

            PixelMapa pixelMapa = new PixelMapa();
            Float auxiliar = mapa.getPixelMapa().get(j).getValor();
            //Classe intervalo = classeDAO.intervaloByValor(id,
            //        Double.valueOf(auxiliar));
            Double valorMinimo;
            Double valorMaximo;
            Classe intervalo = new Classe();
            String cor = "";
            for (int k = 0; k < intervalos.size(); k++) {
                if (auxiliar >= intervalos.get(k).getValorMinimo() && auxiliar <= intervalos.get(k).getValorMaximo()) {
                    intervalo = intervalos.get(k);
                    valorMinimo = intervalos.get(k).getValorMinimo();
                    valorMaximo = intervalos.get(k).getValorMaximo();
                    cor = intervalos.get(k).getCor();
                }
            }

            pixelMapa.setId(mapa.getPixelMapa().get(j).getId());
            pixelMapa.setValor(mapa.getPixelMapa().get(j).getValor());

            pixelMapa.setGeom("{" + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + "," + '"' + "properties" + '"'
                    + ":" + "{" + '"' + "valor" + '"' + ":" + mapa.getPixelMapa().get(j).getValor() + "," + '"'
                    + "intervalo" + '"' + ": 0," + '"' + "minimo" + '"' + ": 0," + '"' + "maximo" + '"' + ": 0," + '"'
                    + "cor" + '"' + ":" + '"' + intervalo.getCor() + '"' + "}" + "," + '"' + "geometry" + '"' + ":"
                    + g.toString(mapa.getPixelMapa().get(j).getGeometry()) + "}");

            pixelMapas.add(pixelMapa);
        }

        mapa.setPixelMapasTrans(pixelMapas);
        result.use(Results.json()).withoutRoot().from(mapa)
                .include("pixelMapasTrans", "area", "classesTran", "amostra").serialize();

    }

    public Mapa mapaClassificadorGenerico(Mapa mapa) {

        GeometryJSON g = new GeometryJSON();

        List<PixelMapa> pixelMapas = new ArrayList<PixelMapa>();

        double valorMinimo = 10000000, valorMaximo = 0;
        System.out.println("*----------------* PIXELMAPA.SIZE()" + mapa.getPixelMapa().size());
        for (int j = 0; j < mapa.getPixelMapa().size(); j++) {

            double valorAtual = mapa.getPixelMapa().get(j).getValor();

            if (j == 0) {
                valorMaximo = mapa.getPixelMapa().get(j).getValor();
                valorMinimo = mapa.getPixelMapa().get(j).getValor();
            }

            if (valorMinimo > valorAtual) {
                valorMinimo = valorAtual;
            }

            if (valorMaximo < valorAtual) {
                valorMaximo = valorAtual;
            }
        }
        System.out.println("*----------------* VALOR MAXIMO" + valorMaximo);
        System.out.println("*----------------* VALOR MINIMO" + valorMinimo);
        double intervalo = ((valorMaximo - valorMinimo) / 5);

        for (int j = 0; j < mapa.getPixelMapa().size(); j++) {

            PixelMapa pixelMapa = new PixelMapa();

            int valorInterpolacao = (int) ((mapa.getPixelMapa().get(j).getValor() - valorMinimo) / intervalo);

            pixelMapa.setId(mapa.getPixelMapa().get(j).getId());
            pixelMapa.setValor(mapa.getPixelMapa().get(j).getValor());
            pixelMapa.setGeom("{" + '"' + "type" + '"' + ":" + '"' + "Feature" + '"' + "," + '"' + "properties" + '"'
                    + ":" + "{" + '"' + "valor" + '"' + ":" + mapa.getPixelMapa().get(j).getValor() + "," + '"'
                    + "intervalo" + '"' + ":" + intervalo + "," + '"' + "minimo" + '"' + ":" + valorMinimo + "," + '"'
                    + "maximo" + '"' + ":" + valorMaximo + "," + '"' + "cor" + '"' + ":" + '"'
                    + gerarCorInterpolacao(valorInterpolacao) + '"' + "}" + "," + '"' + "geometry" + '"' + ":"
                    + g.toString(mapa.getPixelMapa().get(j).getGeometry()) + "}");

            pixelMapas.add(pixelMapa);

        }
        System.out.println("$$$min posicao " + valorMinimo);
        System.out.println("$$$max posicao " + valorMaximo);
        mapa.setPixelMapasTrans(pixelMapas);

        return mapa;

    }

    public String gerarCorInterpolacao(int valorInterpolacao) {

        switch (valorInterpolacao) {
            case 0:
                return "#FFEEDE";
            case 1:
                return "#FFD0AA";
            case 2:
                return "#FF8E32";
            case 3:
                return "#FB520F";
            case 4:
                return "#AD3A01";
           // case 5:
           //     return "#7C2801";
            default:
                return "#FFFFFF";
        }
    }
}
