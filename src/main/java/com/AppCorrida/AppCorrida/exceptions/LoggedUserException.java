package com.AppCorrida.AppCorrida.exceptions;

public class LoggedUserException extends RuntimeException {
        public LoggedUserException() { super("The ride does not belong to the logged in user. Request not allowed."); }
}
