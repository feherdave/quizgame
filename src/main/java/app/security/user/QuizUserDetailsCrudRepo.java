package app.security.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface QuizUserDetailsCrudRepo extends CrudRepository<QuizUserDetails, String> {

    @Query("SELECT count(user) FROM QuizUserDetails user")
    public Integer getUserCount();

}
