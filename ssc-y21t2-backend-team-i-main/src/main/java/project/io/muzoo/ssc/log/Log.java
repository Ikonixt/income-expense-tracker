package project.io.muzoo.ssc.log;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.io.muzoo.ssc.budget.Budget;
import project.io.muzoo.ssc.budget.SubBudget;
import project.io.muzoo.ssc.user.User;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(
        name = "tbl_log"
)
public class Log {

    @Id
    @SequenceGenerator(
            name = "log_sequence",
            sequenceName = "log_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "log_sequence"
    )
    private Long logId;

    @Column(name = "date", columnDefinition = "DATE")
    private LocalDate date;
    private String name;
    private double amount;

    @OneToOne(
            cascade = CascadeType.MERGE
    )
    @JoinColumn(
            name = "budget_id",
            referencedColumnName = "budgetId"
    )
    private Budget budget;

    @Column(
            columnDefinition = "TEXT"
    )
    private String note;

    @Enumerated(EnumType.STRING)
    private LogType type;

    @ManyToOne(
            cascade = CascadeType.MERGE,
            optional = false
    )
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "userId"
    )
    private User user;

    @ManyToOne(
            cascade = CascadeType.MERGE,
            optional = false
    )
    @JoinColumn(
            name = "source_id",
            referencedColumnName = "sourceId"
    )
    private Source source;

    public String deductMoneyFromSubBudget(SubBudget subBudget) {
        String message = "";
        subBudget.setCurrentAmount(subBudget.getCurrentAmount() + amount);
        if (subBudget.getCurrentAmount() > subBudget.getAllocation()) {
            message = "Usage exceeds money allocated";
        }
        return message;
    }

}
