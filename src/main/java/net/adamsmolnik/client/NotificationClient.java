package net.adamsmolnik.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import net.adamsmolnik.model.notification.NotificationRequest;

/**
 * @author ASmolnik
 *
 */
public class NotificationClient {

    public static void main(String[] args) throws Exception {
        Client client = ClientBuilder.newClient();
        Entity<NotificationRequest> request = Entity.json(new NotificationRequest("my first message"));
        System.out.println(Entity.json(request).toString());
        Response response = client.target("http://localhost:8080/notification-service/ns/send").request().post(request);
        System.out.println(response.getStatus());
    }

}
