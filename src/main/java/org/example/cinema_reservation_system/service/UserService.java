package org.example.cinema_reservation_system.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.cinema_reservation_system.dto.auth.ChangePasswordDto;
import org.example.cinema_reservation_system.dto.auth.LoginDto;
import org.example.cinema_reservation_system.dto.auth.LoginResponse;
import org.example.cinema_reservation_system.dto.user.CustomerRegisterDto;
import org.example.cinema_reservation_system.dto.user.EmployeeRegisterDto;
import org.example.cinema_reservation_system.dto.user.UserResponseDto;
import org.example.cinema_reservation_system.dto.user.UserUpdateDto;
import org.example.cinema_reservation_system.entity.UserAccount;

import java.util.List;

public interface UserService {

    List<UserResponseDto> getAllUsers();

    String registerCustomer(CustomerRegisterDto dto);

    String registerEmployee(EmployeeRegisterDto dto);

    UserResponseDto updateCurrentUser(UserUpdateDto dto);

    void changePassword(ChangePasswordDto dto);

    UserAccount login(LoginDto dto);

    void deleteByTenDangNhap(String tenDangNhap);

//        DecodedJWT verifyToken(String token);
//
//        String generateToken(String username);
//
//        LoginResponse login(LoginDto request);
}
