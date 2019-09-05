package br.com.sdum.controller;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.sdum.annotation.Permissao;
import br.com.sdum.dao.ProjetoDAO;
import br.com.sdum.enums.Roles;
import br.com.sdum.model.Projeto;
import br.com.sdum.session.UsuarioSession;

@Resource
@Path("projeto")
public class ProjetoController implements Serializable {

    private static final long serialVersionUID = -1747435946222314539L;

    @Inject
    private ProjetoDAO projetoDAO;

    @Inject
    private UsuarioSession usuarioSession;

    @Inject
    private Result result;

    @Get("lista")
    @Permissao(Roles.ON)
    public void lista() {
        result.include("projetos", projetoDAO.listaProjetoByUsuario(usuarioSession.getUsuario()));
    }

    @Get("abrirprojeto")
    @Permissao(Roles.ON)
    public void abrirprojeto(Long idProjeto) {
        result.include("idProjeto", idProjeto).redirectTo(MapaController.class).mapa(idProjeto);
    }

    @Path("formulario")
    @Permissao(Roles.ON)
    public void formulario() {
    }

    @Post("persiste")
    @Permissao(Roles.ON)
    public void cadastrarEditar(Projeto projeto) {

        if (projeto.getId() == null) {
            projeto.setData(new Date());
            projeto.setUsuario(usuarioSession.getUsuario());

            projetoDAO.adicionar(projeto);

            result.include("success", "success.cadastro.projeto").redirectTo(this).lista();
        } else {
            Projeto projetoAux = projetoDAO.buscarPorId(projeto.getId());

            projeto.setData(projetoAux.getData());
            projeto.setUsuario(projetoAux.getUsuario());

            projetoDAO.alterar(projeto);

            result.include("success", "success.editar.projeto").redirectTo(this).lista();
        }
    }

    @Get("editar")
    @Permissao(Roles.ON)
    public void editar(Long id) {
        String editar= "editar";
        result.include("editar",editar).include("projeto", projetoDAO.buscarPorId(id)).redirectTo(this).formulario();
    }

    @Get("excluir")
    @Permissao(Roles.ON)
    public void excluir(Long id, Long idArea) {

        try {
            projetoDAO.excluir(projetoDAO.buscarPorId(id));
            result.include("success", "success.excluir.projeto").redirectTo(this).lista();
        } catch (Exception e) {
            result.include("danger", "danger.excluir.projeto").redirectTo(this).lista();
        }
    }

    @Get("filtro")
    @Permissao(Roles.ON)
    public void filtro(String filtro) {

        result.redirectTo(this).lista();

    }

    @Get("buscar")
    @Permissao(Roles.ON)
    public void buscarMaisProjetos(String filtro, int pagina) {

        if (filtro == null) {
            filtro = "";
        }

        result.use(Results.json()).from(projetoDAO.listaProjetoByUsuario(usuarioSession.getUsuario())).serialize();
    }
}
