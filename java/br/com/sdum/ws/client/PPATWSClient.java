package br.com.sdum.ws.client;

import br.com.sdum.exception.*;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;

public class PPATWSClient {

	public ClientResponse postResource(String path, String token, String json) throws PPATException {

        try {

            Client client = Client.create();

            WebResource webResource = client.resource(getBaseURI() + path);

            ClientResponse response = webResource.type("application/json").header("Authorization", token).post(ClientResponse.class, json);

            return response;

        } catch (ClientHandlerException | UniformInterfaceException e) {
            throw new PPATException(e.getMessage());
        }
    }
	
	public ClientResponse putResource(String path, String token, String json) throws PPATException {

        try {

            Client client = Client.create();

            WebResource webResource = client.resource(getBaseURI() + path);

            ClientResponse response = webResource.type("application/json").header("Authorization", token).put(ClientResponse.class, json);

            return response;

        } catch (ClientHandlerException | UniformInterfaceException e) {
            throw new PPATException(e.getMessage());
        }
    }
	
	public ClientResponse deleteResource(String path, String token) throws PPATException {

        try {

            Client client = Client.create();

            WebResource webResource = client.resource(getBaseURI() + path);

            ClientResponse response = webResource.type("application/json").header("Authorization", token).delete(ClientResponse.class);

            return response;

        } catch (ClientHandlerException | UniformInterfaceException e) {
            throw new PPATException(e.getMessage());
        }
    }
	
	 public ClientResponse getResource(String path, String token) throws PPATException {

	        try {

	            Client client = Client.create();

	            WebResource webResource = client.resource(getBaseURI() + path);

	            return webResource.type("application/json").header("Authorization", token).get(ClientResponse.class);

	        } catch (ClientHandlerException | UniformInterfaceException e) {
	            throw new PPATException(e.getMessage());
	        }
	  }
	
	 private static URI getBaseURI() {
	        return UriBuilder.fromUri("http://www.ppat.com.br:8080/api/").build();
	 }
}
