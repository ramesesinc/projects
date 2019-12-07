[findFranchise]
select 
	franchiseid,
	franchiseno,
	zoningcode
from franchise 
where franchiseno = $P{franchiseno}
and zoningcode = $P{zoningcode}


[getPayables]
select 
	f.franchiseid, 
	f.zoningcode, 
	f.franchiseno,
	p.cyear, 
	p.amount, 
	p.regularfee, 
	fl.feeid as fee_feeid,
	fl.description as fee_description,
	fl.acctcode as fee_acctcode,
	fl.bclass as fee_bclass,
	op.lastname + ', ' + op.firstname + ' ' + op.middlename as operator_name,
	(
		case when len(ltrim(op.street)) > 0 then op.street + ', ' else '' end +
		case when len(ltrim(op.barangay)) > 0 then op.barangay + ', ' else '' end +
		op.city
	) as operator_address,
	t.plateno as operator_plateno ,
	p.cyear as remarks
from franchise f
inner join orderofpayment p on f.franchiseid = p.franchiseid 
inner join FeeLibrary fl on p.feeid = fl.feeid 
inner join ownership o on p.oid = o.oid 
inner join operator op on o.optrid = op.optrid 
inner join FranchiseTricycleUnit ftu on f.franchiseid = ftu.franchiseid
inner join Tricycle t on ftu.trikeid = t.trikeid 
where p.franchiseid = $P{franchiseid}
and p.paidind = '0'
and ftu.RecStatus = 'A'
order by p.cyear, p.feeid 


[findViewAccount]
select * 
from zview_accounts 
where acctcode = $P{acctcode} and bcode = $P{bclass}

[findViewAccountByAcctId]
select *, bcode as bclass  
from zview_accounts 
where acctid = $P{acctid}


[updatePaidStatus]
update OrderOfPayment set 
	paidind = 1
where franchiseid = $P{franchiseid}
and cyear = $P{cyear}
and feeid = $P{feeid}


[findAf51]
select 
	or_id as orid 
from AF51 
where orno = $P{orno}
and ordate = $P{ordate}
and payee = $P{payee}

[getPaidOrderOfPayments]
select 
	franchiseid,
	cyear,
	feeid
from AF51_Tricycle 
where or_id = $P{orid}

[resetPaidStatus]
update OrderOfPayment set 
	paidind = 0
where franchiseid = $P{franchiseid}
and cyear = $P{cyear}
and feeid = $P{feeid}

[findExpiry]
select * from zz_mtop_expiry where year = $P{year}