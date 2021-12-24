package service;

import com.thoughtworks.xstream.XStream;
import model.Flat;
import org.glassfish.jersey.SslConfigurator;
import util.RequestStructure;
import util.XStreamUtil;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

public class Service {
    public RequestStructure getMostExpensive(Integer id1, Integer id2, Integer id3) {
        RequestStructure requestStructure = new RequestStructure();
        Response flat1Response = getTarget().path(id1.toString()).request().accept(MediaType.APPLICATION_XML).get();
        Response flat2Response = getTarget().path(id2.toString()).request().accept(MediaType.APPLICATION_XML).get();
        Response flat3Response = getTarget().path(id3.toString()).request().accept(MediaType.APPLICATION_XML).get();

        if (flat1Response.getStatus() == 200 && flat2Response.getStatus() == 200 && flat3Response.getStatus() == 200) {
            XStream xStream = XStreamUtil.createXStream();
            Flat flat1 = (Flat) xStream.fromXML(flat1Response.readEntity(String.class));
            Flat flat2 = (Flat) xStream.fromXML(flat2Response.readEntity(String.class));
            Flat flat3 = (Flat) xStream.fromXML(flat3Response.readEntity(String.class));

            Double flat1Price = Objects.requireNonNull(flat1).getPrice();
            Double flat2Price = Objects.requireNonNull(flat2).getPrice();
            Double flat3Price = Objects.requireNonNull(flat3).getPrice();

            if (flat1Price == null) flat1Price = 0.0;
            if (flat2Price == null) flat2Price = 0.0;
            if (flat3Price == null) flat3Price = 0.0;

            Integer num;
            if (flat1Price >= flat2Price && flat1Price >= flat3Price) {
                num = id1;
            } else if (flat2Price >= flat1Price && flat2Price >= flat3Price) {
                num = id2;
            } else {
                num = id3;
            }
            requestStructure.setMessage(num.toString());
            requestStructure.setResponseCode(200);
            return requestStructure;
        }
        requestStructure.setMessage("Flat with one of these ids doesn't exist");
        requestStructure.setResponseCode(404);
        return requestStructure;
    }

    public Response getOrderedByArea(String view, String desc) {
        WebTarget target = getTarget();
        return target.queryParam("view", view)
                .queryParam("orderBy", "area," + desc)
                .request()
                .accept(MediaType.APPLICATION_XML)
                .get();
    }

    public WebTarget getTarget() {
        String backFirst = "https://localhost:8443/soa_lab2-1.0-SNAPSHOT/";
        String api = "api/flats";
        return client().target(backFirst).path(api);
    }

    private Client client() {
        SslConfigurator sslConfigurator = SslConfigurator.newInstance()
                .keyStoreFile("C:\\Users\\serge\\IdeaProjects\\soa_lab2_2\\src\\main\\resources\\keystore\\client.keystore")
                .keyStorePassword("secret")
                .trustStoreFile("C:\\Users\\serge\\IdeaProjects\\soa_lab2_2\\src\\main\\resources\\keystore\\client.truststore")
                .trustStorePassword("secret");

        return ClientBuilder.newBuilder()
                .sslContext(sslConfigurator.createSSLContext())
                .hostnameVerifier((hostname, sslSession) -> hostname.equals("localhost"))
                .build();
    }
}
