
package com.payconiq.spring.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.payconiq.spring.assignment.model.Stock;

public interface ApplicationRepository extends JpaRepository<Stock, Long> {

}
