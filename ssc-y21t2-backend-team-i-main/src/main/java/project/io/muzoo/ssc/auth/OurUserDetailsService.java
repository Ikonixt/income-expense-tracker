package project.io.muzoo.ssc.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.io.muzoo.ssc.repository.UserRepository;

@Service
public class OurUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        project.io.muzoo.ssc.user.User u = userRepository.findUserByUsername(username);

        if(u != null){
            System.out.println("FOUND USER DETAILS");
            return User.withUsername(u.getUsername())
                    .password(u.getPassword())
                    .roles("USER")
                    .build();
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }

}
