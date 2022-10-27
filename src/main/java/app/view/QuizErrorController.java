package app.view;

import app.security.user.QuizUserDetails;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class QuizErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model, @AuthenticationPrincipal QuizUserDetails user) {

        Map<String, Object> quizAppProps = new HashMap<>();

        quizAppProps.put("userLoggedin", user != null);

        model.addAttribute("statuscode", request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
        model.addAttribute("message", request.getAttribute(RequestDispatcher.ERROR_MESSAGE));

        model.addAttribute("quizapp", quizAppProps);

        return "error";
    }
}
