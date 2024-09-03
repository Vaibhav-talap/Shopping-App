package com.psl.gateway.Filter;

import com.psl.gateway.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config>  {


    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }
    public static class Config {

    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                // Check if the request has an Authorization header
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing authorization header");
                }

                // Extract and validate the token
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {
                    jwtUtil.validateToken(authHeader);
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
                }

                // Extract roles from the token
                List<String> roles = jwtUtil.getRolesFromToken(authHeader);

                // Perform role-based authorization for specific routes
                String path = exchange.getRequest().getURI().getPath();
                if (path.startsWith("/admin") && !roles.contains("ROLE_ADMIN")) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied for non-admin users");
                }
                if (path.startsWith("/user") && !roles.contains("ROLE_USER")) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied for non-user roles");
                }
            }
            return chain.filter(exchange);
        });
    }
}