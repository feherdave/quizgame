package app.model.entity;

import app.security.user.QuizUserDetails;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "quizzes")
public class QuizEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String text;
    private String[] options;

    private Integer[] answer = new Integer[]{};

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    public QuizEntity() { }
    public boolean isRight(Integer[] answer) {
        return (this.answer.length == 0 && answer == null) || List.of(this.answer).equals(List.of(answer));
    }
    public Integer[] getAnswer() {
        return answer;
    }

    public String[] getOptions() {
        return options;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public void setAnswer(Integer[] answer) {
        this.answer = answer != null ? answer : new Integer[]{};
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreationTime() { return creationTime; }

    public void setCreationTime(Date creationTime) { this.creationTime = creationTime; }

}
