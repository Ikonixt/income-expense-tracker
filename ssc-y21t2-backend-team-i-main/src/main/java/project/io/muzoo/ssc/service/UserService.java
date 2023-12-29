package project.io.muzoo.ssc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import project.io.muzoo.ssc.models.ChangeUserPasswordModel;
import project.io.muzoo.ssc.models.UserModel;
import project.io.muzoo.ssc.repository.*;
import project.io.muzoo.ssc.response.ChangeUserPasswordResponse;
import project.io.muzoo.ssc.retirement.Retirement;
import project.io.muzoo.ssc.user.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository;
    private final LogRepository logRepository;
    private final SubBudgetRepository subBudgetRepository;
    private final RetirementRepository retirementRepository;
    private final SourceRepository sourceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BudgetRepository budgetRepository, LogRepository logRepository, SubBudgetRepository subBudgetRepository, RetirementRepository retirementRepository, SourceRepository sourceRepository) {
        this.userRepository = userRepository;
        this.budgetRepository = budgetRepository;
        this.logRepository = logRepository;
        this.subBudgetRepository = subBudgetRepository;
        this.retirementRepository = retirementRepository;
        this.sourceRepository = sourceRepository;
    }

    /**
     * Find and return all the user in the database
     * @return
     */

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * Add new user into the database, if the username is already taken
     * then an exception is thrown.
     * @param toAdd
     */
    public void addNewUser(UserModel toAdd) {

        User user = userRepository.findUserByUsername(toAdd.getUsername());

        if (user != null) {
            throw new IllegalStateException("Username Already Taken");
        }

        User newUser = User
                .builder()
                .username(toAdd.getUsername())
                .password(passwordEncoder.encode(toAdd.getPassword()))
                .build();
        userRepository.save(newUser);

        //TODO confirm that this is working
        //Initialize retirement plan associated with user
        Retirement initialRetirement = Retirement
                .builder()
                .grandTotal(0)
                .available(0)
                .monthlyTarget(0)
                .startDate(LocalDate.now().toString())
                .retirementDate(LocalDate.now().plusMonths(20*12).toString())
                .user(user)
                .build();
        retirementRepository.save(initialRetirement);
    }

    /**
     * Delete the user base on id. If the user is not found
     * the method will throw an exception. Otherwise, the user
     * is deleted from the database.
     * @param userId
     */
    public void deleteUser(Long userId) {
        boolean exists = userRepository.existsById(userId);

        if (!exists) {
            throw new IllegalStateException("User with id: " + userId + " does not exists");
        }

        userRepository.deleteById(userId);
    }

    /**
     * Update the user based on username. If the user not found an exception is thrown.
     * Otherwise, the user password is updated
     * @param request
     * @return return a response to the frontend (To be test)
     */
    @PostMapping(path = "changeUserPassword")
    public ChangeUserPasswordResponse changeUserPassword(@RequestBody ChangeUserPasswordModel request){
        if (!userRepository.existsById((long) request.getId())){
            throw new IllegalStateException("User with id" + request.getId() + " does not exists!");
        }
        Optional<User> userPassword = userRepository.findUserPasswordByUsername(request.getUsername());
        if (request.getPassword().equals(userPassword)){
            String newPassword = request.getNewPassword();
            userRepository.updateUserPasswordByUsername(passwordEncoder.encode(newPassword));
        }
        ChangeUserPasswordResponse userResponse = new ChangeUserPasswordResponse();
        userResponse.setMessage("Change password success");
        return userResponse;
    }

    /**
     * Log Utilities methods
     */



    public void modifyLogNameByLogId(String newName, Long logId) {

        if (!sourceRepository.existsById(logId)) {
            throw new IllegalStateException("Log with id: " + logId + "does not exists");
        }

        logRepository.modifyLogNameByLogID(newName, logId);
    }

    public void modifyLogAmountByLogId(double newAmount, Long logId) {
        if (!logRepository.existsById(logId)) {
            throw new IllegalStateException("Log with id: " + logId + " does not exists");
        }
        logRepository.modifyLogAmountByLogId(newAmount, logId);
    }

    public void modifyLogNoteByLogId(String newNote, Long logId) {
        if (!logRepository.existsById(logId)) {
            throw new IllegalStateException("Log with id: " + logId + " does not exists");
        }
        logRepository.modifyLogNoteByLogId(newNote, logId);
    }

    public void modifyLogTypeByLogId(String newType, Long logId) {
        if (!logRepository.existsById(logId)) {
            throw new IllegalStateException("Log with id: " + logId + " does not exists");
        }
        logRepository.modifyLogTypeByLogId(newType, logId);
    }


}
