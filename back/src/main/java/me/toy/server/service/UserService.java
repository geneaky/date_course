package me.toy.server.service;

import lombok.RequiredArgsConstructor;
import me.toy.server.dto.UserDto;
import me.toy.server.entity.User;
import me.toy.server.exception.UserNotFoundException;
import me.toy.server.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto findUser(String userEmail){
        User user = userRepository.findByEmail(userEmail).orElseThrow(() ->
                new UserNotFoundException("그런 이메일로 가입한 사용자는 없습니다.")
        );
        return new UserDto(user);
    }
}
