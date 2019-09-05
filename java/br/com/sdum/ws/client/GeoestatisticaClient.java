/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sdum.ws.client;

import br.com.sdum.exception.PPATException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import java.net.URI;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

/**
 *
 * @author Gabriela
 */
public class GeoestatisticaClient {

    public ClientResponse postResource(String path, String json) throws PPATException {

        try {

            Client clientGeoestatistica = Client.create();

            WebResource webResourceGeoestatistica = clientGeoestatistica.resource(getBaseURI() + path);

            ClientResponse responseGeoestatistica = webResourceGeoestatistica.type("application/json").accept(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, json);

            return responseGeoestatistica;

        } catch (ClientHandlerException | UniformInterfaceException e) {
            throw new PPATException(e.getMessage());
        }
    }

    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://agdatabox.md.utfpr.edu.br/api/isi/v1/").build();
        // return UriBuilder.fromUri("http://japp.md.utfpr.edu.br/agdatabox-api-geostatistics/v1/").build();
    }
}
