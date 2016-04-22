def env = [
'app.host':'localhost:8070',
'app.context':'etracs25'
]

def proxy = new TestProxy(env);
def svc = proxy.create('ETRACS22To254FaasMigrationService');

def count = 10

void migrate(svc, params){
  def size = svc.migrateFaasData(params);
  while( size > 0 ){
    params.migrated += size;
    println 'Migrated Count -> ' + params.migrated
    size = svc.migrateFaasData(params);  
  }
}

//init migration table
println 'Initializing migration'
svc.initMigrationTables();
svc.insertXFaasData();
svc.deleteXFaasMigratedData();

def params = [rputype:'land', count:count, migrated:0]
migrate(svc, params)
println 'Done migrating land...'

/*

params.rputype = 'bldg'
migrate(svc, params)
println 'Done migrating bldg...'

params.rputype = 'mach'
migrate(svc, params)
println 'Done migrating mach...'


void migratePrevFaas(svc, params){
  def size = svc.migratePreviousFaasData(params);
  while( size > 0 ){
    params.migrated += size;
    println 'Migrated Count -> ' + params.migrated
    size = svc.migratePreviousFaasData(params);  
  }
}

params = [count:count, migrated:0]
migratePrevFaas(svc, params)

*/
print 'Migration completed.'