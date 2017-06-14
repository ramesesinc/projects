/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.UIManager;
import junit.framework.TestCase;
import queueing.component.PageLayout;
import queueing.view.UserQueuePage;

public class NewEmptyJUnitTest extends TestCase {
    
    public NewEmptyJUnitTest(String testName) {
        super(testName);
    }

    public void test1() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
        
        PageLayout layout = new PageLayout();
        JPanel p = new JPanel( layout ); 
        p.add( new JButton("Header"), PageLayout.HEADER); 
        p.add( new JButton("Footer"), PageLayout.FOOTER); 
                
        p.add( new UserQueuePage() );
        
        JDialog d = new JDialog();
//        d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//        d.setModal(true); 
//        d.setTitle("Test");
//        d.setContentPane( p ); 
//        d.pack();
//        d.setVisible(true); 
        
        
        Color c = new Color(204, 255, 204); 
        System.out.println( c );
    }
}
