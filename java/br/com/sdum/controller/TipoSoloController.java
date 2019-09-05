package br.com.sdum.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.sdum.annotation.Permissao;
import br.com.sdum.dao.TipoSoloDAO;
import br.com.sdum.dao.UsuarioDAO;
import br.com.sdum.enums.Roles;
import br.com.sdum.exception.PPATException;
import br.com.sdum.model.Area;
import br.com.sdum.model.MensagemRetorno;
import br.com.sdum.model.TipoSolo;
import br.com.sdum.model.Usuario;
import br.com.sdum.session.UsuarioSession;
import br.com.sdum.ws.client.PPATWSClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.ClientResponse;
import javax.swing.JOptionPane;

@Resource
@Path("tipoSolo")
public class TipoSoloController implements Serializable {

    private static final long serialVersionUID = 73875484460134936L;

    @Inject
    private Result result;

    @Inject
    private TipoSoloDAO soloDAO;

    @Inject
    private UsuarioDAO usuarioDAO;

    @Inject
    private UsuarioSession usuarioSession;

    @Inject
    private PPATWSClient client;

    @Inject
    private UsuarioSession userInfo;

    private Gson gson;

    @Get("lista")
    @Permissao(Roles.ON)
    public void lista(Long id) {
        try {

            ClientResponse response = client.getResource("solo", userInfo.getUsuario().getToken());
            String json = response.getEntity(String.class);

            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

            List<TipoSolo> tipoSolo = new ArrayList<>();
            List<TipoSolo> soloUsuLogado = new ArrayList<>();
            List<TipoSolo> soloOutroUsu = new ArrayList<>();

            switch (response.getStatus()) {
                case 200:
                    tipoSolo = (List<TipoSolo>) gson.fromJson(json, new TypeToken<ArrayList<TipoSolo>>() {
                    }.getType());
                    for (int i = 0; i < tipoSolo.size(); i++) {
                        if (tipoSolo.get(i).getUsuario().getCodigo().equals(usuarioSession.getUsuario().getCodigo())) {
                            soloUsuLogado.add(tipoSolo.get(i));
                            System.out.println("\n -------- entrou -------- \n ");
                        } else {
                            soloOutroUsu.add(tipoSolo.get(i));
                        }
                    }
                    break;
                case 404:
                    tipoSolo = new ArrayList<>();
                    break;
                default:
                    System.out.println("Erro  get tiposolos no lista tiposolos");
                //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                //throw new PPATException(mensagem.getMessage());
            }

            result.include("listaSolos", soloUsuLogado).include("listaSolosOutrosUsu", soloOutroUsu).include("idUsuario", usuarioSession.getUsuario().getId());

        } catch (PPATException ex) {
            System.out.println("Exception  get tiposolos no lista tiposolos: " + ex);
            result.include("listaSolos", new ArrayList());
            //result.include("mensagem", ex.getMessage());
        }

        //result.include("idUsuario", usuarioSession.getUsuario().getId()).include("listaSolos", soloDAO.listaSolosByUsuario(usuarioSession.getUsuario().getId()));
    }

    @Get("formulario")
    @Permissao(Roles.ON)
    public void formulario(Long id) {
        result
                .include("idUsuario", usuarioSession.getUsuario().getId());
    }

