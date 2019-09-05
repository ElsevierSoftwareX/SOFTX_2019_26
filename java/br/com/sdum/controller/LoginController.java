package br.com.sdum.controller;

import java.io.Serializable;
import java.util.ArrayList;

import javax.inject.Inject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.jersey.api.client.ClientResponse;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.sdum.annotation.Permissao;
import br.com.sdum.dao.AmostraDAO;
import br.com.sdum.dao.AreaDAO;
import br.com.sdum.dao.AtributoDAO;
import br.com.sdum.dao.ClassificacaoDAO;
import br.com.sdum.dao.EntidadeDAO;
import br.com.sdum.dao.GradeAmostralDAO;
import br.com.sdum.dao.ProjetoDAO;
import br.com.sdum.dao.TipoSoloDAO;
import br.com.sdum.dao.UnidadeMedidaDAO;
import br.com.sdum.dao.UsuarioDAO;
import br.com.sdum.enums.Roles;
import br.com.sdum.exception.PPATException;
import br.com.sdum.model.Amostra;
import br.com.sdum.model.Area;
import br.com.sdum.model.Atributo;
import br.com.sdum.model.Classe;
import br.com.sdum.model.Classificacao;
import br.com.sdum.model.Entidade;
import br.com.sdum.model.GradeAmostral;
import br.com.sdum.model.Login;
import br.com.sdum.model.MensagemRetorno;
import br.com.sdum.model.PixelAmostra;
import br.com.sdum.model.PontoAmostral;
import br.com.sdum.model.TipoSolo;
import br.com.sdum.model.UnidadeMedida;
import br.com.sdum.model.Usuario;
import br.com.sdum.session.UsuarioSession;
import br.com.sdum.ws.client.PPATWSClient;
import com.google.gson.reflect.TypeToken;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Resource
@Path("usuario")
public class LoginController implements Serializable {

    private static final long serialVersionUID = 7697559842997254111L;

    @Inject
    private Result result;

    @Inject
    private PPATWSClient client;

    @Inject
    private UsuarioSession usuarioSession;

    @Inject
    private UsuarioDAO usuarioDAO;

    private Gson gson = new Gson();

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
    private EntidadeDAO entidadeDAO;

    @Inject
    private ProjetoController projeto;

    private Gson gsonArea1, gsonAmostra, gsonAtributo, gsonGradeAmostral, gsonUnidadeMedida, gsonPixelAmostra, gsonPontoAmostral;

    private Gson gsonSolo, gsonSolo2, gsonarea, gsonentidade, gsonclassificacao, gsonclasse;

    @Get("/usuario")
    public void index() {
        result.include("usuarios", new ArrayList());
    }

    @Path("login")
    @Permissao(Roles.OFF)
    public void formulario() {
    }

