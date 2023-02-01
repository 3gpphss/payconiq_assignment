package com.payconiq.spring.assignment.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Tutorial is model class for Books, it will be helpful for mapping DB and
 * entity object.
 * 
 * @author sravana.pulivendula@gmail.com
 *
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

@Entity
@Table(name = "stocks")
public class Stock {

    @Id
    private long id;

    @Column(name = "name", updatable = false)
    private String name;

    @Column(name = "currentPrice")
    private double currentPrice;
    
    @JsonFormat(pattern = "yyyy-MM-ddTHH:mm:ss.SSSz")    
    @Column(name = "lastUpdate")
    private Date lastUpdate;
    
    @PrePersist
    private void onCreate() {
        lastUpdate = new Date();
    }
}