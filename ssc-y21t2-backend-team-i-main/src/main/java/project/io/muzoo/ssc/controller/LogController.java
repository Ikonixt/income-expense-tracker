package project.io.muzoo.ssc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import project.io.muzoo.ssc.log.Log;
import project.io.muzoo.ssc.log.LogType;
import project.io.muzoo.ssc.models.LogModel;
import project.io.muzoo.ssc.service.LogService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/log")
public class LogController {

    private final LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @PostMapping(path = "addLog",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void addNewLog(@RequestBody LogModel logModel) {
        logService.addLog(logModel.getLogName(), logModel.getLogAmount(), logModel.getLogType(), logModel.getSourceId(), logModel.getLogDate(), logModel.getUserId(), logModel.getBudgetId(), logModel.getLogNote());
    }

    @GetMapping(path = "getuserlog")
    public List<Log> getUserLog(@RequestParam(name = "id") Long id, @RequestParam(name = "type", required = false) LogType type, @RequestParam(name = "limit", required = false) Integer limit, @RequestParam(name = "sortBy", required = false) String sortBy) {
        List<Log> logList = logService.getLogsByUserID(id, type, limit, sortBy);
        return logList;
    }

    @PostMapping(path = "deleteLog")
    public void deleteLogById(@RequestParam(name = "id") Long id) {
        logService.deleteLogById(id);
    }

    @GetMapping(path = "getuserincomelog")
    public List<Log> getUserIncomeLog(@RequestParam(name = "id") Long id) {
        List<Log> log = logService.getLogsByUserID(id);
        return log.stream().filter(lg -> lg.getType().equals(LogType.INCOME)).collect(Collectors.toList());
    }

}
