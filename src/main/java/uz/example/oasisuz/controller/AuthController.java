package uz.example.oasisuz.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import uz.example.oasisuz.dto.TokenDto;
import uz.example.oasisuz.dto.UserDto;
import uz.example.oasisuz.dto.UserLoginDto;
import uz.example.oasisuz.dto.UserLoginResponse;
import uz.example.oasisuz.entity.Users;
import uz.example.oasisuz.service.UsersService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    private final UsersService userService;
    private final RestTemplate restTemplate;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;


    @PostMapping("/register")
    @Operation(description = "Register User")
    public HttpEntity<?> register(@Parameter(description = "User details", required = true) @RequestBody @Valid UserDto userDto) {
        Users user = userService.register(userDto);
        return ResponseEntity.ok().body(user);
    }

    @Operation(description = "Login User")
    @PostMapping("/login")
    public HttpEntity<?> login(@Parameter(description = "User details", required = true) @RequestBody @Valid UserLoginDto userLoginDto) {
        UserLoginResponse response = userService.login(userLoginDto);
        return ResponseEntity.status(response != null ? 200 : 403).body(response);
    }

    @PostMapping("/token")
    @Operation(description = "Get Tokens to refresh access token")
    public HttpEntity<?> getTokens(@Parameter(description = "Pass refresh token", required = true) @RequestBody TokenDto tokenDto) {
        return userService.getTokens(tokenDto);
    }

    @GetMapping("/auth/google")
    public UserLoginResponse authenticateGoogleUser(@RequestHeader(value = "Authorization") String idToken) {
       return userService.authenticateWithGoogle(idToken);
    }
}
