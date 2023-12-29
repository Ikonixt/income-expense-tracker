package project.io.muzoo.ssc.repository;

import org.hibernate.Hibernate;
import org.javatuples.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.io.muzoo.ssc.budget.*;
import project.io.muzoo.ssc.log.Log;
import project.io.muzoo.ssc.log.LogType;
import project.io.muzoo.ssc.log.Source;
import project.io.muzoo.ssc.retirement.Retirement;
import project.io.muzoo.ssc.user.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private SubBudgetRepository subBudgetRepository;
    @Autowired
    private RetirementRepository retirementRepository;
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private SourceRepository sourceRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testAll() {
        saveUser();
//        findUserByUsername();
//        saveBudget();
//        updateUsernameByUsername();
//        getBudgetsByUserId();
//        addSubBudget();
//        addRetirement();
//        getUserRetirementById();
//        addSubBudgetToRetirement();
//        addLogTest();
        // insertIncomeLogs();
        // insertExpenseLogs();
    }

    void saveUser() {
        User test = new User().builder()
                .username("test")
                .password(passwordEncoder.encode("test"))
                .build();
        userRepository.saveAll(List.of(test));
    }

    void findUserByUsername() {
        User user = userRepository.findUserByUsername("test");
        System.out.println(user);
    }

    void saveBudget() {
        User user = userRepository.findUserByUsername("test");
        System.out.println("User id is " + user.getUserId());
        Budget pusheenBudget = Budget.builder().name("Pusheen")
                .budgetType(BudgetType.SIMPLE)
                .user(user)
                .build();
        Budget foodBudget = Budget.builder()
                .name("Lunch")
                .budgetType(BudgetType.SIMPLE)
                .user(user
                ).build();
        budgetRepository.saveAll(List.of(pusheenBudget, foodBudget));
    }

    void updateUsernameByUsername() {
        userRepository.updateUsernameByUsername("testChanged", "test");
    }

    void getBudgetsByUserId() {
        List<Budget> budgets = budgetRepository.getBudgetsByUserUserId(1L);
        System.out.println(budgets);
    }

    @Test
    void addSubBudget() {
        User user = userRepository.findById(1L).get();
        List<Budget> budgets = budgetRepository.getBudgetsByUserUserId(user.getUserId());
        SubBudget subBudget = BudgetSubBudget.builder()
           .name("A2")
           .allocation(10000)
           .currentAmount(5000)
           .budget(budgets.get(0))
           .source(sourceRepository.findAll().get(0))
           .build();
        subBudgetRepository.save(subBudget);
    }

    void addRetirement() {
        User user = userRepository.findById(1L).get();
        Retirement retirement = Retirement
                .builder().grandTotal(100000)
                .available(100000)
                .user(user)
                .build();
        retirementRepository.save(retirement);
    }

    void getUserRetirementById() {
        User user = userRepository.findById(1L).get();
        System.out.println(retirementRepository.getRetirementByUserUserId(user.getUserId()));
    }

    void addSubBudgetToRetirement() {
        User user = userRepository.findById(1L).get();
        Retirement retirement = retirementRepository.getRetirementByUserUserId(user.getUserId());
        SubBudget subBudget = RetirementSubBudget.builder()
                .name("50")
                .allocation(20000)
                .currentAmount(0)
                .retirement(retirement)
                .build();
        subBudgetRepository.save(subBudget);
    }

    @Test
    void allocateRetirementMoneyIntoCurrentAmountTest() {
        RetirementSubBudget retirementSubBudget = (RetirementSubBudget) Hibernate.unproxy(subBudgetRepository.getById(2L));
        if (retirementSubBudget.allocateMoneyIntoCurrentAmountFromAvailable(50000)) {
            subBudgetRepository.save(retirementSubBudget);
            retirementRepository.save(retirementSubBudget.getRetirement());
            return;
        }
        System.out.println("Not enough money in available");
    }

    @Test
    void getSubBudgets() {
        for (SubBudget budget : subBudgetRepository.findAll()) {
            System.out.println(budget);
            System.out.println(budget.getSubBudgetId());
        }
    }

    @Test
    void insertIncomeLogs() {
        User user = userRepository.findById(1L).get();
        Log log = Log.builder()
                .date(LocalDate.of(2001, 1, 1))
                .amount(1000)
                .name("Salary")
                .user(user)
                .type(LogType.INCOME)
                .source(Source.builder().build())
                .build();

        Log log1 = Log.builder()
                .date(LocalDate.of(2003, 1, 1))
                .amount(10000)
                .name("New year bonus")
                .user(user)
                .type(LogType.INCOME)
                .source(Source.builder().build())
                .build();

        Log log2 = Log.builder()
                .date(LocalDate.of(2040, 1, 1))
                .amount(233.5)
                .name("Side Job")
                .user(user)
                .type(LogType.INCOME)
                .source(Source.builder().build())
                .build();

        Log log3 = Log.builder()
                .date(LocalDate.of(2001, 3, 2))
                .amount(1500)
                .name("Side Job 2")
                .user(user)
                .type(LogType.INCOME)
                .source(Source.builder().build())
                .build();

        Log log4 = Log.builder()
                .date(LocalDate.of(2001, 1, 15))
                .amount(3000)
                .name("Side Job 3")
                .user(user)
                .type(LogType.INCOME)
                .source(Source.builder().build())
                .build();

        Log log5 = Log.builder()
                .date(LocalDate.of(2001, 4, 12))
                .amount(2000)
                .name("Side Job 4")
                .user(user)
                .type(LogType.INCOME)
                .source(Source.builder().build())
                .build();

        Log log6 = Log.builder()
                .date(LocalDate.of(2000, 12, 12))
                .amount(553.3)
                .name("Side Job 5")
                .user(user)
                .type(LogType.INCOME)
                .source(Source.builder().build())
                .build();

        Log log7 = Log.builder()
                .date(LocalDate.of(1999, 11, 20))
                .amount(1999.1120)
                .name("Side Job 6")
                .user(user)
                .type(LogType.INCOME)
                .source(Source.builder().build())
                .build();

        System.out.println("Log Date: " + log.getDate());
        logRepository.saveAll(List.of(log, log1, log2, log3, log4, log5, log6, log7));
    }

    void insertExpenseLogs() {
        User user = userRepository.findById(1L).get();
        Log log = Log.builder()
                .date(LocalDate.of(2001, 1, 1))
                .amount(1000)
                .name("Pusheen")
                .user(user)
                .type(LogType.EXPENSE)
                .source(Source.builder().build())
                .build();

        Log log1 = Log.builder()
                .date(LocalDate.of(2003, 1, 1))
                .amount(10000)
                .name("More Pusheen")
                .user(user)
                .type(LogType.EXPENSE)
                .source(Source.builder().build())
                .build();

        Log log2 = Log.builder()
                .date(LocalDate.of(2040, 1, 1))
                .amount(233.5)
                .name("Food")
                .user(user)
                .type(LogType.EXPENSE)
                .source(Source.builder().build())
                .build();

        Log log3 = Log.builder()
                .date(LocalDate.of(2001, 3, 2))
                .amount(1500)
                .name("Travel")
                .user(user)
                .type(LogType.EXPENSE)
                .source(Source.builder().build())
                .build();

        Log log4 = Log.builder()
                .date(LocalDate.of(2001, 1, 15))
                .amount(3000)
                .name("Gaming")
                .user(user)
                .type(LogType.EXPENSE)
                .source(Source.builder().build())
                .build();

        Log log5 = Log.builder()
                .date(LocalDate.of(2001, 4, 12))
                .amount(20000)
                .name("New Computer")
                .user(user)
                .type(LogType.EXPENSE)
                .source(Source.builder().build())
                .build();

        Log log6 = Log.builder()
                .date(LocalDate.of(2000, 12, 12))
                .amount(666)
                .name("Headphones")
                .user(user)
                .type(LogType.EXPENSE)
                .source(Source.builder().build())
                .build();

        Log log7 = Log.builder()
                .date(LocalDate.of(1999, 11, 20))
                .amount(1999.1120)
                .name("Even more pusheen")
                .user(user)
                .type(LogType.EXPENSE)
                .source(Source.builder().build())
                .build();

        logRepository.saveAll(List.of(log, log1, log2, log3, log4, log5, log6, log7));
    }

    @Test
    void getAllLogs() {
        LocalDate startingDate = LocalDate.of(2000, 1, 1);
        LocalDate endingDate = LocalDate.of(2040, 1, 1);
        for (Log log : logRepository.getLogsByDateBetweenOrderByDateAsc(startingDate, endingDate)) {
            System.out.println(log);
            System.out.println(log.getLogId());
        }
    }

    @Test
    void getMonthlyIncomeTest() {
        int numMonth = 30;
        LocalDate fromDate = LocalDate.of(2001, 4, 15);
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
        for (Log log: incomeLogs) {
            int indexConverted = numMonth - (fromDate.getYear() - log.getDate().getYear()) * 12 - (fromDate.getMonthValue() - log.getDate().getMonthValue());
            monthlyDataPoints.set(indexConverted, monthlyDataPoints.get(indexConverted).setAt1(monthlyDataPoints.get(indexConverted).getValue1() + log.getAmount()));
        }
        System.out.println(monthlyDataPoints);
    }

    @Test
    void getMonthlyExpenseTest() {
        int numMonth = 4;
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
        for (Log log: incomeLogs) {
            int indexConverted = numMonth - (fromDate.getYear() - log.getDate().getYear()) * 12 - (fromDate.getMonthValue() - log.getDate().getMonthValue());
            monthlyDataPoints.set(indexConverted, monthlyDataPoints.get(indexConverted).setAt1(monthlyDataPoints.get(indexConverted).getValue1() + log.getAmount()));
        }
        System.out.println(monthlyDataPoints);
    }


    @Test
    void addLogTest() {

        Long userId = 1L;

        User user = userRepository.findById(userId).get();

        Source source = Source.builder()
                .sourceName("Luxurious Food")
                .sourceUsage(0)
                .user(user)
                .build();

        Budget budget = Budget.builder()
                .budgetType(BudgetType.SIMPLE)
                .name("Food")
                .user(user)
                .totalLimit(20000)
                .build();

        sourceRepository.save(source);
        budgetRepository.save(budget);

        BudgetSubBudget budgetSubBudget = BudgetSubBudget.builder()
                .name("Luxury Food 2")
                .allocation(10000)
                .currentAmount(2000)
                .budget(budget)
                .source(source)
                .build();

        source.increaseUsage();
        sourceRepository.save(source);
        subBudgetRepository.save(budgetSubBudget);

        double logAmount = 10000;
        String logName = "Omakase";
        LogType logType = LogType.EXPENSE;

        System.out.println("Log Source " + source);

        Log newLog = Log.builder()
                .date(LocalDate.of(2000, 10, 10))
                .amount(logAmount)
                .name(logName)
                .user(user)
                .type(logType)
                .source(source)
                .budget(budget)
                .build();

        Long sourceId = source.getSourceId();
        Source source1 = sourceRepository.getById(sourceId);
        if (logType == LogType.EXPENSE) {
            BudgetSubBudget subBudget = subBudgetRepository.getSubBudgetBySourceId(sourceId);
            subBudget.addExpense(newLog.getAmount());
            subBudgetRepository.save(subBudget);
        }

        source1.increaseUsage();
        sourceRepository.save(source1);
        logRepository.save(newLog);
    }

    @Test
    void deleteLog() {
        Long logId = 1L;
        if (!logRepository.existsById(logId)) {
            throw new IllegalStateException("No log with id: " + logId + " exists");
        }
        Log log = logRepository.getById(logId);
        Source source = log.getSource();
        source.decreaseUsage();
        sourceRepository.save(source);
        logRepository.deleteById(logId);
    }

    @Test
    void deleteSubBudgetBySource() {
        Long sourceId = 2L;
        Source source = sourceRepository.getById(sourceId);
        source.decreaseUsage();
        sourceRepository.save(source);
        subBudgetRepository.deleteBySourceId(sourceId);
    }

    @Test
    void deleteSource() {
        Long sourceId = 2L;
        Source source = sourceRepository.findById(sourceId).get();
        if (source.getSourceUsage() > 0) {
            throw new IllegalStateException("Source usage is more than 0!");
        }
        sourceRepository.deleteBySourceId(sourceId);
    }

    @Test
    void deleteSubBudget() {
        subBudgetRepository.deleteBySubBudgetId(3L);
    }


}