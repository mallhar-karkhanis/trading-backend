package com.bajajbroking.trading.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Bajaj Broking Trading SDK API")
                .version("1.0.0")
                .description("""
                    Production-ready Trading SDK for Bajaj Broking
                    
                    **Features:**
                    - List tradable instruments
                    - Place MARKET and LIMIT orders
                    - Track order status and execution
                    - View executed trades
                    - Monitor portfolio holdings
                    
                    **Demo User:** demo@trade.com
                    """)
                .contact(new Contact()
                    .name("Bajaj Broking Tech Team")
                    .email("tech@bajajbroking.com"))
                .license(new License()
                    .name("Proprietary")
                    .url("https://bajajbroking.com")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:8080")
                    .description("Local Development Server")
            ));
    }
}