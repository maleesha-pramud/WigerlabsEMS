package com.wigerlabs.wigerlabs_ems.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.wigerlabs.wigerlabs_ems.dto.DepartmentDTO;
import com.wigerlabs.wigerlabs_ems.entity.Department;
import com.wigerlabs.wigerlabs_ems.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class DepartmentService {
    public String addDepartment(DepartmentDTO departmentDTO) {
        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Department existingDepartment = session.createQuery("FROM Department d WHERE d.name = :name", Department.class)
                    .setParameter("name", departmentDTO.getName())
                    .getSingleResultOrNull();

            if (existingDepartment != null) {
                message = "Department with this name already exists";
            } else {
                Department department = new Department();
                department.setName(departmentDTO.getName());

                session.persist(department);
                transaction.commit();

                status = true;
                message = "Department created successfully";
                responseObject.addProperty("departmentId", department.getId());
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            message = "Error creating department: " + e.getMessage();
            e.printStackTrace();
        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return responseObject.toString();
    }

    public String getAllDepartments() {
        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";
        JsonArray departmentsArray = new JsonArray();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Department> departments = session.createQuery("FROM Department d ORDER BY d.name", Department.class)
                    .getResultList();

            for (Department department : departments) {
                JsonObject deptObject = new JsonObject();
                deptObject.addProperty("id", department.getId());
                deptObject.addProperty("name", department.getName());
                departmentsArray.add(deptObject);
            }

            status = true;
            message = "Departments retrieved successfully";
        } catch (Exception e) {
            message = "Error retrieving departments: " + e.getMessage();
            e.printStackTrace();
        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        responseObject.add("data", departmentsArray);
        return responseObject.toString();
    }

    public String getDepartmentById(int id) {
        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Department department = session.get(Department.class, id);

            if (department != null) {
                JsonObject deptObject = new JsonObject();
                deptObject.addProperty("id", department.getId());
                deptObject.addProperty("name", department.getName());

                status = true;
                message = "Department retrieved successfully";
                responseObject.add("department", deptObject);
            } else {
                message = "Department not found";
            }
        } catch (Exception e) {
            message = "Error retrieving department: " + e.getMessage();
            e.printStackTrace();
        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return responseObject.toString();
    }

    public String updateDepartment(DepartmentDTO departmentDTO) {
        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Department department = session.get(Department.class, departmentDTO.getId());

            if (department == null) {
                message = "Department not found";
            } else {
                // Check if another department with the same name exists
                Department existingDepartment = session.createQuery("FROM Department d WHERE d.name = :name AND d.id != :id", Department.class)
                        .setParameter("name", departmentDTO.getName())
                        .setParameter("id", departmentDTO.getId())
                        .getSingleResultOrNull();

                if (existingDepartment != null) {
                    message = "Department with this name already exists";
                } else {
                    department.setName(departmentDTO.getName());
                    session.merge(department);
                    transaction.commit();

                    status = true;
                    message = "Department updated successfully";
                    responseObject.addProperty("departmentId", department.getId());
                }
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            message = "Error updating department: " + e.getMessage();
            e.printStackTrace();
        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return responseObject.toString();
    }

    public String deleteDepartment(int id) {
        JsonObject responseObject = new JsonObject();
        boolean status = false;
        String message = "";

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Department department = session.get(Department.class, id);

            if (department == null) {
                message = "Department not found";
            } else {
                session.remove(department);
                transaction.commit();

                status = true;
                message = "Department deleted successfully";
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            message = "Error deleting department: " + e.getMessage();
            e.printStackTrace();
        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return responseObject.toString();
    }

    public String searchDepartments(String query) {
        com.google.gson.JsonObject responseObject = new com.google.gson.JsonObject();
        boolean status = false;
        String message = "";
        com.google.gson.JsonArray departmentsArray = new com.google.gson.JsonArray();
        try (org.hibernate.Session session = com.wigerlabs.wigerlabs_ems.util.HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Department d";
            if (query != null && !query.trim().isEmpty()) {
                hql += " WHERE lower(d.name) LIKE :q";
            }
            var q = session.createQuery(hql, com.wigerlabs.wigerlabs_ems.entity.Department.class);
            if (query != null && !query.trim().isEmpty()) {
                q.setParameter("q", "%" + query.trim().toLowerCase() + "%");
            }
            java.util.List<com.wigerlabs.wigerlabs_ems.entity.Department> departments = q.getResultList();
            for (com.wigerlabs.wigerlabs_ems.entity.Department department : departments) {
                com.google.gson.JsonObject departmentObject = new com.google.gson.JsonObject();
                departmentObject.addProperty("id", department.getId());
                departmentObject.addProperty("name", department.getName());
                departmentsArray.add(departmentObject);
            }
            status = true;
            message = "Search successful";
        } catch (Exception e) {
            message = "Error searching departments: " + e.getMessage();
        }
        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        responseObject.add("data", departmentsArray);
        return responseObject.toString();
    }
}
