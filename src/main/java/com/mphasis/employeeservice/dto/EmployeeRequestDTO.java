package com.mphasis.employeeservice.dto;


import com.mphasis.employeeservice.dto.validators.CreatePatientValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class EmployeeRequestDTO {

    @NotBlank(message = "name is required.")
    @Size(min = 3, max = 100, message = "name cannot be less than 3 characters and greater than 100 characters.")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phNo;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Date of birth is required")
    private String dateOfBirth;

    @NotBlank(groups = CreatePatientValidationGroup.class,
            message = "Registered Date is required")
    private String registeredDate;

    @NotBlank(message = "Password is required")
    @Size(min = 3, max = 18, message = "password cannot be less than 3 or greater than 18")
    private String password;

    private MultipartFile profilePic;

}
