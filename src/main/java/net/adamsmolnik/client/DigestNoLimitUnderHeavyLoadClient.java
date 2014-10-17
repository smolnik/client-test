package net.adamsmolnik.client;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
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
public class DigestNoLimitUnderHeavyLoadClient implements AutoCloseable {

    public static class Progress {

        public final int submitted;

        public final int succeeded;

        public final int failed;

        public Progress(int submitted, int succeeded, int failed) {
            this.submitted = submitted;
            this.succeeded = succeeded;
            this.failed = failed;
        }

    }

    public static class Builder {

        private String host;

        private String algorithm = "SHA-256";

        private String objectKey;

        private int workersNumber = 2 * Runtime.getRuntime().availableProcessors();

        private int requestsNumber = 1000;

        private int suspensionInMs = 300;

        public Builder(String host, String objectKey) {
            this.host = host;
            this.objectKey = objectKey;
        }

        public Builder algorithm(String val) {
            algorithm = val;
            return this;
        }

        public Builder workersNumber(int val) {
            workersNumber = val;
            return this;
        }

        public Builder suspensionInMs(int val) {
            suspensionInMs = val;
            return this;
        }

        public Builder requestsNumber(int val) {
            requestsNumber = val;
            return this;
        }

        public DigestNoLimitUnderHeavyLoadClient build() {
            return new DigestNoLimitUnderHeavyLoadClient(this);
        }

    }

    private final String host;

    private final String algorithm;

    private final String objectKey;

    private final int workersNumber;

    private final int requestsNumber;

    private final int suspensionInMs;

    private final Client client = ClientBuilder.newClient();

    private final ExecutorService launchers;

    private final ExecutorService workers;

    private final AtomicBoolean stop = new AtomicBoolean();

    private DigestNoLimitUnderHeavyLoadClient(Builder builder) {
        host = builder.host;
        algorithm = builder.algorithm;
        objectKey = builder.objectKey;
        workersNumber = builder.workersNumber;
        requestsNumber = builder.requestsNumber;
        suspensionInMs = builder.suspensionInMs;
        launchers = Executors.newCachedThreadPool();
        workers = Executors.newFixedThreadPool(workersNumber);
    }

    public final void send(Optional<Consumer<Progress>> progressConsumer) {
        final Entity<DigestRequest> request = Entity.json(new DigestRequest(algorithm, objectKey));
        final AtomicInteger submittedNo = new AtomicInteger();
        final AtomicInteger succeededNo = new AtomicInteger();
        final AtomicInteger failedNo = new AtomicInteger();

        launchers.submit(() -> {
            for (int i = 0; i < requestsNumber; i++) {
                if (workers.isShutdown() || stop.get()) {
                    break;
                }
                workers.submit(() -> {
                    if (stop.get()) {
                        return;
                    }

                    try {
                        submittedNo.incrementAndGet();
                        Response response = client.target("http://" + host + "/digest-service-no-limit/ds/digest").request().post(request);
                        response.readEntity(DigestResponse.class);
                        succeededNo.incrementAndGet();
                        TimeUnit.MILLISECONDS.sleep(suspensionInMs);
                    } catch (InterruptedException iex) {
                        // deliberately ignored
                    } catch (Exception iex) {
                        failedNo.incrementAndGet();
                    } finally {
                        progressConsumer.ifPresent(consumer -> consumer.accept(new Progress(submittedNo.get(), succeededNo.get(), failedNo.get())));
                    }
                });
            }
        });
    }

    public final void send() {
        send(Optional.empty());
    }

    @Override
    public final void close() {
        stop.set(true);
        launchers.shutdownNow();
        workers.shutdownNow();
    }

    @Override
    public String toString() {
        return "DigestNoLimitUnderHeavyLoadClient [host=" + host + ", algorithm=" + algorithm + ", objectKey=" + objectKey + ", workersNumber="
                + workersNumber + ", requestsNumber=" + requestsNumber + ", suspensionInMs=" + suspensionInMs + ", stop=" + stop + "]";
    }

}
