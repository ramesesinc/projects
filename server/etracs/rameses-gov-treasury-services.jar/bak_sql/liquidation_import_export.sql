[findLiquidationByid]
SELECT * FROM liquidation WHERE objid = $P{liquidationid}

[getLiquidationCashierFund]
SELECT * FROM liquidation_fund WHERE liquidationid = $P{liquidationid}

[getLiquidationCheckPayment]
SELECT * FROM liquidation_noncashpayment WHERE liquidationid = $P{liquidationid}

[getLiquidationRemittance]
SELECT * FROM liquidation_remittance WHERE liquidationid = $P{liquidationid}


[getRemittanceRemittanceByLiquidation]
SELECT r.* FROM remittance r
  inner join liquidation_remittance lr on lr.objid = r.objid 
WHERE lr.liquidationid = $P{liquidationid}

[getRemittanceAfSerials]
SELECT ra.* FROM remittance_af ra 
  inner join  remittance r on r.objid = ra.remittanceid 
  inner join liquidation_remittance lr on lr.objid = r.objid 
WHERE lr.liquidationid = $P{liquidationid}

[getRemittanceCashReceipts]
SELECT rc.* FROM remittance_cashreceipt rc
  inner join  remittance r on r.objid = rc.remittanceid 
  inner join liquidation_remittance lr on lr.objid = r.objid 
WHERE lr.liquidationid = $P{liquidationid}



[getRemittanceCheckPayments]
SELECT rc.* FROM remittance_noncashpayment rc
  inner join  remittance r on r.objid = rc.remittanceid 
  inner join liquidation_remittance lr on lr.objid = r.objid 
WHERE lr.liquidationid = $P{liquidationid}


[getRemittanceFunds]
SELECT rc.* FROM remittance_fund rc
  inner join  remittance r on r.objid = rc.remittanceid 
  inner join liquidation_remittance lr on lr.objid = r.objid 
WHERE lr.liquidationid = $P{liquidationid}



[getUniqueAfControls]
SELECT DISTINCT aid.controlid, raf.remittanceid
FROM remittance_af raf 
  INNER JOIN af_inventory_detail aid ON raf.objid = aid.objid 
WHERE raf.remittanceid = $P{objid} 


[findRemittanceAfSerial]
SELECT objid FROM remittance_af WHERE objid = $P{objid}

[findAfSerialControl]
SELECT * FROM af_control WHERE controlid = $P{controlid}

[findAFSerialInventory]
SELECT * FROM af_inventory WHERE objid = $P{controlid}

[getAFSerialInventoryDetails]
SELECT * FROM af_inventory_detail WHERE objid = $P{objid}



[getCashReceipts]
SELECT cr.* 
FROM liquidation_remittance lr
  inner join remittance_cashreceipt remcr on lr.objid = remcr.remittanceid     
	INNER JOIN cashreceipt cr ON cr.objid = remcr.objid 
WHERE lr.liquidationid = $P{liquidationid}	

[getCashReceiptItems]
SELECT cri.* 
FROM liquidation_remittance lr
  inner join remittance_cashreceipt remcr on lr.objid = remcr.remittanceid     
  INNER JOIN cashreceipt cr ON cr.objid = remcr.objid 
  INNER JOIN cashreceiptitem cri ON cr.objid = cri.receiptid
WHERE lr.liquidationid = $P{liquidationid}  


[getCashReceiptCheckPayments]
SELECT cp.* 
FROM liquidation_remittance lr
  inner join remittance_cashreceipt remcr on lr.objid = remcr.remittanceid     
  INNER JOIN cashreceipt cr ON cr.objid = remcr.objid 
  INNER JOIN cashreceiptpayment_noncash cp ON cr.objid = cp.receiptid 
WHERE lr.liquidationid = $P{liquidationid}  


[getVoidedReceipts]
SELECT cv.* 
FROM liquidation_remittance lr
  inner join remittance_cashreceipt remcr on lr.objid = remcr.remittanceid     
  INNER JOIN cashreceipt_void cv ON remcr.objid = cv.receiptid
WHERE lr.liquidationid = $P{liquidationid}   


#===============================================================
#
# IMPORT SUPPORT 
#
#===============================================================
[insertLiquidation]
insert into liquidation
  (objid,
   state,
   txnno,
   dtposted,
   liquidatingofficer_objid,
   liquidatingofficer_name,
   liquidatingofficer_title,
   amount,
   totalcash,
   totalnoncash,
   cashbreakdown)
values (
  $P{objid},
  $P{state},
  $P{txnno},
  $P{dtposted},
  $P{liquidatingofficer_objid},
  $P{liquidatingofficer_name},
  $P{liquidatingofficer_title},
  $P{amount},
  $P{totalcash},
  $P{totalnoncash},
  $P{cashbreakdown}
 )

[insertLiquidationCashierFund]
insert into liquidation_fund
  (objid,
   liquidationid,
   fund_objid,
   fund_title,
   cashier_objid,
   cashier_name,
   amount,
   totalcash,
   totalnoncash,
   cashbreakdown)
values (
  $P{objid},
  $P{liquidationid},
  $P{fund_objid},
  $P{fund_title},
  $P{cashier_objid},
  $P{cashier_name},
  $P{amount},
  $P{totalcash},
  $P{totalnoncash},
  $P{cashbreakdown}
 )

