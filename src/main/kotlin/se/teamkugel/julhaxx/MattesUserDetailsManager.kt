package se.teamkugel.julhaxx

import org.apache.commons.logging.LogFactory
import org.springframework.core.log.LogMessage
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.SpringSecurityCoreVersion
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsPasswordService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.core.userdetails.memory.UserAttribute
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.util.Assert

/**
 * Non-persistent implementation of `UserDetailsManager` which is backed by an
 * in-memory map.
 *
 *
 * Mainly intended for testing and demonstration purposes, where a full blown persistent
 * system isn't required.
 *
 * @author Luke Taylor
 * @since 3.1
 */
class MattesUserDetailsManager(  //	private final IUserDetailsRepo users = new IUserDetailsRepo() {
        //		private final Map<String, MutableUser> users = new HashMap<>();
        //		
        //		@Override
        //		public MutableUser get(String username) {
        //			return users.get(username);
        //		}
        //		
        //		@Override
        //		public boolean containsKey(String key) {
        //			return users.containsKey(key);
        //		}
        //		
        //		@Override
        //		public void put(String key, MutableUser mutableUser) {
        //			users.put(key, mutableUser);
        //		}
        //
        //		@Override
        //		public void remove(String key) {
        //			users.remove(key);
        //		}
        //	};
        private val users: IUserDetailsRepo) : UserDetailsManager, UserDetailsPasswordService {
    protected val logger = LogFactory.getLog(javaClass)
    private var authenticationManager: AuthenticationManager? = null

    //
    //	public MattesUserDetailsManager(Collection<UserDetails> users) {
    //		for (UserDetails user : users) {
    //			createUser(user);
    //		}
    //	}
    //
    //	public MattesUserDetailsManager(UserDetails... users) {
    //		for (UserDetails user : users) {
    //			createUser(user);
    //		}
    //	}
    //
    //	public MattesUserDetailsManager(Properties users) {
    //		Enumeration<?> names = users.propertyNames();
    //		UserAttributeEditor editor = new UserAttributeEditor();
    //		while (names.hasMoreElements()) {
    //			String name = (String) names.nextElement();
    //			editor.setAsText(users.getProperty(name));
    //			UserAttribute attr = (UserAttribute) editor.getValue();
    //			createUser(createUserDetails(name, attr));
    //		}
    //	}
    private fun createUserDetails(name: String, attr: UserAttribute): User {
        return User(name, attr.password, attr.isEnabled, true, true, true, attr.authorities)
    }

    override fun createUser(user: UserDetails) {
        Assert.isTrue(!userExists(user.username), "user should not exist")
        users.put(user.username.toLowerCase(), MutableUser(user))
    }

    override fun deleteUser(username: String) {
        users.remove(username.toLowerCase())
    }

    override fun updateUser(user: UserDetails) {
        Assert.isTrue(userExists(user.username), "user should exist")
        users.put(user.username.toLowerCase(), MutableUser(user))
    }

    override fun userExists(username: String): Boolean {
        return users.containsKey(username.toLowerCase())
    }

    override fun changePassword(oldPassword: String, newPassword: String) {
        val currentUser = SecurityContextHolder.getContext().authentication
                ?: // This would indicate bad coding somewhere
                throw AccessDeniedException(
                        "Can't change password as no Authentication object found in context " + "for current user.")
        val username = currentUser.name
        logger.debug(LogMessage.format("Changing password for user '%s'", username))
        // If an authentication manager has been set, re-authenticate the user with the
        // supplied password.
        if (authenticationManager != null) {
            logger.debug(LogMessage.format("Reauthenticating user '%s' for password change request.", username))
            authenticationManager!!.authenticate(UsernamePasswordAuthenticationToken(username, oldPassword))
        } else {
            logger.debug("No authentication manager set. Password won't be re-checked.")
        }
        val user = users[username]
        Assert.state(user != null, "Current user doesn't exist in database.")
        user!!.password = newPassword
    }

    override fun updatePassword(user: UserDetails, newPassword: String): UserDetails {
        val username = user.username
        val mutableUser = users[username.toLowerCase()]
        mutableUser!!.password = newPassword
        return mutableUser
    }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = users[username] ?: throw UsernameNotFoundException(username)
        return User(user.username, user.password, user.isEnabled, user.isAccountNonExpired,
                user.isCredentialsNonExpired, user.isAccountNonLocked, user.authorities)
    }

    fun setAuthenticationManager(authenticationManager: AuthenticationManager?) {
        this.authenticationManager = authenticationManager
    }

    /**
     * @author Luke Taylor
     * @since 3.1
     */
    class MutableUser internal constructor(private val delegate: UserDetails) : UserDetails {
        private var password: String
        override fun getPassword(): String {
            return password
        }

        fun setPassword(password: String) {
            this.password = password
        }

        override fun getAuthorities(): Collection<GrantedAuthority?> {
            return delegate.authorities
        }

        override fun getUsername(): String {
            return delegate.username
        }

        override fun isAccountNonExpired(): Boolean {
            return delegate.isAccountNonExpired
        }

        override fun isAccountNonLocked(): Boolean {
            return delegate.isAccountNonLocked
        }

        override fun isCredentialsNonExpired(): Boolean {
            return delegate.isCredentialsNonExpired
        }

        override fun isEnabled(): Boolean {
            return delegate.isEnabled
        }

        companion object {
            private const val serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID
        }

        init {
            password = delegate.password
        }
    }
}