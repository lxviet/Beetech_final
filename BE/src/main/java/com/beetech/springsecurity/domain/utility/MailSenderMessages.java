package com.beetech.springsecurity.domain.utility;


public class MailSenderMessages {

    public static final String resetPasswordMessage(String token) {
        return "<html><body>"
                + "<p><strong>Dear User</strong></p>"
                + "<p>A request has been received to reset the password account. </p>"
                + "<p>Click on this button to reset your password:</p>"
                + "<a href='http://localhost:3000/reset_password/" + token + "' style='"
                + "display: inline-block;"
                + "padding: 10px 20px;"
                + "font-size: 16px;"
                + "text-align: center;"
                + "text-decoration: none;"
                + "background-color: #4CAF50;"
                + "color: white;"
                + "border-radius: 4px;"
                + "border: none;'>Reset Password</a>"
                + "<p>Thank you!</p>"
                + "<p>Best Wishes,</p>"
                + "</body></html>";

    }
}