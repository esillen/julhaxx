package se.teamkugel.julhaxx

interface IUserDetailsRepo {
    operator fun get(key: String): MattesUserDetailsManager.MutableUser
    fun put(key: String, mutableUser: MattesUserDetailsManager.MutableUser)
    fun remove(key: String)
    fun containsKey(key: String): Boolean
}