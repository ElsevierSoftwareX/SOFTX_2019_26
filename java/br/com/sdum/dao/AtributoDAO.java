package br.com.sdum.dao;

import javax.inject.Named;

import br.com.sdum.model.Atributo;
import br.com.sdum.model.Usuario;
import java.util.List;

@Named
public interface AtributoDAO extends DAO<Atributo> {

    public Atributo buscaPorCodigo(Long codigo);
    
    public Atributo buscaPorCodigoEUsuario(Long codigo, Usuario usuario);
    
    public List<Atributo> listaAtributoByUsuarioAll(Usuario usuario);
}
