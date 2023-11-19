package com.example.rqchallenge.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Employee {
    private Integer id;
    @JsonAlias({"employee_name"})
    private String employeeName;
    @JsonAlias({"employee_salary"})
    private Integer employeeSalary;
    @JsonAlias({"employee_age"})
    private Integer employeeAge;
    @JsonAlias({"profile_image"})
    private String profileImage;
}
