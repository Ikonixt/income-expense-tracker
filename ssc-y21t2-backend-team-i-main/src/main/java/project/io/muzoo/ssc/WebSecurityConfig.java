package project.io.muzoo.ssc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import project.io.muzoo.ssc.auth.OurUserDetailsService;
import project.io.muzoo.ssc.util.AjaxUtils;
import project.io.muzoo.ssc.util.SimpleResponseDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private OurUserDetailsService ourUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // configure security for REST backend apis
        // disable csrf
        http.csrf().disable();
        // permit root and logout/login apis
        // whoami api should be accessible without login
        http.authorizeRequests()
                .antMatchers("/", "/api/signup", "/api/login", "/api/logout", "/api/whoami").permitAll();
        // permit all option requests
        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll();
        // output error as json
        http.exceptionHandling()
                .authenticationEntryPoint(new JsonHttp403Forbidden());
        // set every other path to require authentication
        http.authorizeRequests().antMatchers("/**").authenticated();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    public UserDetailsService userDetailsService() {
        return ourUserDetailsService;
    }

    class JsonHttp403Forbidden implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request,
                             HttpServletResponse response,
                             AuthenticationException authException) throws IOException, ServletException {

            String ajaxJson = AjaxUtils.convertToString(
                    SimpleResponseDTO
                            .builder()
                            .success(false)
                            .message("Forbidden")
                            .build());

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().println(ajaxJson);
        }
    }

}
