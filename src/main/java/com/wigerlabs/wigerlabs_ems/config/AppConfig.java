package com.wigerlabs.wigerlabs_ems.config;

import org.glassfish.jersey.server.ResourceConfig;

public class AppConfig extends ResourceConfig {
    public AppConfig(){
        packages("com.wigerlabs.wigerlabs_ems.controller");
        register(org.glassfish.jersey.media.multipart.MultiPartFeature.class);
    }
}
