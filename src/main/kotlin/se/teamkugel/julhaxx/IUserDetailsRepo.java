package se.teamkugel.julhaxx;

import se.teamkugel.julhaxx.MattesUserDetailsManager.MutableUser;

public interface IUserDetailsRepo {
	MutableUser get(String key);
	void put(String key, MutableUser mutableUser);
	void remove(String key);
	boolean containsKey(String key);
}
