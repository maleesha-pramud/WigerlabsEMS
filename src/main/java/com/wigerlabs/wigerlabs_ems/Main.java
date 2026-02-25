package com.wigerlabs.wigerlabs_ems;

import com.wigerlabs.wigerlabs_ems.config.AppConfig;
import com.wigerlabs.wigerlabs_ems.service.DataInitializationService;
import com.wigerlabs.wigerlabs_ems.util.HibernateUtil;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.servlet.ServletContainer;

import java.io.File;

public class Main {

    private static final int SERVER_PORT = 8080;
    private static final String CONTEXT_PATH = "/wigerlabs_ems";

    public static void main(String[] args) {
        try {
            Tomcat tomcat = new Tomcat();
            tomcat.setPort(SERVER_PORT);
            tomcat.getConnector();

            Context context = tomcat.addWebapp(CONTEXT_PATH, new File("src/main/webapp").getAbsolutePath());
            Tomcat.addServlet(context, "JerseyServlet", new ServletContainer(new AppConfig()));
            context.addServletMappingDecoded("/api/*", "JerseyServlet");

            HibernateUtil.getSessionFactory();

            // Initialize default data (user roles, etc.)
            DataInitializationService.initializeDefaultData();

            tomcat.start();
            System.out.println("App URL: http://localhost:" + SERVER_PORT + CONTEXT_PATH);
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            throw new RuntimeException("Tomcat Embedded Server loading failed: " + e.getMessage());
        }
    }
}
