package com.rameses.waterworks.service;

import com.rameses.service.ScriptServiceContext;
import com.rameses.Main;
import java.util.HashMap;
import java.util.Map;

public class ServiceProxy {
    
    public static ScriptServiceContext getContext(){
        String setting_ip = Main.CONNECTION_SETTING.get("ip") != null ? Main.CONNECTION_SETTING.get("ip").toString() : "127.0.0.1";
        String setting_port = Main.CONNECTION_SETTING.get("port") != null ? Main.CONNECTION_SETTING.get("port").toString() : "8070";
        String setting_timeout = Main.CONNECTION_SETTING.get("timeout") != null ? Main.CONNECTION_SETTING.get("timeout").toString() : "30000";
        String setting_context = Main.CONNECTION_SETTING.get("context") != null ? Main.CONNECTION_SETTING.get("context").toString() : "etracs25";
        String setting_cluster = Main.CONNECTION_SETTING.get("cluster") != null ? Main.CONNECTION_SETTING.get("cluster").toString() : "osiris3";
        
        Map env = new HashMap();
        env.put("app.context", "etracs25");
        env.put("app.cluster", "osiris3");
        env.put("app.host", setting_ip+":"+setting_port);
        env.put("readTimeout",setting_timeout);
        return new ScriptServiceContext(env);
    }
    
}
