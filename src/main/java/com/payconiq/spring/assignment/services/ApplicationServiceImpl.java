package com.payconiq.spring.assignment.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.payconiq.spring.assignment.dto.StockRequestDTO;
import com.payconiq.spring.assignment.dto.StockResponseDTO;
import com.payconiq.spring.assignment.exceptions.ApplicationExceptions;
import com.payconiq.spring.assignment.model.Stock;
import com.payconiq.spring.assignment.repository.ApplicationRepository;

/**
 * Application Service Implementation class.
 * 
 * @author sravana.pulivendula
 *
 */

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private static final Logger logger = LogManager.getLogger(ApplicationServiceImpl.class);

    private ApplicationRepository stockRepository;

    public ApplicationServiceImpl(ApplicationRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public void saveStock(StockRequestDTO stock) {

        if (Objects.isNull(stock)) {
            throw new IllegalArgumentException("Input stock data is null");
        }

        Stock stockEntity = mapStockDtoToStockEntity(stock);
        try {
            Stock createdStock = stockRepository.save(stockEntity);
            logger.debug("Stock saved successfully. Stock: " + createdStock);
        } catch (IllegalArgumentException | OptimisticLockingFailureException e) {
            logger.error("Save Stock was failed. Error was " + e.getMessage());
            throw new ApplicationExceptions(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public List<StockResponseDTO> getAllStock(int pageNo, int pageSize, String sortBy, String sortDir) {

        List<StockResponseDTO> stocksRespDtos = new ArrayList<StockResponseDTO>();

        Sort sort = getSortingOrder(sortBy, sortDir);

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Stock> posts = stockRepository.findAll(pageable);

        List<Stock> stocks = posts.getContent();

        stocks.stream().forEach(stock -> stocksRespDtos.add(mapStockToStockDto(stock)));
        return stocksRespDtos;
    }

    @Override
    public Optional<StockResponseDTO> getStockById(Long id) {

        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("Input stock data is null");
        }

        Optional<Stock> stock = stockRepository.findById(id);
        Optional<StockResponseDTO> stockDto = Optional.empty();
        if (stock.isPresent()) {
            stockDto = Optional.ofNullable(mapStockToStockDto(stock.get()));
            logger.debug("Stock data found. \nStock: " + stockDto);
        } else {
            logger.debug("Stock data not found.");
        }
        return stockDto;
    }

    @Override
    public Optional<StockResponseDTO> updateStock(long id, StockRequestDTO stockInput) {
        if (Objects.isNull(id) || Objects.isNull(stockInput)) {
            throw new IllegalArgumentException("Input stock data is null");
        }

        Optional<Stock> stockEntity = stockRepository.findById(id);

        if (!stockEntity.isPresent()) {
            return Optional.empty();
        }

        if (!stockInput.getStockName().equals(stockEntity.get().getName())) {
            throw new ApplicationExceptions("Stock Name is not allowed to modify", HttpStatus.NOT_MODIFIED);
        }

        stockEntity.get().setCurrentPrice(stockInput.getStockCurrentPrice());
        stockEntity.get().setName(stockInput.getStockName());
        stockEntity.get().setLastUpdate(new Date());

        try {
            Stock stock = stockRepository.save(stockEntity.get());
            logger.debug("Stock update was successfull.");

            StockResponseDTO stockDto = mapStockToStockDto(stock);

            logger.debug("Udpated stock data :" + stockDto);

            return Optional.of(stockDto);
        } catch (IllegalArgumentException | OptimisticLockingFailureException e) {
            logger.error("Save Stock was failed. Error was " + e.getMessage());
            throw new ApplicationExceptions(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteStock(long id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("Input stock data is null");
        }
        try {
            stockRepository.deleteById(id);
            logger.debug("Stock " + id + " was deleted successfull.");
        } catch (IllegalArgumentException | EmptyResultDataAccessException e) {
            logger.error("Delete Stock was failed. Error was " + e.getMessage());
            throw new ApplicationExceptions(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Sort getSortingOrder(String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        return sort;
    }

    private StockResponseDTO mapStockToStockDto(Stock stock) {
        StockResponseDTO dto = new StockResponseDTO();
        dto.setId(stock.getId());
        dto.setStockName(stock.getName());
        dto.setLastUpdatedTime(stock.getLastUpdate().toString());
        dto.setStockCurrentPrice(stock.getCurrentPrice());
        return dto;
    }

    private Stock mapStockDtoToStockEntity(StockRequestDTO dto) {
        Stock stock = new Stock();
        stock.setCurrentPrice(dto.getStockCurrentPrice());
        stock.setName(dto.getStockName());
        stock.setLastUpdate(new Date());
        return stock;
    }
}
