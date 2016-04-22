def env = [
    'app.host'    :'localhost:8070',
    'app.context' :'etracs25'
]

def proxy = new TestProxy(env);
def svc = proxy.create('ETRACS22To254SettingMigrationService');

def lgutype = 'CITY'

/*=== LAND ========================== */
svc.migrateLandSettings(lgutype);
svc.migrateLandAssessLevels();
svc.migrateSpecificClasses();
svc.migrateStrippings();
svc.migrateLandAdjustments();


/*=== BLDG ========================== */
svc.migrateBldgSettings(lgutype);
svc.migrateBldgAssessLevels();
svc.migrateBldgTypes();
svc.migrateBldgAdditionalItems();


/*=== MACH ========================== */
svc.migrateMachSettings(lgutype);
svc.migrateMachAssessLevels();
svc.migrateMachForexes();


/*=== PLANT/TREE ========================== */
svc.migratePlantTreeSettings(lgutype);
svc.migratePlantTreeUnitValues();


/*=== PLANT/TREE ========================== */
svc.migrateMiscSettings(lgutype);
svc.migrateMiscAssessLevels();
svc.migrateMiscItemUnitValues();


/*=== SETTING LGU ========================== */
svc.migrateLguRySettings(lgutype);


println 'done';