package se.teamkugel.julhaxx;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.log.LogMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.memory.UserAttribute;
import org.springframework.security.core.userdetails.memory.UserAttributeEditor;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.util.Assert;

/**
 * Non-persistent implementation of {@code UserDetailsManager} which is backed by an
 * in-memory map.
 * <p>
 * Mainly intended for testing and demonstration purposes, where a full blown persistent
 * system isn't required.
 *
 * @author Luke Taylor
 * @since 3.1
 */
public class MattesUserDetailsManager implements UserDetailsManager, UserDetailsPasswordService {

	protected final Log logger = LogFactory.getLog(getClass());

//	private final IUserDetailsRepo users = new IUserDetailsRepo() {
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
	
	private final IUserDetailsRepo users;

	private AuthenticationManager authenticationManager;

	public MattesUserDetailsManager(IUserDetailsRepo users) {
		this.users = users;
	}
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

	private User createUserDetails(String name, UserAttribute attr) {
		return new User(name, attr.getPassword(), attr.isEnabled(), true, true, true, attr.getAuthorities());
	}

	@Override
	public void createUser(UserDetails user) {
		Assert.isTrue(!userExists(user.getUsername()), "user should not exist");
		this.users.put(user.getUsername().toLowerCase(), new MutableUser(user));
	}

	@Override
	public void deleteUser(String username) {
		this.users.remove(username.toLowerCase());
	}

	@Override
	public void updateUser(UserDetails user) {
		Assert.isTrue(userExists(user.getUsername()), "user should exist");
		this.users.put(user.getUsername().toLowerCase(), new MutableUser(user));
	}

	@Override
	public boolean userExists(String username) {
		return this.users.containsKey(username.toLowerCase());
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		if (currentUser == null) {
			// This would indicate bad coding somewhere
			throw new AccessDeniedException(
					"Can't change password as no Authentication object found in context " + "for current user.");
		}
		String username = currentUser.getName();
		this.logger.debug(LogMessage.format("Changing password for user '%s'", username));
		// If an authentication manager has been set, re-authenticate the user with the
		// supplied password.
		if (this.authenticationManager != null) {
			this.logger.debug(LogMessage.format("Reauthenticating user '%s' for password change request.", username));
			this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
		}
		else {
			this.logger.debug("No authentication manager set. Password won't be re-checked.");
		}
		MutableUser user = this.users.get(username);
		Assert.state(user != null, "Current user doesn't exist in database.");
		user.setPassword(newPassword);
	}

	@Override
	public UserDetails updatePassword(UserDetails user, String newPassword) {
		String username = user.getUsername();
		MutableUser mutableUser = this.users.get(username.toLowerCase());
		mutableUser.setPassword(newPassword);
		return mutableUser;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails user = this.users.get(username.toLowerCase());
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new User(user.getUsername(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(),
				user.isCredentialsNonExpired(), user.isAccountNonLocked(), user.getAuthorities());
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	
	/**
	 * @author Luke Taylor
	 * @since 3.1
	 */
	public class MutableUser implements UserDetails {

		private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

		private String password;

		private final UserDetails delegate;

		MutableUser(UserDetails user) {
			this.delegate = user;
			this.password = user.getPassword();
		}

		public String getPassword() {
			return this.password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public Collection<? extends GrantedAuthority> getAuthorities() {
			return this.delegate.getAuthorities();
		}

		public String getUsername() {
			return this.delegate.getUsername();
		}

		public boolean isAccountNonExpired() {
			return this.delegate.isAccountNonExpired();
		}

		public boolean isAccountNonLocked() {
			return this.delegate.isAccountNonLocked();
		}

		public boolean isCredentialsNonExpired() {
			return this.delegate.isCredentialsNonExpired();
		}

		public boolean isEnabled() {
			return this.delegate.isEnabled();
		}

	}
}
