package project.io.muzoo.ssc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import project.io.muzoo.ssc.models.WhoamiModel;
import project.io.muzoo.ssc.repository.UserRepository;
import project.io.muzoo.ssc.user.User;

@RestController
public class WhoamiController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/api/whoami")
    public WhoamiModel whoami() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null && principal instanceof org.springframework.security.core.userdetails.User) {
                org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) principal;
                User u = userRepository.findUserByUsername(user.getUsername());
                return WhoamiModel.builder()
                        .loggedIn(true)
                        .name(u.getUsername())
                        .role("USER")
                        .username(u.getUsername())
                        .userId(u.getUserId())
                        .build();
            }
        } catch (Exception e) {
            System.out.println("WHOAMI exception");
        }
        return WhoamiModel.builder()
                .loggedIn(false)
                .build();
    }
}
