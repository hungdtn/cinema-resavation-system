package org.example.cinema_reservation_system.repository.voucher;

import org.example.cinema_reservation_system.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
}
