package org.example.cinema_reservation_system.repository;

import org.example.cinema_reservation_system.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByTenVaiTro(String tenVaiTro);
}
