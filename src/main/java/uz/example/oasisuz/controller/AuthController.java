package uz.example.oasisuz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uz.example.oasisuz.dto.*;
import uz.example.oasisuz.dto.request.IdTokenRequestDto;
import uz.example.oasisuz.dto.request.UserLoginDto;
import uz.example.oasisuz.dto.response.UserLoginResponse;
import uz.example.oasisuz.entity.Users;
import uz.example.oasisuz.service.UsersService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    private final UsersService userService;

    @PostMapping("/register")
    @Operation(description = "Register User")
    public HttpEntity<?> register(@Parameter(description = "User details", required = true) @RequestBody @Valid UsersDto usersDto) {
        Users user = userService.register(usersDto);
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
    public HttpEntity<?> getTokens(@RequestBody TokenDto tokenDto) {
        return userService.getTokens(tokenDto);
    }

    @Deprecated
    @GetMapping("/auth/google/1")
    public UserLoginResponse authenticateGoogleUser(@RequestHeader(value = "Authorization") String idToken) {
        return userService.authenticateWithGoogle(idToken);
    }

    @PostMapping("/auth/google")
    public UserLoginResponse authenticateGoogleUser(@RequestBody IdTokenRequestDto idToken) {
        return userService.authenticateWithGoogle(idToken);
    }
}
