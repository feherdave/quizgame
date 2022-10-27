package app.util;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Quiz {

    @Data
    @NoArgsConstructor
    public class QuizOption {
        public Boolean isValidOption = false;
        public String text = "";
    }

    public String title;
    public String text;

    public List<QuizOption> options = List.of(
            new QuizOption(),
            new QuizOption(),
            new QuizOption(),
            new QuizOption());

    /**
     * Check validity of a Quiz object.
     *
     * @param quiz
     * @return Empty, if it is valid, otherwise the list of non-fulfilled criteria.
     */
    public static List<String> checkValidity(Quiz quiz) {
        List<String> ret = new ArrayList<>();

        if (quiz.title.isBlank()) {
            ret.add("Title field must not be blank!");
        }

        if (quiz.text.isBlank()) {
            ret.add("Text field must not be blank!");
        }

        boolean hasOneValidOption = false;
        for (int i = 0; i < quiz.getOptions().size(); i++) {
            QuizOption qo = quiz.getOptions().get(i);

            if (qo.getText().isBlank()) {
                ret.add(String.format("Text of quiz option #%d must not be blank!", i + 1));
            }

            hasOneValidOption |= qo.isValidOption;
        }

        if (!hasOneValidOption) {
            ret.add("At least one quiz option must be checked as valid!");
        }

        return ret;
    }
}
