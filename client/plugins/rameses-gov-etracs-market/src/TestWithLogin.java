import com.rameses.osiris2.test.OsirisTestPlatform;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author Elmo
 */
public class TestWithLogin {

    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
        } catch(Exception ex) {;}
        
        Map env = new HashMap();
        env.put("app.host","localhost:8070");
        env.put("app.context","etracs25");
        env.put("app.cluster","osiris3");
        env.put("app.debug",true);
        
        OsirisTestPlatform.runTest(env, new HashMap(), new HashMap());
    }
}
