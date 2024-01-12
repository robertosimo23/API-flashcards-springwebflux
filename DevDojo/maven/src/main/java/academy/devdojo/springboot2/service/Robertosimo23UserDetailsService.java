package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.repository.Robertosimo23UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Robertosimo23UserDetailsService implements UserDetailsService {
    private final Robertosimo23UserRepository robertosimo23UserRepository;

    @Override
    public UserDetails loadUserByUsername(String username)  {
        return Optional.ofNullable(robertosimo23UserRepository.findByUsername(username))
        .orElseThrow(() -> new UsernameNotFoundException("Robertosimo User not found"));
    }
}
