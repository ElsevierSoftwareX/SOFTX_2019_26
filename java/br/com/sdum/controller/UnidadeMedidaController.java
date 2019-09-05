package br.com.sdum.controller;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.sdum.annotation.Permissao;
import br.com.sdum.dao.UnidadeMedidaDAO;
import br.com.sdum.dao.UsuarioDAO;
import br.com.sdum.enums.Roles;
import br.com.sdum.exception.PPATException;
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
@Path("unidadeMedida")
public class UnidadeMedidaController implements Serializable {

    private static final long serialVersionUID = -2027134059183136503L;

    @Inject
    private UsuarioSession usuarioSession;

    @Inject
    private Result result;

    @Inject
    private PPATWSClient client;

    @Inject
    private UsuarioSession userInfo;

    private Gson gson;

    @Inject
    private UnidadeMedidaDAO unidadeMedidaDAO;

    @Inject
    private UsuarioDAO usuarioDAO;

    @Get("lista")
    @Permissao(Roles.ON)
    public void lista(Long idProjeto) {
        try {

            ClientResponse response = client.getResource("unidademedida", userInfo.getUsuario().getToken());
            String json = response.getEntity(String.class);

            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            List<UnidadeMedida> unidadeMedida = new ArrayList<>();

            switch (response.getStatus()) {
                case 200:
                    unidadeMedida = (List<UnidadeMedida>) gson.fromJson(json, new TypeToken<ArrayList<UnidadeMedida>>() {
                    }.getType());
                    break;
                case 404:
                    unidadeMedida = new ArrayList<>();
                    break;
                default:
                    System.out.println("Erro get unidademedida api no listar unidademedida");
                //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                //throw new PPATException(mensagem.getMessage());
            }

            result.include("idProjeto", idProjeto).include("listaUnidades", unidadeMedida);

        } catch (PPATException ex) {
            System.out.println("Exception get unidademedida api no listar unidademedida: " + ex);
            result.include("idProjeto", idProjeto).include("listaUnidades", new ArrayList());
            //result.include("mensagem", ex.getMessage());
        }
        //result.include("listaUnidades", unidadeMedidaDAO.listar());
    }

    @Get("formulario")
    @Permissao(Roles.ON)
    public void formulario(Long idProjeto) {
        result.include("idProjeto", idProjeto);
    }

    @Post("persiste")
    @Permissao(Roles.ON)
    public void cadastrarEditar(UnidadeMedida unidadeMedida, Long idProjeto) {

        try {
            if (unidadeMedida.getCodigo() == null) {

                Usuario u = usuarioDAO.buscarPorId(usuarioSession.getUsuario().getId());
                unidadeMedida.setUsuario(u);

                //salvando na api
                gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                // gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                String jsonOutput = gson.toJson(unidadeMedida);
                System.out.println("gson= " + jsonOutput);

                ClientResponse response = client.postResource("unidademedida", userInfo.getUsuario().getToken(), gson.toJson(unidadeMedida));
                System.out.println("response= " + jsonOutput);

                String json = response.getEntity(String.class);
                System.out.println("json= " + json);

                if (response.getStatus() != 201) {
                    System.out.println("Erro post unidademedida api no cadastrarEditar unidademedida");
                    //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                    //System.out.println("mensagem de erro = " + mensagem.toString());
                    //throw new PPATException(mensagem.getMessage());
                } else {
                    //fim parte que salva na api

                    UnidadeMedida unidadeMedidaaux = null;
                    unidadeMedidaaux = gson.fromJson(json, UnidadeMedida.class);
                    unidadeMedida.setCodigo(unidadeMedidaaux.getCodigo());
                    unidadeMedidaDAO.adicionar(unidadeMedida);
                    result.include("idProjeto", idProjeto).include("success", "success.cadastro.unidadeMedida").redirectTo(this).lista(idProjeto);
                }
            } else {

                //PUT na API
                // gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                //gson = new GsonBuilder().create();
                ClientResponse response = client.putResource("unidademedida", userInfo.getUsuario().getToken(), gson.toJson(unidadeMedida));
                String json = response.getEntity(String.class);

                if (response.getStatus() != 200) {
                    System.out.println("Erro put unidademedida api no cadastrarEditar unidademedida " + gson.toJson(unidadeMedida));
                    if(response.getStatus() == 409){
                        result.include("danger", "unidademedida.editar").include("idProjeto", idProjeto).include("unidadeMedida", unidadeMedida).redirectTo(this).lista(idProjeto);
                    }
                    //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                    //throw new PPATException(mensagem.getMessage());
                } else {
                    //fim do PUT API

                    UnidadeMedida aux = unidadeMedidaDAO.buscaUnidadeByCodigo(unidadeMedida.getCodigo());
                    Long id = aux.getId();
                    unidadeMedida.setUsuario(usuarioDAO.buscarPorId(usuarioSession.getUsuario().getId()));
                    unidadeMedida.setId(id);
                    unidadeMedidaDAO.alterar(unidadeMedida);
                    result.include("idProjeto", idProjeto).include("success", "success.editar.unidadeMedida").redirectTo(this).lista(idProjeto);
                }
            }
        } catch (Exception ex) {
            System.out.println("Exception no cadastrarEditar unidademedida: " + ex);
            result.include("idProjeto", idProjeto).include("unidadeMedida", unidadeMedida).redirectTo(this).lista(idProjeto);
            //result.include("mensagem", ex.getMessage());
        }
    }

