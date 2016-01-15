package com.rameses.common;

import java.io.Serializable;

public class AsyncToken implements Serializable 
{
    public final static int CLOSED  = 1990776172;
    public final static int TIMEOUT = -595928767;
    
    private String id;
    private String connection;
    private boolean is_closed;
    
    public AsyncToken() {
    }
    
    public AsyncToken(String id, String connection) {
        this.id =id;
        this.connection = connection;
    }

    public String getId() {
        return id;
    }

    public String getConnection() {
        return connection;
    }

    public boolean isClosed() { return is_closed; } 
    public void setClosed(boolean is_closed) {
        this.is_closed = is_closed; 
    } 
}
