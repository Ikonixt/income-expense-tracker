package project.io.muzoo.ssc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.io.muzoo.ssc.budget.Budget;
import project.io.muzoo.ssc.user.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom query
    @Query("SELECT u FROM User u WHERE u.username = ?1")
    User findUserByUsername(String name);

    @Modifying
    @Transactional
    @Query(
            value = "update User u set u.username = ?1 where u.username = ?2"
    )
    int updateUsernameByUsername(String updateTo, String toUpdate);

    // Custom query
    @Query("SELECT u.password FROM User u WHERE u.username = ?1")
    Optional<User> findUserPasswordByUsername(String name);

    @Modifying
    @Transactional
    @Query(
            value = "update User u set u.password = ?1 where u.username = ?2"
    )
    int updateUserPasswordByUsername(String updateTo);
}

