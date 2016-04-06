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

def params = [rputype:'bldg', count:count, migrated:0]
migrate(svc, params)
println 'Done migrating bldg...'

print 'Migration completed.'