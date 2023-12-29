package project.io.muzoo.ssc.models;

import lombok.Getter;
import project.io.muzoo.ssc.budget.BudgetType;

@Getter
public class BudgetModel {
    String budgetName;
    String budgetType;
    Double budgetLimit;
    Long userId;
}
