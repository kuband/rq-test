package com.example.rqchallenge.api;

import com.example.rqchallenge.model.Employee;
import com.example.rqchallenge.service.EmployeeApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//TODO: test negative cases
@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerImplTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private EmployeeApiService employeeService;

    private Employee employee;
    private String createJson;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        employee = Employee.builder()
                .employeeName("Jakub")
                .id(123)
                .employeeSalary(123)
                .employeeAge(123)
                .build();
        createJson = "{\n" +
                "    \"employee_name\":\"Jakub\",\n" +
                "    \"employee_age\": 123,\n" +
                "    \"employee_salary\": 123\n" +
                "}";
        when(employeeService.getEmployeesById("123")).thenReturn(employee);
        when(employeeService.createEmployee(objectMapper.readValue(createJson, Map.class))).thenReturn(employee);
        when(employeeService.deleteEmployeeById("123")).thenReturn("success");
        when(employeeService.getHighestSalaryOfEmployees()).thenReturn(123);
        when(employeeService.getTop10HighestEarningEmployeeNames()).thenReturn(List.of("Jakub"));
        when(employeeService.getEmployeesByNameSearch("Jakub")).thenReturn(List.of(employee));
        when(employeeService.getAllEmployees()).thenReturn(List.of(employee));
    }

    @Test
    void getAllEmployees() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(123))
                .andExpect(jsonPath("$.[0].employeeName").value("Jakub"));
    }

    @Test
    void getEmployeesByNameSearch() throws Exception {
        mockMvc.perform(get("/search/{searchString}", "Jakub"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(123))
                .andExpect(jsonPath("$.[0].employeeName").value("Jakub"));
    }

    @Test
    void getEmployeeById() throws Exception {
        mockMvc.perform(get("/{id}", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(123))
                .andExpect(jsonPath("$.employeeName").value("Jakub"));
    }

    @Test
    void getHighestSalaryOfEmployees() throws Exception {
        mockMvc.perform(get("/highestSalary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(123));
    }

    @Test
    void getTopTenHighestEarningEmployeeNames() throws Exception {
        mockMvc.perform(get("/topTenHighestEarningEmployeeNames"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]").value("Jakub"));
    }

    @Test
    void createEmployee() throws Exception {
        mockMvc.perform(post("/")
                        .content(createJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employeeName").value("Jakub"));
    }

    @Test
    void deleteEmployeeById() throws Exception {
        mockMvc.perform(delete("/{id}", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("success"));
    }
}