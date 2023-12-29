package project.io.muzoo.ssc.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.io.muzoo.ssc.budget.Budget;
import project.io.muzoo.ssc.budget.SubBudget;
import project.io.muzoo.ssc.log.Source;
import project.io.muzoo.ssc.user.User;

import java.util.List;

@SpringBootTest
class BudgetRepositoryTest {

    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubBudgetRepository subBudgetRepository;
    @Autowired
    private SourceRepository sourceRepository;

    @Test
    void saveBudget() {
         // User user = userRepository.findUserByUsername("test").get();
         // System.out.println("User id is " + user.getUserId());
         // Budget pusheenBudget = Budget.builder().name("Pusheen")
         //         .type("Custom")
         //         .user(user)
         //         .build();
         // Budget foodBudget = Budget.builder()
         //         .name("Lunch")
         //         .type("Food")
         //         .user(user
         //         ).build();
         // budgetRepository.saveAll(List.of(pusheenBudget, foodBudget));
//        Source s = Source.builder().sourceName("Transportation").sourceUsage(0).build();
//        sourceRepository.saveAll(List.of(s));
    }

    @Test
    void createSubBudget(){
        // SubBudget testBudget1 = SubBudget.builder().allocation(5000).currentAmount(10).build();
        // subBudgetRepository.saveAll(List.of(testBudget1));
    }

    @Test
    void printAllBudgets() {
        List<Budget> budgets = budgetRepository.findAll();
        System.out.println(budgets);
    }

}