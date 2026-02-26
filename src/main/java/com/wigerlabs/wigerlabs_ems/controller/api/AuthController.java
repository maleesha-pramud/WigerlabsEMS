package com.wigerlabs.wigerlabs_ems.controller.api;

import com.google.gson.JsonObject;
import com.wigerlabs.wigerlabs_ems.dto.LoginDTO;
import com.wigerlabs.wigerlabs_ems.service.AuthService;
import com.wigerlabs.wigerlabs_ems.util.AppUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
public class AuthController {

    private final AuthService authService = new AuthService();

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(String jsonData, @Context HttpServletRequest request) {
        LoginDTO loginDTO = AppUtil.GSON.fromJson(jsonData, LoginDTO.class);
        JsonObject authResult = authService.authenticateUser(loginDTO);

        if (authResult.get("success").getAsBoolean()) {
            // Create session
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", authResult.get("userId").getAsInt());
            session.setAttribute("userName", authResult.get("userName").getAsString());
            session.setAttribute("userEmail", authResult.get("userEmail").getAsString());
            session.setAttribute("userRole", authResult.get("userRole").getAsString());
            session.setAttribute("userRoleId", authResult.get("userRoleId").getAsInt());
            session.setAttribute("departmentId", authResult.get("departmentId").getAsInt());
            session.setAttribute("departmentName", authResult.get("departmentName").getAsString());
            session.setAttribute("positionId", authResult.get("positionId").getAsInt());
            session.setAttribute("positionName", authResult.get("positionName").getAsString());
            session.setMaxInactiveInterval(30 * 60); // 30 minutes

            JsonObject response = new JsonObject();
            response.addProperty("status", true);
            response.addProperty("message", authResult.get("message").getAsString());
            response.addProperty("sessionId", session.getId());
            response.addProperty("userId", authResult.get("userId").getAsInt());
            response.addProperty("userName", authResult.get("userName").getAsString());
            response.addProperty("userEmail", authResult.get("userEmail").getAsString());
            response.addProperty("userRole", authResult.get("userRole").getAsString());
            response.addProperty("departmentName", authResult.get("departmentName").getAsString());
            response.addProperty("positionName", authResult.get("positionName").getAsString());

            return Response.ok().entity(response.toString()).build();
        } else {
            JsonObject response = new JsonObject();
            response.addProperty("status", false);
            response.addProperty("message", authResult.get("message").getAsString());

            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(response.toString()).build();
        }
    }

    @POST
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        JsonObject response = new JsonObject();

        if (session != null) {
            session.invalidate();
            response.addProperty("status", true);
            response.addProperty("message", "Logged out successfully");
        } else {
            response.addProperty("status", false);
            response.addProperty("message", "No active session");
        }

        return Response.ok().entity(response.toString()).build();
    }

    @GET
    @Path("/check")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkSession(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        JsonObject response = new JsonObject();

        if (session != null && session.getAttribute("userId") != null) {
            response.addProperty("authenticated", true);
            response.addProperty("userId", (Integer) session.getAttribute("userId"));
            response.addProperty("userName", (String) session.getAttribute("userName"));
            response.addProperty("userEmail", (String) session.getAttribute("userEmail"));
            response.addProperty("userRole", (String) session.getAttribute("userRole"));
            response.addProperty("departmentName", (String) session.getAttribute("departmentName"));
            response.addProperty("positionName", (String) session.getAttribute("positionName"));
        } else {
            response.addProperty("authenticated", false);
            response.addProperty("message", "No active session");
        }

        return Response.ok().entity(response.toString()).build();
    }
}

