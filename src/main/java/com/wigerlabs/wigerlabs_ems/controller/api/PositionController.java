package com.wigerlabs.wigerlabs_ems.controller.api;

import com.wigerlabs.wigerlabs_ems.dto.PositionDTO;
import com.wigerlabs.wigerlabs_ems.service.PositionService;
import com.wigerlabs.wigerlabs_ems.util.AppUtil;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/position")
public class PositionController {

    private final PositionService positionService = new PositionService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPosition(String jsonData) {
        PositionDTO positionDTO = AppUtil.GSON.fromJson(jsonData, PositionDTO.class);
        String responseJson = positionService.addPosition(positionDTO);
        return Response.ok().entity(responseJson).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPositions() {
        String responseJson = positionService.getAllPositions();
        return Response.ok().entity(responseJson).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPositionById(@PathParam("id") int id) {
        String responseJson = positionService.getPositionById(id);
        return Response.ok().entity(responseJson).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePosition(String jsonData) {
        PositionDTO positionDTO = AppUtil.GSON.fromJson(jsonData, PositionDTO.class);
        String responseJson = positionService.updatePosition(positionDTO);
        return Response.ok().entity(responseJson).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePosition(@PathParam("id") int id) {
        String responseJson = positionService.deletePosition(id);
        return Response.ok().entity(responseJson).build();
    }
}
