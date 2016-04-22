import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.camera.*;

class EntityIdentificationModel extends com.rameses.seti2.models.CrudFormModel {

    def base64 = new com.rameses.util.Base64Cipher();
    def idtypes = LOV.INDIVIDUAL_ID_TYPES;
    def image;
    
    void afterInit() { 
        styleRules << new StyleRule("image", "#{image==null}").add("visible", false);
        styleRules << new StyleRule("image", "#{image!=null}").add("visible", true);
    }
    
    void afterCreate() {
        image = null; 
        entity.entityid = caller?.entityid;
    }
    
    void afterOpen() { 
        image = null; 
        def photo = entity.photo; 
        if ( photo instanceof String ) { 
            try { 
                image = base64.decode( photo, false ); 
            } catch( e ) { 
                MsgBox.err( e ); 
            } 
        } 
    }
    
    void takePhoto() { 
        if ( mode == 'read' ) return; 
        
        WebcamViewer.open([
                isAutoCloseOnSelect: { 
                    return false; 
                }, 
                onselect: { o-> 
                    image = o; 
                    entity.photo = base64.encode(o);
                    binding.refresh('image');
                }
        ] as CameraModel);
    }
    
    void removePhoto() {
        if ( mode == 'read' ) return; 
        
        image = null; 
        entity.photo = null; 
        binding.refresh('image'); 
    }
}
