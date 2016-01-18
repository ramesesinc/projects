def env = [
    'app.host'    :'localhost:8071',
    'app.context' :'etracs25'
]

def proxy = new TestProxy(env);
def svc = proxy.create('ETRACS22To254MasterMigrationService');

svc.createMigrationRefTables();
svc.migrateOrg('MUNICIPALITY');
svc.migratePropertyClassifications();
svc.migrateExemptionTypes();
svc.migrateCancelTDReasons();
svc.migrateBldgKinds();
svc.migrateMaterials();
svc.migrateStructures();
svc.migrateMachines();
svc.migratePlantTrees();
svc.migrateMiscItems();
svc.migrateRPTParameters();


println 'done';

