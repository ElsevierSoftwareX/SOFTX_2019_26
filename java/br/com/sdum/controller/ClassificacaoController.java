package br.com.sdum.controller;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.sdum.annotation.Permissao;
import br.com.sdum.dao.AtributoDAO;
import br.com.sdum.dao.ClassificacaoDAO;
import br.com.sdum.dao.EntidadeDAO;
import br.com.sdum.dao.TipoSoloDAO;
import br.com.sdum.enums.Roles;
import br.com.sdum.exception.PPATException;
import br.com.sdum.model.Atributo;
import br.com.sdum.model.Classificacao;
import br.com.sdum.session.UsuarioSession;
import br.com.sdum.model.Classe;
import br.com.sdum.model.Entidade;
import br.com.sdum.model.MensagemRetorno;
import br.com.sdum.model.TipoSolo;
import br.com.sdum.model.UnidadeMedida;
import br.com.sdum.ws.client.PPATWSClient;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.ClientResponse;
import java.util.ArrayList;

@Resource
@Path("classificacao")
public class ClassificacaoController implements Serializable {

    private static final long serialVersionUID = 73875484460134936L;

    @Inject
    private Result result;

    @Inject
    private ClassificacaoDAO classificacaoDAO;

    @Inject
    private AtributoDAO atributoDAO;

    @Inject
    private TipoSoloDAO tipoSoloDAO;

    @Inject
    private EntidadeDAO entidadeDAO;

    @Inject
    private UsuarioSession usuarioSession;

    private Gson gson, gsonClasse, gsonClasse2;

    @Inject
    private PPATWSClient client;

    @Get("lista")
    @Permissao(Roles.ON)
    public void lista(Long idProjeto) {
        try {

            ClientResponse response = client.getResource("classificacao", usuarioSession.getUsuario().getToken());
            String json = response.getEntity(String.class);

            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();
            gsonClasse = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

            List<Classificacao> classificacao = new ArrayList<>();
            List<Classe> classe = new ArrayList<>();

            switch (response.getStatus()) {
                case 200:
                    classificacao = (List<Classificacao>) gson.fromJson(json, new TypeToken<ArrayList<Classificacao>>() {
                    }.getType());
                    ClientResponse responseClasse = client.getResource("classe", usuarioSession.getUsuario().getToken());
                    String jsonClasse = responseClasse.getEntity(String.class);
                    classe = (List<Classe>) gsonClasse.fromJson(jsonClasse, new TypeToken<ArrayList<Classe>>() {
                    }.getType());
                    List<Classe> classes = new ArrayList<Classe>();
                    for (int j = 0; j < classificacao.size(); j++) {
                        Classificacao classif = classificacaoDAO.buscaPorCodigo(classificacao.get(j).getCodigo());
                        if (classif == null) {
                            for (int i = 0; i < classe.size(); i++) {
                                if (classe.get(i).getClassificacao().getCodigo().equals(classificacao.get(j).getCodigo())) {
                                    Classe classemeubanco = new Classe();
                                    classemeubanco.setCor(classe.get(i).getCor());
                                    classemeubanco.setNivel(classe.get(i).getNivel());
                                    classemeubanco.setValorMaximo(classe.get(i).getValorMaximo());
                                    classemeubanco.setValorMinimo(classe.get(i).getValorMinimo());
                                    classes.add(classemeubanco);
                                }
                            }
                            classif = classificacao.get(j);
                            classif.setClasses(classes);
                            classif.setUsuario(usuarioSession.getUsuario());
                            classif.setAtributo(atributoDAO.buscaPorCodigo(classificacao.get(j).getAtributo().getCodigo()));
                            classif.setTipoSolo(tipoSoloDAO.buscaSoloByCodigo(classificacao.get(j).getTipoSolo().getCodigo()));
                            classif.setEntidade(entidadeDAO.buscaEntidadeByCodigo(classificacao.get(j).getEntidade().getCodigo()));
                            classificacaoDAO.adicionar(classif);
                        }
                    }
                    break;
                case 404:
                    classificacao = new ArrayList<>();
                    break;
                default:
                    System.out.println("erro lista classificacao");
                //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                //throw new PPATException(mensagem.getMessage());
            }

            result.include("listaClassificador", classificacao).include("idProjeto", idProjeto);

        } catch (PPATException ex) {
            System.out.println("exception lista classificacao" + ex);
            result.include("listaClassificador", new ArrayList()).include("idProjeto", idProjeto);
            //result.include("mensagem", ex.getMessage());
        }

        // result.include("listaClassificador", classificacaoDAO.listar());
    }

