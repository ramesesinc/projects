package com.rameses.gov.etracs.rptis;

import test.rptis.models.FaasModel;
import com.rameses.gov.etracs.rptis.models.FaasChangeInfoModel;
import test.utils.Data;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class FaasChangeOwnerInfoTest  {
    protected static FaasModel faasModel;
    protected static FaasChangeInfoModel changeModel;
    
    protected static Map faas;
    protected static Map changeInfo;
    protected static Map changeFaas;
    
    protected static Map remoteFaas;
    protected static Map remoteChangeInfo;
    
    
    
    
    static {
        faasModel = new FaasModel();
        changeModel = new FaasChangeInfoModel();
    }
    
    @BeforeClass
    public static void setUpClass() {
        faas = faasModel.createAndApprove();
        changeInfo = changeModel.initChangeOwner(faas);
        changeInfo = changeModel.updateInfo(changeInfo);
        changeInfo = changeModel.open(changeInfo);
        changeFaas = faasModel.open(faas);
        
        remoteChangeInfo = changeModel.open(changeInfo, true);
        remoteFaas = faasModel.open(faas, true);
    }
    
    @AfterClass
    public static void tearDownClass() {
        Data.cleanUp();
        Data.cleanUp(true);
    }
    
    @Before
    public void setUp() {
        
    }
    
    @After
    public void tearDown() {
    }
    
    
    /*----------------------------------------------------------
     * LOCAL TEST 
     ----------------------------------------------------------*/
    
    @Test
    public void shouldCreateChangeInfo() {
        assertNotNull(changeInfo);
    }
            
    @Test
    public void shouldChangeTaxpayerInfo() {
        Map newInfo = (Map) changeInfo.get("newinfo");
        Map newTaxpayer = (Map) newInfo.get("taxpayer");
        Map changedTaxpayer = (Map) changeFaas.get("taxpayer");
        
        assertEquals("Should update taxpayer", newTaxpayer.get("objid"), changedTaxpayer.get("objid"));
    }
    
    @Test
    public void shouldChangeOwnerInfo() {
        Map newInfo = (Map) changeInfo.get("newinfo");
        Map newOwner = (Map) newInfo.get("owner");
        Map changedOwner = (Map) changeFaas.get("owner");
        
        assertEquals("Should update owner name", newOwner.get("name"), changedOwner.get("name"));
        assertEquals("Should update owner address", newOwner.get("address"), changedOwner.get("address"));
    }
    
    @Test
    public void shouldChangeAdministratorInfo() {
        Map newInfo = (Map) changeInfo.get("newinfo");
        Map newAdmin = (Map) newInfo.get("administrator");
        Map changeAdmin = (Map) changeFaas.get("administrator");
        
        assertEquals("Should update administrator name", newAdmin.get("name"), changeAdmin.get("name"));
        assertEquals("Should update administrator address", newAdmin.get("address"), changeAdmin.get("address"));
    }
    
    
    /*----------------------------------------------------------
     * REMOTE TEST 
     ----------------------------------------------------------*/
    
    @Test
    public void shouldCreateRemoteChangeInfo() {
        assertNotNull(remoteChangeInfo);
    }
            
    @Test
    public void shouldChangeRemoteTaxpayerInfo() {
        Map newInfo = (Map) remoteChangeInfo.get("newinfo");
        Map newTaxpayer = (Map) newInfo.get("taxpayer");
        Map changedTaxpayer = (Map) remoteFaas.get("taxpayer");
        
        assertEquals("Should update taxpayer", newTaxpayer.get("objid"), changedTaxpayer.get("objid"));
    }
    
    @Test
    public void shouldChangeRemoteOwnerInfo() {
        Map newInfo = (Map) remoteChangeInfo.get("newinfo");
        Map newOwner = (Map) newInfo.get("owner");
        Map changedOwner = (Map) remoteFaas.get("owner");
        
        assertEquals("Should update owner name", newOwner.get("name"), changedOwner.get("name"));
        assertEquals("Should update owner address", newOwner.get("address"), changedOwner.get("address"));
    }
    
    @Test
    public void shouldChangeRemoteAdministratorInfo() {
        Map newInfo = (Map) remoteChangeInfo.get("newinfo");
        Map newAdmin = (Map) newInfo.get("administrator");
        Map changeAdmin = (Map) remoteFaas.get("administrator");
        
        assertEquals("Should update administrator name", newAdmin.get("name"), changeAdmin.get("name"));
        assertEquals("Should update administrator address", newAdmin.get("address"), changeAdmin.get("address"));
    }
}
