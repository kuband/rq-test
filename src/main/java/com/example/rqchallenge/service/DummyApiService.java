package com.example.rqchallenge.service;

import com.example.rqchallenge.error.UnexpectedException;
import com.example.rqchallenge.model.ApiResponse;
import com.example.rqchallenge.model.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

//TODO: add @Retryable
@Service
@RequiredArgsConstructor
public class DummyApiService {

    private final WebClient webClient;

    public Mono<ApiResponse<List<Employee>>> getAllEmployees() {
        Mono<ApiResponse<List<Employee>>> dummyApiResponseMono =
                webClient.get().uri("/employees").exchangeToMono(clientResponse -> handleResponse(clientResponse,
                        new ParameterizedTypeReference<>() {
                        }));

        return dummyApiResponseMono;
    }

    public Mono<ApiResponse<Employee>> getEmployeesById(String id) {
        Mono<ApiResponse<Employee>> dummyApiResponseMono =
                webClient.get().uri("/employee/{id}", id).exchangeToMono(clientResponse -> handleResponse(clientResponse, new ParameterizedTypeReference<>() {
                }));

        return dummyApiResponseMono;

    }

    public Mono<ApiResponse<Employee>> createEmployee(Employee employee) {
        Mono<ApiResponse<Employee>> dummyApiResponseMono =
                webClient.post().uri("/create").body(Mono.just(employee),
                        Employee.class).exchangeToMono(clientResponse -> handleResponse(clientResponse,
                        new ParameterizedTypeReference<>() {
                        }));

        return dummyApiResponseMono;
    }

    public Mono<ApiResponse<Object>> deleteEmployeeById(String id) {
        Mono<ApiResponse<Object>> employeeMono =
                webClient.delete().uri("/delete/{id}", id).exchangeToMono(clientResponse -> handleResponse(clientResponse, new ParameterizedTypeReference<>() {
                }));

        return employeeMono;
    }

    private <T extends ApiResponse> Mono<T> handleResponse(ClientResponse response,
                                                           ParameterizedTypeReference<T> type) {
        if (response.statusCode().is2xxSuccessful()) {
            return response.bodyToMono(type);
        } else {
            throw new UnexpectedException(response.statusCode());
        }
    }

}
