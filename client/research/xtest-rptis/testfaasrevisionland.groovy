def env = [
    'app.host'    :'localhost:8071',
    'app.context' :'etracs25',
    ORGID         : '063-06',
    ORGCODE       : '063-06',
    ORGCLASS      : 'MUNICIPALITY', 
    USERID        : 'USR-5596bc96:149114d7d7c:-4468',
    FULLNAME      : 'VINCENT AGDA',
    JOBTITLE      : 'LAOO-I',
]

def proxy = new TestProxy(env);
def svc = proxy.create('XFAASRevisionUnitTestService');
def utilSvc = proxy.create('XRPTISTestUtilService');

// def task = utilSvc.getCurrentFaasTask('FDC02-TR01-FAAS');

def task = svc.createBldgRevision();
task = svc.testReceive(task);
task = svc.testExamination(task);
task = svc.testTaxmapping(task);
task = svc.testTaxmappingApproval(task);

task = svc.testAppraisal(task);
task = svc.testAppraisalApproval(task);
task = svc.testRecommender(task);
task = svc.testMunicipalApproval(task);


println 'done';