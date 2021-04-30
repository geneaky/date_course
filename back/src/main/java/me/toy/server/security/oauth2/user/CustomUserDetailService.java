package me.toy.server.security.oauth2.user;

import lombok.RequiredArgsConstructor;
import me.toy.server.entity.User;
import me.toy.server.repository.UserRepository;
import me.toy.server.security.oauth2.user.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> oUser = userRepository.findByEmail(email);

        User user = oUser
                .orElseThrow(()->new UsernameNotFoundException("User not found with email: "+"emai"));

        return UserPrincipal.create(user);
    }
}