[insertLiquidationChecks]
insert into liquidation_noncashpayment
  (objid,
   liquidationid,
   liquidationfundid)
values (
  $P{objid},
  $P{liquidationid},
  $P{liquidationfundid}
  )

[insertLiquidationRemittances]
insert into liquidation_remittance
  (objid,
   liquidationid)
values (
  $P{objid},
  $P{liquidationid}
  )


[insertRemittance]
INSERT INTO remittance(
  objid
  ,state
  ,txnno
  ,dtposted
  ,collector_objid
  ,collector_name
  ,collector_title
  ,liquidatingofficer_objid
  ,liquidatingofficer_name
  ,liquidatingofficer_title
  ,amount
  ,totalcash
  ,totalnoncash
  ,cashbreakdown
)  
VALUES (
  $P{objid}
  ,$P{state}
  ,$P{txnno}
  ,$P{dtposted}
  ,$P{collector_objid}
  ,$P{collector_name}
  ,$P{collector_title}
  ,$P{liquidatingofficer_objid}
  ,$P{liquidatingofficer_name}
  ,$P{liquidatingofficer_title}
  ,$P{amount}
  ,$P{totalcash}
  ,$P{totalnoncash}
  ,$P{cashbreakdown}
)


[insertRemittanceAf]
INSERT INTO remittance_af(
  objid
  ,remittanceid
)
VALUES(
  $P{objid}
  ,$P{remittanceid}
)


[insertRemittanceCashReceipt]
INSERT INTO remittance_cashreceipt(
  objid
  ,remittanceid
)
VALUES(
  $P{objid}
  ,$P{remittanceid}
)



[insertRemittanceCheck]
INSERT INTO remittance_noncashpayment(
  objid
  ,remittanceid
  ,amount
  ,voided
)
VALUES (
  $P{objid}
  ,$P{remittanceid}
  ,$P{amount}
  ,$P{voided}
)


[insertRemittanceFund]
INSERT INTO remittance_fund(
  objid
 ,remittanceid
 ,fund_objid
 ,fund_title
 ,amount
)
VALUES(
 $P{objid}
 ,$P{remittanceid}
 ,$P{fund_objid}
 ,$P{fund_title}
 ,$P{amount}
)



[updateAFControl]
UPDATE af_control SET 
   currentseries = $P{currentseries}
  ,qtyissued = $P{qtyissued}
  ,active = $P{active}
 WHERE controlid = $P{controlid} 


[updateAfInventory]
UPDATE af_inventory SET 
	 startseries = $P{startseries}
	,endseries = $P{endseries}
	,currentseries = $P{currentseries}
	,qtyin = $P{qtyin}
	,qtyout = $P{qtyout}
	,qtycancelled = $P{qtycancelled}
	,qtybalance = $P{qtybalance}
	,currentlineno = $P{currentlineno}
 WHERE objid = $P{objid}


[insertAfInventoryDetail]
INSERT INTO af_inventory_detail(
       objid
      ,controlid
      ,lineno
      ,refid
      ,refno
      ,reftype
      ,refdate
      ,txndate
      ,txntype
      ,receivedstartseries
      ,receivedendseries
      ,beginstartseries
      ,beginendseries
      ,issuedstartseries
      ,issuedendseries
      ,endingstartseries
      ,endingendseries
      ,cancelledstartseries
      ,cancelledendseries
      ,qtyreceived
      ,qtybegin
      ,qtyissued
      ,qtyending
      ,qtycancelled
      ,remarks
)
VALUES (
       $P{objid}
      ,$P{controlid}
      ,$P{lineno}
      ,$P{refid}
      ,$P{refno}
      ,$P{reftype}
      ,$P{refdate}
      ,$P{txndate}
      ,$P{txntype}
      ,$P{receivedstartseries}
      ,$P{receivedendseries}
      ,$P{beginstartseries}
      ,$P{beginendseries}
      ,$P{issuedstartseries}
      ,$P{issuedendseries}
      ,$P{endingstartseries}
      ,$P{endingendseries}
      ,$P{cancelledstartseries}
      ,$P{cancelledendseries}
      ,$P{qtyreceived}
      ,$P{qtybegin}
      ,$P{qtyissued}
      ,$P{qtyending}
      ,$P{qtycancelled}
      ,$P{remarks}
)



[insertCheckPayment]
INSERT INTO cashreceiptpayment_noncash(
  objid
  ,receiptid
  ,bank
  ,refno
  ,refdate
  ,amount
  ,particulars
)
VALUES(
   $P{objid}
  ,$P{receiptid}
  ,$P{bank}
  ,$P{checkno}
  ,$P{checkdate}
  ,$P{amount}
  ,$P{particulars}
)



[insertVoidReceipt]
INSERT INTO cashreceipt_void(
  objid
  ,receiptid
  ,txndate
  ,postedby_objid
  ,postedby_name
  ,reason
)
VALUES (
   $P{objid}
  ,$P{receiptid}
  ,$P{txndate}
  ,$P{postedby_objid}
  ,$P{postedby_name}
  ,$P{reason}
)



