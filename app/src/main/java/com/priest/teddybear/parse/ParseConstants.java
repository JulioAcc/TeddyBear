package com.priest.teddybear.parse;

/**
 * Created by priest on 4/12/15.
 */
public class ParseConstants {

    public static boolean isStringValid(String newValue, String oldValue){
        if(newValue!=null || newValue!="" || newValue!=oldValue)
            return true;
        else
            return false;
    }

    public static boolean isIntValid(int newValue, int oldValue){
        if(newValue!=0 || newValue!=oldValue)
            return true;
        else
            return false;
    }

    public final static String CLASS_ATTRIBUTE_OBJECT_ID = "objectId";

    //--------------------  ATTRIBUTES: USER CLASS
    public final static String USER_CLASS_ATTRIBUTE_FACEBOOK_ID = "facebookId";
    public final static String USER_CLASS_ATTRIBUTE_EMAIL = "email";
    public final static String USER_CLASS_ATTRIBUTE_PROFILE_PICTURE = "profilePicture";
    public final static String USER_CLASS_ATTRIBUTE_NAME = "name";
    public final static String USER_CLASS_ATTRIBUTE_FRIENDS_LIST = "userFriendsList";
    public final static String USER_CLASS_ATTRIBUTE_STATUS_ONLINE = "statusOnline";

}
