package com.example.rqchallenge.service;

import com.example.rqchallenge.model.ApiResponse;
import com.example.rqchallenge.model.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.internal.http2.Settings;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

//TODO: test negative cases
class DummyApiServiceTest {
    private static MockWebServer mockBackEnd;

    private DummyApiService dummyApiService;

    final static Dispatcher dispatcher = new Dispatcher() {

        private ObjectMapper objectMapper = new ObjectMapper();
        @Override
        public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
            Employee employee = Employee.builder()
                    .employeeName("Jakub")
                    .id(123)
                    .employeeSalary(123)
                    .employeeAge(123)
                    .build();
            switch (request.getPath()) {
                case "/employees":
                    try {
                        return new MockResponse()
                                .setResponseCode(200)
                                .setBody(objectMapper.writeValueAsString(new ApiResponse<>("success", List.of(employee), null)))
                                .addHeader("Content-Type", "application/json");
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                case "/employee/123":
                    try {
                        return new MockResponse()
                                .setResponseCode(200)
                                .setBody(objectMapper.writeValueAsString(new ApiResponse<>("success", employee, null)))
                                .addHeader("Content-Type", "application/json");
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                case "/create":
                    try {
                        return new MockResponse()
                                .setResponseCode(200)
                                .setBody(objectMapper.writeValueAsString(new ApiResponse<>("success", employee, null)))
                                .addHeader("Content-Type", "application/json");
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                case "/delete/123":
                    try {
                        return new MockResponse()
                                .setResponseCode(200)
                                .setBody(objectMapper.writeValueAsString(new ApiResponse<Object>("success", null, "successfully! deleted Records")))
                                .addHeader("Content-Type", "application/json");
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
            }
            return new MockResponse().setResponseCode(404);
        }
    };

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.setDispatcher(dispatcher);
        mockBackEnd.start(8080);
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s",
                mockBackEnd.getPort());
        dummyApiService = new DummyApiService(WebClient.create(baseUrl));
    }

    @Test
    void getAllEmployees() throws Exception {
        Mono<ApiResponse<List<Employee>>> apiResponseMono = dummyApiService.getAllEmployees();

        StepVerifier.create(apiResponseMono)
                .expectNextMatches(employeeApiResponse -> employeeApiResponse.getData().get(0).getEmployeeName()
                        .equals("Jakub"))
                .verifyComplete();
    }

    @Test
    void getEmployeeById() throws Exception {
        Mono<ApiResponse<Employee>> apiResponseMono = dummyApiService.getEmployeesById("123");

        StepVerifier.create(apiResponseMono)
                .expectNextMatches(employeeApiResponse -> employeeApiResponse.getData().getEmployeeName()
                        .equals("Jakub"))
                .verifyComplete();
    }

    @Test
    void createEmployee() throws Exception {
        Employee employee = Employee.builder()
                .employeeName("Jakub")
                .id(123)
                .employeeSalary(123)
                .employeeAge(123)
                .build();
        Mono<ApiResponse<Employee>> apiResponseMono = dummyApiService.createEmployee(employee);

        StepVerifier.create(apiResponseMono)
                .expectNextMatches(employeeApiResponse -> employeeApiResponse.getData().getEmployeeName()
                        .equals("Jakub"))
                .verifyComplete();
    }

    @Test
    void deleteEmployeeById() throws Exception {
        Mono<ApiResponse<Object>> apiResponseMono = dummyApiService.deleteEmployeeById("123");

        StepVerifier.create(apiResponseMono)
                .expectNextMatches(employeeApiResponse -> employeeApiResponse.getStatus()
                        .equals("success"))
                .verifyComplete();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }
}