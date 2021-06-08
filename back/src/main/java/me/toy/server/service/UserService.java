package me.toy.server.service;

import lombok.RequiredArgsConstructor;
import me.toy.server.dto.UserDto;
import me.toy.server.entity.User;
import me.toy.server.repository.UserRepository;
import me.toy.server.security.jwt.JwtTokenProvider;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public UserDto findUser(HttpServletRequest request){
        long userId = jwtTokenProvider.getUserIdFromRequest(request);
        Optional<User> oUser = userRepository.findById(userId);
        if(oUser.isPresent()){
            return new UserDto(oUser.get());
        }
        throw new NoResultException();
    }
}
