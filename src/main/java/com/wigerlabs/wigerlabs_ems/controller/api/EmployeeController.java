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

@Path("/employee")
@RoleAllowed({"admin", "manager", "employee"})
public class EmployeeController {

    private final UserService userService = new UserService();
    private static final int EMPLOYEE_ROLE_ID = 3;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addEmployee(String jsonData) {
        UserDTO userDTO = AppUtil.GSON.fromJson(jsonData, UserDTO.class);
        userDTO.setUserRoleId(EMPLOYEE_ROLE_ID);
        String responseJson = userService.addUser(userDTO);
        return Response.ok().entity(responseJson).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEmployees() {
        String responseJson = userService.getAllUsersByRole(EMPLOYEE_ROLE_ID);
        return Response.ok().entity(responseJson).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmployeeById(@PathParam("id") int id) {
        String responseJson = userService.getUserById(id, EMPLOYEE_ROLE_ID);
        return Response.ok().entity(responseJson).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEmployee(String jsonData) {
        UserDTO userDTO = AppUtil.GSON.fromJson(jsonData, UserDTO.class);
        userDTO.setUserRoleId(EMPLOYEE_ROLE_ID);
        String responseJson = userService.updateUser(userDTO);
        return Response.ok().entity(responseJson).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteEmployee(@PathParam("id") int id) {
        String responseJson = userService.deleteUser(id, EMPLOYEE_ROLE_ID);
        return Response.ok().entity(responseJson).build();
    }

    @PUT
    @Path("/{id}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeEmployeeStatus(@PathParam("id") int id, String jsonData) {
        JsonObject jsonObject = AppUtil.GSON.fromJson(jsonData, JsonObject.class);
        String responseJson = userService.changeEmployeeStatus(id, jsonObject.get("statusId").getAsInt());
        return Response.ok().entity(responseJson).build();
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchEmployees(@QueryParam("q") String query) {
        String responseJson = userService.searchEmployees(query);
        return Response.ok().entity(responseJson).build();
    }

    @GET
    @Path("/users/status/{statusId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmployeesByStatus(@PathParam("statusId") int statusId) {
        String responseJson = userService.getUsersByRoleAndStatus(EMPLOYEE_ROLE_ID, statusId);
        return Response.ok().entity(responseJson).build();
    }
}
