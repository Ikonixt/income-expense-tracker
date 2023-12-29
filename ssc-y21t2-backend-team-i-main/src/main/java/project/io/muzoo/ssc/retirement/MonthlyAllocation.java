package project.io.muzoo.ssc.retirement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.io.muzoo.ssc.retirement.Retirement;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(
        name = "tbl_monthly_allocation"
)
public class MonthlyAllocation {

    @Id
    @SequenceGenerator(
            name = "monthly_allocation_sequence",
            sequenceName = "monthly_allocation_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "monthly_allocation_sequence"
    )

    private Long monthlyAllocationId;
    private LocalDate date;
    private double amount;

    @ManyToOne(
            cascade = CascadeType.MERGE,
            optional = false
    )
    @JoinColumn(
            name = "retirement_id",
            referencedColumnName = "retirementId"
    )
    private Retirement retirement;
}
