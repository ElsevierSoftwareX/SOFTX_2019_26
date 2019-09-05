/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sdum.controller;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.sdum.annotation.Permissao;
import br.com.sdum.dao.EntidadeDAO;
import br.com.sdum.dao.UsuarioDAO;
import br.com.sdum.enums.Roles;
import br.com.sdum.exception.PPATException;
import br.com.sdum.model.Entidade;
import br.com.sdum.model.MensagemRetorno;
import br.com.sdum.model.Usuario;
import br.com.sdum.session.UsuarioSession;
import br.com.sdum.ws.client.PPATWSClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.ClientResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 *
 * @author Gabriela
 */
@Resource
@Path("entidade")
public class EntidadeController implements Serializable {

    private static final long serialVersionUID = -5408792479258357616L;

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
    private EntidadeDAO entidadeDAO;

    @Inject
    private UsuarioDAO usuarioDAO;

    @Get("lista")
    @Permissao(Roles.ON)
    public void lista(Long idProjeto) {
        try {

            ClientResponse response = client.getResource("entidade", userInfo.getUsuario().getToken());
            String json = response.getEntity(String.class);

            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            List<Entidade> entidade = new ArrayList<>();

            switch (response.getStatus()) {
                case 200:
                    entidade = (List<Entidade>) gson.fromJson(json, new TypeToken<ArrayList<Entidade>>() {
                    }.getType());
                    for (int j = 0; j < entidade.size(); j++) {
                        Entidade ent = entidadeDAO.buscaEntidadeByCodigo(entidade.get(j).getCodigo());
                        if (ent == null) {
                            ent = entidade.get(j);
                            ent.setUsuario(usuarioSession.getUsuario());
                            entidadeDAO.adicionar(ent);
                        }
                    }
                    break;
                case 404:
                    entidade = new ArrayList<>();
                    break;
                default:
                    System.out.println("Erro get entidade api");
                    //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                    //throw new PPATException(mensagem.getMessage());
            }

            result.include("listaEntidades", entidade).include("idProjeto", idProjeto);

        } catch (PPATException ex) {
            System.out.println("Erro get entidade api: "+ex);
            result.include("listaEntidades", new ArrayList()).include("idProjeto", idProjeto);
            //result.include("mensagem", ex.getMessage());
        }
    }

    @Get("formulario")
    @Permissao(Roles.ON)
    public void formulario(Long idProjeto) {
        result.include("idProjeto", idProjeto);
    }

    @Post("persiste")
    @Permissao(Roles.ON)
    public void cadastrarEditar(Entidade entidade, Long idProjeto) {
        System.out.println("ID PROJETO PERSISTE ENTIDADE "+idProjeto);
        try {
            if (entidade.getCodigo() == null) {

                Usuario u = usuarioDAO.buscarPorId(usuarioSession.getUsuario().getId());
                entidade.setUsuario(u);

                //salvando na api
                gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                // gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                String jsonOutput = gson.toJson(entidade);
                ClientResponse response = client.postResource("entidade", userInfo.getUsuario().getToken(), gson.toJson(entidade));
                String json = response.getEntity(String.class);

                if (response.getStatus() != 201) {
                   // MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                    result.include("danger", "danger.cadastro.entidade").include("idProjeto", idProjeto).redirectTo(this).lista(idProjeto);
                    //throw new PPATException(mensagem.getMessage());
                } else {
                    //fim parte que salva na api

                    Entidade entidadeaux = null;
                    entidadeaux = gson.fromJson(json, Entidade.class);
                    entidade.setCodigo(entidadeaux.getCodigo());
                    entidadeDAO.adicionar(entidade);
                    result.include("success", "success.cadastro.entidade").include("idProjeto", idProjeto).redirectTo(this).lista(idProjeto);
                }
            } else {

                //PUT na API
                // gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                //gson = new GsonBuilder().create();
                ClientResponse response = client.putResource("entidade", userInfo.getUsuario().getToken(), gson.toJson(entidade));
                String json = response.getEntity(String.class);

                if (response.getStatus() != 200) {
                    result.include("danger", "danger.editar.entidade").include("idProjeto", idProjeto).redirectTo(this).lista(idProjeto);
                    //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                    //throw new PPATException(mensagem.getMessage());
                }else{
                //fim do PUT API

                Entidade aux = entidadeDAO.buscaEntidadeByCodigo(entidade.getCodigo());
                Long id = aux.getId();
                entidade.setUsuario(usuarioDAO.buscarPorId(usuarioSession.getUsuario().getId()));
                entidade.setId(id);
                entidadeDAO.alterar(entidade);
                result.include("success", "success.editar.entidade").include("idProjeto", idProjeto).redirectTo(this).lista(idProjeto);
                }
            }
        } catch (PPATException ex) {
            // result.include("entidade", entidade).include("idProjeto",idProjeto);
            System.out.println("Erro persiste: " + ex);
            result.include("idProjeto", idProjeto).redirectTo(this).lista(idProjeto);
            // result.include("mensagem", ex.getMessage());
        }
    }

    @Get("editar")
    @Permissao(Roles.ON)
    public void editar(Long codigo, Long idProjeto) {
        try {
            ClientResponse response = client.getResource("entidade/" + codigo, userInfo.getUsuario().getToken());
            String json = response.getEntity(String.class);

            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            Entidade entidade = null;

            switch (response.getStatus()) {
                case 200:
                    entidade = gson.fromJson(json, Entidade.class);
                    Entidade entidadeAux = entidadeDAO.buscaEntidadeByCodigo(codigo);
                    if (entidadeAux == null) {
                        Usuario u = usuarioDAO.buscarPorId(usuarioSession.getUsuario().getId());
                        entidade.getUsuario().setId(u.getId());
                        entidadeDAO.adicionar(entidade);
                    }
                    break;
                default:
                    System.out.println("Erro editar entidade api");
                    //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                    //throw new PPATException(mensagem.getMessage());
            }
            String editar = "editar";
            result.include("editar",editar).include("entidade", entidade).include("idProjeto", idProjeto).redirectTo(this).formulario(idProjeto);

        } catch (PPATException ex) {
            System.out.println("Exception editar entidade api: "+ex);
            result.include("idProjeto", idProjeto);
            result.redirectTo(this).lista(idProjeto);
        }

    }

    @Get("excluir")
    @Permissao(Roles.ON)
    public void excluir(Long codigo, Long idProjeto) {
        try {

            if (codigo == null) {
                System.out.println("código entidade null dentro do excluir");
                //throw new PPATException("Código da entidade não informado.");
            }

            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            ClientResponse response = client.deleteResource("entidade/" + codigo, userInfo.getUsuario().getToken());
            String json = response.getEntity(String.class);

            if (response.getStatus() != 200) {
                 System.out.println("Erro excluir entidade da api");
                //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                //throw new PPATException(mensagem.getMessage());
            } else {
                Entidade entidade = entidadeDAO.buscaEntidadeByCodigo(codigo);
                if (entidade == null) {

                } else {
                    entidadeDAO.excluir(entidade);
                }
            }
            result.include("success", "success.excluir.entidade").include("idProjeto", idProjeto).redirectTo(this).lista(idProjeto);
        } catch (PPATException ex) {
            System.out.println("Exception ***** " + ex);
            result.include("danger", "danger.excluir.entidade").include("idProjeto", idProjeto).redirectTo(this).lista(idProjeto);
        }
        
    }

}
