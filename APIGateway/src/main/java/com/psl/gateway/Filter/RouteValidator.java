package com.psl.gateway.Filter;


import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/Users/register",
            "/Users/token",
            "/eureka"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}
//
//    // Define role-based secured endpoints
//    private static final Map<String, List<String>> roleBasedEndpoints = new HashMap<>();
//
//static {
//        // Admin-specific routes
//        roleBasedEndpoints.put("/admin", List.of("ADMIN"));
//
//        // User-specific routes
//        roleBasedEndpoints.put("/user", List.of("USER"));
//
//        // Routes that can be accessed by both admin and user
//        roleBasedEndpoints.put("/common", List.of("ADMIN", "USER"));
//        }
//
//// Predicate to check if a request needs to be secured (authentication required)
//public Predicate<ServerHttpRequest> isSecured =
//        request -> openApiEndpoints
//        .stream()
//        .noneMatch(uri -> request.getURI().getPath().contains(uri));
//
//// Method to determine required roles for a given route
//public Optional<List<String>> getRequiredRoles(String path) {
//        return roleBasedEndpoints.entrySet()
//        .stream()
//        .filter(entry -> path.startsWith(entry.getKey()))
//        .map(Map.Entry::getValue)
//        .findFirst();
//        }
//        }
