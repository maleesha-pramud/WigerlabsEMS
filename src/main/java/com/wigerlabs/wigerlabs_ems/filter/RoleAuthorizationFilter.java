package com.wigerlabs.wigerlabs_ems.filter;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.lang.reflect.Method;

@Provider
@RoleAllowed({"*"})
public class RoleAuthorizationFilter implements ContainerRequestFilter {

    @Context
    private HttpServletRequest servletRequest;

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        HttpSession session = servletRequest.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            abortWithUnauthorized(requestContext, "Unauthorized - Please login first");
            return;
        }

        Method method = resourceInfo.getResourceMethod();
        RoleAllowed roleAllowed = method.getAnnotation(RoleAllowed.class);

        if (roleAllowed == null) {
            roleAllowed = resourceInfo.getResourceClass().getAnnotation(RoleAllowed.class);
        }

        if (roleAllowed != null) {
            String[] allowedRoles = roleAllowed.value();
            String userRole = (String) session.getAttribute("userRole");

            boolean hasRole = false;
            for (String role : allowedRoles) {
                if (role.equalsIgnoreCase(userRole)) {
                    hasRole = true;
                    break;
                }
            }

            if (!hasRole) {
                abortWithForbidden(requestContext, "Access denied - Insufficient permissions");
            }
        }
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext, String message) {
        JsonObject response = new JsonObject();
        response.addProperty("status", false);
        response.addProperty("message", message);

        requestContext.abortWith(
            Response.status(Response.Status.UNAUTHORIZED)
                .entity(response.toString())
                .build()
        );
    }

    private void abortWithForbidden(ContainerRequestContext requestContext, String message) {
        JsonObject response = new JsonObject();
        response.addProperty("status", false);
        response.addProperty("message", message);

        requestContext.abortWith(
            Response.status(Response.Status.FORBIDDEN)
                .entity(response.toString())
                .build()
        );
    }
}
