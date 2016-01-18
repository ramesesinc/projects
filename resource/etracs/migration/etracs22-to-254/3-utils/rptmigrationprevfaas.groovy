def env = [
'app.host':'192.168.1.3:8070',
'app.context':'etracs25'
]

def proxy = new TestProxy(env);
def svc = proxy.create('ETRACS22To254MigrationService');



void migrate(svc, params){
  def size = svc.migratePreviousFaasData(params);
  while( size > 0 ){
    params.migrated += size;
    println 'Migrated Count -> ' + params.migrated
    size = svc.migratePreviousFaasData(params);  
  }
}

def params = [count:50, migrated:0]
migrate(svc, params)

print 'Migration completed.'