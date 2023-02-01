package com.payconiq.spring.assignment.controller;

import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.payconiq.spring.assignment.dto.StockRequestDTO;
import com.payconiq.spring.assignment.dto.StockResponseDTO;
import com.payconiq.spring.assignment.repository.ApplicationRepository;
import com.payconiq.spring.assignment.services.ApplicationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Controller class for all application Rest APIs.
 * 
 * @author sravana.pullivendula@gmail.com
 *
 */

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class AppController {

    private static final String INTERNAL_SERVER_ISSUE = "Internal Server Issue";

    private static final String ACCESS_PERMISSION_ERROR = "User don't have access permission for this resouce";

    @Autowired
    ApplicationRepository stockRepository;

    private ApplicationService stockService;

    public AppController(ApplicationService stockService) {
        this.stockService = stockService;
    }

    private static final Logger logger = LogManager.getLogger(AppController.class);

    @ApiOperation(value = "Get a List of Stocks based on request data", response = ResponseEntity.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Return Stocks Data"),
            @ApiResponse(code = 403, message = ACCESS_PERMISSION_ERROR),
            @ApiResponse(code = 500, message = INTERNAL_SERVER_ISSUE) })
    @GetMapping("/stocks")
    public ResponseEntity<List<StockResponseDTO>> getAllStocks(
            @RequestParam(value = "pageNo", defaultValue = "${payconiq.assignment.pageNumber}", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "${payconiq.assignment.pageSize}", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "${payconiq.assignment.sortBy}", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "${payconiq.assignment.sortDirection}", required = false) String sortDir) {
        logger.debug("Input Data: \npageNo: " + pageNo + "\npageSize: " + pageSize + "\nsortBy:" + sortBy + "\nsortDir:"
                + sortDir);

        List<StockResponseDTO> stocks = stockService.getAllStock(pageNo, pageSize, sortBy, sortDir);

        if (Objects.isNull(stocks) || stocks.isEmpty()) {
            logger.debug("No stock data available.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        logger.debug("stocks List: " + stocks);
        return new ResponseEntity<>(stocks, HttpStatus.OK);
    }

    @ApiOperation(value = "Get a Stock data based on input ID", response = ResponseEntity.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Return Stock Data"),
            @ApiResponse(code = 403, message = ACCESS_PERMISSION_ERROR),
            @ApiResponse(code = 500, message = INTERNAL_SERVER_ISSUE) })
    @GetMapping("/stocks/{id}")
    public ResponseEntity<StockResponseDTO> getStockById(@PathVariable("id") long id) {
        logger.debug("Input Data: \nId: " + id);
        return stockService.getStockById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Create Stock Data", response = ResponseEntity.class)
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Successfully Stock data created"),
            @ApiResponse(code = 403, message = ACCESS_PERMISSION_ERROR),
            @ApiResponse(code = 500, message = INTERNAL_SERVER_ISSUE) })
    @RequestMapping(value = "/stocks", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public void createStock(@RequestBody StockRequestDTO stock) {
        logger.debug("Input Data: \nStock: " + stock);
        stockService.saveStock(stock);
    }

    @ApiOperation(value = "Update Stock Data on given ID", response = ResponseEntity.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Return updated Stock Data"),
            @ApiResponse(code = 403, message = ACCESS_PERMISSION_ERROR),
            @ApiResponse(code = 500, message = INTERNAL_SERVER_ISSUE) })
    @RequestMapping(value = "/stocks/{id}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<StockResponseDTO> updateStock(@PathVariable("id") long id,
            @RequestBody StockRequestDTO stock) {
        logger.debug("Input Data: \nId: " + id + "\nStock: " + stock);
        return stockService.updateStock(id, stock).map(saveStock -> {
            return new ResponseEntity<>(saveStock, HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Delete Stock Data on given ID", response = ResponseEntity.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Delete Stock Data"),
            @ApiResponse(code = 403, message = ACCESS_PERMISSION_ERROR),
            @ApiResponse(code = 500, message = INTERNAL_SERVER_ISSUE) })
    @DeleteMapping("/stocks/{id}")
    public ResponseEntity<String> deleteStock(@PathVariable("id") long id) throws Exception {
        stockService.deleteStock(id);
        return new ResponseEntity<String>("Stock data deleted successfully!", HttpStatus.OK);
    }

}
