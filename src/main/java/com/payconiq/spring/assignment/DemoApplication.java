package com.payconiq.spring.assignment;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payconiq.spring.assignment.exceptions.ApiError;
import com.payconiq.spring.assignment.security.JWTAuthorizationFilter;

/**
 * Spring Boot start up class.
 * 
 * @author sravana.pullivendula@gmail.com
 *
 */
@SpringBootApplication
public class DemoApplication {

    private static final String[] AUTH_WHITELIST = {
            // -- Swagger UI v2
            "/v2/api-docs", "/v2/api-docs.*", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
            "/configuration/security", "/swagger-ui.html", "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs.yaml", "/v3/api-docs/**", "/swagger-ui/**", "/user" };

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    /**
     * Spring security class.
     * 
     * if user don't have access for the resource, reject saying FORBIDDEN 
     * 
     * @author sravana.pullivendula@gmail.com
     *
     */
    @EnableWebSecurity
    @Configuration
    class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll().anyRequest().authenticated();

            http.exceptionHandling().authenticationEntryPoint((request, response, e) -> {
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write(new ObjectMapper().writeValueAsString(
                        new ApiError(HttpStatus.FORBIDDEN, "User don't have access for this resource.")));
            });
        }
    }

}
