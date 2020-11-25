package se.teamkugel.julhaxx;

import se.teamkugel.julhaxx.MattesUserDetailsManager.MutableUser;

public class UserDetailsRepositoryAdapter implements IUserDetailsRepo {
	private final UserRepository userRepository;
	
	public UserDetailsRepositoryAdapter(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public MutableUser get(String key) {
		return null;
	}

	@Override
	public void put(String key, MutableUser mutableUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(String key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean containsKey(String key) {
		// TODO Auto-generated method stub
		return false;
	}

}
