package se.teamkugel.julhaxx

import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.stereotype.Component


@Configuration
@EnableWebSecurity
class WebSecurityConfig(val usersConfiguration: UsersConfiguration, val userRepository: UserRepository) : WebSecurityConfigurerAdapter() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/js/**", "/css/**", "/img/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .defaultSuccessUrl("/game")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    @Bean
    @DependsOn("userInit")
    public override fun userDetailsService(): UserDetailsService {
        System.out.println("Num Users" + userRepository.findAll().count())
        val users = userRepository.findAll().map {
            User.withDefaultPasswordEncoder()
                    .username(it.username)
                    .password(it.password)
                    .roles("USER")
                    .build()
        }
        return InMemoryUserDetailsManager(users)
    }
}

@Component
class AuthenticationListener : ApplicationListener<AuthenticationSuccessEvent?> {
    override fun onApplicationEvent(event: AuthenticationSuccessEvent) {
        System.out.println("Loggged in!!")
    }
}