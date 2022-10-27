package app.api;

import app.model.entity.QuizEntity;
import app.model.entity.UserDailyStatsEntity;
import app.model.repo.QuizEntityCrudRepo;
import app.model.repo.UserDailyStatsRepo;
import app.security.user.QuizUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@RestController
@Validated
public class QuizApi {

    @Autowired
    QuizEntityCrudRepo quizRepo;
    @Autowired
    UserDailyStatsRepo userDailyStatsRepo;

    Logger logger = LoggerFactory.getLogger(QuizApi.class);
    public QuizApi() { }

    @PostMapping("/api/quizzes/{id}/solve")
    public ResponseEntity<Map<String, Object>> postQuizSolution(@PathVariable long id, @RequestBody QuizAnswer answer, @AuthenticationPrincipal QuizUserDetails user) {
        QuizEntity quiz = quizRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Store statistical data
        if (user != null) {
            Optional<UserDailyStatsEntity> userDailyStatsEntityOptional = userDailyStatsRepo.getUserDailyStats(new Date(), user);

            UserDailyStatsEntity userDailyStatsEntity = userDailyStatsEntityOptional.orElse(new UserDailyStatsEntity(new Date(), user));

            if (quiz.isRight(answer.getAnswer())) {
                userDailyStatsEntity.increaseGoodAnswerCount();
            } else {
                userDailyStatsEntity.increaseBadAnswerCount();
            }

            userDailyStatsRepo.save(userDailyStatsEntity);
        }

        return ResponseEntity.ok(Map.of("success", quiz.isRight(answer.getAnswer())));
    }

}

class QuizAnswer {
    Integer[] answer;

    QuizAnswer() {}

    public void setAnswer(Integer[] answer) {
        this.answer = answer;
    }

    public Integer[] getAnswer() {
        return answer;
    }
}
