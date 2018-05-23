package com.vasistha.bankingsystem.ModelClasses;

/**
 * Created by Ashutosh Singh on 06-Mar-18.
 */

public class UserFeedback
{
    String feedBack;
    String userRating;

    public UserFeedback()
    {
    }

    public UserFeedback(String feedBack, String userRating) {
        this.feedBack = feedBack;
        this.userRating = userRating;
    }

    public String getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(String feedBack) {
        this.feedBack = feedBack;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }
}
