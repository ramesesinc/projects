package com.rameses.gov.etracs.rptis.models;

import com.rameses.gov.etracs.services.FAASAnnotationService;
import test.utils.Data;
import test.utils.ServiceUtil;
import test.utils.Util;
import java.util.HashMap;
import java.util.Map;

public class AnnotationModel {
    private FAASAnnotationService svc;
    private FAASAnnotationService remoteSvc;
    
    public AnnotationModel() {
        svc = ServiceUtil.create(FAASAnnotationService.class);
        remoteSvc = ServiceUtil.create(FAASAnnotationService.class, true);
    }
    
    public Map create(Map entity) {
        return svc.create(entity);
    }
    
    public Map open(Map entity) {
        return open(entity,false);
    }
    
    public Map open(Map entity, boolean remote) {
        if (remote) {
            return remoteSvc.open(entity);
        }
        return svc.open(entity);
    }
    
    public Map submitForApproval(Map entity) {
        return svc.submitForApproval(entity);
    }
    
    public Map approve(Map entity) {
        return svc.approve(entity);
    }
    
    public Map createAndApprove(Map faas) {
        Map annotation = init(faas);
        annotation = create(annotation);
        annotation = submitForApproval(annotation);
        annotation = approve(annotation);
        return annotation;
    }
            
    public Map init(Map faas) {
        Util.sleep(1000);
        Map annotation = svc.init(faas);
        annotation.put("annotationtype", getAnnotationType());
        annotation.put("fileno", "A001");
        annotation.put("orno", "A001");
        annotation.put("ordate", Data.getServerDate());
        annotation.put("oramount", 50.0);
        annotation.put("memoranda", "MORTGAGE");
        return annotation;
    }
    
    private Map getAnnotationType() {
        Map type = new HashMap();
        type.put("objid", "MT");
        type.put("type", "MORTGAGE");
        return type;
    }


}
