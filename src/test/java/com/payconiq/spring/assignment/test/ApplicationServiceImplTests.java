package com.payconiq.spring.assignment.test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payconiq.spring.assignment.DemoApplication;
import com.payconiq.spring.assignment.dto.StockRequestDTO;
import com.payconiq.spring.assignment.dto.StockResponseDTO;
import com.payconiq.spring.assignment.exceptions.ApplicationExceptions;
import com.payconiq.spring.assignment.model.Stock;
import com.payconiq.spring.assignment.services.ApplicationService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DemoApplication.class)
public class ApplicationServiceImplTests {

    private static StockRequestDTO stockReq;
    private static String testDataRes;

    @Autowired
    private ApplicationService service;

    static {
        stockReq = new StockRequestDTO();
        stockReq.setStockName("ICICI Bank");
        stockReq.setStockCurrentPrice(1000);
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    public void test_getAllStock_thenReturnStockResponseDTO() throws Exception {
        List<StockResponseDTO> allStock = service.getAllStock(0, 5, "id", "acs");
        assertEquals(true, allStock.size() > 0);
    }

    @Test
    public void test_getAllStock_throwException() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> {
            service.getAllStock(-1, 5, "id", "acs");
        });
    }
    
    @Test
    public void test_getStockById_thenReturnStock() throws Exception {
        Optional<StockResponseDTO> stockById = service.getStockById(100L);
        assertEquals(true, stockById.isPresent());
    }
    
    @Test
    public void test_getStockById_thenReturnNoData() throws Exception {
        Optional<StockResponseDTO> stockById = service.getStockById(1L);
        assertEquals(false, stockById.isPresent());
    }

    @Test
    public void test_getStockById_throwException() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> {
            service.getStockById(null);
        });
    }
    
    @Test
    public void test_saveStock_throwException() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> {
            service.saveStock(null);
        });
    }
    
    @Test
    public void test_updateStock_thenReturnStock() throws Exception {
        Optional<StockResponseDTO> stockById = service.updateStock(100, stockReq);
        assertEquals(true, stockById.isPresent());
    }
    
    @Test
    public void test_updateStock_thenReturnEmptyStock() throws Exception {
        Optional<StockResponseDTO> stockById = service.updateStock(1, stockReq);
        assertEquals(false, stockById.isPresent());
    }
    
    @Test
    public void test_updateStock_throwException() throws Exception {        
        assertThrows(IllegalArgumentException.class, () -> {
            service.updateStock(100, null);
        });
    }
    
    @Test
    public void test_deleteStock_throwException() throws Exception {        
        assertThrows(ApplicationExceptions.class, () -> {
            service.deleteStock(-1);
            service.deleteStock(1000000);
        });
    }
    
    @Test
    public void test_saveStock_thenReturnNothing() throws Exception {    
        assertAll(() -> {
            service.saveStock(stockReq);
        });
    }
}
