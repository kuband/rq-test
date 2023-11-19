package com.example.rqchallenge.service;

import com.example.rqchallenge.error.UnexpectedException;
import com.example.rqchallenge.model.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeApiService {

    private final DummyApiService dummyApiService;

    public List<Employee> getAllEmployees() {
        return dummyApiService.getAllEmployees().block().getData();
    }

    public List<Employee> getEmployeesByNameSearch(String searchString) {
        if (!StringUtils.hasText(searchString)) {
            throw new UnexpectedException(HttpStatus.BAD_REQUEST);
        } else {
            return getAllEmployees()
                    .stream().filter(employee -> employee.getEmployeeName().toLowerCase().equals(searchString.toLowerCase()))
                    .collect(Collectors.toList());
        }
    }

    public Employee getEmployeesById(String id) {
        return dummyApiService.getEmployeesById(id).block().getData();
    }

    public Integer getHighestSalaryOfEmployees() {
        List<Employee> allEmployees = getAllEmployees();
        return allEmployees.stream()
                .mapToInt(Employee::getEmployeeSalary)
                .max()
                .orElseThrow(() -> new UnexpectedException(HttpStatus.NOT_FOUND));
    }

    public List<String> getTop10HighestEarningEmployeeNames() {
        List<Employee> allEmployees = getAllEmployees();
        return Optional.of(allEmployees.stream()
                .sorted((e1, e2) -> Double.compare(e2.getEmployeeSalary(), e1.getEmployeeSalary()))
                .limit(10)
                .map(Employee::getEmployeeName)
                .collect(Collectors.toList()))
                .orElseThrow(() -> new UnexpectedException(HttpStatus.NOT_FOUND));
    }

    public Employee createEmployee(Map<String, Object> employeeInput) {
        Employee employee;
        try {
            employee = new ObjectMapper().convertValue(employeeInput, Employee.class);
        } catch (IllegalArgumentException e) {
            throw new UnexpectedException(HttpStatus.BAD_REQUEST);
        }
        if (employee != null) {
            return dummyApiService.createEmployee(employee).block().getData();
        } else {
            throw new UnexpectedException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String deleteEmployeeById(String id) {
        return dummyApiService.deleteEmployeeById(id).block().getStatus();
    }

}
