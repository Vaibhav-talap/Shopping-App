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

public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {


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

        return (exchange, chain) -> {

            if (validator.isSecured.test(exchange.getRequest())) {

                // Check if the request has an Authorization header

                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {

                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing authorization header");

                }

                // Extract and validate the token

                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

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

                String path = exchange.getRequest().getURI().getPath();

                String method = exchange.getRequest().getMethod().name();  // Use getMethod().name() to get the method as a string

                // Get the required roles for the path and method

                String[] requiredRoles = validator.getRolesForPathAndMethod(path, method);

                // Validate if the user has any of the required roles

                boolean authorized = roles.stream().anyMatch(role -> List.of(requiredRoles).contains(role));

                if (!authorized) {

                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied for your role");

                }

            }

            return chain.filter(exchange);

        };

    }

}
