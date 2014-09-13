package net.adamsmolnik.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import net.adamsmolnik.model.extraction.ExtractionRequest;
import net.adamsmolnik.model.extraction.ExtractionResponse;

/**
 * @author ASmolnik
 *
 */
public class ExtractionClient {

    public static void main(String[] args) throws Exception {
        Client client = ClientBuilder.newClient();
        Entity<ExtractionRequest> request = Entity.json(new ExtractionRequest("myfolder/awsugpl.zip", "zip"));
        Response response = client.target("http://localhost:8080/extraction-service/es/extract").request().post(request);
        ExtractionResponse responseObject = response.readEntity(ExtractionResponse.class);
        System.out.println(responseObject);
    }

}
