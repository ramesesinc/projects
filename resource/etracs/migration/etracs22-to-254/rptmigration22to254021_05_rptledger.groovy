def env = [
'app.host':'localhost:8071',
'app.context':'etracs25'
]

def proxy = new TestProxy(env);
def svc = proxy.create('ETRACS22To254RPTLedgerMigrationService');

void migrate(svc, params){
  def size = svc.migrateLedgers(params);
  while( size > 0 ){
    params.migrated += size;
    println 'Migrated Annotation Count -> ' + params.migrated
    size = svc.migrateLedgers(params);  
  }
}

svc.initMigrationTables();

def status = svc.getMigrationStatus()
while(status == 'PROCESSING'){
    println 'status -> ' + status 
    try {sleep(5000) }catch(e){}
    status = svc.getMigrationStatus()
}

def params = [migrated:0, count:25]
migrate(svc, params)

print 'Migration completed.'