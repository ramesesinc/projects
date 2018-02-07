[getReceiptsWithUnpostedShares]
select 
	o.*, a.amount 
from (
	select 
		cr.objid,
		rl.objid as rptledgerid, 
		b.parentid as lguid,
		rl.barangayid,
		cr.receiptno,
		sum(ri.total) as amount 
	from remittance rem 
		inner join liquidation_remittance liqr on rem.objid = liqr.objid 
		inner join liquidation liq on liqr.liquidationid = liq.objid
		inner join remittance_cashreceipt remc on rem.objid = remc.remittanceid 
		inner join cashreceipt cr on remc.objid = cr.objid 
		inner join cashreceiptitem_rpt_online ri on cr.objid = ri.rptreceiptid 
		left join rptledger rl ON ri.rptledgerid = rl.objid  
		left join barangay b on rl.barangayid = b.objid 
		left join propertyclassification pc ON rl.classification_objid = pc.objid 
	where rem.remittancedate >= $P{remfromdate} and rem.remittancedate < $P{remtodate}
		and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid) 
	group by cr.receiptno 
)o
left join 
(
	select 
		cr.objid,
		rl.objid as rptledgerid, 
		b.parentid as lguid,
		rl.barangayid,
		cr.receiptno, 
		sum(ri.amount )  as amount 
	from remittance rem 
		inner join liquidation_remittance liqr on rem.objid = liqr.objid 
		inner join liquidation liq on liqr.liquidationid = liq.objid
		inner join remittance_cashreceipt remc on rem.objid = remc.remittanceid 
		inner join cashreceipt cr on remc.objid = cr.objid 
		inner join cashreceiptitem_rpt_account ri on cr.objid = ri.rptreceiptid 
		left join rptledger rl ON ri.rptledgerid = rl.objid  
		left join barangay b on rl.barangayid = b.objid 
		left join propertyclassification pc ON rl.classification_objid = pc.objid 
	where rem.remittancedate >= $P{remfromdate} and rem.remittancedate < $P{remtodate}
		and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid) 
  group by cr.receiptno 
) a on o.receiptno = a.receiptno
where (a.receiptno is null  or o.amount <> a.amount)


[findShareAccount]
select 
	x.*
