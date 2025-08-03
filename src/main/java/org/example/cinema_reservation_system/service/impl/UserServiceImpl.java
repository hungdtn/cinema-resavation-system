package org.example.cinema_reservation_system.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cinema_reservation_system.common.Constants;
import org.example.cinema_reservation_system.dto.auth.ChangePasswordDto;
import org.example.cinema_reservation_system.dto.auth.LoginDto;
import org.example.cinema_reservation_system.dto.auth.LoginResponse;
import org.example.cinema_reservation_system.dto.user.CustomerRegisterDto;
import org.example.cinema_reservation_system.dto.user.EmployeeRegisterDto;
import org.example.cinema_reservation_system.dto.user.UserResponseDto;
import org.example.cinema_reservation_system.dto.user.UserUpdateDto;
import org.example.cinema_reservation_system.entity.Customer;
import org.example.cinema_reservation_system.entity.Role;
import org.example.cinema_reservation_system.entity.Staff;
import org.example.cinema_reservation_system.entity.UserAccount;
import org.example.cinema_reservation_system.exception.ResourceNotFoundException;
import org.example.cinema_reservation_system.repository.customer.CustomerRepository;
import org.example.cinema_reservation_system.repository.role.RoleRepository;
import org.example.cinema_reservation_system.repository.staff.StaffRepository;
import org.example.cinema_reservation_system.repository.useraccount.UserAccountRepository;
import org.example.cinema_reservation_system.security.JwtService;
import org.example.cinema_reservation_system.service.UserService;
import org.example.cinema_reservation_system.utils.enums.TrangThaiKhachHang;
import org.example.cinema_reservation_system.utils.enums.TrangThaiUserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserAccountRepository userRepo;
    private final RoleRepository roleRepo;
    private final CustomerRepository customerRepo;
    private final StaffRepository staffRepo;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(this::mapToUserResponseDto)
                .toList();
    }

    public String registerCustomer(CustomerRegisterDto dto) {
        if (userRepo.existsByTenDangNhap(dto.getTenDangNhap())) {
            log.warn("Tên đăng nhập đã tồn tại: {}", dto.getTenDangNhap());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tên đăng nhập đã tồn tại!");
        }

        Role role = roleRepo.findByTenVaiTroIgnoreCase("Khách hàng")
                .orElseThrow(() -> new RuntimeException("Vai trò Khách hàng chưa tồn tại"));

        Customer customer = new Customer();
        customer.setTenKhachHang(dto.getTenKhachHang() != null ? dto.getTenKhachHang() : dto.getTenDangNhap());
        customer.setEmail(dto.getEmail());
        customer.setSoDienThoaiKhachHang(dto.getSoDienThoai());
        customer.setTrangThai(TrangThaiKhachHang.HOAT_DONG);
        customerRepo.save(customer);

        UserAccount user = new UserAccount();
        user.setTenDangNhap(dto.getTenDangNhap());
        user.setMatKhau(dto.getMatKhau());
        user.setVaiTro(role);
        user.setTrangThai(TrangThaiUserAccount.HOAT_DONG);
        user.setKhachHang(customer);

        userRepo.save(user);
        return "Đăng ký khách hàng thành công";
    }


    @Override
    @Transactional
    public String registerEmployee(EmployeeRegisterDto dto) {
        if (userRepo.existsByTenDangNhap(dto.getTenDangNhap())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }

        // Tìm vai trò
        Role role = roleRepo.findById(dto.getIdVaiTro())
                .orElseThrow(() -> new RuntimeException("Vai trò không tồn tại"));

        // Tìm nhân viên
        Staff staff = staffRepo.findById(dto.getIdNhanVien())
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại"));

        // Tạo user account
        UserAccount user = new UserAccount();
        user.setTenDangNhap(dto.getTenDangNhap());
        user.setMatKhau(dto.getMatKhau());
        user.setVaiTro(role);
        user.setTrangThai(TrangThaiUserAccount.HOAT_DONG);
        user.setNhanVien(staff);

        userRepo.save(user);
        return "Tạo tài khoản nhân viên thành công";
    }


