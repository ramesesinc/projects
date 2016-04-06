def env = [
'app.host':'localhost:8070',
'app.context':'etracs25'
]

def proxy = new TestProxy(env);
def svc = proxy.create('ETRACS22To254RPTCompromiseMigrationService');

void migrate(svc, params){
  def size = svc.migrateCompromises(params);
  while( size > 0 ){
    params.migrated += size;
    println 'Migrated Ledger Compromises Count -> ' + params.migrated
    size = svc.migrateCompromises(params);  
  }
}

svc.initMigrationTables();


def status = svc.getMigrationStatus()
while(status == 'PROCESSING'){
    println 'status -> ' + status 
    try {sleep(5000) }catch(e){}
    status = svc.getMigrationStatus()
}

def params = [migrated:0, count:10]
migrate(svc, params)

print 'Migration completed.'