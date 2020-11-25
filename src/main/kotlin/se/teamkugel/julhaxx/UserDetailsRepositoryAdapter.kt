package se.teamkugel.julhaxx

import org.springframework.security.core.userdetails.User

class UserDetailsRepositoryAdapter(private val userRepository: UserRepository) : IUserDetailsRepo {
    override fun get(username: String): MattesUserDetailsManager.MutableUser {
        val user = userRepository.findByUsername(username)
        val springUser = User.withDefaultPasswordEncoder()
                .username(user!!.username)
                .password(user.password)
                .roles("USER")
                .build()
        return MattesUserDetailsManager.MutableUser(springUser)
    }

    override fun put(key: String, mutableUser: MattesUserDetailsManager.MutableUser) {
        // TODO Auto-generated method stub
    }

    override fun remove(key: String) {
        // TODO Auto-generated method stub
    }

    override fun containsKey(key: String): Boolean {
        return userRepository.findByUsername(key) != null
    }
}