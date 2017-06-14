/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.waterworks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author rameses
 */

public class TestMobileDownloadService extends AbstractTestCase {
    
    final LinkedBlockingQueue LOCK = new LinkedBlockingQueue(); 
    
    IService svc; 
    String userid; 
    
    public TestMobileDownloadService(String testName) {
        super(testName);
    }

    protected void afterSetup() { 
        svc = create("WaterworksMobileDownloadService", IService.class); 
        userid = "USR-56b200c1:153d499966d:-7fe9"; 
    }

    public String getAppHost() { return "localhost:8570"; } 
    
    public void xtestMain() throws Exception { 
        System.out.println("** getSectoryByUser ");
        Map params = new HashMap(); 
        params.put("userid", userid); 
        List<Map> sectors = svc.getSectorByUser( params ); 
        System.out.println( sectors );
        
        for ( Map map : sectors ) {
            Object sectorid = map.get("objid"); 
            params.put("sectorid", sectorid); 
            System.out.println("** getZoneBySector " + sectorid);
            System.out.println( svc.getZoneBySector( params )); 
            
            System.out.print("** getStuboutsBySector " + sectorid);
            List stubouts = svc.getStuboutsBySector( params );
            System.out.print("  ( TOTAL: " + stubouts.size() + " ) \n"); 
            if ( !stubouts.isEmpty() ) {
                System.out.println("  " + stubouts.get(0)); 
            }
            
            System.out.println("** getReaderBySector " + sectorid);
            System.out.println( svc.getReaderBySector( params )); 
        } 
    }

    public void testDownload() throws Exception { 
        System.out.println("** initForDownload ");
        Map params = new HashMap(); 
        params.put("assigneeid", userid); 
        params.put("sectorid", "C"); 
        String batchid = svc.initForDownload( params ); 
        System.out.println("  " + batchid); 
        
        System.out.println(" ");
        System.out.print("** getBatchStatus "); 
        int totalcount = -1; 
        while ( (totalcount=svc.getBatchStatus( batchid )) < 0 ) { 
            System.out.println(" fetching results... " );
            LOCK.poll(1000, TimeUnit.MILLISECONDS);  
        } 

        System.out.println(" TOTAL-COUNT is " + totalcount );
        if ( totalcount == 0 ) return; 
        
        int _start = 0;
        int _limit = 50;
        
        System.out.println("** download "); 
        params = new HashMap(); 
        params.put("batchid", batchid); 
        while ( _start < totalcount ) { 
            params.put("_start", _start); 
            params.put("_limit", _limit);    
            List list = svc.download( params ); 
            System.out.println("  fetched " + list.size()); 
            _start += _limit; 
        }
    }
    
    public interface IService {
        String initForDownload( Object o );
        int getBatchStatus( Object o ); 
        List download( Object o ); 

        List getSectorByUser( Object o ); 
        List getZoneBySector( Object o );
        List getStuboutsBySector( Object o );
        List getReaderBySector( Object o );
    }
}
