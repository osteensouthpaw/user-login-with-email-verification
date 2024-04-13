package com.example.loginsystemwithemailverification.user;

import com.example.loginsystemwithemailverification.exceptions.DuplicationResourceException;
import com.example.loginsystemwithemailverification.exceptions.ResourceNotFoundException;
import com.example.loginsystemwithemailverification.token.ConfirmationToken;
import com.example.loginsystemwithemailverification.token.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }


    public List<UserDto> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(userDtoMapper)
                .collect(Collectors.toList());
    }


    public UserDto findById(Integer id) {
        return userRepository.findById(id)
                .map(userDtoMapper)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exists"));
    }


    public AppUser findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exists with email: " + email));
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public AppUser createUser(AppUser user) {
        boolean userExists = userRepository.existsByEmail(user.getEmail());
        if (userExists) {
            throw new DuplicationResourceException("email already taken");
        }
        return userRepository.save(user);
    }

    public void enableUser(ConfirmationToken userToken) {
        AppUser appUser = userToken.getAppUser();
        appUser.setEnabled(true);
    }
}
