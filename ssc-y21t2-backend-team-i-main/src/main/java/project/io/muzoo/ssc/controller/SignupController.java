package project.io.muzoo.ssc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import project.io.muzoo.ssc.repository.RetirementRepository;
import project.io.muzoo.ssc.repository.UserRepository;
import project.io.muzoo.ssc.retirement.Retirement;
import project.io.muzoo.ssc.user.User;
import project.io.muzoo.ssc.util.SimpleResponseDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@RestController
public class SignupController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RetirementRepository retirementRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/api/signup")
    public SimpleResponseDTO signup(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = userRepository.findUserByUsername(username);
        try {
            // check whether the username has already been taken or not
            if (user != null) {
                // if so, throw exception
                throw new ServletException(String.format("username %s has already been taken", username));
            } else {
                // otherwise, create new user
                user = new User();
                user.setUsername(username);
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
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
                return SimpleResponseDTO
                        .builder()
                        .success(true)
                        .message(String.format("user %s has been created successfully", username))
                        .build();
            }
        } catch (ServletException e) {
            return SimpleResponseDTO
                    .builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
        }
    }

}
