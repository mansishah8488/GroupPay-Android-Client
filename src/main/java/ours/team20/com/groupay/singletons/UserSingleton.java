package ours.team20.com.groupay.singletons;

import ours.team20.com.groupay.models.User;

/**
 * Created by Ken on 4/24/2015.
 */
public class UserSingleton {
    private static User user;
    public UserSingleton(User user){
        if(this.user == null) {
            this.user = user;
        }
    }

    public static User getCurrentUser(){
        return user;
    }
}
