package com.project.android.exampledictionary.model;

public class Feedback {
    public String feedBackID;
    public String email;
    public String subject;
    public String message;

    public Feedback(String feedBackID, String email, String subject, String message) {
        this.feedBackID = feedBackID;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    public String getFeedBackID() {
        return feedBackID;
    }

    public void setFeedBackID(String feedBackID) {
        this.feedBackID = feedBackID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
