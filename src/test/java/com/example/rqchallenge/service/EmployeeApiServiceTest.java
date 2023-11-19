package com.example.rqchallenge.service;

import com.example.rqchallenge.model.ApiResponse;
import com.example.rqchallenge.model.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

//TODO: test negative cases

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeApiServiceTest {

    @Autowired
    private EmployeeApiService employeeApiService;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private DummyApiService dummyApiService;

    private Employee employee;
    private String createJson;
    @BeforeEach
    void setUp() {
        employee = Employee.builder()
                .employeeName("Jakub")
                .id(123)
                .employeeSalary(123)
                .employeeAge(123)
                .build();
        createJson = "{\n" +
                "    \"id\":\"123\",\n" +
                "    \"employee_name\":\"Jakub\",\n" +
                "    \"employee_age\": 123,\n" +
                "    \"employee_salary\": 123\n" +
                "}";
        when(dummyApiService.getEmployeesById("123")).thenReturn(Mono.just(new ApiResponse<>("success", employee, null)));
        when(dummyApiService.createEmployee(ArgumentMatchers.eq(employee))).thenReturn(Mono.just(new ApiResponse<>("success", employee, null)));
        when(dummyApiService.deleteEmployeeById("123")).thenReturn(Mono.just(new ApiResponse<>("success", null, null)));
        when(dummyApiService.getAllEmployees()).thenReturn(Mono.just(new ApiResponse<>("success", List.of(employee), null)));
    }

    @Test
    void getAllEmployees() {
        Assertions.assertThat(employeeApiService.getAllEmployees()).contains(employee);
    }

    @Test
    void getEmployeesByNameSearch() {
        Assertions.assertThat(employeeApiService.getEmployeesByNameSearch("Jakub")).contains(employee);
    }

    @Test
    void getEmployeesById() {
        Assertions.assertThat(employeeApiService.getEmployeesById("123")).isEqualTo(employee);
    }

    @Test
    void getHighestSalaryOfEmployees() {
        Assertions.assertThat(employeeApiService.getHighestSalaryOfEmployees()).isEqualTo(123);
    }

    @Test
    void getTop10HighestEarningEmployeeNames() {
        Assertions.assertThat(employeeApiService.getTop10HighestEarningEmployeeNames()).contains("Jakub");
    }

    @Test
    void createEmployee() throws JsonProcessingException {
        Assertions.assertThat(employeeApiService.createEmployee(objectMapper.readValue(createJson, Map.class))).isEqualTo(employee);
    }

    @Test
    void deleteEmployeeById() {
        Assertions.assertThat(employeeApiService.deleteEmployeeById("123")).isEqualTo("success");
    }
}