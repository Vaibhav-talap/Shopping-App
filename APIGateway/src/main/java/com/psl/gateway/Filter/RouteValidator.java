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
            "/eureka",
            "/Users/roles"
    );
    private static final Map<String, Map<String, String[]>> roleBasedRoutes = Map.of(
            "/Products", Map.of(
                    "GET", new String[]{"ROLE_USER", "ROLE_ADMIN"},  // GET access
                    "POST", new String[]{"ROLE_ADMIN"}               // POST access
            ),
            "/Products/{productId}", Map.of(
                    "GET", new String[]{"ROLE_USER", "ROLE_ADMIN"},  // GET access
                    "PUT", new String[]{"ROLE_ADMIN"},               // PUT access
                    "DELETE", new String[]{"ROLE_ADMIN"}             // DELETE access
            ),
            "/Orders", Map.of(
//                            "GET", new String[]{"ROLE_USER", "ROLE_ADMIN"},  // GET access (add one more end point to get all orders of that specific user.)
                            "POST", new String[]{"ROLE_USER", "ROLE_ADMIN"}               // POST access
                    ),
                    "/Orders/{orderId}", Map.of(
                            "GET", new String[]{"ROLE_USER", "ROLE_ADMIN"},  // GET access
                            "PUT", new String[]{"ROLE_ADMIN", "ROLE_USER"},               // PUT access
                            "DELETE", new String[]{"ROLE_ADMIN", "ROLE_USER"}             // DELETE access
                    ),
            "/Orders/{orderId}/Product/{productId}", Map.of(
                    "DELETE", new String[]{"ROLE_ADMIN", "ROLE_USER"}             // DELETE access
            ),
            "/Users/register/admin", Map.of(
                    "POST", new String[]{"ROLE_ADMIN"}               // POST access
            )

    );
    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
    public String[] getRolesForPathAndMethod(String path, String method) {
        return roleBasedRoutes.entrySet()
                .stream()
                .filter(entry -> path.matches(entry.getKey().replace("{productId}", "\\d+")))
                .map(entry -> entry.getValue().get(method))
                .findFirst()
                .orElse(new String[]{});
    }
}