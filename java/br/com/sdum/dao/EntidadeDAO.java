/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sdum.dao;

import br.com.sdum.model.Entidade;
import br.com.sdum.model.Usuario;
import java.util.List;
import javax.inject.Named;

/**
 *
 * @author Gabriela
 */
@Named
public interface EntidadeDAO extends DAO<Entidade>{
    
    public Entidade buscaEntidadeByCodigo(Long codigo);
    
    public List<Entidade> listaEntidadeByUsuarioAll(Usuario usuario);
}
