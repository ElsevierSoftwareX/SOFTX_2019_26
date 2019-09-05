package br.com.sdum.dao;

import br.com.sdum.model.Usuario;

public interface UsuarioDAO extends DAO<Usuario> {

    public Usuario verificaCadastro(Usuario usuario);

    public void refresh(Usuario usuario);

}
