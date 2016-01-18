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
def svc = proxy.create('XRPTISTestUtilService');

def task = svc.getCurrentFaasTaskByRefId('F-1af3e78e:14e6b46872c:-7fd9');

//task = svc.testTaxmapping(task);
task = svc.testTaxmappingApproval(task);
task = svc.testAppraisal(task);
task = svc.testAppraisalApproval(task);
task = svc.testRecommender(task);
//task = svc.testMunicipalApproval(task);


println 'done';