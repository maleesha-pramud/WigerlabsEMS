package com.wigerlabs.wigerlabs_ems.controller.api;

import com.google.gson.JsonObject;
import com.wigerlabs.wigerlabs_ems.dto.UserDTO;
import com.wigerlabs.wigerlabs_ems.filter.RoleAllowed;
import com.wigerlabs.wigerlabs_ems.service.UserService;
import com.wigerlabs.wigerlabs_ems.util.AppUtil;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/admin")
@RoleAllowed({"admin"})
public class AdminController {

    private final UserService userService = new UserService();
    private static final int ADMIN_ROLE_ID = 1;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAdmin(String jsonData) {
        UserDTO userDTO = AppUtil.GSON.fromJson(jsonData, UserDTO.class);
        userDTO.setUserRoleId(ADMIN_ROLE_ID);
        String responseJson = userService.addUser(userDTO);
        return Response.ok().entity(responseJson).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAdmins() {
        String responseJson = userService.getAllUsersByRole(ADMIN_ROLE_ID);
        return Response.ok().entity(responseJson).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAdminById(@PathParam("id") int id) {
        String responseJson = userService.getUserById(id, ADMIN_ROLE_ID);
        return Response.ok().entity(responseJson).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAdmin(String jsonData) {
        UserDTO userDTO = AppUtil.GSON.fromJson(jsonData, UserDTO.class);
        userDTO.setUserRoleId(ADMIN_ROLE_ID);
        String responseJson = userService.updateUser(userDTO);
        return Response.ok().entity(responseJson).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAdmin(@PathParam("id") int id) {
        String responseJson = userService.deleteUser(id, ADMIN_ROLE_ID);
        return Response.ok().entity(responseJson).build();
    }

    @PUT
    @Path("/{id}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeAdminStatus(@PathParam("id") int id, String jsonData) {
        JsonObject jsonObject = AppUtil.GSON.fromJson(jsonData, JsonObject.class);
        String responseJson = userService.changeAdminStatus(id, jsonObject.get("statusId").getAsInt());
        return Response.ok().entity(responseJson).build();
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchAdmins(@QueryParam("q") String query) {
        String responseJson = userService.searchAdmins(query);
        return Response.ok().entity(responseJson).build();
    }

    @GET
    @Path("/users/status/{statusId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAdminsByStatus(@PathParam("statusId") int statusId) {
        String responseJson = userService.getUsersByRoleAndStatus(ADMIN_ROLE_ID, statusId);
        return Response.ok().entity(responseJson).build();
    }
}
