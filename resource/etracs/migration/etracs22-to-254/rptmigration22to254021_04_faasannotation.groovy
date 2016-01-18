def env = [
'app.host':'localhost:8071',
'app.context':'etracs25'
]

def proxy = new TestProxy(env);
def svc = proxy.create('ETRACS22To254FaasMigrationService');


void migrate(svc, params){
  def size = svc.migrateFaasAnnotations(params);
  while( size > 0 ){
    params.migrated += size;
    println 'Migrated Annotation Count -> ' + params.migrated
    size = svc.migrateFaasAnnotations(params);  
  }
}

svc.initAnnotationMigrationTables();

def params = [migrated:0, count:25]
migrate(svc, params)

print 'Migration completed.'