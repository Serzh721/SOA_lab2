package controller;

import service.FlatService;
import util.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Path("/flats")
public class MainController {
    private final FlatService flatService;

    public MainController() {
        this.flatService = new FlatService();
    }

    private Response processResponse(RequestStructure requestStructure) {
        return Response
                .status(requestStructure.getResponseCode())
                .entity(requestStructure.getMessage())
                .build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_XML)
    public Response getFlatList(@Context UriInfo uriParams) {
        RequestStructure requestStructure = new RequestStructure();
        requestStructure.setParams(new HashMap<>());
        MultivaluedMap<String, String> mpAllQueParams = uriParams.getQueryParameters();
        for (String s : mpAllQueParams.keySet()) {
            List<String> values = mpAllQueParams.get(s);
            String[] newValues = new String[values.size()];
            for (int i = 0; i < values.size(); i++) {
                newValues[i] = values.get(i);
            }
            requestStructure.getParams().put(s, newValues);
        }
        flatService.getFlatList(requestStructure);
        return processResponse(requestStructure);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Response getFlat(@PathParam("id") Integer id) {
        RequestStructure requestStructure = new RequestStructure();
        requestStructure.setId(id);
        flatService.getFlat(requestStructure);
        return processResponse(requestStructure);
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Response modifyFlat(@PathParam("id") Integer id, String body) {
        RequestStructure requestStructure = new RequestStructure();
        requestStructure.setId(id);
        requestStructure.setRequestBody(body);
        flatService.modifyFlat(requestStructure);
        return processResponse(requestStructure);
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_XML)
    public Response addFlat(String body) {
        RequestStructure requestStructure = new RequestStructure();
        requestStructure.setRequestBody(body);
        flatService.addFlat(requestStructure);
        return processResponse(requestStructure);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Response deleteFlat(@PathParam("id") Integer id) {
        RequestStructure requestStructure = new RequestStructure();
        requestStructure.setId(id);
        flatService.deleteFlat(requestStructure);
        return processResponse(requestStructure);
    }

//////////////////////////////////////////////

    @GET
    @Path("/pricelower")
    @Produces(MediaType.APPLICATION_XML)
    public Response getPriceLower(@QueryParam(value = "price") String str) {
        RequestStructure requestStructure = new RequestStructure();
        if (Objects.isNull(str)) {
            requestStructure.setResponseCode(400);
            requestStructure.setMessage("Missed required param 'price'");
        } else {
            Long num = JPAUtil.getNumberPriceLower(str);
            requestStructure.setMessage(Objects.isNull(num) ? null : num.toString());
            requestStructure.setResponseCode(200);
        }
        return processResponse(requestStructure);
    }

    @GET
    @Path("/namescontain")
    @Produces(MediaType.APPLICATION_XML)
    public Response getNamesContain(@QueryParam(value = "string") String str) {
        RequestStructure requestStructure = new RequestStructure();
        if (Objects.isNull(str)) {
            requestStructure.setResponseCode(400);
            requestStructure.setMessage("Missed required param 'string'");
        } else {
            requestStructure.setMessage(XStreamUtil.toXML(new FlatListWrap(JPAUtil.getNamesContain(str), 0)));
            requestStructure.setResponseCode(200);
        }
        return processResponse(requestStructure);
    }

    @GET
    @Path("/namesstart")
    @Produces(MediaType.APPLICATION_XML)
    public Response getNamesStart(@QueryParam(value = "string") String str) {
        RequestStructure requestStructure = new RequestStructure();
        if (Objects.isNull(str)) {
            requestStructure.setResponseCode(400);
            requestStructure.setMessage("Missed required param 'string'");
        } else {
            requestStructure.setMessage(XStreamUtil.toXML(new FlatListWrap(JPAUtil.getNamesStart(str), 0)));
            requestStructure.setResponseCode(200);
        }
        return processResponse(requestStructure);
    }
}
