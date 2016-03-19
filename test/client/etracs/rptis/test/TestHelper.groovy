class TestHelper
{
    public static void waitForFaas(faas, helper){
        println 'Waiting for remote faas data...'  
        def available = helper.findFaas(faas)
        while(!available){
            sleep(3000)
            available = helper.findFaas(faas)
        }
    }

    public static void waitForCurrentFaas(faas, helper){
        println 'Waiting for remote current faas data...'  
        def available = helper.findFaas(faas)
        while(!available || !available.state.equalsIgnoreCase('CURRENT')){
            sleep(3000)
            available = helper.findFaas(faas)
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
