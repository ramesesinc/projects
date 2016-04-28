[getExaminationFindings]
select x.* 
from (
	select 
		ef.dtinspected, ef.findings, ef.recommendations, ef.notedby, ef.notedbytitle,
		f.tdno as refno, 
		null  as refnoprefix
	from examiner_finding ef
		inner join faas f on ef.parent_objid = f.objid 
	where f.lguid = $P{lguid}
    and ef.dtinspected >= $P{startdate} and ef.dtinspected < $P{enddate}

	union all 

	select 
		ef.dtinspected, ef.findings, ef.recommendations, ef.notedby, ef.notedbytitle,
		s.txnno as refno,
		'SD #'  as refnoprefix
	from examiner_finding ef
		inner join subdivision s on ef.parent_objid = s.objid 
	where s.lguid = $P{lguid}
    and ef.dtinspected >= $P{startdate} and ef.dtinspected < $P{enddate}

	union all 

	select 
		ef.dtinspected, ef.findings, ef.recommendations, ef.notedby, ef.notedbytitle,
		c.txnno as refno,
		'CS #'  as refnoprefix
	from examiner_finding ef
		inner join consolidation c on ef.parent_objid = c.objid 
	where c.lguid = $P{lguid}
    and ef.dtinspected >= $P{startdate} and ef.dtinspected < $P{enddate}
) x
order by x.dtinspected
