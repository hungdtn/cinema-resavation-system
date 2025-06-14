package org.example.cinema_reservation_system.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.cinema_reservation_system.dto.auth.ChangePasswordDto;
import org.example.cinema_reservation_system.dto.auth.LoginDto;
import org.example.cinema_reservation_system.dto.user.UserRegisterDto;
import org.example.cinema_reservation_system.dto.user.UserResponseDto;
import org.example.cinema_reservation_system.dto.user.UserUpdateDto;
import org.example.cinema_reservation_system.entity.Role;
import org.example.cinema_reservation_system.entity.UserAccount;
import org.example.cinema_reservation_system.utils.Enum;
import org.example.cinema_reservation_system.repository.RoleRepository;
import org.example.cinema_reservation_system.repository.UserAccountRepository;
import org.example.cinema_reservation_system.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserAccountRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String register(UserRegisterDto dto) {
        if (userRepo.existsByTenDangNhap(dto.getTenDangNhap())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }

        Role role = roleRepo.findById(dto.getIdVaiTro())
                .orElseThrow(() -> new RuntimeException("Vai trò không tồn tại"));

        UserAccount user = new UserAccount();
        user.setTenDangNhap(dto.getTenDangNhap());
        user.setMatKhau(passwordEncoder.encode(dto.getMatKhau()));
        user.setVaiTro(role);
        user.setTrangThai(Enum.TrangThaiUserAccount.hoat_dong);

        userRepo.save(user);
        return "Đăng ký thành công";
    }

    @Override
    public UserResponseDto getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserAccount user = userRepo.findByTenDangNhap(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        return mapToUserResponseDto(user);
    }

    @Override
    public UserResponseDto updateCurrentUser(UserUpdateDto dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserAccount user = userRepo.findByTenDangNhap(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        user.setTenDangNhap(dto.getTenDangNhap());

        if (dto.getIdVaiTro() != null) {
            Role role = roleRepo.findById(dto.getIdVaiTro())
                    .orElseThrow(() -> new RuntimeException("Vai trò không tồn tại"));
            user.setVaiTro(role);
        }

        userRepo.save(user);
        return mapToUserResponseDto(user);
    }

    @Override
    public void changePassword(ChangePasswordDto dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserAccount user = userRepo.findByTenDangNhap(username)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getMatKhau())) {
            throw new RuntimeException("Mật khẩu hiện tại không chính xác");
        }

        user.setMatKhau(passwordEncoder.encode(dto.getNewPassword()));
        userRepo.save(user);
    }

    private UserResponseDto mapToUserResponseDto(UserAccount user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setIdUserAccount(user.getIdUserAccount());
        dto.setTenDangNhap(user.getTenDangNhap());
        dto.setVaiTro(user.getVaiTro().getTenVaiTro());
        return dto;
    }

    @Override
    public UserAccount login(LoginDto dto) {
        UserAccount user = userRepo.findByTenDangNhap(dto.getTenDangNhap())
                .orElseThrow(() -> new RuntimeException("Tên đăng nhập không tồn tại"));

        if (!passwordEncoder.matches(dto.getMatKhau(), user.getMatKhau())) {
            throw new RuntimeException("Mật khẩu không đúng");
        }

        if (user.getTrangThai() != Enum.TrangThaiUserAccount.hoat_dong) {
            throw new RuntimeException("Tài khoản đang bị khóa");
        }

        return user;
    }

}
