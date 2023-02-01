package com.payconiq.spring.assignment.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payconiq.spring.assignment.DemoApplication;
import com.payconiq.spring.assignment.dto.StockRequestDTO;
import com.payconiq.spring.assignment.dto.StockResponseDTO;
import com.payconiq.spring.assignment.services.ApplicationService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DemoApplication.class)
@WebAppConfiguration
public class StockControllerTests {

    public static final String TEST_DATA_ID = "/2";

    public static final String API_URI = "/api/stocks";

    private MockMvc mockMvc;
    private static String testData;
    private static StockResponseDTO stockResp;
    private static String testDataRes;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    private ApplicationService stockservice;

    static {

        StockRequestDTO stock = new StockRequestDTO("springbook", 500);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            testData = objectMapper.writeValueAsString(stock);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        stockResp = new StockResponseDTO();
        stockResp.setStockName("springbook");
        stockResp.setStockCurrentPrice(500);
        stockResp.setId(1);
        stockResp.setLastUpdatedTime("2023-01-30 05:30:12");

        try {
            testDataRes = objectMapper.writeValueAsString(stockResp);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
    }

    @BeforeEach
    protected void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    public void test_GetAllStocks_noData() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get(API_URI).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(204, status);
    }

    @Test
    public void test_GetAllStocks_withData() throws Exception {
        ArrayList<StockResponseDTO> stockRespList = new ArrayList<StockResponseDTO>();
        stockRespList.add(stockResp);
        when(stockservice.getAllStock(any(Integer.class), any(Integer.class), any(String.class), any(String.class)))
                .thenReturn(stockRespList);

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get(API_URI).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    public void test_GetAllStocks_throwException() throws Exception {
        when(stockservice.getAllStock(any(Integer.class), any(Integer.class), any(String.class), any(String.class)))
                .thenThrow(new RuntimeException());
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get(API_URI).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(500, status);
    }

    @Test
    public void test_getStockById_noData() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get(API_URI + TEST_DATA_ID).accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    public void test_getStockById_throwException() throws Exception {
        when(stockservice.getStockById(any(Long.class))).thenThrow(new RuntimeException());
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get(API_URI + TEST_DATA_ID).accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(500, status);
    }

    @Test
    public void test_getStockById_withData() throws Exception {
        when(stockservice.getStockById(any(Long.class))).thenReturn(Optional.of(stockResp));
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get(API_URI + TEST_DATA_ID).accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    public void test_createStock_success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post(API_URI).contentType(MediaType.APPLICATION_JSON_VALUE).content(testData))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();

        assertEquals(201, status);
    }

    @Test
    public void test_updateStock_NotFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(API_URI + TEST_DATA_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(testData)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    public void test_updateStock_success() throws Exception {
        when(stockservice.updateStock(any(Long.class), any(StockRequestDTO.class))).thenReturn(Optional.of(stockResp));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(API_URI + TEST_DATA_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(testData)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(testDataRes, mvcResult.getResponse().getContentAsString(), "Stock data should be same");
        assertEquals(200, status);
    }
    
    @Test
    public void test_updateStock_throwException() throws Exception {
        when(stockservice.updateStock(any(Long.class), any(StockRequestDTO.class))).thenThrow(new RuntimeException());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch(API_URI + TEST_DATA_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(testData)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(500, status);
    }

    @Test
    public void test_deleteStock_success() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(API_URI + TEST_DATA_ID)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "Stock data deleted successfully!");
    }
}
