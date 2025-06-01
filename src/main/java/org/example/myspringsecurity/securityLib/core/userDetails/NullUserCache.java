package org.example.myspringsecurity.securityLib.core.userDetails;

public class NullUserCache implements UserCache {
    @Override
    public UserDetails getUserFromCache(String username) {
        return null;
    }

    @Override
    public void putUserInCache(UserDetails user) {

    }

    @Override
    public void removeFromUserCache(String username) {

    }
}
