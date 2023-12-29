package project.io.muzoo.ssc.service;

import org.springframework.stereotype.Service;
import project.io.muzoo.ssc.budget.RetirementSubBudget;
import project.io.muzoo.ssc.models.RetirementModel;
import project.io.muzoo.ssc.repository.RetirementRepository;
import project.io.muzoo.ssc.repository.SubBudgetRepository;
import project.io.muzoo.ssc.repository.UserRepository;
import project.io.muzoo.ssc.retirement.Retirement;
import project.io.muzoo.ssc.user.User;

@Service
public class RetirementService {

    private final UserRepository userRepository;
    private final RetirementRepository retirementRepository;
    private final SubBudgetRepository subBudgetRepository;

    public RetirementService(UserRepository userRepository, RetirementRepository retirementRepository, SubBudgetRepository subBudgetRepository) {
        this.userRepository = userRepository;
        this.retirementRepository = retirementRepository;
        this.subBudgetRepository = subBudgetRepository;
    }

    public void addRetirement(RetirementModel retirementModel, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalStateException("No user with id: " + userId + "!");
        }
        User user = userRepository.getById(userId);
        if (retirementRepository.getRetirementByUserUserId(userId) != null) {
            throw new IllegalStateException("User already have a retirement plan!");
        }
        Retirement retirement = Retirement.builder()
                .user(user)
                .grandTotal(retirementModel.getGrandTotal())
                .available(0)
                .build();

        retirementRepository.save(retirement);
    }

    public void modifyRetirementGrandTotalByUserId(Long amountToChange, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalStateException("User with id: " + userId + " does not exists");
        }
        Retirement retirement = retirementRepository.getRetirementByUserUserId(userId);
        retirement.setGrandTotal(amountToChange);
        retirementRepository.updateGrandTotalByRetirementId(amountToChange, retirement.getRetirementId());
    }

    public void modifyRetirementMonthlyTargetByUserId(Double amountToChange, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalStateException("User with id: " + userId + " does not exists");
        }
        Retirement retirement = retirementRepository.getRetirementByUserUserId(userId);
        retirement.setMonthlyTarget(amountToChange);
        retirementRepository.updateMonthlyTargetByRetirementId(amountToChange, retirement.getRetirementId());
    }

    public void modifyRetirementRetirementDateByUserId(String dateToChange, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalStateException("User with id: " + userId + " does not exists");
        }
        Retirement retirement = retirementRepository.getRetirementByUserUserId(userId);
        retirement.setRetirementDate(dateToChange);
        retirementRepository.updateRetirementDateByRetirementId(dateToChange, retirement.getRetirementId());
    }

    public boolean allocateRetirementMoneyFromAvailableBySubBudgetId(double amount, Long subBudgetId) {
        if (!subBudgetRepository.existsById(subBudgetId)) {
            throw new IllegalStateException("SubBudget with id: " + subBudgetId + " does not exists");
        }
        RetirementSubBudget retirementSubBudget = (RetirementSubBudget) subBudgetRepository.getById(subBudgetId);
        if (retirementSubBudget.allocateMoneyIntoCurrentAmountFromAvailable(amount)) {
            subBudgetRepository.save(retirementSubBudget);
            retirementRepository.save(retirementSubBudget.getRetirement());
        }
        return false;
    }

    public Retirement getUserRetirement(Long userId){
        if (!userRepository.existsById(userId)) {
            throw new IllegalStateException("User with id: " + userId + " does not exists");
        }
        return retirementRepository.getRetirementByUserUserId(userId);
    }

}
