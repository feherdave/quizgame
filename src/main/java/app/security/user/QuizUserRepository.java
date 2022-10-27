package app.security.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuizUserRepository {

    @Autowired
    QuizUserDetailsCrudRepo userDetailsCrudRepo;

    public enum UserAdditionResult {USER_ADDED_SUCCESFULLY, USER_ALREADY_EXISTS};

    public QuizUserDetails getUser(String userName) {

        return userDetailsCrudRepo.findById(userName).orElse(null);
    }

    public UserAdditionResult addUser(QuizUserDetails user) {
        if (userDetailsCrudRepo.existsById(user.getUsername())) {
            return UserAdditionResult.USER_ALREADY_EXISTS;
        }

        userDetailsCrudRepo.save(user);

        return UserAdditionResult.USER_ADDED_SUCCESFULLY;
    }

    public QuizUserDetailsCrudRepo getCrudRepo() {
        return userDetailsCrudRepo;
    }
}
