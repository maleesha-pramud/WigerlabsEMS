package com.wigerlabs.wigerlabs_ems.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.wigerlabs.wigerlabs_ems.dto.PositionDTO;
import com.wigerlabs.wigerlabs_ems.entity.Position;
import com.wigerlabs.wigerlabs_ems.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PositionService {
    public String addPosition(PositionDTO positionDTO) {
        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Position existingPosition = session.createQuery("FROM Position p WHERE p.name = :name", Position.class)
                    .setParameter("name", positionDTO.getName())
                    .getSingleResultOrNull();

            if (existingPosition != null) {
                message = "Position with this name already exists";
            } else {
                Position position = new Position();
                position.setName(positionDTO.getName());

                session.persist(position);
                transaction.commit();

                status = true;
                message = "Position created successfully";
                responseObject.addProperty("positionId", position.getId());
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            message = "Error creating position: " + e.getMessage();
            e.printStackTrace();
        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return responseObject.toString();
    }

    public String getAllPositions() {
        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";
        JsonArray positionsArray = new JsonArray();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Position> positions = session.createQuery("FROM Position p ORDER BY p.name", Position.class)
                    .getResultList();

            for (Position position : positions) {
                JsonObject posObject = new JsonObject();
                posObject.addProperty("id", position.getId());
                posObject.addProperty("name", position.getName());
                positionsArray.add(posObject);
            }

            status = true;
            message = "Positions retrieved successfully";
        } catch (Exception e) {
            message = "Error retrieving positions: " + e.getMessage();
            e.printStackTrace();
        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        responseObject.add("data", positionsArray);
        return responseObject.toString();
    }

    public String getPositionById(int id) {
        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Position position = session.get(Position.class, id);

            if (position != null) {
                JsonObject posObject = new JsonObject();
                posObject.addProperty("id", position.getId());
                posObject.addProperty("name", position.getName());

                status = true;
                message = "Position retrieved successfully";
                responseObject.add("position", posObject);
            } else {
                message = "Position not found";
            }
        } catch (Exception e) {
            message = "Error retrieving position: " + e.getMessage();
            e.printStackTrace();
        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return responseObject.toString();
    }

    public String updatePosition(PositionDTO positionDTO) {
        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Position position = session.get(Position.class, positionDTO.getId());

            if (position == null) {
                message = "Position not found";
            } else {
                // Check if another position with the same name exists
                Position existingPosition = session.createQuery("FROM Position p WHERE p.name = :name AND p.id != :id", Position.class)
                        .setParameter("name", positionDTO.getName())
                        .setParameter("id", positionDTO.getId())
                        .getSingleResultOrNull();

                if (existingPosition != null) {
                    message = "Position with this name already exists";
                } else {
                    position.setName(positionDTO.getName());
                    session.merge(position);
                    transaction.commit();

                    status = true;
                    message = "Position updated successfully";
                    responseObject.addProperty("positionId", position.getId());
                }
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            message = "Error updating position: " + e.getMessage();
            e.printStackTrace();
        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return responseObject.toString();
    }

    public String deletePosition(int id) {
        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Position position = session.get(Position.class, id);

            if (position == null) {
                message = "Position not found";
            } else {
                session.remove(position);
                transaction.commit();

                status = true;
                message = "Position deleted successfully";
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            message = "Error deleting position: " + e.getMessage();
            e.printStackTrace();
        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return responseObject.toString();
    }

    public String searchPositions(String query) {
        com.google.gson.JsonObject responseObject = new com.google.gson.JsonObject();
        boolean status = false;
        String message = "";
        com.google.gson.JsonArray positionsArray = new com.google.gson.JsonArray();
        try (org.hibernate.Session session = com.wigerlabs.wigerlabs_ems.util.HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Position p";
            if (query != null && !query.trim().isEmpty()) {
                hql += " WHERE lower(p.name) LIKE :q";
            }
            var q = session.createQuery(hql, com.wigerlabs.wigerlabs_ems.entity.Position.class);
            if (query != null && !query.trim().isEmpty()) {
                q.setParameter("q", "%" + query.trim().toLowerCase() + "%");
            }
            java.util.List<com.wigerlabs.wigerlabs_ems.entity.Position> positions = q.getResultList();
            for (com.wigerlabs.wigerlabs_ems.entity.Position position : positions) {
                com.google.gson.JsonObject positionObject = new com.google.gson.JsonObject();
                positionObject.addProperty("id", position.getId());
                positionObject.addProperty("name", position.getName());
                positionsArray.add(positionObject);
            }
            status = true;
            message = "Search successful";
        } catch (Exception e) {
            message = "Error searching positions: " + e.getMessage();
        }
        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        responseObject.add("data", positionsArray);
        return responseObject.toString();
    }
}
