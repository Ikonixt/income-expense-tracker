package project.io.muzoo.ssc.auth;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import project.io.muzoo.ssc.util.SimpleResponseDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthenticationController {

    @PostMapping("/api/login")
    public SimpleResponseDTO login(HttpServletRequest request){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // if there is a user currently logged in
            if (principal != null && principal instanceof org.springframework.security.core.userdetails.User) {
                // log that user out first
                request.logout();
            }
            request.login(username, password);
            return SimpleResponseDTO
                    .builder()
                    .success(true)
                    .message("Successfully logged in")
                    .build();
        } catch (ServletException e) {
            return SimpleResponseDTO
                    .builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
        }

    }

    @GetMapping("/api/logout")
    public SimpleResponseDTO logout(HttpServletRequest request){
        try {
            request.logout();
            return SimpleResponseDTO
                    .builder()
                    .success(true)
                    .message("Successfully logged out")
                    .build();

        } catch (ServletException e) {
            return SimpleResponseDTO
                    .builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
        }
    }

}
