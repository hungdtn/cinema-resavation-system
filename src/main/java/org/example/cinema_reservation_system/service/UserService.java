package org.example.cinema_reservation_system.service;

import jakarta.validation.Valid;
import org.example.cinema_reservation_system.Entity.UserAccount;
import org.example.cinema_reservation_system.dto.*;

public interface UserService {

    String register(UserRegisterDto dto);

    UserResponseDto getCurrentUser();

    UserResponseDto updateCurrentUser(UserUpdateDto dto);

    void changePassword(ChangePasswordDto dto);

    UserAccount login(LoginDto dto);

}
