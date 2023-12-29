package project.io.muzoo.ssc.models;

import lombok.Getter;
import project.io.muzoo.ssc.budget.Budget;
import project.io.muzoo.ssc.log.LogType;

import java.util.Date;

@Getter
public class LogModel {
    String logName;
    Double logAmount;
    LogType logType;
    Long sourceId;
    String logDate;
    String logNote;
    Long userId;
    Long budgetId;
}
