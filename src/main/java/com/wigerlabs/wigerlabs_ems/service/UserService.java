package com.wigerlabs.wigerlabs_ems.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.wigerlabs.wigerlabs_ems.dto.UserDTO;
import com.wigerlabs.wigerlabs_ems.entity.*;
import com.wigerlabs.wigerlabs_ems.util.HibernateUtil;
import com.wigerlabs.wigerlabs_ems.util.SecurityUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserService {

    public String addUser(UserDTO userDTO) {
        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Check if email already exists
            User existingUser = session.createQuery("FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", userDTO.getEmail())
                    .getSingleResultOrNull();

            if (existingUser != null) {
                message = "User with this email already exists";
            } else {
                // Fetch related entities
                UserRole userRole = session.get(UserRole.class, userDTO.getUserRoleId());
                Position position = session.get(Position.class, userDTO.getPositionId());
                Department department = session.get(Department.class, userDTO.getDepartmentId());
                Status statusEntity = session.get(Status.class, userDTO.getStatusId());

                if (userRole == null) {
                    message = "Invalid user role";
                } else if (position == null) {
                    message = "Invalid position";
                } else if (department == null) {
                    message = "Invalid department";
                } else if (statusEntity == null) {
                    message = "Invalid status";
                } else {
                    User user = new User();
                    user.setName(userDTO.getName());
                    user.setEmail(userDTO.getEmail());
                    user.setPassword(SecurityUtil.hashPassword(userDTO.getPassword()));
                    user.setUserRole(userRole);
                    user.setPosition(position);
                    user.setDepartment(department);
                    user.setStatus(statusEntity);

                    session.persist(user);
                    transaction.commit();

                    status = true;
                    message = "User created successfully";
                    responseObject.addProperty("userId", user.getId());
                }
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            message = "Error creating user: " + e.getMessage();
            e.printStackTrace();
        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return responseObject.toString();
    }

    public String getAllUsersByRole(int userRoleId) {
        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";
        JsonArray usersArray = new JsonArray();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<User> users = session.createQuery(
                            "FROM User u WHERE u.userRole.id = :roleId ORDER BY u.name", User.class)
                    .setParameter("roleId", userRoleId)
                    .getResultList();

            for (User user : users) {
                JsonObject userObject = new JsonObject();
                userObject.addProperty("id", user.getId());
                userObject.addProperty("name", user.getName());
                userObject.addProperty("userRoleId", user.getUserRole().getId());
                userObject.addProperty("userRoleName", user.getUserRole().getName());
                userObject.addProperty("positionId", user.getPosition().getId());
                userObject.addProperty("positionName", user.getPosition().getName());
                userObject.addProperty("departmentId", user.getDepartment().getId());
                userObject.addProperty("departmentName", user.getDepartment().getName());
                userObject.addProperty("statusId", user.getStatus().getId());
                userObject.addProperty("statusValue", user.getStatus().getValue());
                usersArray.add(userObject);
            }

            status = true;
            message = "Users retrieved successfully";
        } catch (Exception e) {
            message = "Error retrieving users: " + e.getMessage();
            e.printStackTrace();
        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        responseObject.add("data", usersArray);
        return responseObject.toString();
    }

    public String getUserById(int id, int userRoleId) {
        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";
        JsonObject userObject = new JsonObject();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, id);

            if (user != null && user.getUserRole().getId() == userRoleId) {
                userObject.addProperty("id", user.getId());
                userObject.addProperty("name", user.getName());
                userObject.addProperty("email", user.getEmail());
                userObject.addProperty("userRoleId", user.getUserRole().getId());
                userObject.addProperty("userRoleName", user.getUserRole().getName());
                userObject.addProperty("positionId", user.getPosition().getId());
                userObject.addProperty("positionName", user.getPosition().getName());
                userObject.addProperty("departmentId", user.getDepartment().getId());
                userObject.addProperty("departmentName", user.getDepartment().getName());
                userObject.addProperty("statusId", user.getStatus().getId());
                userObject.addProperty("statusValue", user.getStatus().getValue());

                status = true;
                message = "User retrieved successfully";
            } else {
                message = "User not found or role mismatch";
            }
        } catch (Exception e) {
            message = "Error retrieving user: " + e.getMessage();
            e.printStackTrace();
        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        responseObject.add("data", userObject);
        return responseObject.toString();
    }

    public String updateUser(UserDTO userDTO) {
        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            User user = session.get(User.class, userDTO.getId());

            if (user == null) {
                message = "User not found";
            } else {
                // Fetch related entities
                UserRole userRole = session.get(UserRole.class, userDTO.getUserRoleId());
                Position position = session.get(Position.class, userDTO.getPositionId());
                Department department = session.get(Department.class, userDTO.getDepartmentId());
                Status statusEntity = session.get(Status.class, userDTO.getStatusId());

                if (userRole == null) {
                    message = "Invalid user role";
                } else if (position == null) {
                    message = "Invalid position";
                } else if (department == null) {
                    message = "Invalid department";
                } else if (statusEntity == null) {
                    message = "Invalid status";
                } else {
                    user.setName(userDTO.getName());
                    user.setUserRole(userRole);
                    user.setPosition(position);
                    user.setDepartment(department);
                    user.setStatus(statusEntity);

                    session.merge(user);
                    transaction.commit();

                    status = true;
                    message = "User updated successfully";
                    responseObject.addProperty("userId", user.getId());
                }
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            message = "Error updating user: " + e.getMessage();
            e.printStackTrace();
        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return responseObject.toString();
    }

    public String deleteUser(int id, int userRoleId) {
        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            User user = session.get(User.class, id);

            if (user == null) {
                message = "User not found";
            } else if (user.getUserRole().getId() != userRoleId) {
                message = "User role mismatch";
            } else {
                session.remove(user);
                transaction.commit();

                status = true;
                message = "User deleted successfully";
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            message = "Error deleting user: " + e.getMessage();
            e.printStackTrace();
        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return responseObject.toString();
    }
}
