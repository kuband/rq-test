package com.example.rqchallenge;

import com.example.rqchallenge.api.EmployeeControllerImpl;
import com.example.rqchallenge.service.DummyApiService;
import com.example.rqchallenge.service.EmployeeApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class RqChallengeApplicationTests {

    @Autowired
    private EmployeeControllerImpl iEmployeeController;
    @Autowired
    private EmployeeApiService employeeApiService;
    @Autowired
    private DummyApiService dummyApiService;

    @Test
    void contextLoads() {
        assertThat(iEmployeeController).isNotNull();
        assertThat(employeeApiService).isNotNull();
        assertThat(dummyApiService).isNotNull();
    }

}
