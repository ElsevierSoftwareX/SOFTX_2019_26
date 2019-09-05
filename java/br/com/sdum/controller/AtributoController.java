package br.com.sdum.controller;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.sdum.annotation.Permissao;
import br.com.sdum.dao.AtributoDAO;
import br.com.sdum.dao.UnidadeMedidaDAO;
import br.com.sdum.dao.UsuarioDAO;
import br.com.sdum.enums.Roles;
import br.com.sdum.exception.PPATException;
import br.com.sdum.model.Atributo;
import br.com.sdum.model.MensagemRetorno;
import br.com.sdum.model.UnidadeMedida;
import br.com.sdum.model.Usuario;
import br.com.sdum.session.UsuarioSession;
import br.com.sdum.ws.client.PPATWSClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.ClientResponse;
import java.util.ArrayList;
import java.util.List;

@Resource
@Path("atributo")
public class AtributoController implements Serializable {

    private static final long serialVersionUID = 73875484460134936L;

    @Inject
    private Result result;

    @Inject
    private AtributoDAO atributoDAO;

    @Inject
    private UnidadeMedidaDAO unidadeMedidaDAO;

    @Inject
    private PPATWSClient client;

    @Inject
    private UsuarioSession userInfo;

    private Gson gson;

    @Inject
    private UsuarioDAO usuarioDAO;

    @Get("lista")
    @Permissao(Roles.ON)
    public void lista(Long id) {
        try {

            ClientResponse response = client.getResource("atributo", userInfo.getUsuario().getToken());
            String json = response.getEntity(String.class);

            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            List<Atributo> atributo = new ArrayList<>();

            switch (response.getStatus()) {
                case 200:
                    atributo = (List<Atributo>) gson.fromJson(json, new TypeToken<ArrayList<Atributo>>() {
                    }.getType());
                    break;
                case 404:
                    atributo = new ArrayList<>();
                    break;
                default:
                    System.out.println("erro get atributo api");
                    //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                    //throw new PPATException(mensagem.getMessage());
            }

            ClientResponse response2 = client.getResource("unidademedida", userInfo.getUsuario().getToken());
            String json2 = response2.getEntity(String.class);

            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            List<UnidadeMedida> unidadeMedida = new ArrayList<>();

            switch (response2.getStatus()) {
                case 200:
                    unidadeMedida = (List<UnidadeMedida>) gson.fromJson(json2, new TypeToken<ArrayList<UnidadeMedida>>() {
                    }.getType());
                    break;
                case 404:
                    unidadeMedida = new ArrayList<>();
                    break;
                default:
                     System.out.println("erro get unidademedida api");
                    //MensagemRetorno mensagem = gson.fromJson(json2, MensagemRetorno.class);
                    //throw new PPATException(mensagem.getMessage());
            }

            result.include("listaAtributos", atributo).include("idProjeto", id).include("listaUnidadeMedida", unidadeMedida);

        } catch (PPATException ex) {

            result.include("listaAtributos", new ArrayList());
            result.include("idProjeto", id).include("mensagem", ex.getMessage());
        }

        // result.include("listaAtributos", atributoDAO.listar())
        //.include("listaUnidadeMedida", unidadeMedidaDAO.listar());;
    }

    @Get("formulario")
    @Permissao(Roles.ON)
    public void formulario(Long id) {

        try {
            //result.include("listaUnidadeMedida", unidadeMedidaDAO.listar());
            ClientResponse response2 = client.getResource("unidademedida", userInfo.getUsuario().getToken());
            String json2 = response2.getEntity(String.class);

            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            List<UnidadeMedida> unidadeMedida = new ArrayList<>();

            switch (response2.getStatus()) {
                case 200:
                    unidadeMedida = (List<UnidadeMedida>) gson.fromJson(json2, new TypeToken<ArrayList<UnidadeMedida>>() {
                    }.getType());
                    break;
                case 404:
                    unidadeMedida = new ArrayList<>();
                    break;
                default:
                    System.out.println("erro get unidademedida api");
                    //MensagemRetorno mensagem = gson.fromJson(json2, MensagemRetorno.class);
                   // throw new PPATException(mensagem.getMessage());
            }

            result.include("idProjeto",id).include("listaUnidadeMedida", unidadeMedida);

        } catch (Exception ex) {
            System.out.println("Exception Atributo form: "+ex);
            result.include("listaAtributos", new ArrayList());
            result.include("idProjeto",id);
        }

    }

