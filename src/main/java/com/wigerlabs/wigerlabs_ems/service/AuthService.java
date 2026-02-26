package com.wigerlabs.wigerlabs_ems.service;

import com.google.gson.JsonObject;
import com.wigerlabs.wigerlabs_ems.dto.LoginDTO;
import com.wigerlabs.wigerlabs_ems.entity.User;
import com.wigerlabs.wigerlabs_ems.util.HibernateUtil;
import com.wigerlabs.wigerlabs_ems.util.SecurityUtil;
import org.hibernate.Session;

public class AuthService {

    public JsonObject authenticateUser(LoginDTO loginDTO) {
        JsonObject response = new JsonObject();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.createQuery(
                "FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", loginDTO.getEmail())
                .getSingleResultOrNull();

            if (user != null && SecurityUtil.checkPassword(loginDTO.getPassword(), user.getPassword())) {
                // Check if user is active
                if (user.getStatus().getValue().equalsIgnoreCase("active")) {
                    response.addProperty("success", true);
                    response.addProperty("message", "Login successful");
                    response.addProperty("userId", user.getId());
                    response.addProperty("userName", user.getName());
                    response.addProperty("userEmail", user.getEmail());
                    response.addProperty("userRole", user.getUserRole().getName());
                    response.addProperty("userRoleId", user.getUserRole().getId());
                    response.addProperty("departmentId", user.getDepartment().getId());
                    response.addProperty("departmentName", user.getDepartment().getName());
                    response.addProperty("positionId", user.getPosition().getId());
                    response.addProperty("positionName", user.getPosition().getName());
                } else {
                    response.addProperty("success", false);
                    response.addProperty("message", "User account is inactive");
                }
            } else {
                response.addProperty("success", false);
                response.addProperty("message", "Invalid email or password");
            }
        } catch (Exception e) {
            response.addProperty("success", false);
            response.addProperty("message", "Authentication error: " + e.getMessage());
            e.printStackTrace();
        }

        return response;
    }
}

