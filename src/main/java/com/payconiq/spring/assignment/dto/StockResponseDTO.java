package com.payconiq.spring.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class StockResponseDTO extends StockRequestDTO {

    private long id;

    private String lastUpdatedTime;
}
