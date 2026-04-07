package com.mphasis.employeeservice.mapper;

import com.mphasis.employeeservice.dto.EmployeeRequestDTO;
import com.mphasis.employeeservice.dto.EmployeeResponseDTO;
import com.mphasis.employeeservice.model.Employee;

import java.time.LocalDate;

public class EmployeeMapper {

    public static EmployeeResponseDTO toDTO(Employee employee) {
        EmployeeResponseDTO employeeDTO = new EmployeeResponseDTO();

        employeeDTO.setId(employee.getId().toString());
        employeeDTO.setName(employee.getName());
        employeeDTO.setEmail(employee.getEmail());
        employeeDTO.setPhNo(employee.getPhNo());
        employeeDTO.setAddress(employee.getAddress());
        employeeDTO.setDateOfBirth(employee.getDateOfBirth().toString());
        employeeDTO.setProfilePic(employee.getProfilePic());

        employeeDTO.setRegisteredDate(employee.getRegisteredDate().toString());

        return employeeDTO;
    }

    public static Employee toModel(EmployeeRequestDTO employeeRequestDTO) {
        Employee employee = new Employee();

        employee.setName(employeeRequestDTO.getName());
        employee.setEmail(employeeRequestDTO.getEmail());
        employee.setPhNo(employeeRequestDTO.getPhNo());
        employee.setAddress(employeeRequestDTO.getAddress());
        employee.setDateOfBirth(LocalDate.parse(employeeRequestDTO.getDateOfBirth()));
        employee.setRegisteredDate(LocalDate.parse(employeeRequestDTO.getRegisteredDate()));
        employee.setPassword(employeeRequestDTO.getPassword());

        return employee;
    }

    public static Employee toModel(EmployeeRequestDTO employeeRequestDTO, String fileName) {
        Employee employee = toModel(employeeRequestDTO);
        employee.setProfilePic(fileName);
        return employee;
    }


}
