package com.rameses.service;

import com.rameses.common.AbstractAsyncHandler;
import com.rameses.common.AsyncBatchResult;
import com.rameses.common.AsyncException;
import com.rameses.common.AsyncHandler;
import com.rameses.common.AsyncToken;
import java.util.concurrent.TimeoutException;

public class AsyncTask implements Runnable 
{
    private ServiceProxy proxy;
    private String methodName;
    private Object[] args;
    private AsyncHandlerProxy handlerProxy;
    
    private AsyncPoller poller; 
    private int retrycount;
    
    public AsyncTask(ServiceProxy proxy, String methodName, Object[] args, AsyncHandler handler) {
        this.proxy = proxy;
        this.methodName = methodName;
        this.args = args;
        
        handlerProxy = new AsyncHandlerProxy(handler); 
    }
    
    public void run() {
        try {
            retrycount = 0;
            
            Object result = proxy.invoke( methodName, args );
            if (result instanceof AsyncToken) {
                AsyncToken token = (AsyncToken)result; 
                if (token.isClosed()) {
                    handlerProxy.onMessage(AsyncHandler.EOF); 
                    return;
                }
                
                AsyncPoller poller = new AsyncPoller(proxy.getConf(), token); 
                handle(poller, poller.poll()); 
                return;
            }             
            notify( result );
            
        }  catch (Exception e) {
            e.printStackTrace();
            handlerProxy.onError( e );
        }
    } 
    
    private void handle(AsyncPoller poller, Object result) throws Exception {
        if (handlerProxy.isCancelRequested()) {
            try {
                handlerProxy.onCancel(); 
            } catch(Throwable t) {
                t.printStackTrace(); 
            } finally {
                return; 
            }
        }
        
        if (result instanceof AsyncToken) {
            AsyncToken at = (AsyncToken)result;
            if (at.isClosed()) {
                poller.close(); 
                handlerProxy.onMessage(AsyncHandler.EOF); 
                return;
            } 
        } 
        
        if (result == null) {
            retrycount++; 
            if (retrycount < 3) {
                handle(poller, poller.poll()); 
            } else {
                handlerProxy.onTimeout("poll failed after 3 retries");
                if (handlerProxy.isRetryRequested()) {
                    retrycount = 0;
                    handle(poller, poller.poll()); 
                }
            }
        } else { 
            if (notify( result )) { 
                handle(poller, poller.poll()); 
            } else { 
                poller.close(); 
                handlerProxy.onMessage(AsyncHandler.EOF); 
            } 
        } 
    } 
    
    private boolean notify(Object o) {
        if (o instanceof AsyncException) {
            handlerProxy.onError((AsyncException) o);
            return false;
            
        } else if (o instanceof AsyncBatchResult) {
            boolean is_closed = false;
            AsyncBatchResult batch = (AsyncBatchResult)o;
            for (Object item : batch) {
                if (item instanceof AsyncToken) {
                    is_closed = ((AsyncToken)item).isClosed(); 
                    
                } else if (item instanceof AsyncException) {
                    handlerProxy.onError((AsyncException) item);
                    return false; 
                    
                } else {
                    handlerProxy.onMessage(item); 
                } 
            } 
            return !is_closed; 
            
        } else {
            handlerProxy.onMessage(o); 
            return true; 
        } 
    }
        
    private class AsyncHandlerProxy extends AbstractAsyncHandler {
        private AsyncHandler source;
        
        AsyncHandlerProxy(AsyncHandler source) {
            this.source = source; 
            if (source == null) {
                this.source = new AsyncHandler() {
                    public void onMessage(Object value) {
                        System.out.println("unhandled message. No handler passed ");
                    }
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                    public void call(Object o) {
                    }
                };
            }
        }

        public void onError(Exception e) {
            if (source != null) {
                source.onError(e);
            } 
        }

        public void onMessage(Object o) {
            if (source != null) {
                source.onMessage(o);
            } 
        }

        public void call(Object o) {
            if (source != null) {
                source.call(o);
            } 
        }

        public void onTimeout(String msg) {
            if (source instanceof AbstractAsyncHandler) {
                ((AbstractAsyncHandler) source).onTimeout(); 
            } else if (source != null) {
                source.onError(new TimeoutException(msg)); 
            }
        }
        
        public void onCancel() {
            if (source instanceof AbstractAsyncHandler) {
                ((AbstractAsyncHandler) source).onCancel(); 
            } 
        }
        
        public boolean isRetryRequested() {
            if (source instanceof AbstractAsyncHandler) {
                return ((AbstractAsyncHandler) source).hasRequestRetry();
            } else {
                return false; 
            }
        }
        
        public boolean isCancelRequested() {
            if (source instanceof AbstractAsyncHandler) {
                return ((AbstractAsyncHandler) source).hasRequestCancel();
            } else {
                return false; 
            }
        }        
    }
}