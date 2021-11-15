package pl.kmiecik.holistech.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(AuthenticationManagerBuilder authenticationMgr) throws Exception {
        authenticationMgr.inMemoryAuthentication()
                .withUser("quality").password(passwordEncoder().encode("4321")).roles("QUALITY")
                .and()
                .withUser("tester").password(passwordEncoder().encode("1234")).roles("TESTER")
                .and()
                .withUser("user").password(passwordEncoder().encode("1")).roles("USER");

    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
      /*  http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/fixtures/**").hasRole("TESTER")
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);*/

        http.authorizeRequests()
                .antMatchers("/register", "/hello").permitAll()
                .antMatchers("/", "/**/fixtures").hasAnyRole("USER", "ADMIN", "TESTER", "QUALITY")
                .antMatchers("/admin").hasRole("ADMIN")
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .formLogin().defaultSuccessUrl("/fixtures", true)
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and()
                .httpBasic()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