from (
	select 'barangay' as sharetype, 'basic' as revtype, 'advance' as revperiod, basicadvacct_objid as item_objid from brgy_taxaccount_mapping
	union 
	select 'barangay' as sharetype, 'basic' as revtype, 'previous' as revperiod, basicprevacct_objid as item_objid from brgy_taxaccount_mapping
	union 
	select 'barangay' as sharetype, 'basicint' as revtype, 'previous' as revperiod, basicprevintacct_objid as item_objid from brgy_taxaccount_mapping
	union 
	select 'barangay' as sharetype, 'basic' as revtype, 'prior' as revperiod, basicprioracct_objid as item_objid from brgy_taxaccount_mapping
	union 
	select 'barangay' as sharetype, 'basicint' as revtype, 'prior' as revperiod, basicpriorintacct_objid as item_objid from brgy_taxaccount_mapping
	union 
	select 'barangay' as sharetype, 'basic' as revtype, 'current' as revperiod, basiccurracct_objid as item_objid from brgy_taxaccount_mapping
	union 
	select 'barangay' as sharetype, 'basicint' as revtype, 'current' as revperiod, basiccurrintacct_objid as item_objid from brgy_taxaccount_mapping
	union 
	select 'municipality' as sharetype, 'basic' as revtype, 'advance' as revperiod, basicadvacct_objid as item_objid from municipality_taxaccount_mapping
	union 
	select 'municipality' as sharetype, 'basic' as revtype, 'previous' as revperiod, basicprevacct_objid as item_objid from municipality_taxaccount_mapping
	union 
	select 'municipality' as sharetype, 'basicint' as revtype, 'previous' as revperiod, basicprevintacct_objid as item_objid from municipality_taxaccount_mapping
	union 
	select 'municipality' as sharetype, 'basic' as revtype, 'prior' as revperiod, basicprioracct_objid as item_objid from municipality_taxaccount_mapping
	union 
	select 'municipality' as sharetype, 'basicint' as revtype, 'prior' as revperiod, basicpriorintacct_objid as item_objid from municipality_taxaccount_mapping
	union 
	select 'municipality' as sharetype, 'basic' as revtype, 'current' as revperiod, basiccurracct_objid as item_objid from municipality_taxaccount_mapping
	union 
	select 'municipality' as sharetype, 'basicint' as revtype, 'current' as revperiod, basiccurrintacct_objid as item_objid from municipality_taxaccount_mapping
	union
	select 'municipality' as sharetype, 'sef' as revtype, 'advance' as revperiod, sefadvacct_objid as item_objid from municipality_taxaccount_mapping
	union 
	select 'municipality' as sharetype, 'sef' as revtype, 'previous' as revperiod, sefprevacct_objid as item_objid from municipality_taxaccount_mapping
	union 
	select 'municipality' as sharetype, 'sefint' as revtype, 'previous' as revperiod, sefprevintacct_objid as item_objid from municipality_taxaccount_mapping
	union 
	select 'municipality' as sharetype, 'sef' as revtype, 'prior' as revperiod, sefprioracct_objid as item_objid from municipality_taxaccount_mapping
	union 
	select 'municipality' as sharetype, 'sefint' as revtype, 'prior' as revperiod, sefpriorintacct_objid as item_objid from municipality_taxaccount_mapping
	union 
	select 'municipality' as sharetype, 'sef' as revtype, 'current' as revperiod, sefcurracct_objid as item_objid from municipality_taxaccount_mapping
	union 
	select 'municipality' as sharetype, 'sefint' as revtype, 'current' as revperiod, sefcurrintacct_objid as item_objid from municipality_taxaccount_mapping
	union
	select 'municipality' as sharetype, 'basicidle' as revtype, 'advance' as revperiod, basicidleadvacct_objid as item_objid from municipality_taxaccount_mapping
	union 
	select 'municipality' as sharetype, 'basicidle' as revtype, 'previous' as revperiod, basicidleprevacct_objid as item_objid from municipality_taxaccount_mapping
	union 
	select 'municipality' as sharetype, 'basicidleint' as revtype, 'previous' as revperiod, basicidleprevintacct_objid as item_objid from municipality_taxaccount_mapping
	union 
	select 'municipality' as sharetype, 'basicidle' as revtype, 'current' as revperiod, basicidlecurracct_objid as item_objid from municipality_taxaccount_mapping
	union 
	select 'municipality' as sharetype, 'basicidleint' as revtype, 'current' as revperiod, basicidlecurrintacct_objid as item_objid from municipality_taxaccount_mapping
	union 
	select 'province' as sharetype, 'basic' as revtype, 'advance' as revperiod, basicadvacct_objid as item_objid from province_taxaccount_mapping
	union 
	select 'province' as sharetype, 'basic' as revtype, 'previous' as revperiod, basicprevacct_objid as item_objid from province_taxaccount_mapping
	union 
	select 'province' as sharetype, 'basicint' as revtype, 'previous' as revperiod, basicprevintacct_objid as item_objid from province_taxaccount_mapping
	union 
	select 'province' as sharetype, 'basic' as revtype, 'prior' as revperiod, basicprioracct_objid as item_objid from province_taxaccount_mapping
	union 
	select 'province' as sharetype, 'basicint' as revtype, 'prior' as revperiod, basicpriorintacct_objid as item_objid from province_taxaccount_mapping
	union 
	select 'province' as sharetype, 'basic' as revtype, 'current' as revperiod, basiccurracct_objid as item_objid from province_taxaccount_mapping
	union 
	select 'province' as sharetype, 'basicint' as revtype, 'current' as revperiod, basiccurrintacct_objid as item_objid from province_taxaccount_mapping
	union
	select 'province' as sharetype, 'sef' as revtype, 'advance' as revperiod, sefadvacct_objid as item_objid from province_taxaccount_mapping
	union 
	select 'province' as sharetype, 'sef' as revtype, 'previous' as revperiod, sefprevacct_objid as item_objid from province_taxaccount_mapping
	union 
	select 'province' as sharetype, 'sefint' as revtype, 'previous' as revperiod, sefprevintacct_objid as item_objid from province_taxaccount_mapping
	union 
	select 'province' as sharetype, 'sef' as revtype, 'prior' as revperiod, sefprioracct_objid as item_objid from province_taxaccount_mapping
	union 
	select 'province' as sharetype, 'sefint' as revtype, 'prior' as revperiod, sefpriorintacct_objid as item_objid from province_taxaccount_mapping
	union 
	select 'province' as sharetype, 'sef' as revtype, 'current' as revperiod, sefcurracct_objid as item_objid from province_taxaccount_mapping
	union 
	select 'province' as sharetype, 'sefint' as revtype, 'current' as revperiod, sefcurrintacct_objid as item_objid from province_taxaccount_mapping
	union
	select 'province' as sharetype, 'basicidle' as revtype, 'advance' as revperiod, basicidleadvacct_objid as item_objid from province_taxaccount_mapping
	union 
	select 'province' as sharetype, 'basicidle' as revtype, 'previous' as revperiod, basicidleprevacct_objid as item_objid from province_taxaccount_mapping
	union 
	select 'province' as sharetype, 'basicidleint' as revtype, 'previous' as revperiod, basicidleprevintacct_objid as item_objid from province_taxaccount_mapping
	union 
	select 'province' as sharetype, 'basicidle' as revtype, 'current' as revperiod, basicidlecurracct_objid as item_objid from province_taxaccount_mapping
	union 
	select 'province' as sharetype, 'basicidleint' as revtype, 'current' as revperiod, basicidlecurrintacct_objid as item_objid from province_taxaccount_mapping
	union 
	select 'province' as sharetype, 'basic' as revtype, 'advance' as revperiod, 'REVITEM-1a70d4ec:147f0b16b58:-6297'
	union 
	select 'province' as sharetype, 'basic' as revtype, 'previous' as revperiod, 'REVITEM-1a70d4ec:147f0b16b58:-6265'
	union 
	select 'province' as sharetype, 'basicint' as revtype, 'previous' as revperiod, 'REVITEM-1a70d4ec:147f0b16b58:-6218'
	union 
	select 'province' as sharetype, 'basic' as revtype, 'prior' as revperiod, 'ITMACCT-118466fe:15a1b092350:-1fa1'
	union 
	select 'province' as sharetype, 'basicint' as revtype, 'prior' as revperiod, 'ITMACCT-118466fe:15a1b092350:-1f31'
	union 
	select 'province' as sharetype, 'basic' as revtype, 'current' as revperiod, 'REVITEM-1a70d4ec:147f0b16b58:-63ff'
	union 
	select 'province' as sharetype, 'basicint' as revtype, 'current' as revperiod, 'REVITEM-1a70d4ec:147f0b16b58:-6334'
	union
	select 'province' as sharetype, 'sef' as revtype, 'advance' as revperiod, 'REVITEM-1a70d4ec:147f0b16b58:-6072'
	union 
	select 'province' as sharetype, 'sef' as revtype, 'previous' as revperiod, 'REVITEM-1a70d4ec:147f0b16b58:-5f99'
	union 
	select 'province' as sharetype, 'sefint' as revtype, 'previous' as revperiod, 'REVITEM-1a70d4ec:147f0b16b58:-5f67'
	union 
	select 'province' as sharetype, 'sef' as revtype, 'prior' as revperiod, 'ITMACCT-118466fe:15a1b092350:-1ef4'
	union 
	select 'province' as sharetype, 'sefint' as revtype, 'prior' as revperiod, 'ITMACCT-118466fe:15a1b092350:-1ec5'
	union 
	select 'province' as sharetype, 'sef' as revtype, 'current' as revperiod, 'REVITEM-1a70d4ec:147f0b16b58:-60d8'
	union 
	select 'province' as sharetype, 'sefint' as revtype, 'current' as revperiod, 'REVITEM-1a70d4ec:147f0b16b58:-60a5'
) x
where x.item_objid = $P{itemid}

