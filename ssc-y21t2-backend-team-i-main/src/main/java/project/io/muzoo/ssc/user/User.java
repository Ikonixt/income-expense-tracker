package project.io.muzoo.ssc.user;

import lombok.*;
import javax.persistence.*;

@Setter
@Entity
@Table(
        name = "tbl_user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username")
        }
)
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long userId;
    private String username;
    private String password;




    /*
    Commented for out now, changed implementation

    @OneToMany(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    )
    private List<Budget> budgets;

     */

}
