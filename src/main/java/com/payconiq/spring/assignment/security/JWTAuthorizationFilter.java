package com.payconiq.spring.assignment.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.payconiq.spring.assignment.exceptions.ApplicationExceptions;
import com.payconiq.spring.assignment.util.AppConstant;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * JWT Authorization Filter class
 * 
 * @author sravana.pulivendula@gmail.com
 *
 */
public class JWTAuthorizationFilter extends OncePerRequestFilter {
    
    private static final Logger logger = LogManager.getLogger(JWTAuthorizationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            if (checkJWTToken(request, response)) {
                Claims claims = validateToken(request);
                if (claims.get(AppConstant.AUTHORITIES) != null) {
                    setUpSpringAuthentication(claims);
                } else {
                    SecurityContextHolder.clearContext();
                }
            }else {
                SecurityContextHolder.clearContext();
            }
            chain.doFilter(request, response);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e ) { 
            logger.error(e.getMessage(), e);
           throw new ApplicationExceptions(e.getMessage(), HttpStatus.FORBIDDEN, e);
        }
    }   

    private Claims validateToken(HttpServletRequest request) {
        String jwtToken = request.getHeader(AppConstant.HEADER).replace(AppConstant.PREFIX, "");
        return Jwts.parser().setSigningKey(AppConstant.SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
    }

    /**
     * Authentication method in Spring flow
     * 
     * @param claims
     */
    private void setUpSpringAuthentication(Claims claims) {
        List<String> authorities = (List<String>) claims.get(AppConstant.AUTHORITIES);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(auth);

    }

    private boolean checkJWTToken(HttpServletRequest request, HttpServletResponse res) {
        String authenticationHeader = request.getHeader(AppConstant.HEADER);
        if (authenticationHeader == null || !authenticationHeader.startsWith(AppConstant.PREFIX))
            return false;
        return true;
    }

}
