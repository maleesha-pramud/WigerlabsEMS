package com.wigerlabs.wigerlabs_ems.service;

import com.google.gson.JsonObject;
import com.wigerlabs.wigerlabs_ems.dto.DepartmentDTO;
import com.wigerlabs.wigerlabs_ems.entity.Department;
import com.wigerlabs.wigerlabs_ems.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
            if(transaction != null) {
                transaction.rollback();
            }
            message = "Error creating department: " + e.getMessage();
            e.printStackTrace();
        }

        responseObject.addProperty("status", status);
        responseObject.addProperty("message", message);
        return responseObject.toString();
    }
}
