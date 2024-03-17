package uz.example.oasisuz.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.example.oasisuz.dto.*;
import uz.example.oasisuz.entity.Role;
import uz.example.oasisuz.entity.Users;
import uz.example.oasisuz.entity.enums.RoleEnum;
import uz.example.oasisuz.exception.CustomException;
import uz.example.oasisuz.repository.RoleRepository;
import uz.example.oasisuz.repository.UsersRepository;
import uz.example.oasisuz.util.JwtProvider;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtProvider jwtProvider;

    public Users getUser(Integer userId) {
        return usersRepository.findById(userId).orElse(null);
    }

    public Users register(UserDto userDto) {
        boolean existsByEmail = usersRepository.existsByEmail(userDto.getEmail());
        if (existsByEmail) {
            throw new CustomException(MessageFormat.format("User {0} already exist", userDto.getEmail()), HttpStatus.BAD_REQUEST);
        }
        if (userDto.getEmail() == null) {
            throw new CustomException("Email must be filled", HttpStatus.BAD_REQUEST);
        }
        List<Role> roleList = roleRepository.findAllByRoleEnumIn(List.of(RoleEnum.USER));
        Users user = Users.builder()
                .fullName(userDto.getFullName())
                .phoneNumber(userDto.getPhoneNumber())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .roles(roleList)
                .build();
        usersRepository.save(user);
        return user;
    }

    public UserLoginResponse login(UserLoginDto userLoginDto) {
        Optional<Users> optionalUser = usersRepository.findByEmail(userLoginDto.getEmail());
        if (optionalUser.isEmpty()) {
            return null;
        }
        Users user = optionalUser.get();
        if (!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException(MessageFormat.format("Invalid username or password {0}", userLoginDto.getEmail()));
        }
        String token = jwtProvider.generateToken(user.getEmail());
        String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());
        return new UserLoginResponse("Bearer " + token, "Bearer " + refreshToken, user.getFullName(), user.getId());
    }

    public HttpEntity<?> getTokens(TokenDto tokenDto) {
        if (tokenDto.getRefreshToken() != null) {
            if (jwtProvider.isTokenValid(tokenDto.getRefreshToken().substring(7))) {
                String email = jwtProvider.getSubject(tokenDto.getRefreshToken().substring(7));
                if (email != null) {
                    String accessToken = jwtProvider.generateToken(email);
                    return ResponseEntity.ok().body(new TokenDto("Bearer " + accessToken, "Bearer " + tokenDto.getRefreshToken()));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    public UserLoginResponse authenticateWithGoogle(String idToken) {
        String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken.substring(7);
        RestTemplate restTemplate = new RestTemplate();
        GoogleTokenInfo tokenInfo = restTemplate.getForObject(url, GoogleTokenInfo.class);

        if (tokenInfo != null && tokenInfo.getEmail() != null) {
            Optional<Users> byEmail = usersRepository.findByEmail(tokenInfo.getEmail());

            if (byEmail.isEmpty()) {
                List<Role> roleList = roleRepository.findAllByRoleEnumIn(List.of(RoleEnum.USER));

                Users user = Users.builder()
                        .email(tokenInfo.getEmail())
                        .fullName(tokenInfo.getName())
                        .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                        .roles(roleList).build();
                Users save = usersRepository.save(user);
                String token = jwtProvider.generateToken(tokenInfo.getEmail());
                String refreshToken = jwtProvider.generateRefreshToken(tokenInfo.getEmail());
                return new UserLoginResponse("Bearer " + token, "Bearer " + refreshToken, save.getFullName(), save.getId());
            } else {
                String token = jwtProvider.generateToken(tokenInfo.getEmail());
                String refreshToken = jwtProvider.generateRefreshToken(tokenInfo.getEmail());
                return new UserLoginResponse("Bearer " + token, "Bearer " + refreshToken, tokenInfo.getName(), byEmail.get().getId());
            }
        } else {
            throw new BadCredentialsException("Failed to login or sing up!");
        }
    }
}
