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
def svc = proxy.create('XFAASDataCaptureUnitTestService');

svc.testApproveDataCapture('FDC01');

println 'done';