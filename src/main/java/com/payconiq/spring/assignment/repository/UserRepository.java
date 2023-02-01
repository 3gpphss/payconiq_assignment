
package com.payconiq.spring.assignment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.payconiq.spring.assignment.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
