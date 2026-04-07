package com.mphasis.employeeservice.service;


import com.mphasis.employeeservice.dto.EmployeeRequestDTO;
import com.mphasis.employeeservice.dto.EmployeeResponseDTO;
import com.mphasis.employeeservice.dto.LoginRequestDTO;


public interface AuthService {

    EmployeeResponseDTO signup(EmployeeRequestDTO request, String fileName);
    EmployeeResponseDTO login(LoginRequestDTO request);


}
