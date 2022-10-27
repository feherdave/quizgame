package app.model.repo;

import app.model.entity.UserDailyStatsEntity;
import app.security.user.QuizUserDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserDailyStatsRepo extends CrudRepository<UserDailyStatsEntity, Long> {

    @Query("SELECT uds FROM UserDailyStatsEntity uds WHERE date = :date AND user = :user")
    public Optional<UserDailyStatsEntity> getUserDailyStats(@Param("date") Date date, @Param("user") QuizUserDetails user);

    @Query("SELECT uds FROM UserDailyStatsEntity uds WHERE user = :user")
    public Optional<List<UserDailyStatsEntity>> getAllTimeUserDailyStats(@Param("user") QuizUserDetails user);

}
