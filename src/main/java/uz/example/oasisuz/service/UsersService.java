package uz.example.oasisuz.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uz.example.oasisuz.dto.*;
import uz.example.oasisuz.dto.request.GoogleTokenInfo;
import uz.example.oasisuz.dto.request.IdTokenRequestDto;
import uz.example.oasisuz.dto.request.UserLoginDto;
import uz.example.oasisuz.dto.response.UserLoginResponse;
import uz.example.oasisuz.entity.Role;
import uz.example.oasisuz.entity.Users;
import uz.example.oasisuz.entity.enums.AuthType;
import uz.example.oasisuz.entity.enums.RoleEnum;
import uz.example.oasisuz.exception.CustomException;
import uz.example.oasisuz.repository.RoleRepository;
import uz.example.oasisuz.repository.UsersRepository;
import uz.example.oasisuz.util.JwtProvider;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtProvider jwtProvider;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    public Users getUser(Integer userId) {
        return usersRepository.findById(userId).orElseThrow(() ->
                new CustomException(String.format("User not found, id:{%s} ", userId), HttpStatus.BAD_REQUEST));
    }

    public Users register(UsersDto usersDto) {
        boolean existsByEmail = usersRepository.existsByEmail(usersDto.getEmail());
        if (existsByEmail) {
            throw new CustomException(MessageFormat.format("User {0} already exist", usersDto.getEmail()), HttpStatus.BAD_REQUEST);
        }
        if (usersDto.getEmail() == null) {
            throw new CustomException("Email must be filled", HttpStatus.BAD_REQUEST);
        }
        List<Role> roleList = roleRepository.findAllByRoleEnumIn(List.of(RoleEnum.USER));
        Users user = Users.builder()
                .fullName(usersDto.getFullName())
                .phoneNumber(usersDto.getPhoneNumber())
                .email(usersDto.getEmail())
                .authType(AuthType.BASIC)
                .password(passwordEncoder.encode(usersDto.getPassword()))
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
        return new UserLoginResponse("Bearer " + token, "Bearer " + refreshToken, user.getFullName(), user.getId(), user.getRoles().get(0).getRoleEnum());
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

    @Deprecated
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
                        .authType(AuthType.GOOGLE)
                        .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                        .roles(roleList).build();
                Users save = usersRepository.save(user);
                String token = jwtProvider.generateToken(tokenInfo.getEmail());
                String refreshToken = jwtProvider.generateRefreshToken(tokenInfo.getEmail());
                return new UserLoginResponse("Bearer " + token, "Bearer " + refreshToken, save.getFullName(), save.getId(), save.getRoles().get(0).getRoleEnum());
            } else {
                Users users = byEmail.get();
                if (!Objects.equals(users.getAuthType(), AuthType.GOOGLE)) {
                    throw new CustomException(String.format("This username, %s, already exists! Please login without google!", users.getUsername()), HttpStatus.BAD_REQUEST);
                }
                String token = jwtProvider.generateToken(tokenInfo.getEmail());
                String refreshToken = jwtProvider.generateRefreshToken(tokenInfo.getEmail());
                return new UserLoginResponse("Bearer " + token, "Bearer " + refreshToken, tokenInfo.getName(), users.getId(), users.getRoles().get(0).getRoleEnum());
            }
        } else {
            throw new BadCredentialsException("Failed to login or sing up!");
        }
    }

    public UserLoginResponse authenticateWithGoogle(IdTokenRequestDto idTokenRequestDto) {
        GoogleIdToken googleIdToken = verifyIDToken(idTokenRequestDto.getTokenId());

        if (googleIdToken == null) {
            throw new IllegalArgumentException();
        }

        GoogleIdToken.Payload payload = googleIdToken.getPayload();

        String email = payload.getEmail();
        String firstName = (String) payload.get("given_name");

        Optional<Users> optionalUser = usersRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            Users user = createUser(email, firstName, payload);
            Users save = usersRepository.save(user);
            String token = jwtProvider.generateToken(user.getEmail());
            String refreshToken = jwtProvider.generateRefreshToken(email);
            return new UserLoginResponse("Bearer " + token, "Bearer " + refreshToken, firstName, save.getId(), save.getRoles().get(0).getRoleEnum());
        } else {
            Users users = optionalUser.get();
            if (!Objects.equals(users.getAuthType(), AuthType.GOOGLE)) {
                throw new CustomException(String.format("This username, %s, already exists! Please login without google!", users.getUsername()), HttpStatus.BAD_REQUEST);
            }
            String token = jwtProvider.generateToken(users.getEmail());
            String refreshToken = jwtProvider.generateRefreshToken(users.getEmail());
            return new UserLoginResponse("Bearer " + token, "Bearer " + refreshToken, firstName, users.getId(), users.getRoles().get(0).getRoleEnum());
        }
    }


    private GoogleIdToken verifyIDToken(String idToken) {
        try {
            return GoogleIdToken.parse(googleIdTokenVerifier.getJsonFactory(), idToken);
        } catch (IOException e) {
            return null;
        }
    }

    private Users createUser(String email, String firstName, GoogleIdToken.Payload payload) {
        String lastName = (String) payload.get("family_name");
        String pictureUrl = (String) payload.get("picture");
        List<Role> roleList = roleRepository.findAllByRoleEnumIn(List.of(RoleEnum.USER));

        return Users.builder()
                .email(email)
                .fullName(firstName + ' ' + lastName)
                .authType(AuthType.GOOGLE)
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .imgUrl(pictureUrl)
                .roles(roleList).build();
    }
}
