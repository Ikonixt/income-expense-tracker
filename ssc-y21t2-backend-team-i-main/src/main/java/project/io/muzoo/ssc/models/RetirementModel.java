package project.io.muzoo.ssc.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RetirementModel {
    Long retirementId;
    double grandTotal;
    double available;
    double monthlyTarget;
    String startDate;
    String retirementDate;
}
