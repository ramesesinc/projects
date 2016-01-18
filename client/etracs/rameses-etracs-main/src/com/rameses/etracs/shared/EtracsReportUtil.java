/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.etracs.shared;

import com.rameses.rcp.framework.ClientContext;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 *
 * @author arnel
 */
public class EtracsReportUtil {

    public static String getImagePath(String imagename) {
        ClientContext clientContext = ClientContext.getCurrentContext();

        Map appEnv = clientContext.getAppEnv();
        String customfolder = (String) appEnv.get("report.custom");
        if (customfolder == null || customfolder.trim().length() == 0) {
            customfolder = (String) appEnv.get("app.custom");
        }
        

        String path = "images/" + imagename;
        if (customfolder != null) {
            String cpath = "images/" + customfolder + "/" + imagename;
            if (clientContext.getResource(cpath) != null) {
                path = cpath;
            }
        } 
        return path;
    }

    public static InputStream getInputStream(String imagename) {
        String path = getImagePath(imagename);
        if (path == null) {
            return null;
        }
        return ClientContext.getCurrentContext().getClassLoader().getResourceAsStream(path);
    }

    public static String getResource(String imagename) {
        String path = getImagePath(imagename);
        if (path == null) {
            return null;
        }
        URL url = ClientContext.getCurrentContext().getClassLoader().getResource(path);
        return (url != null) ? url.toString() : null;
    }
}
