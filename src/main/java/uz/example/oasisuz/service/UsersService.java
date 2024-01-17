package uz.example.oasisuz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.example.oasisuz.entity.Users;
import uz.example.oasisuz.repository.UsersRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;

    public Users getUser(Integer userId) {
        return usersRepository.findById(userId).orElse(null);
    }
}
