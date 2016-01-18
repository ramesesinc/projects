def env = [
    'app.host'    :'localhost:8072',
    'app.context' :'etracs25',
    ORGID         : '063',
    ORGCODE       : '063',
    ORGCLASS      : 'PROVINCE',
    USERID        : 'USR3cc6f7a7:147b4d6b0d8:-7f5b',
    FULLNAME      : 'VINCENT AGDA',
    JOBTITLE      : 'LAOO-I',
]

def proxy = new TestProxy(env);
def svc = proxy.create('XConsolidationProvinceUnitTestService');

def entity = [objid:'CS73d708d2:14df004c54f:-7f27'];
def task = svc.getCurrentConsolidationTask(entity);

task = svc.testReceive(task);
task = svc.testExamination(task);
task = svc.testTaxmapping(task);
task = svc.testTaxmappingApproval(task);
task = svc.testAppraisal(task);
task = svc.testAppraisalApproval(task);
task = svc.testProvApproval(task);

println 'done';