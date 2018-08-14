[insertRemittanceFund]
INSERT INTO eor_remittance_fund
(objid,remittanceid,fundid,amount)

SELECT CONCAT( eor.remittanceid, ':', eoi.item_fund_objid ) , 
   eor.remittanceid,
   eoi.item_fund_objid,
   SUM(eoi.amount)
FROM eor_item eoi 
INNER JOIN eor eor ON eoi.parentid = eor.objid 
WHERE eor.remittanceid = $P{remittanceid}
GROUP BY eor.remittanceid, eoi.item_fund_objid


[getItemsForIncomeSummary]
SELECT a.* FROM 
(
SELECT 
    rem.objid AS refid,
    rem.controlno AS refno,
    rem.controldate AS reftdate,
    'eor_remittance' AS reftype,
    eori.item_objid, 
    eori.item_code,
    eori.item_title,
    eori.item_fund_objid AS fund_objid,
    SUM(eori.amount) AS amount 
FROM eor_item eori
INNER JOIN eor eor ON eor.objid = eori.parentid
INNER JOIN eor_remittance rem ON eor.remittanceid = rem.objid
WHERE eor.remittanceid = $P{remittanceid} 
GROUP BY rem.objid, rem.controlno, rem.controldate,  eori.item_objid, eori.item_code,  eori.item_title, eori.item_fund_objid

UNION ALL 

SELECT 
    rem.objid AS refid,
    rem.controlno AS refno,
    rem.controldate AS reftdate,
    'eor_remittance' AS reftype,
    eori.refitem_objid AS item_objid, 
    eori.refitem_code AS item_code,
    eori.refitem_title AS item_title,
    eori.refitem_fund_objid AS fund_objid,
    SUM(eori.amount * -1) AS amount 
FROM eor_share eori
INNER JOIN eor eor ON eor.objid = eori.parentid
INNER JOIN eor_remittance rem ON eor.remittanceid = rem.objid
WHERE eor.remittanceid = $P{remittanceid} 
GROUP BY rem.objid, rem.controlno, rem.controldate,  eori.item_objid, eori.item_code,  eori.item_title, eori.item_fund_objid
) a


[getItemsForPayableSummary]
SELECT 
    rem.objid AS refid,
    rem.controlno AS refno,
    rem.controldate AS reftdate,
    'eor_remittance' AS reftype,
    eori.payableitem_objid AS item_objid, 
    eori.payableitem_code AS item_code,
    eori.payableitem_title AS item_title,
    eori.payableitem_fund_objid AS fund_objid,
    SUM(eori.amount) AS amount 
FROM eor_share eori
INNER JOIN eor eor ON eor.objid = eori.parentid
INNER JOIN eor_remittance rem ON eor.remittanceid = rem.objid
WHERE eor.remittanceid = $P{remittanceid} 
GROUP BY rem.objid, rem.controlno, rem.controldate,  eori.item_objid, eori.item_code,  eori.item_title, eori.item_fund_objid

[getIncomeLedgerItems]
SELECT 
    a.fundid, a.itemacctid, SUM(a.dr) AS dr, SUM(a.cr) AS cr, 
    'income_ledger' AS _schemaname  
FROM ( 
    select 
        ei.item_fund_objid as fundid, 
        ei.item_objid as itemacctid, 
        0.0 as dr, sum(ei.amount) as cr 
  from eor 
        inner join eor_item ei on ei.parentid = eor.objid 
    where eor.remittanceid = $P{remittanceid} 
    group by ei.item_fund_objid, ei.item_objid

  UNION ALL

    select fundid, itemacctid, sum(dr) as dr, 0.0 as cr 
    from ( 
        select 
            (
                select min(item_fund_objid) from eor_item 
                where parentid = ei.parentid and item_objid = ei.refitem_objid 
            ) as fundid, 
            ei.refitem_objid as itemacctid, 
            ei.amount as dr 
        from eor 
            inner join eor_share ei on ei.parentid = eor.objid 
        where eor.remittanceid = $P{remittanceid} 
    )tmp1 
    group by fundid, itemacctid 
) a 
GROUP BY a.fundid, a.itemacctid


[getPayableLedgerItems]
select 
    fundid, refitemacctid, itemacctid, 0.0 as dr, sum(amount) as cr, 
    'payable_ledger' AS _schemaname  
from ( 
    select 
        (
            select min(item_fund_objid) from eor_item 
            where parentid = ei.parentid and item_objid = ei.refitem_objid  
        ) as fundid,
        ei.refitem_objid as refitemacctid, 
        ei.payableitem_objid as itemacctid, 
        ei.amount
    from eor 
        inner join eor_share ei on ei.parentid = eor.objid 
    where eor.remittanceid = $P{remittanceid} 
)tmp1 
group by fundid, refitemacctid, itemacctid 

