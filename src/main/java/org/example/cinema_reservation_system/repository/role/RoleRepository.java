package org.example.cinema_reservation_system.repository.role;

import org.example.cinema_reservation_system.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByTenVaiTroIgnoreCase(String tenVaiTro);

}
