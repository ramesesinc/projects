/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package queueing.model;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author rameses
 */
public abstract class AbstractUserQueueHandler {
    
    private final static Global global = new Global(); 
    
    public static void add( String key, AbstractUserQueueHandler handler ) {
        global.add( key, handler );
    }
    public static void remove( String key ) {
        global.remove( key ); 
    }
    public static void fireNotify( String action, Object data ) {
        global.fireNotify( action, data ); 
    }
    
    private static class Global {
        
        private HashMap<String,AbstractUserQueueHandler> handlers = new HashMap();
        private ExecutorService service = Executors.newSingleThreadExecutor(); 
        
        void add( String key, AbstractUserQueueHandler handler ) {
            handlers.put(key, handler); 
        }
        void remove( String key ) {
            handlers.remove( key ); 
        }
        void fireNotify( String action, Object data ) {
            AbstractUserQueueHandler[] values = handlers.values().toArray(new AbstractUserQueueHandler[]{});
            for ( int i=0; i<values.length; i++ ) { 
                service.submit(new ExecTask( values[i], action, data )); 
            } 
        }
    }
    private static class ExecTask implements Runnable {

        private AbstractUserQueueHandler handler;
        private String action;
        private Object data; 
        
        ExecTask( AbstractUserQueueHandler handler, String action, Object data ) {
            this.handler = handler; 
            this.action = action; 
            this.data = data; 
        }
        
        public void run() { 
            if ( handler == null ) return; 
            
            try { 
                handler.notify( this.action, this.data ); 
            } catch(Throwable t) {
                t.printStackTrace(); 
            }
        }
    }
    
    public abstract void notify( String action, Object data );
    
}
