package com.market.ths.user;

import com.market.ths.exception.RegistrationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("Email not found!"));
    }


    public void SignUpUser(User user){
        boolean exists=userRepository.findByEmail(user.getEmail()).isPresent();
        if(exists)
        {
            throw new RegistrationException("Пользователь с таким email уже существует!");
        }
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        String password=encoder.encode(user.getPassword());
        user.setPassword(password);
        userRepository.save(user);
    }
}
