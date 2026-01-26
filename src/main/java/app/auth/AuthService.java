package app.auth;

import app.entities.User;
import app.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
@Slf4j
@Service
public class AuthService {

    private UserRepository userRepository;


    public AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    public User login(String username,String password){
        log.info("login service ========================");
        User user = userRepository.findFirstByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"username atau password salah")
        );

        if(password.equals(user.getPassword())){
            return user;
        }
        return null;
    }




}
