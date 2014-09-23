package net.adamsmolnik.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
public class DigestNoLimitUnderHeavyLoadClient {

    public static void main(String[] args) throws Exception {
        final Client client = ClientBuilder.newClient();
        final Entity<DigestRequest> request = Entity.json(new DigestRequest("SHA-256", "largefiles/file_sizedOf10000000"));
        ExecutorService es = null;
        try {
            es = Executors.newFixedThreadPool(10);
            for (int i = 0; i < 2000; i++) {
                es.submit(() -> {
                    Response response = client
                            .target("http://student098-elb-2093092545.us-east-1.elb.amazonaws.com/digest-service-no-limit/ds/digest").request()
                            .post(request);
                    DigestResponse responseObject = response.readEntity(DigestResponse.class);
                    System.out.println(responseObject);
                });
                TimeUnit.MILLISECONDS.sleep(300);
            }
            TimeUnit.MINUTES.sleep(3);
        } finally {
            if (es != null) {
                es.shutdownNow();
            }
        }
    }

}
