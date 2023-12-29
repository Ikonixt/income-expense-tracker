package project.io.muzoo.ssc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.io.muzoo.ssc.budget.BudgetSubBudget;
import project.io.muzoo.ssc.budget.SubBudget;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface SubBudgetRepository extends JpaRepository<SubBudget, Long> {

    @Query("SELECT s FROM BudgetSubBudget s WHERE s.budget.budgetId = ?1")
    List<BudgetSubBudget> findSubBudgetByBudgetId(Long budgetId);

    @Query("SELECT s FROM BudgetSubBudget s WHERE s.source.sourceId = ?1")
    List<BudgetSubBudget> findSubBudgetBySourceId(Long sourceId);

    @Query("SELECT bs FROM BudgetSubBudget bs where bs.source.sourceName = ?1")
    BudgetSubBudget getSubBudgetBySourceName(String name);

    //Don't use this
    @Query(
            value = "select * from tbl_budget_sub_budget b where b.source_id = ?1",
            nativeQuery = true
    )
    BudgetSubBudget getSubBudgetBySourceId(Long id);

    @Query(
            value = "select * from tbl_budget_sub_budget b where b.sub_budget_id = ?1",
            nativeQuery = true
    )
    BudgetSubBudget getSubBudgetBySubBudgetId(Long id);

    @Modifying
    @Transactional
    @Query(
            value = "update SubBudget sb set sb.name = ?1 where sb.subBudgetId = ?2"
    )
    void modifySubBudgetNameByBudgetId(String newName, Long subBudgetId);

    @Modifying
    @Transactional
    @Query(
            value = "update SubBudget sb set sb.allocation = ?1 where sb.subBudgetId = ?2"
    )
    void modifySubBudgetAllocationByBudgetId(double newValue, Long subBudgetId);

    @Modifying
    @Transactional
    @Query(
            value = "update SubBudget sb set sb.currentAmount = ?1 where sb.subBudgetId = ?2"
    )
    void modifySubBudgetCurrentAmountByBudgetId(double newValue, Long subBudgetId);

    @Modifying
    @Transactional
    void deleteBySubBudgetId(Long id);

    @Modifying
    @Transactional
    @Query(
            value = "delete from tbl_budget_sub_budget where source_id = ?1",
            nativeQuery = true
    )
    void deleteBySourceId(Long id);


}