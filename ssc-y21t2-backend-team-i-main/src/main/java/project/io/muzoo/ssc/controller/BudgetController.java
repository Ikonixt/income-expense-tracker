package project.io.muzoo.ssc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import project.io.muzoo.ssc.budget.Budget;
import project.io.muzoo.ssc.budget.BudgetType;
import project.io.muzoo.ssc.models.BudgetModel;
import project.io.muzoo.ssc.repository.BudgetRepository;
import project.io.muzoo.ssc.service.BudgetService;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/budget")
public class BudgetController {

    private final BudgetService budgetService;

    @Autowired
    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping(path = "addBudget",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void addNewBudget(@RequestBody BudgetModel budgetModel) {
        if (budgetModel.getBudgetType().equals("Simple")){
            Budget newBudget = Budget.builder().name(budgetModel.getBudgetName()).budgetType(BudgetType.SIMPLE).totalLimit(budgetModel.getBudgetLimit()).currentAmount(0).archive(false).build();
            budgetService.addBudgetByUserId(newBudget,budgetModel.getUserId());
        }
        else {
            Budget newBudget = Budget.builder().name(budgetModel.getBudgetName()).budgetType(BudgetType.COMPLEX).totalLimit(budgetModel.getBudgetLimit()).currentAmount(0).archive(false).build();
            budgetService.addBudgetByUserId(newBudget,budgetModel.getUserId());
        }
    }


    @GetMapping(path = "/getuserbudget")
    public List<Budget> getUserBudget(@RequestParam(name = "id") Long id) {
        List<Budget> budgetList = budgetService.getUserBudgetsByUserId(id);
        return budgetList;
    }

    @PostMapping(path = "archivebudget")
    public void archiveBudgetById(@RequestParam(name = "id") Long id){
        budgetService.archiveBudgetbyBudgetId(id);
    }

    @PostMapping(path = "reallocate")
    public void modifyBudgetAllocationById(@RequestParam(name = "id") Long id, @RequestParam(name = "alloc") Double newAlloc){
        budgetService.modifyBudgetTotalLimitByBudgetId(newAlloc,id);
    }

}
