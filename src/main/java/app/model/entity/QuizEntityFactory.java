package app.model.entity;

import app.util.Quiz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QuizEntityFactory {

    private QuizEntityFactory() {}

    /**
     * Creates a quiz entity from a Quiz object.
     *
     * @param quiz
     */
    public static QuizEntity createQuizEntity(Quiz quiz) {

        QuizEntity quizEntity = new QuizEntity();

        quizEntity.setTitle(quiz.getTitle());
        quizEntity.setText(quiz.getText());

        Stream<String> optTextStream = quiz.getOptions().stream().map(Quiz.QuizOption::getText);
        quizEntity.setOptions(optTextStream.collect(Collectors.toList()).toArray(new String[0]));

        List<Integer> validOptsList = new ArrayList<>();

        for (int i = 0; i < quiz.getOptions().size(); i++) {
            if (quiz.getOptions().get(i).isValidOption) {
                validOptsList.add(i);
            }
        }

        quizEntity.setAnswer(validOptsList.toArray(new Integer[0]));

        quizEntity.setCreationTime(new Date());

        return quizEntity;

    }
}
