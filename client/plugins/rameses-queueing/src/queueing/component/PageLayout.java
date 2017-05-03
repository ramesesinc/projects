/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package queueing.component;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.Rectangle;

/**
 *
 * @author wflores 
 */
public class PageLayout implements LayoutManager, LayoutManager2 {

    public final static String HEADER  = "HEADER";
    public final static String FOOTER  = "FOOTER";
    public final static String CONTENT = "CONTENT";
    
    private Component headerComp;
    private Component footerComp;
    private Component viewComp;
    
    public float getLayoutAlignmentX(Container target) { return 0.5f; }
    public float getLayoutAlignmentY(Container target) { return 0.5f; }
    
    public void addLayoutComponent(String name, Component comp) { 
        addLayoutComponent(comp, name);
    }
    public void addLayoutComponent(Component comp, Object constraints) {
        String skey = (constraints == null? null: constraints.toString()); 
        if ( HEADER.equals( skey )) { 
            headerComp = comp; 
        } else if ( FOOTER.equals( skey )) {
            footerComp = comp; 
        } else {
            viewComp = comp; 
        }
    }

    public void removeLayoutComponent(Component comp) {
        if ( comp == null ) return; 
        if ( headerComp != null && headerComp.equals(comp)) {
            headerComp = null; 
        } else if ( footerComp != null && footerComp.equals(comp)) {
            footerComp = null; 
        } else if ( viewComp != null && viewComp.equals(comp)){
            viewComp = null; 
        }
    }

    public Dimension minimumLayoutSize(Container parent) { 
        return preferredLayoutSize(parent);
    }

    public Dimension maximumLayoutSize(Container target) {
        return preferredLayoutSize(target);
    }
    public Dimension preferredLayoutSize(Container parent) {
        synchronized( parent.getTreeLock()) {
            int w=0, h=0;            
            Component[] comps = new Component[]{ headerComp, viewComp, footerComp };
            for (int i=0; i<comps.length; i++ ) {
                Component c = comps[i]; 
                if ( c == null || !c.isVisible()) continue; 
                
                Dimension dim = c.getPreferredSize(); 
                w = Math.max( w, dim.width ); 
                h += dim.height; 
            }
            Insets margin = parent.getInsets(); 
            w += (margin.left + margin.right);
            h += (margin.top + margin.bottom);
            return new Dimension(w, h); 
        }
    }
    
    public void invalidateLayout(Container target) {
        layoutContainer( target ); 
    }
    public void layoutContainer(Container parent) {
        synchronized( parent.getTreeLock()) {
            Insets margin = parent.getInsets(); 
            int pw = parent.getWidth(); 
            int ph = parent.getHeight(); 
            int x = margin.left; 
            int y = margin.top; 
            int w = pw - (margin.left + margin.top);
            int h = ph - (margin.top + margin.bottom); 
            
            Rectangle headerRect = new Rectangle(x, y, w, 0);  
            if ( headerComp != null && headerComp.isVisible() ) {
                Dimension dim = headerComp.getPreferredSize(); 
                headerComp.setBounds(x, y, w, dim.height); 
                headerRect = new Rectangle(x, y, w, dim.height); 
            }
            
            int fy = ph - margin.bottom; 
            int hy = headerRect.y + headerRect.height; 
            if ( fy < hy ) { fy = hy; } 
            
            Rectangle footerRect = new Rectangle(x, fy, w, 0); 
            boolean footerVisible = ( footerComp != null && footerComp.isVisible()); 
            if ( footerVisible ) { 
                Dimension dim = footerComp.getPreferredSize();                 
                int cy = ph - margin.bottom - dim.height; 
                if ( cy < hy ) { cy = hy; } 
                
                footerRect = new Rectangle(x, cy, w, dim.height); 
                footerComp.setBounds(footerRect.x, footerRect.y, footerRect.width, footerRect.height); 
            } 
            
            int vh = footerRect.y - hy; 
            if ( vh < 0 ) { vh = 0; } 
            
            if ( viewComp != null && viewComp.isVisible() && vh > 0 ) {
                viewComp.setBounds( x, hy, w, vh ); 
            }             
        }
    }    
}
