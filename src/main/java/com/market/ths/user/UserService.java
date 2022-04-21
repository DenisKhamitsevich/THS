package com.market.ths.user;

import com.market.ths.exception.RegistrationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

    public User loadUserById(Long id)
    {
        return userRepository.getById(id);
    }


    public void signUpUser(User user){
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

    public void updateNameAndEmail(Long id, String newEmail, String name) {
        User user = userRepository.getById(id);

        if ((userRepository.findByEmail(newEmail).isPresent())&&(!Objects.equals(newEmail, user.getEmail()))) {
            throw new RegistrationException("Пользователь с таким email уже существует!");
        }
        user.setEmail(newEmail);
        user.setName(name);
        userRepository.save(user);
    }

    public void updatePassword(Long id, String newPassword)
    {
        User user = userRepository.getById(id);
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        String encodedPassword=encoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }
}
