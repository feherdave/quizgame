package app.model.entity;


import app.security.user.QuizUserDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_daily_stats")
@Data
@NoArgsConstructor
public class UserDailyStatsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Temporal(TemporalType.DATE)
    Date date;

    @ManyToOne
    @JoinColumn(name = "user_name")
    QuizUserDetails user;

    Integer goodAnswers = 0;
    Integer badAnswers = 0;

    public UserDailyStatsEntity(Date date, QuizUserDetails user) {
        this.date = date;
        this.user = user;
    }

    public void increaseGoodAnswerCount() {
        goodAnswers++;
    }

    public void increaseBadAnswerCount() {
        badAnswers++;
    }

}
