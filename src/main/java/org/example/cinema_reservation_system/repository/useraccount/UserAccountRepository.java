package org.example.cinema_reservation_system.repository.useraccount;

import org.example.cinema_reservation_system.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {
    Optional<UserAccount> findByTenDangNhap(String tenDangNhap);
    boolean existsByTenDangNhap(String tenDangNhap);
    void deleteByTenDangNhap(String tenDangNhap);


}