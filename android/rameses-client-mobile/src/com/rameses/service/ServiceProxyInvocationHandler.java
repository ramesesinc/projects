/*
 * ServiceProxyInvocationHandler.java
 * Created on September 23, 2011, 11:29 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */
package com.rameses.service;

import com.rameses.common.AsyncHandler;
import com.rameses.util.AppException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author jzamss
 */
public class ServiceProxyInvocationHandler implements InvocationHandler {
    
    private static final ExecutorService thread = Executors.newFixedThreadPool(10);
    
    private ServiceProxy proxy;
    private boolean failOnConnectionError;
    
    public ServiceProxyInvocationHandler(ServiceProxy proxy) {
        this(proxy, true);
    }
    
    public ServiceProxyInvocationHandler(ServiceProxy proxy, boolean failOnConnectionError) {
        this.proxy = proxy;
        this.failOnConnectionError = failOnConnectionError; 
    }
    
    //do not use invokeMethod because it is a reserved word in groovy
    public Object invokeMethod(String methodName, Object[] args) throws Throwable 
    {
        if (methodName.equals("toString")) return proxy.getClass().getName();
                
        try {
            if( args == null || args.length == 0 ) {
                return this.proxy.invoke(methodName);
                
            } else {
                AsyncHandler handler = null;
                if(args[args.length-1] instanceof AsyncHandler ) {
                    Object[] newArgs = new Object[args.length-1];
                    for(int i=0; i<newArgs.length; i++) {
                        newArgs[i] = args[i];
                    }
                    handler = (AsyncHandler)args[args.length-1];
                    args = newArgs;
                }
                
                if(handler !=null) {
                    thread.submit( new AsyncTask(proxy, methodName, args, handler) );
                    return null;
                } else {
                    return this.proxy.invoke( methodName, args );
                }
            }
        } 
        catch(Throwable t) 
        {
            t.printStackTrace();
            
            if (isConnectionError(t) && !failOnConnectionError) {
                return null; 
            } else if (t instanceof AppException) { 
                throw t; 
            } else if (t instanceof RuntimeException) { 
                throw (RuntimeException) t; 
            } else { 
                throw new RuntimeException(t.getMessage(), t); 
            } 
        }
    }
    
    public Object invoke(Object sproxy, Method method, Object[] args) throws Throwable 
    {
        return invokeMethod( method.getName(), args );
        /*
        if (method.getName().equals("toString")) return proxy.getClass().getName();
        try {
            if( args == null ) {
                return this.proxy.invoke(method.getName());
            } else {
                AsyncHandler handler = null;
                if(args[args.length-1] instanceof AsyncHandler ) {
                    Object[] newArgs = new Object[args.length-1];
                    for(int i=0; i<newArgs.length; i++) {
                        newArgs[i] = args[i];
                    }
                    handler = (AsyncHandler)args[args.length-1];
                    args = newArgs;
                }
                
                if(handler !=null) {
                    thread.submit( new AsyncTask( proxy, method.getName(), args, handler) );
                    return null;
                } else {
                    return this.proxy.invoke( method.getName(), args );
                }
            }
        } 
        catch(Throwable t) 
        {
            t.printStackTrace();
            if (t instanceof AppException)
                throw t;
            else if (t instanceof RuntimeException) 
                throw (RuntimeException) t;
            else
                throw new RuntimeException(t.getMessage(), t);
        }
         */
    }
    
    private boolean isConnectionError(Throwable e) {
        if (e instanceof java.net.ConnectException) return true; 
        else if (e instanceof java.net.SocketException) return true; 
        else if (e instanceof java.net.SocketTimeoutException) return true; 
        else if (e instanceof java.net.UnknownHostException) return true; 
        else if (e instanceof java.net.MalformedURLException) return true; 
        else if (e instanceof java.net.ProtocolException) return true; 
        else if (e instanceof java.net.UnknownServiceException) return true; 
        else return false; 
    }    
}