    @Post("persiste")
    @Permissao(Roles.ON)
    public void cadastrarEditar(TipoSolo tipoSolo, Long idUsuario) {
        List<Usuario> usuarios = new ArrayList<Usuario>();
        List<TipoSolo> solos = new ArrayList<TipoSolo>();

        try {
            if (tipoSolo.getCodigo() == null) {
                //if (tipoSolo.getId() == null) {
                //tipoSolo.setDatacadastro(new Date());
                Usuario usu = usuarioDAO.buscarPorId(idUsuario);
                usuarios.add(usu);
                tipoSolo.setUsuarios(usuarios);
                tipoSolo.setDescricaoEN(tipoSolo.getDescricaoPT());
                tipoSolo.setDescricaoES(tipoSolo.getDescricaoPT());
                //salvando na api
                gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                //gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
                String jsonOutput = gson.toJson(tipoSolo);
                System.out.println("gson= " + jsonOutput);

                ClientResponse response = client.postResource("solo", userInfo.getUsuario().getToken(), gson.toJson(tipoSolo));
                String json = response.getEntity(String.class);

                if (response.getStatus() != 201) {
                    System.out.println("Erro post tiposolos api no persiste tiposolos");
                    //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                    //System.out.println("mensagem de erro = " + mensagem.toString());
                    //throw new PPATException(mensagem.getMessage());
                }
                //fim parte que salva na api

                TipoSolo tipoSoloaux = null;
                tipoSoloaux = gson.fromJson(json, TipoSolo.class);
                tipoSolo.setCodigo(tipoSoloaux.getCodigo());
                tipoSolo.setDataCadastro(new Date());
                //tipoSolo.setDataCadastro(tipoSoloaux.getDataCadastro());
                soloDAO.adicionar(tipoSolo);

                if (!(usu.getTipoSolos().isEmpty()) && (usu.getTipoSolos() != null)) {
                    solos.addAll(usu.getTipoSolos());
                    //System.out.println("---------------------is empty()!!!!"+usuarios.get(0).getTipoSolos().get(0).getDescricao());
                }
                solos.add(tipoSolo);
                usu.setTipoSolos(solos);
                usuarioDAO.alterarT(usu);
                //System.out.println("---------------------is empty()!!!!"+usu.getTipoSolos().size());

                result.include("success", "success.cadastro.solo").redirectTo(this).lista(usuarioSession.getUsuario().getId());
            } else {
                //PUT na API
                tipoSolo.setDescricaoEN(tipoSolo.getDescricaoPT());
                tipoSolo.setDescricaoES(tipoSolo.getDescricaoPT());
                gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();//new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                ClientResponse response = client.putResource("solo", userInfo.getUsuario().getToken(), gson.toJson(tipoSolo));
                String json = response.getEntity(String.class);

                if (response.getStatus() != 200) {
                    System.out.println("Erro put tiposolos api no persiste tiposolos");
                    //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                    //throw new PPATException(mensagem.getMessage());
                } else {
                    //fim do PUT API
                    TipoSolo aux = soloDAO.buscaSoloByCodigo(tipoSolo.getCodigo());
                    Long id = aux.getId();
                    tipoSolo.setId(id);
                    Usuario usu = usuarioSession.getUsuario();
                    usuarios.add(usu);
                    tipoSolo.setUsuarios(usuarios);
                    tipoSolo.setDescricaoEN(tipoSolo.getDescricaoPT());
                    tipoSolo.setDescricaoES(tipoSolo.getDescricaoPT());
                    tipoSolo.setDataCadastro(new Date());
                    soloDAO.alterarT(tipoSolo);
                    result.include("success", "success.editar.solo").redirectTo(this).lista(usuarioSession.getUsuario().getId());
                }
            }
        } catch (PPATException ex) {
              System.out.println("Exception tiposolos no persiste tiposolos: "+ex);
              result.redirectTo(this).lista(usuarioSession.getUsuario().getId());
        }
    }

    @Get("editar")
    @Permissao(Roles.ON)
    public void editar(Long codigo) {

        try {
            ClientResponse response = client.getResource("solo/" + codigo, userInfo.getUsuario().getToken());
            String json = response.getEntity(String.class);

            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            TipoSolo tipoSolo = null;

            switch (response.getStatus()) {
                case 200:
                    tipoSolo = gson.fromJson(json, TipoSolo.class);
                    Long cod = tipoSolo.getUsuario().getCodigo();
                    if (cod == usuarioSession.getUsuario().getCodigo()) {
                        System.out.println("\nentrei no if---------------------\n");
                    }
                    TipoSolo tipoSoloAux = soloDAO.buscaSoloByCodigo(codigo);
                    if (tipoSoloAux == null) {
                        tipoSoloAux = tipoSolo;
                        List<Usuario> usuarios = new ArrayList<Usuario>();
                        Usuario u = usuarioDAO.buscarPorId(usuarioSession.getUsuario().getId());
                        usuarios.add(u);
                        tipoSoloAux.getUsuarios().addAll(usuarios);
                        soloDAO.adicionar(tipoSoloAux);
                    }
                    result.include("tipoSolo", tipoSoloAux).redirectTo("formulario?codigo=" + tipoSoloAux.getCodigo());
                    break;
                case 403:
                    result.include("Acesso negado, somente quem cadastrou pode editar!");
                    break;
                default:
                    System.out.println("Erro get solo api no editar tipo solo");
                    //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                    //throw new PPATException(mensagem.getMessage());
            }

        } catch (PPATException ex) {
            System.out.println("Exception get solo api no editar tipo solo: "+ex);
            //result.include("mensagem", ex.getMessage());
            result.redirectTo(this).lista(userInfo.getUsuario().getId());
        }

    }

    @Get("excluir")
    @Permissao(Roles.ON)
    public void excluir(Long codigo) {
        try {

            if (codigo == null) {
                throw new PPATException("Código do tipo de solo não informado.");
            }

            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            ClientResponse response = client.deleteResource("solo/" + codigo, userInfo.getUsuario().getToken());
            String json = response.getEntity(String.class);

            if (response.getStatus() != 200) {
                 System.out.println("Erro delete solo api no excluir tipo solo");
                //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                //throw new PPATException(mensagem.getMessage());
            } else {
                TipoSolo tipoSolo = soloDAO.buscaSoloByCodigo(codigo);
                if (tipoSolo == null) {

                } else {
                    Usuario usuario = tipoSolo.getUsuarios().get(0);
                    usuario.getTipoSolos().remove(tipoSolo);
                    usuarioDAO.alterarT(usuario);
                    soloDAO.excluir(tipoSolo);
                }
            }

        } catch (PPATException ex) {
            System.out.println("Exception get solo api no editar tipo solo: "+ex);
            result.redirectTo(this).lista(usuarioSession.getUsuario().getId());
        }

        result.include("success", "success.excluir.solo").redirectTo(this).lista(usuarioSession.getUsuario().getId());
    }

}
