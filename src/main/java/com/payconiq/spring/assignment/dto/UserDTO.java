package com.payconiq.spring.assignment.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class UserDTO {
    
    private String userName;
    
    @JsonInclude(Include.NON_NULL)
    private String password;
    
    @JsonInclude(Include.NON_NULL)
    private String token;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSz")
    private Date timestamp;
}
