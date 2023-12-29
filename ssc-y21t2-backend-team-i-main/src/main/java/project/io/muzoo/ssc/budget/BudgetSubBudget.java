package project.io.muzoo.ssc.budget;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import project.io.muzoo.ssc.log.Source;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(
        name = "tbl_budget_sub_budget"
)
public class BudgetSubBudget extends SubBudget {

    @OneToOne(
            cascade = CascadeType.MERGE
    )
    @JoinColumn(
            name = "source_id",
            referencedColumnName = "sourceId"
    )
    private Source source;

    @ManyToOne(
            cascade = CascadeType.MERGE
    )
    @JoinColumn(
            name = "budget_id",
            referencedColumnName = "budgetId"
    )
    private Budget budget;

    public String addExpense(double amount) {
        String messsage = "";
        currentAmount += amount;
        if (currentAmount > allocation) {
            messsage = "Usage exceeds allocated amount";
        }
        return messsage;
    }

}
