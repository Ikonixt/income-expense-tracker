package project.io.muzoo.ssc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.io.muzoo.ssc.budget.Budget;
import project.io.muzoo.ssc.budget.BudgetType;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    @Modifying
    @Transactional
    @Query(
            value = "update Budget b set b.name = ?1 where b.budgetId = ?2"
    )
    void modifyBudgetNameByBudgetId(String newName, Long budgetId);

    @Modifying
    @Transactional
    @Query(
            value = "update Budget b set b.totalLimit = ?1 where b.budgetId = ?2"
    )
    void modifyBudgetTotalLimitByBudgetId(double newLimit, Long budgetId);

    @Modifying
    @Transactional
    @Query(
            value = "update Budget b set b.currentAmount = ?1 where b.budgetId = ?2"
    )
    void modifyBudgetCurrentAmountByBudgetId(Double newAmount, Long budgetId);

    @Modifying
    @Transactional
    @Query(
            value = "update Budget b set b.archive = true where b.budgetId = ?1"
    )
    void archiveBudgetbyBudgetId(Long budgetId);


    List<Budget> getBudgetsByUserUserId(Long userId);

    Budget getBudgetByBudgetId(Long budgetId);
}
