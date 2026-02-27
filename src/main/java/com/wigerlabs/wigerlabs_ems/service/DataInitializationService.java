package com.wigerlabs.wigerlabs_ems.service;


import com.wigerlabs.wigerlabs_ems.entity.Department;
import com.wigerlabs.wigerlabs_ems.entity.Position;
import com.wigerlabs.wigerlabs_ems.entity.Status;
import com.wigerlabs.wigerlabs_ems.entity.User;
import com.wigerlabs.wigerlabs_ems.entity.UserRole;
import com.wigerlabs.wigerlabs_ems.util.HibernateUtil;
import com.wigerlabs.wigerlabs_ems.util.SecurityUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

public class DataInitializationService {

    public static void initializeDefaultData() {
        initializeUserRoles();
        initializeStatuses();
        initializeDepartments();
        initializePositions();
        initializeUsers();
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
                session.createNativeMutationQuery("INSERT INTO user_role (id, name) VALUES (:id, :name)")
                        .setParameter("id", 3)
                        .setParameter("name", "employee")
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

                session.createNativeMutationQuery("INSERT INTO department (id, name, created_at, updated_at) VALUES (:id, :name, :created_at, :updated_at)")
                        .setParameter("id", 4)
                        .setParameter("name", "HR")
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

                session.createNativeMutationQuery("INSERT INTO position (id, name, created_at, updated_at) VALUES (:id, :name, :created_at, :updated_at)")
                        .setParameter("id", 10)
                        .setParameter("name", "HR Manager")
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

    private static void initializeUsers() {
        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // Check if users already exist
            List<User> existingUsers = session.createQuery("FROM User", User.class).list();

            if (existingUsers.isEmpty()) {
                // Fetch required entities
                UserRole adminRole = session.get(UserRole.class, 1); // admin role
                UserRole managerRole = session.get(UserRole.class, 2); // manager role
                Position ceoPosition = session.get(Position.class, 1); // CEO position
                Position hrManagerPosition = session.get(Position.class, 10); // HR Manager position
                Department engineeringDept = session.get(Department.class, 1); // Engineering department
                Department hrDept = session.get(Department.class, 4); // HR department
                Status activeStatus = session.get(Status.class, 1); // active status

                // Create admin user
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@gmail.com");
                admin.setPassword(SecurityUtil.hashPassword("admin"));
                admin.setUserRole(adminRole);
                admin.setPosition(ceoPosition);
                admin.setDepartment(engineeringDept);
                admin.setStatus(activeStatus);
                admin.setHireDate(LocalDateTime.now());
                admin.setSalary(new java.math.BigDecimal("10000.00"));
                session.persist(admin);

                // Create HR manager user
                User hrManager = new User();
                hrManager.setName("HR Manager");
                hrManager.setEmail("hrmanager@gmail.com");
                hrManager.setPassword(SecurityUtil.hashPassword("hrmanager"));
                hrManager.setUserRole(managerRole);
                hrManager.setPosition(hrManagerPosition);
                hrManager.setDepartment(hrDept);
                hrManager.setStatus(activeStatus);
                hrManager.setHireDate(LocalDateTime.now());
                hrManager.setSalary(new java.math.BigDecimal("8000.00"));
                session.persist(hrManager);

                transaction.commit();
                System.out.println("Default user values initialized successfully.");
            } else {
                System.out.println("User values already exist. Skipping initialization.");
            }

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error initializing user values: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}