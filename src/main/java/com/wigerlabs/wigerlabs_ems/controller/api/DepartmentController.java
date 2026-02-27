package com.wigerlabs.wigerlabs_ems.controller.api;

import com.wigerlabs.wigerlabs_ems.dto.DepartmentDTO;
import com.wigerlabs.wigerlabs_ems.filter.RoleAllowed;
import com.wigerlabs.wigerlabs_ems.filter.Secured;
import com.wigerlabs.wigerlabs_ems.service.DepartmentService;
import com.wigerlabs.wigerlabs_ems.util.AppUtil;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/department")
@RoleAllowed({"admin"})
public class DepartmentController {

    private final DepartmentService departmentService = new DepartmentService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDepartment(String jsonData) {
        DepartmentDTO departmentDTO = AppUtil.GSON.fromJson(jsonData, DepartmentDTO.class);
        String responseJson = departmentService.addDepartment(departmentDTO);
        return Response.ok().entity(responseJson).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDepartments() {
        String responseJson = departmentService.getAllDepartments();
        return Response.ok().entity(responseJson).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDepartmentById(@PathParam("id") int id) {
        String responseJson = departmentService.getDepartmentById(id);
        return Response.ok().entity(responseJson).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDepartment(String jsonData) {
        DepartmentDTO departmentDTO = AppUtil.GSON.fromJson(jsonData, DepartmentDTO.class);
        String responseJson = departmentService.updateDepartment(departmentDTO);
        return Response.ok().entity(responseJson).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDepartment(@PathParam("id") int id) {
        String responseJson = departmentService.deleteDepartment(id);
        return Response.ok().entity(responseJson).build();
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchDepartments(@QueryParam("q") String query) {
        String responseJson = departmentService.searchDepartments(query);
        return Response.ok().entity(responseJson).build();
    }
}
