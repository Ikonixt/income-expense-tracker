package project.io.muzoo.ssc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import project.io.muzoo.ssc.budget.SubBudget;
import project.io.muzoo.ssc.log.Source;

import javax.transaction.Transactional;
import java.util.List;

public interface SourceRepository extends JpaRepository<Source, Long> {

    @Query("SELECT s FROM Source s WHERE s.sourceId = ?1")
    Source findSourceBySourceId(Long sourceId);

    @Query("SELECT s FROM Source s WHERE s.user.userId = ?1")
    List<Source> findSourceByUserId(Long userId);

    @Query("SELECT s FROM Source s WHERE s.user.userId = ?1")
    List<Source> getAllSourceNameByUserId(Long userId);

    @Modifying
    @Transactional
    @Query(
            value = "update Source s set s.sourceName = ?1 where s.sourceId = ?2"
    )
    void updateSourceNameBySourceId(String newName, Long id);

    @Modifying
    @Transactional
    void deleteBySourceId(Long id);
}