    @Post("persiste")
    @Permissao(Roles.ON)
    public void cadastrarEditar(Atributo atributo, Long idProjeto) throws PPATException {
        Atributo atributoAux = new Atributo();
        try {
            UnidadeMedida um = unidadeMedidaDAO.buscaUnidadeByCodigoEUsuario(atributo.getUnidadeMedidaPT().getCodigo(), userInfo.getUsuario());
            if (um == null) {
                //get unidademedida pra salvar no banco sdum
                ClientResponse response = client.getResource("unidademedida/" + atributo.getUnidadeMedidaPT().getCodigo(), userInfo.getUsuario().getToken());
                String json = response.getEntity(String.class);

                gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create();

                switch (response.getStatus()) {
                    case 200:
                        //get deu sucesso, salva unidademedida no banco sdum
                        um = gson.fromJson(json, UnidadeMedida.class);
                        Usuario u = usuarioDAO.buscarPorId(userInfo.getUsuario().getId());
                        um.getUsuario().setId(u.getId());
                        unidadeMedidaDAO.adicionar(um);
                        break;
                    default:
                        System.out.println("Erro get unidade medida api");
                        //MensagemRetorno mensagem2 = gson.fromJson(json, MensagemRetorno.class);
                       //throw new PPATException(mensagem2.getMessage());
                }

            }
            if (atributo.getCodigo() == null) {
                atributo.setDescricaoEN(atributo.getDescricaoPT());
                atributo.setDescricaoES(atributo.getDescricaoPT());
                atributo.setSiglaEN(atributo.getSiglaPT());
                atributo.setSiglaES(atributo.getSiglaPT());
                atributo.setUnidadeMedidaEN(atributo.getUnidadeMedidaPT());
                atributo.setUnidadeMedidaES(atributo.getUnidadeMedidaPT());

                //salvando atributo na api
                gson = new GsonBuilder().create();

                ClientResponse response2 = client.postResource("atributo", userInfo.getUsuario().getToken(), gson.toJson(atributo));
                String json2 = response2.getEntity(String.class);

                if (response2.getStatus() != 201) {
                    //MensagemRetorno mensagem = gson.fromJson(json2, MensagemRetorno.class);
                    System.out.println("Erro post atributo api ");
                    //throw new PPATException(mensagem.getMessage());
                }
                //fim parte que salva na api
                // salva atributo banco sdum
                atributoAux = atributo;
                int fim = json2.indexOf("\"descricaoPT");
                Long aux = Long.parseLong(json2.substring(10, fim - 1));
                atributoAux.setCodigo(aux);
                atributoAux.setUnidadeMedidaPT(um);
                atributoAux.setUnidadeMedidaEN(um);
                atributoAux.setUnidadeMedidaES(um);
                atributoDAO.adicionar(atributoAux);
                result.include("idProjeto", idProjeto).include("success", "success.cadastro.atributo").redirectTo(this).lista(idProjeto);
            } else {
                //put atributo na api

                gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create();
                atributo.setDescricaoEN(atributo.getDescricaoPT());
                atributo.setDescricaoES(atributo.getDescricaoPT());
                atributo.setSiglaEN(atributo.getSiglaPT());
                atributo.setSiglaES(atributo.getSiglaPT());
                atributo.setUnidadeMedidaEN(atributo.getUnidadeMedidaPT());
                atributo.setUnidadeMedidaES(atributo.getUnidadeMedidaPT());
                ClientResponse response3 = client.putResource("atributo", userInfo.getUsuario().getToken(), gson.toJson(atributo));
                String json3 = response3.getEntity(String.class);

                if (response3.getStatus() != 200) {
                    System.out.println("Erro put atributo api");
                    //MensagemRetorno mensagem = gson.fromJson(json3, MensagemRetorno.class);
                    //throw new PPATException(mensagem.getMessage());
                }
                //fim do PUT API
                Atributo aux = atributoDAO.buscaPorCodigoEUsuario(atributo.getCodigo(), userInfo.getUsuario());
                if (aux == null) {
                    aux = atributo;
                    aux.setUnidadeMedidaPT(um);
                    aux.setUnidadeMedidaEN(um);
                    aux.setUnidadeMedidaES(um);
                    atributoDAO.adicionar(aux);
                } else {
                    Long idatributo = aux.getId();
                    aux = atributo;
                    aux.setId(idatributo);
                    aux.setUnidadeMedidaPT(um);
                    aux.setUnidadeMedidaEN(um);
                    aux.setUnidadeMedidaES(um);
                    atributoDAO.alterar(aux);
                    result.include("idProjeto", idProjeto).include("success", "success.editar.atributo").redirectTo(this).lista(idProjeto);
                }

            }

        } catch (PPATException ex) {
            System.out.println("Exception cadastrar atributo: "+ex);
            result.include("atributo", atributo).include("idProjeto", idProjeto).include("danger", "danger.cadastrar.atributo").redirectTo(this).lista(idProjeto);
            //result.include("mensagem", ex.getMessage());
        }
        /*if (atributo.getId() == null) {

            atributoDAO.adicionar(atributo);
            result.include("success", "success.cadastro.atributo").redirectTo(this).lista();
        } else {

            atributoDAO.alterar(atributo);
            result.include("success", "success.editar.atributo").redirectTo(this).lista();
        }*/
    }

