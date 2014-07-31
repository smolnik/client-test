package net.adamsmolnik.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import net.adamsmolnik.model.digest.DigestRequest;
import net.adamsmolnik.model.digest.DigestResponse;

/**
 * @author ASmolnik
 *
 */
public class DigestClient {

    public static void main(String[] args) throws Exception {
        Client client = ClientBuilder.newClient();
        Entity<DigestRequest> request = Entity.json(new DigestRequest("SHA-256", "internal/adam-smolnik.png"));
        System.out.println(Entity.json(request).toString());
        Response response = client.target("http://localhost:8080/digest-service/ds/digest").request().post(request);
        DigestResponse responseObject = response.readEntity(DigestResponse.class);
        System.out.println(responseObject);
    }

}
