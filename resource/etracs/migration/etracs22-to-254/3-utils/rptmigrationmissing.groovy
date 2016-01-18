def env = [
'app.host':'192.168.1.3:8070',
'app.context':'etracs25'
]

def proxy = new TestProxy(env);
def svc = proxy.create('ETRACS22To254MigrationService');



def list = [
    [objid:'F48285348:144cd4ab617:-7707'],
    [objid:'F645c710f:13e1038b101:-798d'],
    [objid:'F645c710f:13e1038b101:-7a0c'],
    [objid:'F645c710f:13e1038b101:-7a17'],
]


list.each{
    try{
        println 'Migrating FAAS ' + it.objid 
        svc.migrateFaas(it)
    }
    catch(e){
        println e.message 
    }
}