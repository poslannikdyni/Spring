package com.edu.ulab.app.exception;

public class DaoException extends RuntimeException {
    public DaoException(String msg){super(msg);}
    public DaoException(String msg, Throwable cause){super(msg, cause);}
    public DaoException(Throwable cause){super(cause);}
}
