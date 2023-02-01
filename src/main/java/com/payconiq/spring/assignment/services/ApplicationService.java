package com.payconiq.spring.assignment.services;

import java.util.List;
import java.util.Optional;

import com.payconiq.spring.assignment.dto.StockRequestDTO;
import com.payconiq.spring.assignment.dto.StockResponseDTO;

/**
 * Application Service Interface
 * 
 * @author sravana.pulivendula
 *
 */
public interface ApplicationService {
    /**
     * Create stock.
     * 
     * @param stock StockRequestDTO object contains stock name and stock price.
     */
    void saveStock(StockRequestDTO stock);

    /**
     * Get All Stocks for given page.
     * 
     * @param pageNo   page number
     * @param pageSize each page how many stock entries
     * @param sortBy   sort by name or Id
     * @param sortDir  Sorting order
     * @return success case stock objects List otherwise return empty entry.
     */
    List<StockResponseDTO> getAllStock(int pageNo, int pageSize, String sortBy, String sortDir);

    /**
     * Get stock based on given Id.
     * 
     * @param id id value
     * @return if stock found return stock otherwise return not found message
     */
    Optional<StockResponseDTO> getStockById(Long id);

    /**
     * Update stock based on given Id
     * 
     * @param id
     * @param Stock modified stock object
     * @return success case send updated stock.
     */
    Optional<StockResponseDTO> updateStock(long id, StockRequestDTO Stock);

    /**
     * Delete stock based on given Id.
     * 
     * @param id id value
     * @return if stock found delete stock
     */
    void deleteStock(long id);
}
