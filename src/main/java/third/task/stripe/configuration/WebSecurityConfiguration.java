package third.task.stripe.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import third.task.stripe.security.jwt.JwtAuthEntryPoint;
import third.task.stripe.security.jwt.JwtAuthTokenFilter;
import third.task.stripe.security.jwt.JwtProvider;
import third.task.stripe.security.services.CustomAuthenticationProvider;
import third.task.stripe.service.CustomerDetailServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final CustomAuthenticationProvider customAuthProvider;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final JwtProvider tokenProvider;
    private final CustomerDetailServiceImpl customerDetailsService;

    public WebSecurityConfiguration(JwtProvider tokenProvider, CustomerDetailServiceImpl customerDetailsService, JwtAuthEntryPoint jwtAuthEntryPoint, CustomAuthenticationProvider customAuthProvider) {
        this.tokenProvider = tokenProvider;
        this.customerDetailsService = customerDetailsService;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
        this.customAuthProvider = customAuthProvider;
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthTokenFilter(tokenProvider, customerDetailsService);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customAuthProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/api/customer/**").permitAll()
                .antMatchers("/**").authenticated()
                .and().cors()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthEntryPoint)
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.csrf().disable();
    }

}

