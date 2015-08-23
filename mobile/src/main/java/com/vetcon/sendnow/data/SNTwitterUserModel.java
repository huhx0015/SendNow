package com.vetcon.sendnow.data;

/**
 * Created by Michael Yoon Huh on 8/22/2015.
 */

public class SNTwitterUserModel {

    /** CLASS VARIABLES ________________________________________________________________________ **/

    private String user_image; // Stores the image URL of the Twitter user.
    private String user_name; // Stores the name of the Twitter user.
    private String user_handle; // Stores the name of the user's Twitter handle.
    private String user_location; // Stores the name of the user's current location.

    /** INITIALIZATION METHODS _________________________________________________________________ **/

    // SNTwitterUserModel(): Constructor method for the class.
    public SNTwitterUserModel(String name, String handle, String location, String image) {
        this.user_name = name;
        this.user_handle = handle;
        this.user_image = image;
        this.user_location = location;
    }

    /** GET / SET METHODS ______________________________________________________________________ **/

    // getUserImage(): Returns the user image URL.
    public String getUserImage() { return user_image; }

    // getUserName(): Returns the user's full name.
    public String getUserName() {
        return user_name;
    }

    // getUserHandle(): Returns the user's Twitter handle.
    public String getUserHandle() { return user_handle; }

    // getUserLocation(): Returns the user's current location.
    public String getUserLocation() { return user_location; }

    // setUserImage(): Sets the user image URL.
    public void setUserImage(String image) { this.user_image = image; }

    // setUserName(): Sets the user name for the class.
    public void setUserName(String user_name) { this.user_name = user_name; }

    // setUserHandle(): Sets the user handle for the class.
    public void setUserHandle(String user_handle) {
        this.user_handle = user_handle;
    }

    // setUserLocation(): Sets the user location for the class.
    public void setUserLocation(String user_location) {
        this.user_location = user_location;
    }
}