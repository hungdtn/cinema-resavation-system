package org.example.cinema_reservation_system.security;

import org.example.cinema_reservation_system.Entity.UserAccount;
import org.example.cinema_reservation_system.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserAccountRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount user = userRepo.findByTenDangNhap(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy: " + username));

        return new User(user.getTenDangNhap(), user.getMatKhau(), Collections.emptyList());
    }
}