//    @Override
//    public UserResponseDto updateCurrentUser(UserUpdateDto dto) {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        System.out.println("Updating user with username = " + username);
//
//        UserAccount user = userRepo.findByTenDangNhap(username)
//                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với username: " + username));
//
//        // ✅ Cho phép đổi tên đăng nhập và mật khẩu
//        user.setTenDangNhap(dto.getTenDangNhap());
//        user.setMatKhau(dto.getMatKhau());  // bạn nên băm mật khẩu nhé
//
//        userRepo.save(user);
//        return mapToUserResponseDto(user);
//    }

    @Override
    @Transactional
    public UserResponseDto updateCurrentUser(UserUpdateDto dto) {
        // tìm user theo tenDangNhap gửi trong body
        UserAccount user = userRepo.findByTenDangNhap(dto.getTenDangNhap())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với tên đăng nhập: " + dto.getTenDangNhap()));

        user.setMatKhau(dto.getMatKhau());
        userRepo.save(user);

        return mapToUserResponseDto(user);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordDto dto) {
        // Tìm user theo tenDangNhap gửi trong body DTO
        UserAccount user = userRepo.findByTenDangNhap(dto.getTenDangNhap())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với tên đăng nhập: " + dto.getTenDangNhap()));

        // Cập nhật mật khẩu
        user.setMatKhau(dto.getNewPassword());
        userRepo.save(user);
    }


//    @Override
//    public void changePassword(ChangePasswordDto dto) {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        UserAccount user = userRepo.findByTenDangNhap(username)
//                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
//
//        if (!user.getMatKhau().equals(dto.getCurrentPassword())) {
//            throw new RuntimeException("Mật khẩu hiện tại không chính xác");
//        }
//
//        user.setMatKhau(dto.getNewPassword()); // Không mã hoá
//        userRepo.save(user);
//    }

    private UserResponseDto mapToUserResponseDto(UserAccount user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setIdUserAccount(user.getIdUserAccount());
        dto.setTenDangNhap(user.getTenDangNhap());
        dto.setVaiTro(user.getVaiTro() != null ? user.getVaiTro().getTenVaiTro() : null);
        dto.setTrangThai(
                user.getTrangThai() != null ? user.getTrangThai().name() : null
        );
        return dto;
    }

    @Override
    @Transactional
    public UserAccount login(LoginDto dto) {
        UserAccount user = userRepo.findByTenDangNhap(dto.getTenDangNhap())
                .orElseThrow(() -> new RuntimeException("Tên đăng nhập không tồn tại"));

        if (!dto.getMatKhau().equals(user.getMatKhau())) {
            throw new RuntimeException("Mật khẩu không đúng");
        }

        if (user.getTrangThai() != TrangThaiUserAccount.HOAT_DONG) {
            throw new RuntimeException("Tài khoản đang bị khóa");
        }

        return user;
    }

//    private final JwtService jwtService;

//    @Override
//    public LoginResponse login(LoginDto loginDto) {
//        UserAccount user = userRepo.findByTenDangNhap(loginDto.getTenDangNhap())
//                .orElseThrow(() -> new UsernameNotFoundException("Sai tên đăng nhập"));
//
//        if (!passwordEncoder.matches(loginDto.getMatKhau(), user.getMatKhau())) {
//            throw new BadCredentialsException("Sai mật khẩu");
//        }
//
//        if (user.getTrangThai() != TrangThaiUserAccount.HOAT_DONG) {
//            throw new DisabledException("Tài khoản bị vô hiệu hóa");
//        }
//
//        String token = jwtService.generateToken(user);
//
//        return new LoginResponse(token, user.getTenDangNhap(), user.getVaiTro().getTenVaiTro());
//    }


//    public DecodedJWT verifyToken(String token) {
//        try {
//            Algorithm algorithm = Algorithm.HMAC256("your-secret");
//            JWTVerifier verifier = JWT.require(algorithm)
//                    .withIssuer("auth0")
//                    .build();
//
//            return verifier.verify(token);
//        } catch (JWTVerificationException exception) {
//            throw new JWTVerificationException(exception.getMessage());
//        }
//    }

//    private final Algorithm algorithm = Algorithm.HMAC256(Constants.SECRET_KEY);

//    // ✅ 1. Tạo token
//    public String generateToken(String username) {
//        return JWT.create()
//                .withIssuer(Constants.ISSUER)
//                .withSubject(username)
//                .sign(algorithm);
//    }
//
//    // ✅ 2. Verify token
//    public DecodedJWT verifyToken(String token) {
//        try {
//            JWTVerifier verifier = JWT.require(algorithm)
//                    .withIssuer(Constants.ISSUER)
//                    .build();
//            return verifier.verify(token);
//        } catch (JWTVerificationException exception) {
//            throw new JWTVerificationException("Invalid token: " + exception.getMessage());
//        }
//    }

    @Override
    @Transactional
    public void deleteByTenDangNhap(String tenDangNhap) {
        if (!userAccountRepository.existsByTenDangNhap(tenDangNhap)) {
            throw new ResourceNotFoundException("Không tìm thấy tài khoản với tên đăng nhập: " + tenDangNhap);
        }
        userAccountRepository.deleteByTenDangNhap(tenDangNhap);
    }



}
