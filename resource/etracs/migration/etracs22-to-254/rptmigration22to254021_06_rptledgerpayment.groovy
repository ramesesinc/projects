def env = [
'app.host':'localhost:8070',
'app.context':'etracs25'
]

def proxy = new TestProxy(env);
def svc = proxy.create('ETRACS22To254RPTLedgerMigrationService');

svc.initPaymentMigrationTables();

void migrate(svc, params){
  def size = svc.migrateLedgerPayments(params);
  while( size > 0 ){
    params.migrated += size;
    println 'Migrated Ledger Payment Count -> ' + params.migrated
    size = svc.migrateLedgerPayments(params);  
  }
}

svc.initPaymentMigrationTables();

def status = svc.getPaymentMigrationStatus()
while(status == 'PROCESSING'){
    println 'status -> ' + status 
    try {sleep(5000) }catch(e){}
    status = svc.getPaymentMigrationStatus()
}

def params = [migrated:0, count:10]
migrate(svc, params)

print 'Migration completed.'