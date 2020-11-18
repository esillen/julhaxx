package se.teamkugel.julhaxx

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import java.lang.Thread.sleep


@Configuration
@EnableWebSecurity
@ComponentScan("se.teamkugel.julhaxx")
class WebSecurityConfig(val usersConfiguration: UsersConfiguration, val userRepository: UserRepository) : WebSecurityConfigurerAdapter() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .csrf()
                    .disable()
                .authorizeRequests()
                    .antMatchers("/", "/home").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                .logout()
                    .permitAll()
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