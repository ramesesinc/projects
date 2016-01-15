
package com.rameses.common;

import java.io.Serializable;
import java.rmi.server.UID;
import java.util.Map;

public class AsyncRequest implements Serializable {
    
    private String id;
    private String connection;
    
    private String methodName;
    private String serviceName;
    private Object[] args;
    private Map env;
    private String contextName;
    private String varStatus;

    public AsyncRequest(String serviceName, String methodName, Object[] args, Map env) {
        this(serviceName, methodName, args, env, "ASYNC"+ new UID()); 
    }
    
    public AsyncRequest(String serviceName, String methodName, Object[] args, Map env, String id) { 
        if (id == null || id.trim().length() == 0) { 
            throw new NullPointerException("Please specify id");
        }
        
        this.id = id; 
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.args = args;
        this.env = env;
    }

    public String getId() { return id; }

    public String getConnection() { return connection; }
    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getMethodName() { return methodName; }
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Object[] getArgs() { return args; }
    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Map getEnv() { return env; }
    public void setEnv(Map env) {
        this.env = env;
    }

    public String getContextName() { return contextName; }
    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public String getVarStatus() { return varStatus; }
    public void setVarStatus(String varStatus) {
        this.varStatus = varStatus;
    }
}
