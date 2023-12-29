package project.io.muzoo.ssc.service;

import org.javatuples.Pair;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import project.io.muzoo.ssc.budget.Budget;
import project.io.muzoo.ssc.budget.BudgetSubBudget;
import project.io.muzoo.ssc.log.Log;
import project.io.muzoo.ssc.log.LogType;
import project.io.muzoo.ssc.log.Source;
import project.io.muzoo.ssc.repository.*;
import project.io.muzoo.ssc.retirement.Retirement;
import project.io.muzoo.ssc.user.User;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogService {

    private final UserRepository userRepository;
    private final LogRepository logRepository;
    private final SourceRepository sourceRepository;
    private final RetirementRepository retirementRepository;
    private final BudgetRepository budgetRepository;
    private final SubBudgetRepository subBudgetRepository;

    public LogService(LogRepository logRepository, UserRepository userRepository, SourceRepository sourceRepository, RetirementRepository retirementRepository, BudgetRepository budgetRepository, SubBudgetRepository subBudgetRepository) {
        this.logRepository = logRepository;
        this.userRepository = userRepository;
        this.sourceRepository = sourceRepository;
        this.retirementRepository = retirementRepository;
        this.budgetRepository = budgetRepository;
        this.subBudgetRepository = subBudgetRepository;
    }


    /**
     * Retrieve the logs of the user.
     * If the user is not found
     * an exception is thrown
     *
     * @param id
     * @return return the list of log
     */
    public List<Log> getLogsByUserID(Long id) {
        return getLogsByUserID(id, null, null, null);
    }

    public List<Log> getLogsByUserID(Long id, LogType type, Integer limit, String sortBy) {
        if (!userRepository.existsById(id)) {
            throw new IllegalStateException("User with id: " + id + " does not exists!");
        }

        Pageable paging = PageRequest.of(0, limit == null ? Integer.MAX_VALUE : limit, Sort.by(sortBy == null ? "amount" : sortBy).descending());
        List<Log> logs = type != null ? logRepository.getLogsByUserUserIdAndType(id, type, paging) : logRepository.getLogsByUserUserId(id, paging);
        return logs;
    }

    /**
     * Get all the logs from the database
     * where the date of the log falls
     * between startingDate and endingDate
     *
     * @param startingDate
     * @param endingDate
     * @return
     */
    public List<Log> getAllLogsBetweenDate(LocalDate startingDate, LocalDate endingDate) {
        List<Log> logs = logRepository.getLogsByDateBetweenOrderByDateAsc(startingDate, endingDate);
        return logs;
    }

    public List<Log> getAllLogsSinceYesterdayDay() {
        LocalDate now = LocalDate.now();
        LocalDate oneMonthBefore = now.minusDays(1);
        List<Log> logs = logRepository.getLogsByDateBetweenOrderByDateAsc(oneMonthBefore, now);
        return logs;
    }

    /**
     * Get all the logs since
     * numdays ago
     *
     * @param numDays
     * @return list of logs
     */
    public List<Log> getAllLogsSinceNumDay(Long numDays) {
        LocalDate now = LocalDate.now();
        LocalDate oneMonthBefore = now.minusDays(numDays);
        List<Log> logs = logRepository.getLogsByDateBetweenOrderByDateAsc(oneMonthBefore, now);
        return logs;
    }

    public List<Log> getAllLogsSinceLastMonth() {
        LocalDate now = LocalDate.now();
        LocalDate oneMonthBefore = now.minusMonths(1);
        List<Log> logs = logRepository.getLogsByDateBetweenOrderByDateAsc(oneMonthBefore, now);
        return logs;
    }

    /**
     * Get all the logs since
     * numMonths ago
     *
     * @param numMonths
     * @return list of logs
     */
    public List<Log> getAllLogsSinceNumMonth(Long numMonths) {
        LocalDate now = LocalDate.now();
        LocalDate monthsBefore = now.minusMonths(numMonths);
        List<Log> logs = logRepository.getLogsByDateBetweenOrderByDateAsc(monthsBefore, now);
        return logs;
    }

    public List<Log> getAllLogsSinceLastYear() {
        LocalDate now = LocalDate.now();
        LocalDate monthsBefore = now.minusYears(1);
        List<Log> logs = logRepository.getLogsByDateBetweenOrderByDateAsc(monthsBefore, now);
        return logs;
    }

    /**
     * Get all the logs since
     * numYears ago
     *
     * @param numYears
     * @return list of logs
     */
    public List<Log> getAllLogsSinceNumYear(Long numYears) {
        LocalDate now = LocalDate.now();
        LocalDate monthsBefore = now.minusYears(numYears);
        List<Log> logs = logRepository.getLogsByDateBetweenOrderByDateAsc(monthsBefore, now);
        return logs;
    }

    /**
     * Get the total amount of income in
     * each month dating back numMonths including this month,
     * so total data points = numMonth + 1
     *
     * @param numMonth
     * @return List of data points of income of each month
     */
    public List<Pair<String, Double>> getMonthlyIncomeDataPoints(int numMonth) {
        LocalDate fromDate = LocalDate.now();
        LocalDate numMonthsBefore = fromDate.minusMonths(numMonth).withDayOfMonth(1);
        List<Pair<String, Double>> monthlyDataPoints = new ArrayList<>();
        List<Log> incomeLogs = logRepository.getLogsByDateBetweenOrderByDateAsc(numMonthsBefore, fromDate)
                .stream()
                .filter(
                        log -> log.getType() == LogType.INCOME
                )
                .collect(Collectors.toList());
        for (int i = 0; i < numMonth + 1; i++) {
            LocalDate monthBefore = numMonthsBefore.plusMonths(i);
            String monthYear = monthBefore.getMonthValue() + "-" + monthBefore.getYear();
            monthlyDataPoints.add(new Pair<>(monthYear, 0.0));
        }
        for (Log log : incomeLogs) {
            int indexConverted = numMonth - (fromDate.getYear() - log.getDate().getYear()) * 12 - (fromDate.getMonthValue() - log.getDate().getMonthValue());
            monthlyDataPoints.set(indexConverted, monthlyDataPoints.get(indexConverted).setAt1(monthlyDataPoints.get(indexConverted).getValue1() + log.getAmount()));
        }
        return monthlyDataPoints;
    }

    /**
     * Get the total amount of expense in
     * each month dating back numMonths including this month,
     * so total data points = numMonth + 1
     *
     * @param numMonth
     * @return List of data points of expense of each month
     */
    public List<Pair<String, Double>> getMonthlyExpenseDataPoints(int numMonth) {
        LocalDate fromDate = LocalDate.of(2001, 4, 15);
        LocalDate numMonthsBefore = fromDate.minusMonths(numMonth).withDayOfMonth(1);
        List<Pair<String, Double>> monthlyDataPoints = new ArrayList<>();
        List<Log> incomeLogs = logRepository.getLogsByDateBetweenOrderByDateAsc(numMonthsBefore, fromDate)
                .stream()
                .filter(
                        log -> log.getType() == LogType.EXPENSE
                )
                .collect(Collectors.toList());
        for (int i = 0; i < numMonth + 1; i++) {
            LocalDate monthBefore = numMonthsBefore.plusMonths(i);
            String monthYear = monthBefore.getMonthValue() + "-" + monthBefore.getYear();
            monthlyDataPoints.add(new Pair<>(monthYear, 0.0));
        }
        for (Log log : incomeLogs) {
            int indexConverted = numMonth - (fromDate.getYear() - log.getDate().getYear()) * 12 - (fromDate.getMonthValue() - log.getDate().getMonthValue());
            monthlyDataPoints.set(indexConverted, monthlyDataPoints.get(indexConverted).setAt1(monthlyDataPoints.get(indexConverted).getValue1() + log.getAmount()));
        }
        return monthlyDataPoints;
    }

    public void modifyLogNameByLogId(String newName, Long logId) {

        if (!sourceRepository.existsById(logId)) {
            throw new IllegalStateException("Log with id: " + logId + "does not exists");
        }

        logRepository.modifyLogNameByLogID(newName, logId);
    }

    public void modifyLogAmountByLogId(double newAmount, Long logId) {
        if (!logRepository.existsById(logId)) {
            throw new IllegalStateException("Log with id: " + logId + " does not exists");
        }
        logRepository.modifyLogAmountByLogId(newAmount, logId);
    }

    public void modifyLogNoteByLogId(String newNote, Long logId) {
        if (!logRepository.existsById(logId)) {
            throw new IllegalStateException("Log with id: " + logId + " does not exists");
        }
        logRepository.modifyLogNoteByLogId(newNote, logId);
    }

    public void modifyLogTypeByLogId(String newType, Long logId) {
        if (!logRepository.existsById(logId)) {
            throw new IllegalStateException("Log with id: " + logId + " does not exists");
        }
        logRepository.modifyLogTypeByLogId(newType, logId);
    }

    public void addLog(String logName, Double logAmount, LogType logType, Long sourceId, String logDate, Long userId, Long budgetId, String logNote) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalStateException("User with id: " + userId + " does not exists");
        }

        //Must have a valid source to add
        if (!sourceRepository.existsById(sourceId)) {
            throw new IllegalStateException("Source with id " + sourceId + " does not exists");
        }


        String[] dateString = logDate.split("-");
        int receivedYear = Integer.parseInt(dateString[0]);
        int receivedMonth = (Integer.parseInt(dateString[1]));
        int receivedDate = (Integer.parseInt(dateString[2]));


        User user = userRepository.getById(userId);
        Source logSource = sourceRepository.getById(sourceId);
        Budget logBudget = budgetRepository.getBudgetByBudgetId(budgetId);

        if (logBudget != null) {
            Double newAmount = logBudget.getCurrentAmount() + logAmount;
            budgetRepository.modifyBudgetCurrentAmountByBudgetId(newAmount, logBudget.getBudgetId());
        }

        Log newLog = Log.builder()
                .date(LocalDate.of(receivedYear, receivedMonth, receivedDate))
                .amount(logAmount)
                .name(logName)
                .user(user)
                .type(logType)
                .note(logNote)
                .source(logSource)
                .budget(logBudget)
                .build();

        if (logType.equals(LogType.INCOME)) {
            Retirement retirement = retirementRepository.getRetirementByUserUserId(userId);
            retirement.setAvailable(retirement.getAvailable() + logAmount);
            retirementRepository.updateAvailableByRetirementId(retirement.getAvailable(), retirement.getRetirementId());
        } else if (logType.equals(LogType.EXPENSE)) {
            Retirement retirement = retirementRepository.getRetirementByUserUserId(userId);
            retirement.setAvailable(retirement.getAvailable() - logAmount);
            retirementRepository.updateAvailableByRetirementId(retirement.getAvailable(), retirement.getRetirementId());
        }

