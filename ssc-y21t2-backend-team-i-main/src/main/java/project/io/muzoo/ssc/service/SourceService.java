package project.io.muzoo.ssc.service;

import org.springframework.stereotype.Service;
import project.io.muzoo.ssc.budget.BudgetSubBudget;
import project.io.muzoo.ssc.log.Source;
import project.io.muzoo.ssc.log.SourceType;
import project.io.muzoo.ssc.repository.SourceRepository;
import project.io.muzoo.ssc.repository.SubBudgetRepository;
import project.io.muzoo.ssc.repository.UserRepository;
import project.io.muzoo.ssc.user.User;

import java.util.List;

@Service
public class SourceService {

    private final UserRepository userRepository;
    private final SourceRepository sourceRepository;
    private final SubBudgetRepository subBudgetRepository;

    public SourceService(UserRepository userRepository, SourceRepository sourceRepository, SubBudgetRepository subBudgetRepository) {
        this.userRepository = userRepository;
        this.sourceRepository = sourceRepository;
        this.subBudgetRepository = subBudgetRepository;
    }

    public void deleteSourceBySourceId(Long id){
        if(!sourceRepository.existsById(id)){
            throw new IllegalStateException("Source does not exist");
        }
        Source source = sourceRepository.findSourceBySourceId(id);

        if(source.getSourceUsage()>0){
            System.out.println("Cannot delete source with more than 0 usage");
        }

        sourceRepository.deleteById(id);
    }

    public void modifySourceNameBySourceId(String newName, Long id, Long userId){
        if(!sourceRepository.existsById(id)){
            throw new IllegalStateException("Source does not exist");
        }

        List<Source> allSources = sourceRepository.getAllSourceNameByUserId(userId);
        for(Source s: allSources){
            if(s.getSourceName().equals(newName)){
                throw new IllegalStateException("Source name already exist");
            }
        }

        //Update subbudget
        Source source = sourceRepository.findSourceBySourceId(id);
        List<BudgetSubBudget> allSubBudget = subBudgetRepository.findSubBudgetBySourceId(id);
        for(BudgetSubBudget b: allSubBudget){
            if(b.getName().equals(source.getSourceName())){
                subBudgetRepository.modifySubBudgetNameByBudgetId(newName,b.getSubBudgetId());
            }
        }
        source.setSourceName(newName);
        //Update source
        sourceRepository.updateSourceNameBySourceId(newName, id);
    }

    public void addSource(String sourceName, Long userId,String sourceType){
        if (!userRepository.existsById(userId)) {
            throw new IllegalStateException("User with id: " + userId + " does not exists!");
        }

        List<Source> queriedSources = sourceRepository.getAllSourceNameByUserId(userId);
        if(queriedSources.size()>0){
            for(Source s: queriedSources){
                if(s.getSourceName() == sourceName && s.getUser().getUserId()==userId){
                    throw new IllegalStateException("Source name "+sourceName+" already exist");
                }
            }
        }

        User user = userRepository.getById(userId);
        if(sourceType.equals("INCOME")){
            sourceRepository.save(Source.builder().sourceType(SourceType.INCOME).sourceName(sourceName).sourceUsage(0).user(user).build());
        }
        else{
            sourceRepository.save(Source.builder().sourceType(SourceType.EXPENSE).sourceName(sourceName).sourceUsage(0).user(user).build());
        }
    }

    public void deleteSource(Long sourceId) {
        Source source = sourceRepository.findById(sourceId).get();
        if (source.getSourceUsage() > 0) {
            throw new IllegalStateException("Source usage is more than 0!");
        } else if (source.getSourceUsage() == 1) {
            subBudgetRepository.deleteBySourceId(sourceId);
        }
        sourceRepository.deleteBySourceId(sourceId);
    }

    public List<Source> getSourcesByUserID(Long id) {
        List<Source> sources = sourceRepository.findSourceByUserId(id);
        return sources;
    }

}
