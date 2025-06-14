package org.example.cinema_reservation_system.service;

import org.example.cinema_reservation_system.dto.auth.ChangePasswordDto;
import org.example.cinema_reservation_system.dto.auth.LoginDto;
import org.example.cinema_reservation_system.dto.user.UserRegisterDto;
import org.example.cinema_reservation_system.dto.user.UserResponseDto;
import org.example.cinema_reservation_system.dto.user.UserUpdateDto;
import org.example.cinema_reservation_system.entity.UserAccount;

public interface UserService {

    String register(UserRegisterDto dto);

    UserResponseDto getCurrentUser();

    UserResponseDto updateCurrentUser(UserUpdateDto dto);

    void changePassword(ChangePasswordDto dto);

    UserAccount login(LoginDto dto);

}
