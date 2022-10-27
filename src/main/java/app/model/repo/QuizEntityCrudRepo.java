package app.model.repo;

import app.model.entity.QuizEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QuizEntityCrudRepo extends PagingAndSortingRepository<QuizEntity, Long> {

    @Query("SELECT count(qe) FROM QuizEntity qe")
    public Integer getNumberOfQuizzes();

    @Query(value = "SELECT qe FROM QuizEntity qe ORDER BY RANDOM()")
    public Page<QuizEntity> getRandomQuizzes(Pageable p);
}
