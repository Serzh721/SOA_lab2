package controller;

import service.Service;
import util.RequestStructure;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/agency")
public class MainController {

    public Service service = new Service();

    private Response processResponse(RequestStructure requestStructure) {
        return Response
                .status(requestStructure.getResponseCode())
                .entity(requestStructure.getMessage())
                .build();
    }

    @GET
    @Path("/get-most-expensive/{id1}/{id2}/{id3}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getMostExpensive(@PathParam("id1") Integer id1,
                                     @PathParam("id2") Integer id2,
                                     @PathParam("id3") Integer id3) {
        return processResponse(service.getMostExpensive(id1, id2, id3));
    }

    @GET
    @Path("/get-ordered-by-area/{view}/{desc}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getOrderedByArea(@PathParam("view") String view,
                                     @PathParam("desc") String desc) {
        return service.getOrderedByArea(view, desc);
    }
}
