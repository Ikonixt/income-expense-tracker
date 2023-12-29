package project.io.muzoo.ssc.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SubBudgetModel {
     Long budgetId;
     Long sourceId;
     double allocation;
     double currentAmount;
}
