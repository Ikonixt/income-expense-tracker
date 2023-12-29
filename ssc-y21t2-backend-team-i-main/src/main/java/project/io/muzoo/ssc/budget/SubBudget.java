package project.io.muzoo.ssc.budget;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import project.io.muzoo.ssc.log.Source;
import project.io.muzoo.ssc.retirement.Retirement;

import javax.persistence.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Table(
        name = "tbl_sub_budget"
)
@Inheritance(
        strategy = InheritanceType.TABLE_PER_CLASS
)
public abstract class SubBudget {

    @Id
    @SequenceGenerator(
            name = "sub_budget_sequence",
            sequenceName = "sub_budget_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sub_budget_sequence"
    )
    protected Long subBudgetId;
    protected String name;
    protected double allocation;
    protected double currentAmount;

}
