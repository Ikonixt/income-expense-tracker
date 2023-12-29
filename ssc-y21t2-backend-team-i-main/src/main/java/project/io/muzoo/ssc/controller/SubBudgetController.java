package project.io.muzoo.ssc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import project.io.muzoo.ssc.budget.Budget;
import project.io.muzoo.ssc.budget.BudgetSubBudget;
import project.io.muzoo.ssc.budget.BudgetType;
import project.io.muzoo.ssc.budget.SubBudget;
import project.io.muzoo.ssc.models.BudgetModel;
import project.io.muzoo.ssc.models.SubBudgetModel;
import project.io.muzoo.ssc.service.SubBudgetService;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/subbudget")
public class SubBudgetController {

    private final SubBudgetService subBudgetService;

    @Autowired
    public SubBudgetController(SubBudgetService subBudgetService) {
        this.subBudgetService = subBudgetService;
    }

    @GetMapping(path = "/getsubbudgetforbudget")
    public List<BudgetSubBudget> getSubBudgets(@RequestParam(name = "id") Long id) {
        List<BudgetSubBudget> subBudgets = subBudgetService.findSubBudgetByBudgetId(id);
        return subBudgets;
    }

    @PostMapping(path = "addsubbudget",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void addNewSubBudget(@RequestBody SubBudgetModel subBudgetModel) {
        subBudgetService.addNewSubBudget(subBudgetModel);
    }

    @PostMapping(path = "deletesubbudget")
    public void deleteLogById(@RequestParam(name = "id") Long id){
        subBudgetService.deleteSubBudget(id);
    }

    @PostMapping(path = "reallocate")
    public void modifySubBudgetAllocationById(@RequestParam(name = "id") Long id, @RequestParam(name = "alloc") Double newAlloc){
        subBudgetService.modifySubBudgetAllocationByBudgetId(newAlloc,id);
    }
}
