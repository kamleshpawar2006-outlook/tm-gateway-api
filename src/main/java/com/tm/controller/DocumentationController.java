package com.tm.controller;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@Component
@Primary
public class DocumentationController implements SwaggerResourcesProvider {

    private final DiscoveryClient discoveryClient;

    public DocumentationController(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        System.out.println("====================================");
        System.out.println(discoveryClient.getServices());
        // Add Swagger resources dynamically based on Eureka-registered services
        discoveryClient.getServices().forEach(serviceId -> {
            System.out.println("====================================");
            System.out.println(serviceId);
            if (serviceId.equals("branch-service")) {
                resources.add(swaggerResource("branch-service", "/branch-service/v3/api-docs", "3.0"));
            } else if (serviceId.equals("admin-service")) {
                resources.add(swaggerResource("admin-service", "/admin-service/v3/api-docs", "3.0"));
            }
        });

        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}