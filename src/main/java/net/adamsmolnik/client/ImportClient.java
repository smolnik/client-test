package net.adamsmolnik.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import net.adamsmolnik.model.dataimport.ImportRequest;
import net.adamsmolnik.model.dataimport.ImportResponse;

/**
 * @author ASmolnik
 *
 */
public class ImportClient {

    public static void main(String[] args) throws Exception {
        Client client = ClientBuilder.newClient();
        Entity<ImportRequest> request = Entity.json(new ImportRequest("myfolder/adam-smolnik.png"));
        Response response = client.target("http://localhost:8080/import-service/is/import").request().post(request);
        ImportResponse responseObject = response.readEntity(ImportResponse.class);
        System.out.println(responseObject);
    }

}
