package app.api;

import app.security.QuizSecurity;
import app.security.QuizUserJSON;
import app.security.user.QuizUserDetails;
import app.security.user.QuizUserDetailsCrudRepo;
import app.security.user.QuizUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Map;

@RestController
@Validated
public class QuizUserApi {

    @Autowired
    QuizUserRepository userRepo;

    @Autowired
    QuizSecurity quizSecurity;

    @PostMapping("/api/register")
    public ResponseEntity<?> postUser(@Valid @RequestBody QuizUserJSON userJSON) {
        QuizUserRepository.UserAdditionResult uar = userRepo.addUser(new QuizUserDetails(userJSON.getUsername(), quizSecurity.getEncoder().encode(userJSON.getPassword()), userJSON.getEmail(), "ROLE_USER"));

        switch(uar) {
            case USER_ALREADY_EXISTS: throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
            case USER_ADDED_SUCCESFULLY: return ResponseEntity.ok(Map.of("message", "User added successfully"));
            default: throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown error");
        }
    }

    @PutMapping("/api/user/update")
    public ResponseEntity<?> updateUser(
            @Valid @Email @RequestParam String email,
            @Valid @Size(min = 5) @RequestParam(name = "newpassword", required = false) String newPassword,
            @Valid @Size(min = 5) @RequestParam String password,
            @AuthenticationPrincipal QuizUserDetails user
            ) {

        if (user != null) {
            if (quizSecurity.getEncoder().matches(password, user.getPassword())) {

                user.setEmail(email);
                if (newPassword != null) user.setPassword(quizSecurity.getEncoder().encode(newPassword));

                userRepo.getCrudRepo().save(user);

                return ResponseEntity.ok(Map.of("message", "OK"));
            }

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The old password you entered is incorrect");

        }

        return ResponseEntity.badRequest().body(Map.of("message", "Unknown error (user == null)"));
    }

    @DeleteMapping("/api/user/delete")
    public ResponseEntity<?> deleteUser(
            @Valid @Size(min = 5) @RequestParam String password,
            @AuthenticationPrincipal QuizUserDetails user
    ) {

        if (user != null) {
            if (quizSecurity.getEncoder().matches(password, user.getPassword())) {

                userRepo.getCrudRepo().delete(user);

                return ResponseEntity.ok(Map.of("message", "OK"));
            }

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The old password you entered is incorrect");

        }

        return ResponseEntity.badRequest().body(Map.of("message", "Unknown error (user == null)"));
    }
}
