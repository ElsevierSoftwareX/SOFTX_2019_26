package br.com.sdum.service;

import java.io.Serializable;
import java.util.HashMap;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.Restfulie;
import br.com.caelum.vraptor.ioc.Component;
import br.com.sdum.model.Usuario;

@Component
public class ConsultaService implements Serializable {

    private static final long serialVersionUID = -4370057408004728355L;

    public String login(String telefone, String senha) {

        //String jsonString = "{'email': '"+ email +"', 'password': '"+ senha +"'}";
        String jsonString = "{\"telefone\": \"" + telefone + "\", \"password\": \"" + senha + "\"}";
        System.out.println(jsonString);
        //String url = "http://sdum.md.utfpr.edu.br/api/login";
        String url = "http://www.ppat.com.br:8080/api/usuario/login";

        try {
            Response response = Restfulie.at(url).accept("application/json").as("application/json").post(jsonString);
            return response.getContent();
        } catch (Exception e) {
            return null;
        }
    }

    public String cadastrarUsuario(Usuario usuario) {

        String jsonString = "{'nome': '" + usuario.getNome() + "','telefone': '" + usuario.getTelefone() + "','email': '" + usuario.getEmail() + "', 'password': '" + usuario.getSenha() + "'}";
        String url = "http://sdum.md.utfpr.edu.br/api/usuario";

        try {
            Response response = Restfulie.at(url).accept("application/json").as("application/json").post(jsonString);
            return response.getContent();
        } catch (Exception e) {
            return null;
        }
    }

    public String buscarAreaUsuario(final Usuario usuario) {

        String url = "http://sdum.md.utfpr.edu.br/api/area/usuario/" + usuario.getCodigo();

        try {
            @SuppressWarnings("serial")
            Response response = Restfulie.at(url).addHeaders(
                    new HashMap<String, String>() {
                {
                    put("Authorization", usuario.getToken());
                }
            }).accept("application/json").as("application/json").get();
            return response.getContent();
        } catch (Exception e) {
            return null;
        }
    }
}
