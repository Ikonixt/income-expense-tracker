package project.io.muzoo.ssc.service;

import org.springframework.stereotype.Service;
import project.io.muzoo.ssc.budget.Budget;
import project.io.muzoo.ssc.budget.BudgetType;
import project.io.muzoo.ssc.repository.BudgetRepository;
import project.io.muzoo.ssc.repository.UserRepository;
import project.io.muzoo.ssc.user.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class BudgetService {

    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository;


    public BudgetService(UserRepository userRepository, BudgetRepository budgetRepository) {
        this.userRepository = userRepository;
        this.budgetRepository = budgetRepository;
    }

    /**
     * Add the budget to the database
     * by user id. If the user is not found
     * an exception is thrown
     * @param budget
     */
    public void addBudgetByUserId(Budget budget, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalStateException("User with id: " + userId + " does not exists");
        }
        User user = userRepository.findById(userId).get();
        budget.setUser(user);
        budgetRepository.save(budget);
    }

    /**
     * Retrieve the budgets of the user by id.
     * If the user is not found
     * an exception is thrown
     * @param id
     * @return A list of all user's budget
     */
    public List<Budget> getUserBudgetsByUserId(Long id) {

        if (!userRepository.existsById(id)) {
            throw new IllegalStateException("User with id: " + id + " does not exists!");
        }


        List<Budget> userBudgets = budgetRepository.getBudgetsByUserUserId(id);
        List<Budget> listToSend = new ArrayList<>();
        for(Budget b: userBudgets){
            if(b.isArchive()==false){
                listToSend.add(b);
            }
        }

        return listToSend;
    }

    public void archiveBudgetbyBudgetId(Long budgetId) {
        if (!budgetRepository.existsById(budgetId)) {
            throw new IllegalStateException("Budget with id: " + budgetId + "does not exists");
        }
        budgetRepository.archiveBudgetbyBudgetId(budgetId);
    }

    public Budget getBudgetByBudgetId(Long budgetId) {
        if (!budgetRepository.existsById(budgetId)) {
            throw new IllegalStateException("Budget with id: " + budgetId + " does not exists");
        }
        return budgetRepository.getBudgetByBudgetId(budgetId);
    }

    public void modifyBudgetNameByBudgetId(String newName, Long budgetId) {
        if (!budgetRepository.existsById(budgetId)) {
            throw new IllegalStateException("Budget with id: " + budgetId + "does not exists");
        }
        budgetRepository.modifyBudgetNameByBudgetId(newName, budgetId);
    }

    public void modifyBudgetTotalLimitByBudgetId(double newLimit, Long budgetId) {
        if (!budgetRepository.existsById(budgetId)) {
            throw new IllegalStateException("Budget with id: " + budgetId + "does not exists");
        }
        budgetRepository.modifyBudgetTotalLimitByBudgetId(newLimit, budgetId);
    }

    public void modifyBudgetCurrentAmountByBudgetId(Double newAmount, Long budgetId) {
        if (!budgetRepository.existsById(budgetId)) {
            throw new IllegalStateException("Budget with id: " + budgetId + "does not exists");
        }
        budgetRepository.modifyBudgetCurrentAmountByBudgetId(newAmount, budgetId);
    }

}