//        if (logType == LogType.EXPENSE) {
//            BudgetSubBudget subBudget = subBudgetRepository.getSubBudgetBySourceId(sourceId);
//            subBudget.addExpense(newLog.getAmount());
//            subBudgetRepository.save(subBudget);
//        }

        logSource.increaseUsage();
        sourceRepository.save(logSource);
        logRepository.save(newLog);

    }

    public void deleteLogById(Long logId) {
        if (!logRepository.existsById(logId)) {
            throw new IllegalStateException("No log with id: " + logId + " exists");
        }

        Log log = logRepository.getById(logId);

        LogType logType = log.getType();
        double logAmount = log.getAmount();
        long userId = log.getUser().getUserId();

        if (logType.equals(LogType.INCOME)) {
            Retirement retirement = retirementRepository.getRetirementByUserUserId(userId);
            retirement.setAvailable(retirement.getAvailable() - logAmount);
            retirementRepository.updateAvailableByRetirementId(retirement.getAvailable(), retirement.getRetirementId());
        } else if (logType.equals(LogType.EXPENSE)) {
            Retirement retirement = retirementRepository.getRetirementByUserUserId(userId);
            retirement.setAvailable(retirement.getAvailable() + logAmount);
            retirementRepository.updateAvailableByRetirementId(retirement.getAvailable(), retirement.getRetirementId());
        }

        if (log.getBudget() != null) {
            Budget budget = budgetRepository.getBudgetByBudgetId(log.getBudget().getBudgetId());
            Double newAmount = budget.getCurrentAmount() - log.getAmount();
            budgetRepository.modifyBudgetCurrentAmountByBudgetId(newAmount, budget.getBudgetId());
        }

        Source source = log.getSource();
        source.decreaseUsage();
        sourceRepository.save(source);
        logRepository.deleteById(logId);
    }

    /*

        public void addLog(String logName, Double logAmount, LogType logType, Long sourceId, Date logDate, Long userId){
        if (!userRepository.existsById(userId)) {
            throw new IllegalStateException("User with id: " + userId + " does not exists");
        }

        //Must have a valid source to add
        if (!sourceRepository.existsById(sourceId)) {
            throw new IllegalStateException("Source with id " + sourceId + " does not exists");
        }

        User user = userRepository.getById(userId);
        Source logSource = sourceRepository.getById(sourceId);

        Log newlog = Log.builder()
                .date(LocalDate.of(logDate.getYear(), logDate.getMonth(), logDate.getMonth()))
                .amount(logAmount)
                .name(logName)
                .user(user)
                .type(logType)
                .source(logSource)
                .build();

        logRepository.save(newlog);
    }
     */

}
