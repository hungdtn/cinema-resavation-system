package org.example.cinema_reservation_system.repository.employee;

import org.example.cinema_reservation_system.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    boolean existsByEmail(String email);
    boolean existsBySoDienThoai(String phone);
}