    @Get("editar")
    @Permissao(Roles.ON)
    public void editar(Long codigo, Long idProjeto) {
        try {
            ClientResponse response = client.getResource("atributo/" + codigo, userInfo.getUsuario().getToken());
            String json = response.getEntity(String.class);

            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            Atributo atributo = null;

            switch (response.getStatus()) {
                case 200:
                    atributo = gson.fromJson(json, Atributo.class);
                    Atributo atributoAux = atributoDAO.buscaPorCodigo(codigo);
                    if (atributoAux == null) {
                        atributoAux = atributo;
                        UnidadeMedida uni = unidadeMedidaDAO.buscaUnidadeByCodigo(atributo.getUnidadeMedidaPT().getCodigo());
                        if (uni == null) {
                            //get da UnidadeMedida para salvar no banco sdum
                            ClientResponse response2 = client.getResource("unidademedida/" + atributo.getUnidadeMedidaPT().getCodigo(), userInfo.getUsuario().getToken());
                            String json2 = response2.getEntity(String.class);

                            gson = new GsonBuilder()
                                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                                    .create();

                            switch (response2.getStatus()) {
                                case 200:
                                    //get deu sucesso, salva unidademedida no banco sdum
                                    uni = gson.fromJson(json2, UnidadeMedida.class);
                                    Usuario u = usuarioDAO.buscarPorId(userInfo.getUsuario().getId());
                                    uni.getUsuario().setId(u.getId());
                                    unidadeMedidaDAO.adicionar(uni);
                                    atributoAux.setUnidadeMedidaPT(uni);
                                    atributoAux.setUnidadeMedidaEN(uni);
                                    atributoAux.setUnidadeMedidaES(uni);
                                    atributoAux.setCodigo(codigo);
                                    atributoDAO.adicionar(atributoAux);
                                    break;
                                default:
                                    System.out.println("Erro get unidade medida api no editar atributo");
                                    //MensagemRetorno mensagem2 = gson.fromJson(json, MensagemRetorno.class);
                                    //throw new PPATException(mensagem2.getMessage());
                            }

                        } else {
                            Atributo atrSdum = atributo;
                            atrSdum.setUnidadeMedidaPT(uni);
                            atrSdum.setUnidadeMedidaEN(uni);
                            atrSdum.setUnidadeMedidaES(uni);
                            atrSdum.setCodigo(codigo);
                            atributoDAO.adicionar(atrSdum);
                        }
                    }
                    break;

                default:
                    System.out.println("Erro get atributo api no editar atributo");
                    //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                    //throw new PPATException(mensagem.getMessage());
            }
            String editar = "editar";
            result.include("editar", editar).include("idProjeto", idProjeto).include("atributo", atributo).redirectTo(this).formulario(idProjeto);

        } catch (Exception ex) {
            System.out.println("Exceção atributo editar: "+ex);
            result.include("idProjeto", idProjeto).redirectTo(this).lista(idProjeto);
        }

        // result.include("atributo", atributoDAO.buscarPorId(id)).redirectTo(this).formulario();
    }

    @Get("excluir")
    @Permissao(Roles.ON)
    public void excluir(Long codigo, Long idProjeto) {
        try {

            if (codigo == null) {
                System.out.println("Código da unidade de medida não informado.");
               // throw new PPATException("Código da unidade de medida não informado.");
            }

            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            ClientResponse response = client.deleteResource("atributo/" + codigo, userInfo.getUsuario().getToken());
            String json = response.getEntity(String.class);

            if (response.getStatus() != 200) {
                System.out.println("erro delete atributo api");
                //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                //throw new PPATException(mensagem.getMessage());
            } else {
                Atributo atributo = atributoDAO.buscaPorCodigo(codigo);
                if (atributo == null) {

                } else {
                    atributoDAO.excluir(atributo);
                }
            }
            result.include("idProjeto", idProjeto).include("success", "success.excluir.atributo").redirectTo(this).lista(idProjeto);

        } catch (PPATException ex) {
            System.out.println("erro delete atributo api"+ex);
            result.include("idProjeto", idProjeto).include("danger", "danger.excluir.atributo").redirectTo(this).lista(idProjeto);
        }

      

        //atributoDAO.excluir(atributoDAO.buscarPorId(id));
        //result.include("success", "success.excluir.atributo").redirectTo(this).lista();
    }

}
