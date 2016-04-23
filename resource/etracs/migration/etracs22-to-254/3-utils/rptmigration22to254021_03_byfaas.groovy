def env = [
'app.host':'localhost:8070',
'app.context':'etracs25'
]

def proxy = new TestProxy(env);
def svc = proxy.create('ETRACS22To254FaasMigrationService');

svc.migrateFaasByTdNo('02-0023-01482');

print 'Migration completed.'