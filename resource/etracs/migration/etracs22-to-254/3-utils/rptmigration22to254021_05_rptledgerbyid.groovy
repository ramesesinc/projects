def env = [
'app.host':'localhost:8070',
'app.context':'etracs25'
]

def proxy = new TestProxy(env);
def svc = proxy.create('ETRACS22To254RPTLedgerMigrationService');

svc.migrateLedgerByIds([
'01-0012-00267'
]);

print 'Migration completed.'