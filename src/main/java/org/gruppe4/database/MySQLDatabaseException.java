package org.gruppe4.database;

public class MySQLDatabaseException extends RuntimeException{

    public MySQLDatabaseException(String message, Throwable cause){
        super(message, cause);
    }
}