    @Get("editar")
    @Permissao(Roles.ON)
    public void editar(Long codigo, Long idProjeto
    ) {
        try {
            ClientResponse response = client.getResource("unidademedida/" + codigo, userInfo.getUsuario().getToken());
            String json = response.getEntity(String.class);

            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            UnidadeMedida unidadeMedida = null;

            switch (response.getStatus()) {
                case 200:
                    unidadeMedida = gson.fromJson(json, UnidadeMedida.class);
                    UnidadeMedida unidadeMedidaAux = unidadeMedidaDAO.buscaUnidadeByCodigo(codigo);
                    if (unidadeMedidaAux == null) {
                        Usuario u = usuarioDAO.buscarPorId(usuarioSession.getUsuario().getId());
                        unidadeMedida.getUsuario().setId(u.getId());
                        unidadeMedidaDAO.adicionar(unidadeMedida);
                    }
                    break;
                default:
                    System.out.println("Erro get unidademedida api no editar unidademedida");
                //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                //throw new PPATException(mensagem.getMessage());
            }
            String editar = "editar";
            result.include("editar", editar).include("idProjeto", idProjeto).include("unidadeMedida", unidadeMedida).redirectTo(this).formulario(idProjeto);

        } catch (Exception ex) {
            System.out.println("Exception no editar unidademedida: " + ex);
            // result.include("mensagem", ex.getMessage());
            result.include("idProjeto", idProjeto).redirectTo(this).lista(idProjeto);
        }
        //result.include("unidadeMedida", unidadeMedidaDAO.buscarPorId(id)).redirectTo(this).formulario();
    }

    @Get("excluir")
    @Permissao(Roles.ON)
    public void excluir(Long codigo, Long idProjeto) {
        try {

            if (codigo == null) {
                throw new PPATException("Código da unidade de medida não informado.");
            }

            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            ClientResponse response = client.deleteResource("unidademedida/" + codigo, userInfo.getUsuario().getToken());
            String json = response.getEntity(String.class);

            if (response.getStatus() != 200) {
                System.out.println("Erro delete unidademedida api no excluir unidademedida");
                //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                //throw new PPATException(mensagem.getMessage());
            } else {
                UnidadeMedida unidadeMedida = unidadeMedidaDAO.buscaUnidadeByCodigo(codigo);
                if (unidadeMedida == null) {

                } else {
                    unidadeMedidaDAO.excluir(unidadeMedida);
                }
            }

        } catch (PPATException ex) {
            System.out.println("Exception excluir unidademedida: " + ex);
            result.include("idProjeto", idProjeto).redirectTo(this).lista(idProjeto);
        }

        //unidadeMedidaDAO.excluir(unidadeMedidaDAO.buscarPorId(id));
        result.include("idProjeto", idProjeto).include("success", "success.excluir.unidadeMedida").redirectTo(this).lista(idProjeto);
    }
}
