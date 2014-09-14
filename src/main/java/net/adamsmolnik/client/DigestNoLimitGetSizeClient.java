package net.adamsmolnik.client;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * @author ASmolnik
 *
 */
public class DigestNoLimitGetSizeClient {

    public static void main(String[] args) throws Exception {
        Client client = ClientBuilder.newClient();
        Response response = client
                .target("http://digest.adamsmolnik.com/digest-service-no-limit/ds/objects/"
                        + URLEncoder.encode("largefiles/file_sizedOf100000000", StandardCharsets.UTF_8.toString())).queryParam("metadata", "size")
                .request().get();
        if (response.getStatusInfo() == Status.OK) {
            String responseSize = response.readEntity(String.class);
            System.out.println(Long.valueOf(responseSize));
        } else {
            System.out.println(response.getStatusInfo() + " " + response.readEntity(String.class));
        }
    }
}
