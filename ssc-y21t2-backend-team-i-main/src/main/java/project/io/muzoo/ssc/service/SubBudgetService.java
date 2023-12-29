package project.io.muzoo.ssc.service;

import org.springframework.stereotype.Service;
import project.io.muzoo.ssc.budget.Budget;
import project.io.muzoo.ssc.budget.BudgetSubBudget;
import project.io.muzoo.ssc.budget.SubBudget;
import project.io.muzoo.ssc.log.Source;
import project.io.muzoo.ssc.models.SubBudgetModel;
import project.io.muzoo.ssc.repository.BudgetRepository;
import project.io.muzoo.ssc.repository.SourceRepository;
import project.io.muzoo.ssc.repository.SubBudgetRepository;
import project.io.muzoo.ssc.repository.UserRepository;
import project.io.muzoo.ssc.user.User;

import java.util.List;

@Service
public class SubBudgetService {

    private final UserRepository userRepository;
    private final SubBudgetRepository subBudgetRepository;
    private final BudgetRepository budgetRepository;
    private final SourceRepository sourceRepository;

    public SubBudgetService(UserRepository userRepository, SubBudgetRepository subBudgetRepository, BudgetRepository budgetRepository, SourceRepository sourceRepository) {
        this.userRepository = userRepository;
        this.subBudgetRepository = subBudgetRepository;
        this.budgetRepository = budgetRepository;
        this.sourceRepository = sourceRepository;
    }

    public void addNewSubBudget(SubBudgetModel subBudgetModel) {
        Budget budget = budgetRepository.getById(subBudgetModel.getBudgetId());
        Source source = sourceRepository.getById(subBudgetModel.getSourceId());
        SubBudget subBudget = BudgetSubBudget.builder()
                .name(source.getSourceName())
                .allocation(subBudgetModel.getAllocation())
                .currentAmount(subBudgetModel.getCurrentAmount())
                .budget(budget)
                .source(source)
                .build();
        subBudgetRepository.save(subBudget);

//        source.increaseUsage();
        // No need to save source twice
//        sourceRepository.save(source);
    }

//    public void modifySubBudgetNameByBudgetId(String newName, Long subBudgetId) {
//        if (!subBudgetRepository.existsById(subBudgetId)) {
//            throw new IllegalStateException("SubBudget with id: " + subBudgetId + " does not exists");
//        }
//        subBudgetRepository.modifySubBudgetNameByBudgetId(newName, subBudgetId);
//    }

    public void modifySubBudgetAllocationByBudgetId(double newValue, Long subBudgetId) {
        if (!subBudgetRepository.existsById(subBudgetId)) {
            throw new IllegalStateException("SubBudget with id: " + subBudgetId + " does not exists");
        }
        subBudgetRepository.modifySubBudgetAllocationByBudgetId(newValue, subBudgetId);
    }

    public void modifySubBudgetCurrentAmountByBudgetId(double newValue, Long subBudgetId) {
        if (!subBudgetRepository.existsById(subBudgetId)) {
            throw new IllegalStateException("SubBudget with id: " + subBudgetId + " does not exists");
        }
        subBudgetRepository.modifySubBudgetCurrentAmountByBudgetId(newValue, subBudgetId);
    }

    public List<BudgetSubBudget> findSubBudgetByBudgetId(Long budgetId){
        return subBudgetRepository.findSubBudgetByBudgetId(budgetId);
    }

    public void deleteSubBudget(Long subBudgetId) {
        BudgetSubBudget subbudget = subBudgetRepository.getSubBudgetBySubBudgetId(subBudgetId);
        Budget budget = budgetRepository.getBudgetByBudgetId(subbudget.getBudget().getBudgetId());
        Double newBudgetCurrentAmount = budget.getCurrentAmount()-subbudget.getCurrentAmount();
        budgetRepository.modifyBudgetCurrentAmountByBudgetId(newBudgetCurrentAmount,budget.getBudgetId());
        subBudgetRepository.deleteBySubBudgetId(subBudgetId);
    }

}
