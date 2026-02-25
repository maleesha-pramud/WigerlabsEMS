package com.wigerlabs.wigerlabs_ems.service;


import com.wigerlabs.wigerlabs_ems.entity.Department;
import com.wigerlabs.wigerlabs_ems.entity.Position;
import com.wigerlabs.wigerlabs_ems.entity.Status;
import com.wigerlabs.wigerlabs_ems.entity.UserRole;
import com.wigerlabs.wigerlabs_ems.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public class DataInitializationService {

    public static void initializeDefaultData() {
        initializeUserRoles();
        initializeStatuses();
        initializeDepartments();
        initializePositions();
    }

    private static void initializeUserRoles() {
        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // Check if user roles already exist
            List<UserRole> existingRoles = session.createQuery("FROM UserRole", UserRole.class).list();

            if (existingRoles.isEmpty()) {
                // Create default user roles
                session.createNativeMutationQuery("INSERT INTO user_role (id, name) VALUES (:id, :name)")
                        .setParameter("id", 1)
                        .setParameter("name", "admin")
                        .executeUpdate();

                session.createNativeMutationQuery("INSERT INTO user_role (id, name) VALUES (:id, :name)")
                        .setParameter("id", 2)
                        .setParameter("name", "manager")
                        .executeUpdate();

                transaction.commit();
                System.out.println("Default user role values initialized successfully.");
            } else {
                System.out.println("User role values already exist. Skipping initialization.");
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error initializing user role values: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private static void initializeStatuses() {
        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // Check if statuses already exist
            List<Status> existingStatuses = session.createQuery("FROM Status", Status.class).list();

            if (existingStatuses.isEmpty()) {
                // Create default statuses
                session.createNativeMutationQuery("INSERT INTO status (id, value) VALUES (:id, :value)")
                        .setParameter("id", 1)
                        .setParameter("value", "active")
                        .executeUpdate();

                session.createNativeMutationQuery("INSERT INTO status (id, value) VALUES (:id, :value)")
                        .setParameter("id", 2)
                        .setParameter("value", "inactive")
                        .executeUpdate();

                transaction.commit();
                System.out.println("Default status values initialized successfully.");
            } else {
                System.out.println("Status values already exist. Skipping initialization.");
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error initializing status values: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private static void initializeDepartments() {
        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // Check if departments already exist
            List<Department> existingDepartments = session.createQuery("FROM Department", Department.class).list();

            if (existingDepartments.isEmpty()) {
                // Create default departments
                session.createNativeMutationQuery("INSERT INTO department (id, name, created_at, updated_at) VALUES (:id, :name, :created_at, :updated_at)")
                        .setParameter("id", 1)
                        .setParameter("name", "Engineering")
                        .setParameter("created_at", LocalDateTime.now())
                        .setParameter("updated_at", LocalDateTime.now())
                        .executeUpdate();

                session.createNativeMutationQuery("INSERT INTO department (id, name, created_at, updated_at) VALUES (:id, :name, :created_at, :updated_at)")
                        .setParameter("id", 2)
                        .setParameter("name", "Designing")
                        .setParameter("created_at", LocalDateTime.now())
                        .setParameter("updated_at", LocalDateTime.now())
                        .executeUpdate();

                session.createNativeMutationQuery("INSERT INTO department (id, name, created_at, updated_at) VALUES (:id, :name, :created_at, :updated_at)")
                        .setParameter("id", 3)
                        .setParameter("name", "Sales")
                        .setParameter("created_at", LocalDateTime.now())
                        .setParameter("updated_at", LocalDateTime.now())
                        .executeUpdate();

                transaction.commit();
                System.out.println("Default department values initialized successfully.");
            } else {
                System.out.println("Department values already exist. Skipping initialization.");
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error initializing department values: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private static void initializePositions() {
        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // Check if positions already exist
            List<Position> existingPositions = session.createQuery("FROM Position", Position.class).list();

            if (existingPositions.isEmpty()) {
                // Create default positions
                session.createNativeMutationQuery("INSERT INTO position (id, name, created_at, updated_at) VALUES (:id, :name, :created_at, :updated_at)")
                        .setParameter("id", 1)
                        .setParameter("name", "CEO")
                        .setParameter("created_at", LocalDateTime.now())
                        .setParameter("updated_at", LocalDateTime.now())
                        .executeUpdate();

                session.createNativeMutationQuery("INSERT INTO position (id, name, created_at, updated_at) VALUES (:id, :name, :created_at, :updated_at)")
                        .setParameter("id", 2)
                        .setParameter("name", "Intern Developer")
                        .setParameter("created_at", LocalDateTime.now())
                        .setParameter("updated_at", LocalDateTime.now())
                        .executeUpdate();

                session.createNativeMutationQuery("INSERT INTO position (id, name, created_at, updated_at) VALUES (:id, :name, :created_at, :updated_at)")
                        .setParameter("id", 3)
                        .setParameter("name", "Junior Software Engineer")
                        .setParameter("created_at", LocalDateTime.now())
                        .setParameter("updated_at", LocalDateTime.now())
                        .executeUpdate();

                session.createNativeMutationQuery("INSERT INTO position (id, name, created_at, updated_at) VALUES (:id, :name, :created_at, :updated_at)")
                        .setParameter("id", 4)
                        .setParameter("name", "Software Engineer")
                        .setParameter("created_at", LocalDateTime.now())
                        .setParameter("updated_at", LocalDateTime.now())
                        .executeUpdate();

                session.createNativeMutationQuery("INSERT INTO position (id, name, created_at, updated_at) VALUES (:id, :name, :created_at, :updated_at)")
                        .setParameter("id", 5)
                        .setParameter("name", "Full-Stack Software engineer")
                        .setParameter("created_at", LocalDateTime.now())
                        .setParameter("updated_at", LocalDateTime.now())
                        .executeUpdate();

                session.createNativeMutationQuery("INSERT INTO position (id, name, created_at, updated_at) VALUES (:id, :name, :created_at, :updated_at)")
                        .setParameter("id", 6)
                        .setParameter("name", "QA Engineer")
                        .setParameter("created_at", LocalDateTime.now())
                        .setParameter("updated_at", LocalDateTime.now())
                        .executeUpdate();

                session.createNativeMutationQuery("INSERT INTO position (id, name, created_at, updated_at) VALUES (:id, :name, :created_at, :updated_at)")
                        .setParameter("id", 7)
                        .setParameter("name", "DevOps Engineer")
                        .setParameter("created_at", LocalDateTime.now())
                        .setParameter("updated_at", LocalDateTime.now())
                        .executeUpdate();

                session.createNativeMutationQuery("INSERT INTO position (id, name, created_at, updated_at) VALUES (:id, :name, :created_at, :updated_at)")
                        .setParameter("id", 8)
                        .setParameter("name", "Project Manager")
                        .setParameter("created_at", LocalDateTime.now())
                        .setParameter("updated_at", LocalDateTime.now())
                        .executeUpdate();

                session.createNativeMutationQuery("INSERT INTO position (id, name, created_at, updated_at) VALUES (:id, :name, :created_at, :updated_at)")
                        .setParameter("id", 9)
                        .setParameter("name", "Social Media Manager")
                        .setParameter("created_at", LocalDateTime.now())
                        .setParameter("updated_at", LocalDateTime.now())
                        .executeUpdate();

                transaction.commit();
                System.out.println("Default position values initialized successfully.");
            } else {
                System.out.println("Position values already exist. Skipping initialization.");
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error initializing position values: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}