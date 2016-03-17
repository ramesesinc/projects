class ProvinceTestProxy
{
    private def env 
    private def proxy = null
    private static ProvinceTestProxy instance = null
    
    private ProvinceTestProxy(){
        env = [
            'app.host'    :'localhost:8072',
            'app.context' :'etracs25',
            ORGID         : '063',
            ORGCODE       : '063',
            ORGCLASS      : 'PROVINCE', 
            USERID        : 'USR3cc6f7a7:147b4d6b0d8:-7f5b',
            USER          : 'VINZ',    
            FULLNAME      : 'VINCENT AGDA',
            JOBTITLE      : 'LAOO-I',
        ]
        proxy = new TestScriptProxy(env)
    }

    public def createService(serviceName){
        return proxy.create(serviceName)
    }

    public static def create(serviceName){
        if (!instance){
            instance = new ProvinceTestProxy();
        }
        return instance.createService(serviceName)
    }


    public static void waitForFaas(faas, helper){
        println 'Waiting for faas data...'  
        def available = helper.findFaas(faas)
        while(!available){
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
