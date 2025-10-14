package org.gruppe4.database;

public class MySQLDatabaseException extends RuntimeException{

    public MySQLDatabaseException(String messae, Throwable cause){
        super(messae, cause);
    }
}
