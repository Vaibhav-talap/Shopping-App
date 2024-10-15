package com.psl.gateway.Filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Component
public class RouteValidator {
    public static final List<String> openApiEndpoints = List.of(
            "/Users/register",
            "/Users/token",
            "/eureka",
            "/Users/roles"
    );
    private static final Map<String, Map<String, String[]>> roleBasedRoutes = Map.of(
            "/Products", Map.of(
                    "GET", new String[]{"ROLE_USER", "ROLE_ADMIN"},  // GET access
                    "POST", new String[]{"ROLE_ADMIN"}               // POST access
            ),
            "/Products/\\d+", Map.of(  // Path for specific productId
                    "GET", new String[]{"ROLE_USER", "ROLE_ADMIN"},  // GET access
                    "PUT", new String[]{"ROLE_ADMIN"},               // PUT access
                    "DELETE", new String[]{"ROLE_ADMIN"}             // DELETE access
            ),
            "/Orders", Map.of(
                    "GET", new String[]{"ROLE_USER", "ROLE_ADMIN"},  // GET access
                    "POST", new String[]{"ROLE_USER", "ROLE_ADMIN"},  // POST access
                    "DELETE", new String[]{"ROLE_ADMIN", "ROLE_USER"}
            ),
            "/Orders/\\d+", Map.of(  // Path for specific orderId
                    "GET", new String[]{"ROLE_USER", "ROLE_ADMIN"},  // GET access
                    "PUT", new String[]{"ROLE_ADMIN", "ROLE_USER"},  // PUT access
                    "DELETE", new String[]{"ROLE_ADMIN", "ROLE_USER"} // DELETE access
            ),
            "/Users/register/admin", Map.of(
                    "POST", new String[]{"ROLE_ADMIN"}  // POST access
            ),
            "/Users/\\d+", Map.of(  // Path for specific orderId
                    "GET", new String[]{"ROLE_USER", "ROLE_ADMIN"}
            )
    );
    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
    public String[] getRolesForPathAndMethod(String path, String method) {
        return roleBasedRoutes.entrySet()
                .stream()
                .filter(entry -> Pattern.matches(entry.getKey(), path))  // Use regex to match paths
                .map(entry -> entry.getValue().get(method))
                .findFirst()
                .orElse(new String[]{});
    }
}