/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.utils;

import com.rameses.gov.etracs.services.DateService;
import com.rameses.gov.etracs.services.PersistenceService;
import com.rameses.gov.etracs.services.QueryService;
import com.rameses.service.ScriptServiceContext;


public class ServiceUtil {
    private static ScriptServiceContext ctx; 
    private static ScriptServiceContext remoteCtx; 
    
    static{
        ctx = new ScriptServiceContext(LocalConfig.getEnv());
        remoteCtx = new ScriptServiceContext(RemoteConfig.getEnv());
    }
    
    private ServiceUtil(){}

    public static <T> T create(Class<T> clazz) {
        return create(clazz, false);
    }
    
    public static <T> T create(Class<T> clazz, boolean remote) {
        if (remote) {
            return (T) getRemoteContext().create(clazz.getSimpleName(), RemoteConfig.getEnv(), clazz);
        }
        return (T) getContext().create(clazz.getSimpleName(), LocalConfig.getEnv(), clazz);
    }
    
    public static <T> T create(String serviceName, Class<T> clazz) {
        return create(serviceName, clazz, false);
    }
    
    public static <T> T create(String serviceName, Class<T> clazz, boolean remote) {
        if (remote) {
            return (T) getRemoteContext().create(serviceName, RemoteConfig.getEnv(), clazz);
        }
        return (T) getContext().create(serviceName, LocalConfig.getEnv(), clazz);
    }

    
    
    public static DateService getDateService(){
        return getDateService(false);
    }
    
    public static DateService getDateService(boolean remote){
        return create(DateService.class, remote);
    }
    
    public static QueryService query(){
        return query(false);
    }
    
    public static QueryService query(boolean remote){
        return create(QueryService.class, remote);
    }
    
    public static PersistenceService persistence(){
        return persistence(false);
    }
    
    public static PersistenceService persistence(boolean remote){
        return create(PersistenceService.class, remote);
    }
    
    public static ScriptServiceContext getContext(){
        return ctx;
    }
    
    public static ScriptServiceContext getRemoteContext(){
        return remoteCtx;
    }
}