    @Get("formulario")
    @Permissao(Roles.ON)
    public void formulario(Long idProjeto) {
        result.include("listaAtributo", atributoDAO.listaAtributoByUsuarioAll(usuarioSession.getUsuario()))
                .include("listaSolo", tipoSoloDAO.listaSolosByUsuario(usuarioSession.getUsuario().getId()))
                .include("listaEntidade", entidadeDAO.listaEntidadeByUsuarioAll(usuarioSession.getUsuario()))
                .include("idProjeto", idProjeto);
    }

    @Post("persiste")
    @Permissao(Roles.ON)
    public void cadastrarEditar(Classificacao classificacao, String classes, Long idProjeto) {
        try {
            System.out.println("Classes**** " + classes);
            TipoSolo solo = new TipoSolo();
            Atributo atr = atributoDAO.buscaPorCodigo(classificacao.getAtributo().getCodigo());
            classificacao.setAtributo(atr);
            Entidade ent = entidadeDAO.buscaEntidadeByCodigo(classificacao.getEntidade().getCodigo());
            classificacao.setEntidade(ent);
            solo.setCodigo(classificacao.getTipoSolo().getCodigo());
            TipoSolo soloaux = tipoSoloDAO.buscaSoloByCodigo(classificacao.getTipoSolo().getCodigo());
            solo.setUsuarios(soloaux.getUsuarios());
            classificacao.setTipoSolo(solo);
            classificacao.setUsuario(usuarioSession.getUsuario());
            gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            // gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            ClientResponse response = client.postResource("classificacao", usuarioSession.getUsuario().getToken(), gson.toJson(classificacao));
            String json = response.getEntity(String.class);

            if (response.getStatus() != 201) {
                System.out.println("erro persiste classificacao api");
                // MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                //System.out.println("mensagem de erro = " + mensagem.toString());
                // throw new PPATException(mensagem.getMessage());
            }
            int fim = json.indexOf("\"entidade");
            Long aux = Long.parseLong(json.substring(10, fim - 1));
            classificacao.setCodigo(aux);
            //fim parte que salva classificacao na api
            List<Classe> doList = new Gson().fromJson(classes, new TypeToken<List<Classe>>() {
            }.getType());
            System.out.println("doList**** " + doList.size());
            for (int i = 0; i < doList.size(); i++) {
                doList.get(i).setClassificacao(classificacao);
                Classe classe = new Classe();
                Classificacao classificacaoaux = new Classificacao();
                classificacaoaux.setCodigo(classificacao.getCodigo());
                classe.setClassificacao(classificacaoaux);
                classe.setCor(doList.get(i).getCor());
                classe.setNivel(doList.get(i).getNivel());
                classe.setValorMaximo(doList.get(i).getValorMaximo());
                classe.setValorMinimo(doList.get(i).getValorMinimo());
                gsonClasse = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create();
                ClientResponse responseClasse = client.postResource("classe", usuarioSession.getUsuario().getToken(), gsonClasse.toJson(classe));
                String jsonClasse = responseClasse.getEntity(String.class);

                if (responseClasse.getStatus() != 201) {
                    System.out.println("erro persiste classe api");
                    // MensagemRetorno mensagem = gsonClasse.fromJson(jsonClasse, MensagemRetorno.class);
                    // System.out.println("mensagem de erro = " + mensagem.toString());
                    //throw new PPATException(mensagem.getMessage());
                } else {
                    int fimclasse = jsonClasse.indexOf("\"nivel");
                    Long auxclasse = Long.parseLong(jsonClasse.substring(10, fimclasse - 1));
                    classe.setCodigo(auxclasse);
                    doList.get(i).setCodigo(auxclasse);
                }

            }

            classificacao.setUsuario(usuarioSession.getUsuario());
            classificacao.setAtributo(atributoDAO.buscaPorCodigo(classificacao.getAtributo().getCodigo()));
            classificacao.setTipoSolo(tipoSoloDAO.buscaSoloByCodigo(classificacao.getTipoSolo().getCodigo()));
            classificacao.setEntidade(entidadeDAO.buscaEntidadeByCodigo(classificacao.getEntidade().getCodigo()));

            if (classificacao.getId() == null) {
                classificacaoDAO.adicionar(classificacao);
                classificacao.setClasses(doList);
                classificacaoDAO.alterar(classificacao);
                result.include("success", "success.cadastro.classificacao").include("idProjeto", idProjeto).redirectTo(this).lista(idProjeto);
            } else {

                classificacaoDAO.alterar(classificacao);
                result.include("success", "success.editar.classificacao").include("idProjeto", idProjeto).redirectTo(this).lista(idProjeto);
            }

        } catch (Exception e) {
            System.out.println("erro persiste classificacao: " + e);
            result.include("danger", "danger.cadastro.classificacao").include("idProjeto", idProjeto).redirectTo(this).formulario(idProjeto);
        }
    }

