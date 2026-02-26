package com.wigerlabs.wigerlabs_ems.config;

import com.wigerlabs.wigerlabs_ems.filter.AuthenticationFilter;
import com.wigerlabs.wigerlabs_ems.filter.RoleAuthorizationFilter;
import org.glassfish.jersey.server.ResourceConfig;

public class AppConfig extends ResourceConfig {
    public AppConfig(){
        packages("com.wigerlabs.wigerlabs_ems.controller");
        register(org.glassfish.jersey.media.multipart.MultiPartFeature.class);
        register(AuthenticationFilter.class);
        register(RoleAuthorizationFilter.class);
    }
}
