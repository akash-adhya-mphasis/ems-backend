package com.mphasis.employeeservice.controller;


import com.mphasis.employeeservice.dto.EmployeeRequestDTO;
import com.mphasis.employeeservice.dto.EmployeeResponseDTO;
import com.mphasis.employeeservice.dto.validators.CreatePatientValidationGroup;
import com.mphasis.employeeservice.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
@CrossOrigin
@Tag(name = "Employee", description = "Controller for managing Employee Operations")
public class EmployeeController {


    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

//    @GetMapping("/getEmployees")
//    @Operation(summary = "Get all employees with pagination")
//    public ResponseEntity<Page<EmployeeResponseDTO>> getEmployees(
//            @PageableDefault(page = 0, size = 10) Pageable pageable) {
//        Page<EmployeeResponseDTO> employees = employeeService.getEmployees(pageable);
//        return ResponseEntity.ok(employees);
//    }


    @GetMapping("/getEmployees")
    public ResponseEntity<Page<EmployeeResponseDTO>> getEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            Pageable pageable) {



        Page<EmployeeResponseDTO> employees =
                employeeService.getEmployeesWithFilters(
                        name, status, minAge, maxAge, startDate, endDate, pageable
                );

        return ResponseEntity.ok(employees);
    }



    @GetMapping("/getEmployee/{id}")
    @Operation(summary = "Get a specific employee details")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(
            @PathVariable Long id
    ) {
        EmployeeResponseDTO response =  employeeService.getEmployeeById(id);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/createEmployee")
    @Operation(summary = "Create a new Employee")
    public ResponseEntity<EmployeeResponseDTO> createEmployee(
            @Validated({Default.class, CreatePatientValidationGroup.class})
            @RequestBody EmployeeRequestDTO employeeRequestDTO) {

        EmployeeResponseDTO employeeResponseDTO = employeeService.createEmployee(employeeRequestDTO);

        return ResponseEntity.ok().body(employeeResponseDTO);
    }

    @PutMapping("/updateEmployee/{id}")
    @Operation(summary = "Update an existing employee")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable Long id, @Validated({Default.class}) @RequestBody EmployeeRequestDTO employeeRequestDTO) {
        EmployeeResponseDTO employeeResponseDTO = employeeService.updateEmployee(id, employeeRequestDTO);

        return ResponseEntity.ok().body(employeeResponseDTO);
    }

    @DeleteMapping("/deleteEmployee/{id}")
    @Operation(summary = "Delete an existing employee")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        System.out.println("it comes here");
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok().body("Deleted id: " + id);
    }
}
