package project.io.muzoo.ssc.budget;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import project.io.muzoo.ssc.user.User;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(
        name = "tbl_budget"
)
public class Budget {

    @Id
    @SequenceGenerator(
            name = "budget_sequence",
            sequenceName = "budget_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "budget_sequence"
    )
    protected Long budgetId;
    protected String name;
    private double totalLimit;
    private double currentAmount;
    private boolean archive;

    @Enumerated(EnumType.STRING)
    protected BudgetType budgetType;

    @ManyToOne(
        cascade = CascadeType.MERGE,
        optional = false
    )
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "userId"
    )
    protected User user;
}
