package com.mphasis.employeeservice.specification;

import com.mphasis.employeeservice.model.Employee;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class EmployeeSpecification {
    public static Specification<Employee> hasName(String name) {
        return (root, query, cb) ->
                name == null || name.isBlank()
                        ? null
                        : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }


    public static Specification<Employee> hasStatus(String status) {
        return (root, query, cb) ->
                status == null || status.isBlank()
                        ? null
                        : cb.equal(root.get("status"), status);
    }

    public static Specification<Employee> registeredAfter(LocalDate start) {
        return (root, query, cb) ->
                start == null ? null :
                        cb.greaterThanOrEqualTo(root.get("registeredDate"), start);
    }

    public static Specification<Employee> registeredBefore(LocalDate end) {
        return (root, query, cb) ->
                end == null ? null :
                        cb.lessThanOrEqualTo(root.get("registeredDate"), end);
    }

    public static Specification<Employee> ageBetween(Integer minAge, Integer maxAge) {
        return (root, query, cb) -> {
            if (minAge == null && maxAge == null) return null;

            LocalDate today = LocalDate.now();

            if (minAge != null && maxAge != null) {
                return cb.between(
                        root.get("dateOfBirth"),
                        today.minusYears(maxAge),
                        today.minusYears(minAge)
                );
            }

            if (minAge != null) {
                return cb.lessThanOrEqualTo(
                        root.get("dateOfBirth"),
                        today.minusYears(minAge)
                );
            }

            return cb.greaterThanOrEqualTo(
                    root.get("dateOfBirth"),
                    today.minusYears(maxAge)
            );
        };
    }

}
