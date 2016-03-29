class TestHelper
{
    public static void waitForFaas(faas, helper){
        println 'Waiting for remote faas data -> ' + faas.objid 
        def available = helper.findFaas(faas)
        while(!available){
            sleep(3000)
            available = helper.findFaas(faas)
        }
    }

    public static void waitForCurrentFaas(faas, helper){
        println 'Waiting for remote current faas data -> ' + faas.objid 
        def available = helper.findFaas(faas)
        while(!available || !available.state.equalsIgnoreCase('CURRENT')){
            sleep(3000)
            available = helper.findFaas(faas)
        }
    }


    public static void waitForSubdivision(subdivision, helper){
        println 'Waiting for remote subdivision data -> ' + subdivision.objid 
        def available = helper.findSubdivision(subdivision)
        while(!available){
            sleep(3000)
            available = helper.findSubdivision(subdivision)
        }
    }

    public static void waitForApprovedSubdivision(subdivision, helper){
        println 'Waiting for remote approved subdivision -> ' + subdivision.objid 
        def available = helper.findSubdivision(subdivision)
        while(!available || !available.state.equalsIgnoreCase('APPROVED')){
            sleep(3000)
            available = helper.findSubdivision(subdivision)
        }
    }

    public static void waitForConsolidation(consolidation, helper){
        println 'Waiting for remote consolidation data -> ' + consolidation.objid 
        def available = helper.findConsolidation(consolidation)
        while(!available){
            sleep(3000)
            available = helper.findConsolidation(consolidation)
        }
    }

    public static void waitForApprovedConsolidation(consolidation, helper){
        println 'Waiting for remote approved consolidation -> ' + consolidation.objid 
        def available = helper.findConsolidation(consolidation)
        while(!available || !available.state.equalsIgnoreCase('APPROVED')){
            sleep(3000)
            available = helper.findConsolidation(consolidation)
        }
    }

    public static void sleep(ms){
        try{ 
            Thread.sleep(ms)
        }catch(e){
            println e.printStackTrace()
        }
    }
}
