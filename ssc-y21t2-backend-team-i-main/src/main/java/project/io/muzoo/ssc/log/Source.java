package project.io.muzoo.ssc.log;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.io.muzoo.ssc.user.User;

import javax.persistence.*;
import java.sql.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(
        name = "tbl_source"
)
public class Source {

    @Id
    @SequenceGenerator(
            name = "source_sequence",
            sequenceName = "source_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "source_sequence"
    )
    private Long sourceId;
    private String sourceName;
    private Integer sourceUsage;
    private SourceType sourceType;

    @ManyToOne(
            cascade = CascadeType.MERGE,
            optional = false
    )
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "userId"
    )
    private User user;

    public void increaseUsage() {
        sourceUsage += 1;
    }

    public void decreaseUsage() {
        sourceUsage -= 1;
    }
}
