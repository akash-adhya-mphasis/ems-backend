package com.mphasis.employeeservice.service;


import com.mphasis.employeeservice.dto.EmployeeRequestDTO;
import com.mphasis.employeeservice.dto.EmployeeResponseDTO;
import com.mphasis.employeeservice.exception.EmailAlreadyExistsException;
import com.mphasis.employeeservice.exception.EmployeeNotFoundException;
import com.mphasis.employeeservice.mapper.EmployeeMapper;
import com.mphasis.employeeservice.model.Employee;
import com.mphasis.employeeservice.repository.EmployeeRepository;
import com.mphasis.employeeservice.specification.EmployeeSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeResponseDTO> getEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream().map(EmployeeMapper::toDTO).toList();
    }

    public Page<EmployeeResponseDTO> getEmployees(Pageable pageable) {

        return employeeRepository.findAll(pageable).map(EmployeeMapper::toDTO);
    }

    public EmployeeResponseDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new EmployeeNotFoundException("Employee not found with ID: " + id)
        );

        return EmployeeMapper.toDTO(employee);
    }


    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO employeeRequestDTO) {
        if (employeeRepository.existsByEmail(employeeRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("An Employee with this email already exists " + employeeRequestDTO.getEmail());
        }

        Employee employee = employeeRepository.save(EmployeeMapper.toModel(employeeRequestDTO));

        return EmployeeMapper.toDTO(employee);
    }

    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO employeeRequestDTO) {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new EmployeeNotFoundException("Employee not found with ID: " + id)
        );

        if (employeeRepository.existsByEmailAndIdNot(employeeRequestDTO.getEmail(), id)) {
            throw new EmailAlreadyExistsException(
                    "An Email with this email " + "already exists "
                            + employeeRequestDTO.getEmail()
            );
        }

        employee.setName(employeeRequestDTO.getName());
        employee.setEmail(employeeRequestDTO.getEmail());
        employee.setPhNo(employeeRequestDTO.getPhNo());
        employee.setAddress(employeeRequestDTO.getAddress());
        employee.setDateOfBirth(LocalDate.parse(employeeRequestDTO.getDateOfBirth()));
//        employee.setProfilePic(employeeRequestDTO.getProfilePic());

        Employee updatedEmployee = employeeRepository.save(employee);

        return EmployeeMapper.toDTO(updatedEmployee);

    }

    public void deleteEmployee(Long id) {

        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new EmployeeNotFoundException("Employee not found with ID: " + id)
        );

        employeeRepository.deleteById(id);
    }


    public Page<EmployeeResponseDTO> getEmployeesWithFilters(
            String name,
            String status,
            Integer minAge,
            Integer maxAge,
            String startDate,
            String endDate,
            Pageable pageable) {

        Specification<Employee> spec = Specification
                .where(EmployeeSpecification.hasName(name))
                .and(EmployeeSpecification.hasStatus(status))
                .and(EmployeeSpecification.ageBetween(minAge, maxAge))
                .and(EmployeeSpecification.registeredAfter(
                        startDate == null || startDate.isEmpty() ? null : LocalDate.parse(startDate)
                ))
                .and(EmployeeSpecification.registeredBefore(
                        endDate == null || endDate.isEmpty() ? null : LocalDate.parse(endDate)
                ));

        Page<Employee> page = employeeRepository.findAll(spec, pageable);

        return page.map(EmployeeMapper::toDTO);
    }

}
