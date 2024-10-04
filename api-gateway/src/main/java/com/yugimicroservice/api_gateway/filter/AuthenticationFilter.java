package com.yugimicroservice.api_gateway.filter;

import com.yugimicroservice.api_gateway.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {


    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RouteValidator validator;
    @Autowired
    private RouteValidator routeValidator;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
            if(routeValidator.isSecured.test(exchange.getRequest())) {
                if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Missing authorization header");
                }
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if(authHeader!=null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.replace("Bearer ", "");
                }
                try{
                    //restTemplate.getForObject("http://IDENTITY-SERVICE/auth/validate?token"+authHeader, String.class);
                    jwtUtil.validateToken(authHeader);

                }catch(Exception e){
                    System.out.println("invalid access....");
                    throw new RuntimeException("unauthorized access to application");
                }
            }
            return chain.filter(exchange);
        }));
    }

    public static class Config{

    }

}
