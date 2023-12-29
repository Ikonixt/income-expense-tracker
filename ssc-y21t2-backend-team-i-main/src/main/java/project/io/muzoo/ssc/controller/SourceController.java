package project.io.muzoo.ssc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import project.io.muzoo.ssc.log.Log;
import project.io.muzoo.ssc.log.Source;
import project.io.muzoo.ssc.models.SourceModel;
import project.io.muzoo.ssc.service.SourceService;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/source")
public class SourceController {

    private final SourceService sourceService;

    @Autowired
    public SourceController(SourceService sourceService) {
        this.sourceService = sourceService;
    }

    @PostMapping(path = "addSource",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void addNewSource(@RequestBody SourceModel sourceModel) {
        sourceService.addSource(sourceModel.getSourceName(), sourceModel.getUserId(), sourceModel.getSourceType());
    }

    @GetMapping(path = "getusersources")
    public List<Source> getSourceList(@RequestParam(name = "id") Long id) {
        List<Source> sourceList = sourceService.getSourcesByUserID(id);
        return sourceList;
    }

    @PostMapping(path = "deletesource")
    public void deleteSourceById(@RequestParam(name = "id") Long id){
        sourceService.deleteSourceBySourceId(id);
    }

    @PostMapping(path = "renamesource")
    public void renameSourceById(@RequestParam(name = "id") Long id,@RequestParam(name = "userId") Long userId, @RequestParam(name = "newName") String newName){
        sourceService.modifySourceNameBySourceId(newName,id,userId);
    }

}
