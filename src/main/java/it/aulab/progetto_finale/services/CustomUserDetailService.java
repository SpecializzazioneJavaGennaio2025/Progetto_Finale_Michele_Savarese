package it.aulab.progetto_finale.services;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.aulab.progetto_finale.models.User;
import it.aulab.progetto_finale.repositories.UserRepository;
import it.aulab.progetto_finale.models.Role;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findByEmail(username);
        if (user==null){
            throw new UsernameNotFoundException("Invalid credentials");
        }
        return new CustomUserDetails(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    public Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        Collection<? extends GrantedAuthority> mapRoles = null;
        if(roles.size() != 0){
            mapRoles = roles.stream().map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
        } else {
            mapRoles = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return mapRoles;
    }

}