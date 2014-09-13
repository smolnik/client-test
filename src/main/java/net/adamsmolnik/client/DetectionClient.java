package net.adamsmolnik.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import net.adamsmolnik.model.detection.DetectionRequest;
import net.adamsmolnik.model.detection.DetectionResponse;

/**
 * @author ASmolnik
 *
 */
public class DetectionClient {

    public static void main(String[] args) throws Exception {
        Client client = ClientBuilder.newClient();
        Entity<DetectionRequest> request = Entity.json(new DetectionRequest("myfolder/adam-smolnik.png"));
        Response response = client.target("http://localhost:8080/detection-service/ds/detect").request().post(request);
        DetectionResponse responseObject = response.readEntity(DetectionResponse.class);
        System.out.println(responseObject);
    }

}
