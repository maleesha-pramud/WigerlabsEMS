package com.wigerlabs.wigerlabs_ems.filter;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
@Secured
public class AuthenticationFilter implements ContainerRequestFilter {

    @Context
    private HttpServletRequest servletRequest;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        HttpSession session = servletRequest.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            JsonObject response = new JsonObject();
            response.addProperty("status", false);
            response.addProperty("message", "Unauthorized - Please login first");

            requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                    .entity(response.toString())
                    .build()
            );
        }
    }
}

