[getList]
select * 
from rpt_redflag 
where parentid = $P{parentid}
order by filedby_date desc 


[findByCaseNo]
select * from rpt_redflag where caseno = $P{caseno}
