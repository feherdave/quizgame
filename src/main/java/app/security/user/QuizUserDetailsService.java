package app.security.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class QuizUserDetailsService implements UserDetailsService {

    @Autowired
    QuizUserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QuizUserDetails user = userRepo.getUser(username);

        if (user != null) {
            return user;
        }

        throw new UsernameNotFoundException("Sorry, user '" + username + "' not found...");
    }
}