    @Get("editar")
    @Permissao(Roles.ON)
    public void editar(Long id) {
        //  result.include("classificacao", classificacaoDAO.buscarPorId(id)).redirectTo(this).formulario();
    }

    @Get("excluir")
    @Permissao(Roles.ON)
    public void excluir(Long codigo, Long idProjeto) {
        try {
            System.out.println("IDPROJETO: " + idProjeto);
            if (codigo == null) {
                System.out.println("Código da classificação não informado.");
                //throw new PPATException("Código da classificação não informado.");
            }
            gsonClasse = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            List<Classe> classesapi = new ArrayList<>();

            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();
            ClientResponse responseClasse = client.getResource("classe", usuarioSession.getUsuario().getToken());
            String jsonClasse = responseClasse.getEntity(String.class);
            switch (responseClasse.getStatus()) {
                case 200:
                    classesapi = (List<Classe>) gsonClasse.fromJson(jsonClasse, new TypeToken<ArrayList<Classe>>() {
                    }.getType());
                    for (int i = 0; i < classesapi.size(); i++) {
                        if (classesapi.get(i).getClassificacao().getCodigo().equals(codigo)) {
                            gsonClasse2 = new GsonBuilder()
                                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                                    .create();

                            ClientResponse responseClasse2 = client.deleteResource("classe/" + classesapi.get(i).getCodigo(), usuarioSession.getUsuario().getToken());
                            String jsonClasse2 = responseClasse2.getEntity(String.class);

                            if (responseClasse2.getStatus() != 200) {
                                MensagemRetorno mensagem = gsonClasse2.fromJson(jsonClasse2, MensagemRetorno.class);
                                throw new PPATException(mensagem.getMessage());
                            }
                        }
                    }
                    ClientResponse response = client.deleteResource("classificacao/" + codigo, usuarioSession.getUsuario().getToken());
                    String json = response.getEntity(String.class);

                    if (response.getStatus() != 200) {
                        MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                        throw new PPATException(mensagem.getMessage());
                    } else {
                        Classificacao classificacao = classificacaoDAO.buscaPorCodigo(codigo);
                        if (classificacao == null) {

                        } else {
                            classificacaoDAO.excluir(classificacao);
                        }
                    }
                case 404:
                    classesapi = new ArrayList<>();
                    break;
                default:
                    System.out.println("erro excluir classificacao api.");
                //MensagemRetorno mensagem = gsonClasse.fromJson(jsonClasse, MensagemRetorno.class);
                //throw new PPATException(mensagem.getMessage());
            }
        } catch (PPATException ex) {
            System.out.println("erro excluir classificacao api: " + ex);
            result.include("danger", "danger.excluir.classificacao").include("idProjeto", idProjeto).redirectTo(this).lista(idProjeto);
        }
        result.include("success", "success.excluir.classificacao").include("idProjeto", idProjeto).redirectTo(this).lista(idProjeto);
    }
}
