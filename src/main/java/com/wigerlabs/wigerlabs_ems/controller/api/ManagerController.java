package com.wigerlabs.wigerlabs_ems.controller.api;

import com.wigerlabs.wigerlabs_ems.dto.UserDTO;
import com.wigerlabs.wigerlabs_ems.filter.RoleAllowed;
import com.wigerlabs.wigerlabs_ems.service.UserService;
import com.wigerlabs.wigerlabs_ems.util.AppUtil;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/manager")
@RoleAllowed({"admin", "manager"})
public class ManagerController {

    private final UserService userService = new UserService();
    private static final int MANAGER_ROLE_ID = 2;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addManager(String jsonData) {
        UserDTO userDTO = AppUtil.GSON.fromJson(jsonData, UserDTO.class);
        userDTO.setUserRoleId(MANAGER_ROLE_ID);
        String responseJson = userService.addUser(userDTO);
        return Response.ok().entity(responseJson).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllManagers() {
        String responseJson = userService.getAllUsersByRole(MANAGER_ROLE_ID);
        return Response.ok().entity(responseJson).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getManagerById(@PathParam("id") int id) {
        String responseJson = userService.getUserById(id, MANAGER_ROLE_ID);
        return Response.ok().entity(responseJson).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateManager(String jsonData) {
        UserDTO userDTO = AppUtil.GSON.fromJson(jsonData, UserDTO.class);
        userDTO.setUserRoleId(MANAGER_ROLE_ID);
        String responseJson = userService.updateUser(userDTO);
        return Response.ok().entity(responseJson).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteManager(@PathParam("id") int id) {
        String responseJson = userService.deleteUser(id, MANAGER_ROLE_ID);
        return Response.ok().entity(responseJson).build();
    }
}
