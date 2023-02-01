package com.payconiq.spring.assignment.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.payconiq.spring.assignment.dto.UserDTO;
import com.payconiq.spring.assignment.exceptions.ApplicationExceptions;
import com.payconiq.spring.assignment.model.User;
import com.payconiq.spring.assignment.repository.UserRepository;
import com.payconiq.spring.assignment.util.AppConstant;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * User Controller class having user login API.
 * 
 * @author sravana.pulivendula@gmail.com
 *
 */
@RestController
public class UserController {

    private UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    private static final Logger logger = LogManager.getLogger(UserController.class);

    /**
     * User login API method
     * 
     * @param username
     * @param pwd
     * @return user token for success case otherwise authentication error.
     */
    @ApiOperation(value = "Login user into application", response = ResponseEntity.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "User token will be send"),
            @ApiResponse(code = 401, message = "User is unauthorized"),
            @ApiResponse(code = 500, message = "Internal Server Issue") })
    @PostMapping("user")
    public ResponseEntity<UserDTO> login(@RequestParam("user") String username, @RequestParam("password") String pwd) {

        User userObj = repository.findAll().get(0);

        logger.debug("userObj.getUsername(): " + userObj.getUserName());
        UserDTO user = new UserDTO();
        user.setUserName(username);
        user.setTimestamp(new Date());

        if (username == null || !username.equals(userObj.getUserName()) || pwd == null
                || !pwd.equals(userObj.getPassword())) {
            throw new ApplicationExceptions("User is unauthorized", HttpStatus.UNAUTHORIZED);
        }

        String token = getJWTToken(username);
        user.setToken(token);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    private String getJWTToken(String username) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts.builder().setId("softtekJWT").setSubject(username)
                .claim(AppConstant.AUTHORITIES,
                        grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512, AppConstant.SECRET.getBytes()).compact();

        return AppConstant.PREFIX + token;
    }
}
