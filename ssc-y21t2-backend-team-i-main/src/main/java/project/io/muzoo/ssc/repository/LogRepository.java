package project.io.muzoo.ssc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import project.io.muzoo.ssc.log.Log;
import project.io.muzoo.ssc.log.LogType;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

    /**
     * Retrieves all the logs of user by
     * user's id from the database
     *
     * @param id
     * @param paging For sorting and limit
     * @return
     */
    List<Log> getLogsByUserUserId(Long id, Pageable paging);

    List<Log> getLogsByUserUserIdAndType(Long id, LogType type, Pageable paging);

    List<Log> getLogsByDateBetweenOrderByDateAsc(LocalDate starting, LocalDate ending);

    @Modifying
    @Transactional
    @Query(
            value = "update Log l set l.name = ?1 where l.logId = ?2"
    )
    void modifyLogNameByLogID(String newName, Long logId);

    @Modifying
    @Transactional
    @Query(
            value = "update Log l set l.amount = ?1 where l.logId = ?2"
    )
    void modifyLogAmountByLogId(double newAmount, Long id);

    @Modifying
    @Transactional
    @Query(
            value = "update Log l set l.note = ?1 where l.logId = ?2"
    )
    void modifyLogNoteByLogId(String newNote, Long id);

    @Modifying
    @Transactional
    @Query(
            value = "update Log l set l.type = ?1 where l.logId = ?2"
    )
    void modifyLogTypeByLogId(String newType, Long id);

}
