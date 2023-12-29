package project.io.muzoo.ssc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.io.muzoo.ssc.retirement.Retirement;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface RetirementRepository extends JpaRepository<Retirement, Long> {

    @Query("SELECT r FROM Retirement r WHERE r.user.userId = ?1")
    Retirement getRetirementByUserUserId(Long id);

    @Transactional
    @Modifying
    @Query(
            value = "update Retirement r set r.grandTotal = ?1 where r.retirementId = ?2"
    )
    void updateGrandTotalByRetirementId(double newAmount, Long id);

    @Transactional
    @Modifying
    @Query(
            value = "update Retirement r set r.available = ?1 where r.retirementId = ?2"
    )
    void updateAvailableByRetirementId(double newAmount, Long id);

    @Transactional
    @Modifying
    @Query(
            value = "update Retirement r set r.monthlyTarget = ?1 where r.retirementId = ?2"
    )
    void updateMonthlyTargetByRetirementId(double newAmount, Long id);

    @Transactional
    @Modifying
    @Query(
            value = "update Retirement r set r.retirementDate = ?1 where r.retirementId = ?2"
    )
    void updateRetirementDateByRetirementId(String newDate, Long id);

}
