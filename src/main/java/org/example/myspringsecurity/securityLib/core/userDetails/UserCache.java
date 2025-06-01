package org.example.myspringsecurity.securityLib.core.userDetails;

public interface UserCache {
    UserDetails getUserFromCache(String username);
    void putUserInCache(UserDetails user);
    void removeFromUserCache(String username);
}
