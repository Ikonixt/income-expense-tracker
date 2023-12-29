package project.io.muzoo.ssc.retirement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.io.muzoo.ssc.user.User;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(
        name = "tbl_retirement"
)
public class Retirement {

    @Id
    @SequenceGenerator(
            name = "retirement_sequence",
            sequenceName = "retirement_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "retirement_sequence"
    )
    private Long retirementId;
    private double grandTotal;
    private double available;
    private double monthlyTarget;
    private String startDate;
    private String retirementDate;

    @OneToOne(
            cascade = CascadeType.MERGE,
            optional = false
    )
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "userId"
    )
    private User user;

}
