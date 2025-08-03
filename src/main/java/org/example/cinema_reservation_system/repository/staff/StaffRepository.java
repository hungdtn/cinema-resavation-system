package org.example.cinema_reservation_system.repository.staff;

import org.example.cinema_reservation_system.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
}
