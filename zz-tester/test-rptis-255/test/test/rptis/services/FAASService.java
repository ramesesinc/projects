package test.rptis.services;


import java.util.Map;

public interface FAASService {
    public Map initCaptureAndCreate(Map entity);
    public Map updateFaas(Map entity);
    public void deleteFaas(Map entity);
    public Map openFaas(Map entity);

    public Map approveFaas(Map entity);
    public Map initOnlineTransaction(Map info);
    public Map submitForApproval(Map transferFaas);
    public void revertToInterim(Map faas);
    
}

