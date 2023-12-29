package project.io.muzoo.ssc.budget;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import project.io.muzoo.ssc.retirement.Retirement;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(
        name = "tbl_retirement_sub_budget"
)
public class RetirementSubBudget extends SubBudget {

    @ManyToOne(
            cascade = CascadeType.MERGE
    )
    @JoinColumn(
            name = "retirement_id",
            referencedColumnName = "retirementId"
    )
    private Retirement retirement;

    public boolean allocateMoneyIntoCurrentAmountFromAvailable(double amount) {
        double available = retirement.getAvailable();

        if (available - amount < 0) {
            return false;
        }

        retirement.setAvailable(available - amount);
        currentAmount += amount;
        return true;
    }

}
