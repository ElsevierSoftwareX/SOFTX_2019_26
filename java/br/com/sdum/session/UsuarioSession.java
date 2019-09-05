package br.com.sdum.session;

import java.io.Serializable;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;
import br.com.sdum.model.Usuario;

@Component
@SessionScoped
public class UsuarioSession implements Serializable {

    private static final long serialVersionUID = -33020301665717223L;

    private Usuario usuario;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
