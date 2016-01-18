def env = [
'app.host':'localhost:8071',
'app.context':'etracs25'
]

def proxy = new TestProxy(env);
def svc = proxy.create('ETRACS22To254RPTLedgerMigrationService');

svc.initMigrationTables();
svc.migrateLedgerByIds([
    'L6e41c57f:1396a7ea98c:-71c7'
]);

print 'Migration completed.'