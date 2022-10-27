package app.view;

import app.model.entity.QuizEntity;
import app.model.entity.QuizEntityFactory;
import app.model.entity.UserDailyStatsEntity;
import app.model.repo.QuizEntityCrudRepo;
import app.model.repo.UserDailyStatsRepo;
import app.security.user.QuizUserDetails;
import app.security.user.QuizUserDetailsCrudRepo;
import app.util.Quiz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@Validated
public class QuizView {

    @Autowired
    QuizEntityCrudRepo quizRepo;

    @Autowired
    QuizUserDetailsCrudRepo userRepo;

    @Autowired
    UserDailyStatsRepo userDailyStatsRepo;

    Logger logger = LoggerFactory.getLogger(QuizView.class);

    @GetMapping(path = {"/", "/home"})
    public String homeView(Model model, @AuthenticationPrincipal UserDetails user) {
        Map<String, Object> quizAppProps = new HashMap<>();

        if (user != null) {
            quizAppProps.put("username", user.getUsername());
        }

        quizAppProps.put("userLoggedin", user != null);

        model.addAttribute("quizapp", quizAppProps);

        return "home";
    }

    @GetMapping("/register")
    public String registerView(Model model) {

        Map<String, Object> quizAppProps = new HashMap<>();

        quizAppProps.put("userLoggedin", false);

        model.addAttribute("userCount", userRepo.getUserCount());

        model.addAttribute("quizapp", quizAppProps);

        return "register";
    }

    @GetMapping("/account")
    public String accountView(Model model, @AuthenticationPrincipal QuizUserDetails user) {

        Map<String, Object> quizAppProps = new HashMap<>();

        quizAppProps.put("userLoggedin", user != null);

        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("quizapp", quizAppProps);

        return "account";
    }

    @GetMapping("/play")
    public String playView(Model model, @AuthenticationPrincipal UserDetails user) {
        Map<String, Object> quizAppProps = new HashMap<>();

        quizAppProps.put("userLoggedin", user != null);

        Integer numOfQuizzes = quizRepo.getNumberOfQuizzes();

        if (numOfQuizzes < 0) {
            quizAppProps.put("notEnoughQuizzesInDB", true);
            quizAppProps.put("numberOfQuizzes", numOfQuizzes);
        } else {
            quizAppProps.put("notEnoughQuizzesInDB", false);

            QuizEntity quizEntity = quizRepo.getRandomQuizzes(Pageable.ofSize(1)).getContent().get(0);

            logger.info("Randomized quiz: " + quizEntity.getTitle());

            model.addAttribute("quizid", quizEntity.getId());
            model.addAttribute("quiztitle", quizEntity.getTitle());
            model.addAttribute("quiztext", quizEntity.getText());

            model.addAttribute("quizoptions", quizEntity.getOptions());
        }

        model.addAttribute("quizapp", quizAppProps);

        return "play";
    }

    @GetMapping("/contribute")
    public String contributeView(Model model, @AuthenticationPrincipal UserDetails user) {
        Map<String, Object> quizAppProps = new HashMap<>();

        quizAppProps.put("userLoggedin", user != null);

        model.addAttribute("quizapp", quizAppProps);
        model.addAttribute("quiz", new Quiz());
        model.addAttribute("initial", true);

        return "contribute";
    }

    @PostMapping("/contribute")
    public String contributePost(
            Model model,
            @AuthenticationPrincipal QuizUserDetails user,
            @ModelAttribute Quiz quiz,
            BindingResult errors) {

        // TODO: implement a solution to prevent resending the form data on page refresh

        Map<String, Object> quizAppProps = new HashMap<>();

        quizAppProps.put("userLoggedin", user != null);

        List<String> quizErrors = Quiz.checkValidity(quiz);

        // Create a quiz entity and store to DB if everything is fine
        if (quizErrors.size() == 0) {
            QuizEntity quizEntity = QuizEntityFactory.createQuizEntity(quiz);

            quizRepo.save(quizEntity);

            model.addAttribute("quiz", new Quiz());
        } else {

        }

        model.addAttribute("errors", quizErrors);

        model.addAttribute("quizapp", quizAppProps);

        return "contribute";
    }

    @GetMapping("/stats")
    public String statsView(Model model, @AuthenticationPrincipal QuizUserDetails user) {

        Map<String, Object> quizAppProps = new HashMap<>();

        quizAppProps.put("userLoggedin", user != null);

        if (user != null) {
            Optional<List<UserDailyStatsEntity>> userDailyStatsOpt = userDailyStatsRepo.getAllTimeUserDailyStats(user);

            // Okay, this could have been nicer...
            userDailyStatsOpt.ifPresentOrElse(
                    userDailyStatsEntities -> {
                        Long goodAnswers = 0L;
                        Long badAnswers = 0L;

                        for (UserDailyStatsEntity userDailyStatsEntity : userDailyStatsEntities) {
                            goodAnswers += userDailyStatsEntity.getGoodAnswers();
                            badAnswers += userDailyStatsEntity.getBadAnswers();
                        }

                        model.addAttribute("numgoodanswers", goodAnswers);
                        model.addAttribute("numbadanswers", badAnswers);
                        model.addAttribute("nostats", false);

                    },
                    () -> { model.addAttribute("nostats", true); });

        }

        model.addAttribute("quizapp", quizAppProps);

        return "stats";
    }

}
