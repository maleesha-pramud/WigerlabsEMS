package com.wigerlabs.wigerlabs_ems.controller.api;

import com.wigerlabs.wigerlabs_ems.dto.DepartmentDTO;
import com.wigerlabs.wigerlabs_ems.service.DepartmentService;
import com.wigerlabs.wigerlabs_ems.util.AppUtil;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/department")
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
}
