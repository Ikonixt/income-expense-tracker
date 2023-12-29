package project.io.muzoo.ssc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import project.io.muzoo.ssc.models.RetirementModel;
import project.io.muzoo.ssc.retirement.Retirement;
import project.io.muzoo.ssc.service.RetirementService;
import project.io.muzoo.ssc.util.SimpleResponseDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@RestController
@RequestMapping(path = "api/v1/retirement")
public class RetirementController {

    private final RetirementService retirementService;

    @Autowired
    public RetirementController(RetirementService retirementService) {
        this.retirementService = retirementService;
    }

    @GetMapping(path = "/getuserretirement")
    public RetirementModel getUserRetirement(@RequestParam(name = "id") Long id) {
        Retirement retirement = retirementService.getUserRetirement(id);
        return RetirementModel
                .builder()
                .retirementId(retirement.getRetirementId())
                .available(retirement.getAvailable())
                .grandTotal(retirement.getGrandTotal())
                .monthlyTarget(retirement.getMonthlyTarget())
                .startDate(retirement.getStartDate())
                .retirementDate(retirement.getRetirementDate())
                .build();
    }

    @GetMapping(path = "/updateuserretirementplanner")
    public SimpleResponseDTO updateUserRetirementPlanner(@RequestParam(name = "id") Long id, @RequestParam(name = "grandTotal") Long grandTotal, @RequestParam(name = "retirementDate") String retirementDate) {
        try {
            // update the grand total
            retirementService.modifyRetirementGrandTotalByUserId(grandTotal, id);
            // update the retirement date
            retirementService.modifyRetirementRetirementDateByUserId(retirementDate, id);
            // calculate new monthly target
            LocalDate startDate = LocalDate.parse(retirementService.getUserRetirement(id).getStartDate());
            LocalDate endDate = LocalDate.parse(retirementDate);
            System.out.println(startDate);
            System.out.println(endDate);
            double periodInMonths = Math.max((endDate.getYear() - startDate.getYear()) * 12 + (endDate.getMonthValue() - startDate.getMonthValue()), 0);
            double monthlyTarget = grandTotal / periodInMonths;
            // update the monthly target
            retirementService.modifyRetirementMonthlyTargetByUserId(monthlyTarget, id);
            return SimpleResponseDTO
                    .builder()
                    .success(true)
                    .message("Retirement Grand Total has been successfully updated")
                    .build();
        } catch (Exception e) {
            return SimpleResponseDTO
                    .builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
        }
    }

}
