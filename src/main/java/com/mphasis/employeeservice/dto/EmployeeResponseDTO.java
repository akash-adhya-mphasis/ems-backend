package com.mphasis.employeeservice.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmployeeResponseDTO {

    private String id;
    private String name;
    private String email;
    private String phNo;
    private String address;
    private String dateOfBirth;
    private String registeredDate;
    private String profilePic;

}