    @Post("logar")
    @Permissao(Roles.OFF)
    //	public void formLogin(String telefone, String senha) {
    public void formLogin(Login login) {

        System.out.println(login);

        //String stringUsuario = consultaService.login(telefone, senha);
        try {
            //Usuario usuario = new Gson().fromJson(stringUsuario, Usuario.class);
            System.out.println("antes do postResource");
            ClientResponse response = client.postResource("usuario/login", null, gson.toJson(login));
            System.out.println("depois do postResource");
            String json = response.getEntity(String.class);
            System.out.println("json----------------- " + json);
            if (response.getStatus() != 200) {
                System.out.println("Entrou no if " + response.getStatus());
                //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                //System.out.println("mensagem: " + mensagem);
                //throw new PPATException(mensagem.getMessage());
            }

            Usuario usuario = this.getDadosUsuario(json);

            //result.include("success", "login.success").redirectTo(MapaController.class).mapa();
            //if (usuario != null) {
            Usuario usuarioAux = usuarioDAO.verificaCadastro(usuario);

            if (usuarioAux == null) {
                usuario = usuarioDAO.alterarT(usuario);
            } else {

                usuario = usuarioAux;
                // Gambiarra
                json = json.replace("\"", "");
                usuario.setToken(json);
                usuarioDAO.alterarT(usuario);

            }

            usuarioSession.setUsuario(usuario);

            //SINCRONIZAÇÃO COM A API
            ClientResponse responseSolo = client.getResource("solo", usuario.getToken());
            String jsonSolo = responseSolo.getEntity(String.class);
            ClientResponse responseAtributo = client.getResource("atributo", usuario.getToken());
            String jsonAtributo = responseAtributo.getEntity(String.class);
            ClientResponse responseUnidadeMedida = client.getResource("unidademedida", usuario.getToken());
            String jsonUnidadeMedida = responseUnidadeMedida.getEntity(String.class);
            ClientResponse responseEntidade = client.getResource("entidade", usuario.getToken());
            String jsonEntidade = responseEntidade.getEntity(String.class);
            ClientResponse responseClassificacao = client.getResource("classificacao", usuario.getToken());
            String jsonClassificacao = responseClassificacao.getEntity(String.class);
            ClientResponse responseClasse = client.getResource("classe", usuario.getToken());
            String jsonClasse = responseClasse.getEntity(String.class);
            gsonSolo = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            gsonAtributo = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            gsonUnidadeMedida = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            gsonentidade = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            gsonclassificacao = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            gsonclasse = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

            List<TipoSolo> tipoSolo = new ArrayList<>();
            List<Atributo> atributo = new ArrayList<>();
            List<UnidadeMedida> unidadeMedida = new ArrayList<>();
            List<Entidade> entidade = new ArrayList<>();
            List<Classificacao> classificacao = new ArrayList<>();
            List<Classe> classe = new ArrayList<>();
            if (responseSolo.getStatus() == 200) {
                tipoSolo = (List<TipoSolo>) gsonSolo.fromJson(jsonSolo, new TypeToken<ArrayList<TipoSolo>>() {
                }.getType());
            }

            if (responseAtributo.getStatus() == 200) {
                atributo = (List<Atributo>) gsonAtributo.fromJson(jsonAtributo, new TypeToken<ArrayList<Atributo>>() {
                }.getType());
            }

            if (responseUnidadeMedida.getStatus() == 200) {
                unidadeMedida = (List<UnidadeMedida>) gsonUnidadeMedida.fromJson(jsonUnidadeMedida, new TypeToken<ArrayList<UnidadeMedida>>() {
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
                    um.getUsuario().setId(usuario.getId());
                    unidadeMedidaDAO.adicionar(um);
                }
            }
            for (int j = 0; j < entidade.size(); j++) {
                Entidade ent = entidadeDAO.buscaEntidadeByCodigo(entidade.get(j).getCodigo());
                if (ent == null) {
                    ent = entidade.get(j);
                    ent.getUsuario().setId(usuario.getId());
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
            }
            for (int j = 0; j < classificacao.size(); j++) {
                Classificacao classif = classificacaoDAO.buscaPorCodigo(classificacao.get(j).getCodigo());
                List<Classe> classes = new ArrayList<>();
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
            }
            for (int i = 0; i < tipoSolo.size(); i++) {
                TipoSolo tiposolomeubanco = tipoSoloDAO.buscaSoloByCodigo(tipoSolo.get(i).getCodigo());
                if (tiposolomeubanco == null) {
                    ClientResponse responseSolo2 = null;
                    try {
                        responseSolo2 = client.getResource("solo/" + tipoSolo.get(i).getCodigo(), usuario.getToken());
                        String jsonSolo2 = responseSolo2.getEntity(String.class);

                        gsonSolo2 = new GsonBuilder()
                                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                                .create();

                        tiposolomeubanco = gsonSolo2.fromJson(jsonSolo2, TipoSolo.class);
                        List<Usuario> usuarios = new ArrayList<Usuario>();
                        usuarios.add(usuarioSession.getUsuario());
                        tiposolomeubanco.getUsuarios().addAll(usuarios);
                        tiposolomeubanco.setDataCadastro(new Date());
                        tiposolomeubanco.setDescricaoPT(tipoSolo.get(i).getDescricaoPT());
                        tiposolomeubanco.setDescricaoEN(tipoSolo.get(i).getDescricaoPT());
                        tiposolomeubanco.setDescricaoES(tipoSolo.get(i).getDescricaoPT());
                        tiposolomeubanco.setCodigo(tipoSolo.get(i).getCodigo());
                        tipoSoloDAO.adicionar(tiposolomeubanco);
                    } catch (PPATException ex) {
                        Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
            //FIM SINCRONIZAÇÃO API

            //result.include("success", "login.success").redirectTo(MapaController.class).mapa();
            result.redirectTo(projeto).lista();
            //} else {
            //	if (stringUsuario != null) {
            //		List<Error> errors = new Gson().fromJson(stringUsuario, new TypeToken<List<Error>>() {
            //		}.getType());
            //		result.include("danger", errors.get(0)).redirectTo(this).formulario();
            //	} else {
            // System.out.println("cai no primeiro verifique conexão");
            //		result.include("danger", "error.verifique.conexao.internet").redirectTo(this).formulario();
            //	}
            //}
        } catch (Exception e) {
            System.out.println("EXCEÇÃO " + e);

            //if (stringUsuario != null) {
            //	try {
            //		List<Error> errors = new Gson().fromJson(stringUsuario, new TypeToken<List<Error>>() {
            //		}.getType());
            //		result.include("danger", errors.get(0)).redirectTo(this).formulario();
            //	} catch (Exception ex) {
            result.include("danger", "erro.usuario.senha").redirectTo(this).formulario();
            //	}
            //} else {
            // System.out.println("cai no segundo verifique conexão");
            //	result.include("danger", "error.verifique.conexao.internet").redirectTo(this).formulario();
            //}
        }
    }

    @Post("cadastrar")
    @Permissao(Roles.OFF)
    public void formCadastroUsuario(Usuario usuario
    ) {

        /*
		 * String stringUsuario = consultaService.cadastrarUsuario(usuario);
		 * 
		 * usuario = new Gson().fromJson(stringUsuario, Usuario.class);
		 * 
		 * if(usuario != null){ usuarioSession.setUsuario(usuario);
		 * result.include("success",
		 * "success.cadastrar.usuario").redirectTo(this).formulario(); } else{
		 * result.include("danger",
		 * "danger.cadastrar.usuario").redirectTo(this).formulario(); }
         */
        try {

            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

            ClientResponse response = client.postResource("usuario", null, gson.toJson(usuario));

            String json = response.getEntity(String.class);

            if (response.getStatus() != 201) {
                System.out.println("Erro no cadastrar api");
                //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
                //throw new PPATException(mensagem.getMessage());
                result.include("danger", "danger.cadastrar.usuario").redirectTo(this).formulario();
            } else {

                result.include("success", "success.cadastrar.usuario").redirectTo(this).formulario();
            }
        } catch (Exception ex) {
            System.out.println("Exception no cadastrar api: " + ex);
            result.include("danger","danger.cadastrar.usuario.form").include("usuario", usuario).redirectTo(this).formulario();
        }

    }

    @Get("logoff")
    @Permissao(Roles.ON)
    public void logoff() {
        usuarioSession.setUsuario(null);
        result.redirectTo(LoginController.class).formulario();
    }

    private Usuario getDadosUsuario(String token) throws PPATException {

        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

        // Gambiarra
        token = token.replace("\"", "");

        System.out.println(token);

        ClientResponse response = client.getResource("usuario", token);
        String json = response.getEntity(String.class);

        if (response.getStatus() != 200) {
            System.out.println("Erro pegar dados usuario api");
            //MensagemRetorno mensagem = gson.fromJson(json, MensagemRetorno.class);
            //throw new PPATException(mensagem.getMessage());
        }

        System.out.println(json);

        Usuario usuario = gson.fromJson(json, Usuario.class);
        usuario.setToken(token);

        return usuario;
    }
}
