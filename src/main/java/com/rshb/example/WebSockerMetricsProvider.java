package com.rshb.example;

import io.smallrye.metrics.MetricRegistries;
import io.smallrye.metrics.exporters.JsonExporter;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

@ServerEndpoint("/ws/metrics")
@ApplicationScoped
@Counted
public class WebSockerMetricsProvider {

    private static final Logger logger = LoggerFactory.getLogger(WebSockerMetricsProvider.class);

    @Inject
    MetricRegistries registries;

    @OnMessage
    public void onMessage(Session session, String message){
        try(ByteArrayInputStream stream = new ByteArrayInputStream(message.getBytes())){
            JsonObject query = Json.createReader(stream).readObject();
            switch (query.getString("action")){
                case  "clean":
                    registries.cleanUp();
                    JsonObjectBuilder response = Json.createObjectBuilder();
                    response.add("success",true);
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    JsonWriter writer = Json.createWriter(outStream);
                    writer.writeObject(response.build());
                    writer.close();;
                    session.getAsyncRemote().sendText(new String(outStream.toByteArray()));
                    break;
                case "metrics":
                    JsonExporter exporter = new JsonExporter();
                    session.getAsyncRemote().sendText(exporter.exportAllScopes().toString());
            }
        } catch (IOException e) {
            logger.error("can`t read query:",message);
        }

    }
}
