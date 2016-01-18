package test;

import com.rameses.osiris2.test.OsirisTestPlatform;
import java.util.HashMap;
import java.util.Map;
import javax.swing.UIManager;

public class Main 
{
    public static void main(String[] args) throws Exception 
    {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
        }
        catch(Exception e){;} 
        
        Map env = new HashMap();
        env.put("app.debugMode", "true");         
        env.put("app.cluster", "osiris3");
        env.put("app.context", "etracs25");
        env.put("app.host", "localhost:8070");
        env.put("readTimeout", "30000"); 
        env.put("USERID", "USER001");
        env.put("USERNAME", "Santos, Juan");
        env.put("USERFORMALNAME", "Juan Santos");
        env.put("JOBTITLE", "AA - II");
        
        Map roles = new HashMap();
        //roles.put("RPT.APPRAISER", null);
        //roles.put("RPT.CERTIFICATION_ISSUER", null);
        roles.put("RPT.RULE_AUTHOR", null);
        roles.put("RPT.LANDTAX", null);
        roles.put("RPT.LANDTAX_DATAMGMT", null);
        roles.put("RPT.APPRAISER", null);
        roles.put("RPT.RECOMMENDER", null);
        roles.put("RPT.APPROVER", null);
        roles.put("RPT.ASSESSOR_DATAMGMT", null);
        roles.put("RPT.ASSESSOR_SHARED", null);
        roles.put("RPT.LANDTAX_SHARED", null);
        roles.put("ADMIN.SYSADMIN", null);
        OsirisTestPlatform.runTest(env, roles);         
    }

    public Main() {
    }
}
