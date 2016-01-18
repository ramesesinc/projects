def env = [
'app.host':'localhost:8071',
'app.context':'etracs25'
]

def proxy = new TestProxy(env);
def svc = proxy.create('ETRACS22To254FaasMigrationService');

def list = [
    [objid:'F-4a978dbc:13955dbe79f:-7ddf']
]

list.each{
   svc.migrateFaas(it);
}
println 'done'