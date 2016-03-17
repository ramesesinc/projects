class MunicipalityTestProxy
{
    private def env 
    private def proxy = null
    private static MunicipalityTestProxy instance = null
    
    private MunicipalityTestProxy(){
        env = [
            'app.host'    :'localhost:8071',
            'app.context' :'etracs25',
            ORGID         : '063-06',
            ORGCODE       : '063-06',
            ORGCLASS      : 'MUNICIPALITY', 
            USERID        : 'USR7e15465b:14a51353b1a:-7fb7',
            USER          : 'ADMIN',    
            FULLNAME      : 'ADMINISTRATOR',
            JOBTITLE      : 'ADMIN',
        ]
        proxy = new TestScriptProxy(env)
    }

    public def createService(serviceName){
        return proxy.create(serviceName)
    }

    public static def create(serviceName){
        if (!instance){
            instance = new MunicipalityTestProxy();
        }
        return instance.createService(serviceName)
    }
}
