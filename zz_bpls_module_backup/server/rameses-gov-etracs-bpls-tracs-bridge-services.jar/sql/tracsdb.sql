[getList]
SELECT * FROM (
   SELECT 
      b.ObjID AS objid, 
      b.strTradeName AS businessname, 
      b.strTradeName AS tradename, 
      strBusinessAddress AS address,
      ta.intyear AS activeyear, 
      t.strTaxpayer AS name, 
      t.strTaxpayerID AS ownerid, 
      t.strTaxpayer AS ownername, 
      t.strTaxpayerAddress AS owneraddress 
   FROM tblBusiness b
      INNER JOIN tblTaxpayer t on t.strTaxpayerID = b.strTaxpayerID 
      INNER JOIN tblBPLedger bpl on bpl.strBusinessID = b.ObjID 
      INNER JOIN tblAssessment ta on ta.strBusinessID = b.ObjID 
)i 
WHERE NOT EXISTS (SELECT 1 FROM etracs25_capture_business bb WHERE bb.oldbusinessid=i.objid) 
${filter} 
ORDER BY i.activeyear DESC 


[getInfo]
select
   b.ObjID AS oldbusinessid, 
   b.strTradeName as business_businessname,
   b.strTradeName as business_tradename, 
   strBusinessAddress as business_address_text,
   a.objid as applicationid, 
   case stat.strtype 
      WHEN 'RENEWAL' THEN 'RENEW'
      ELSE stat.strtype 
   end AS apptype, 
   a.intyear as activeyear, 
   intYearStarted as yearstarted, 
   t.strTaxpayer as business_owner_name, 
   t.strTaxpayerID AS business_owner_oldid,
   it.strLastname AS business_owner_lastname,   
   it.strFirstname AS business_owner_firstname, 
   it.strMiddlename AS business_owner_middlename,
   it.strSexID AS business_owner_gender,
   t.strTaxpayerID AS business_owner_entityno,
   t.strTaxpayerAddress as business_owner_address,  
   case strOrganizationTypeID 
      when 'S' then 'SING'
      when 'P' then 'PART'
      WHEN 'CORP' THEN 'CORP'
      WHEN 'COOP' THEN 'COOP'
      WHEN 'ASSO' THEN 'ASSO'
      when 'FOUN' then 'FOUND'
      else strOrganizationTypeID 
   end as business_orgtype
from tblBusiness b 
   inner join (
      select strbusinessid, max(intyear) as intyear 
      from tblAssessment 
      group by strbusinessid 
   )g ON b.objid=g.strbusinessid 
   inner join tblAssessment a ON (g.strbusinessid=a.strbusinessid AND g.intyear=a.intyear) 
   inner join sysTblAssessmentType stat ON a.inttype=stat.objid 
   inner join tblTaxpayer t on t.strTaxpayerID = b.strTaxpayerID 
   inner join tblBPLedger bpl on bpl.strBusinessID = b.ObjID 
   left join tblIndividualTaxpayer it ON it.ObjID=t.strTaxpayerID
   left join etracs25_capture_entity e ON e.oldentityid = b.strTaxpayerID
where b.ObjID=$P{objid}


[getLobs]
SELECT DISTINCT 
   bl.objid as oldlobid,
   bl.strbusinessline as oldlobname, 
   bl.strbusinessline as oldname, 
   CASE stat.strtype 
      WHEN 'RENEWAL' THEN 'RENEW'
      ELSE stat.strtype 
   END AS assessmenttype,
   cl.lob_objid AS lobid, 
   cl.lob_name AS name 
FROM tblAssessment a
   INNER JOIN tblAssessmentBO bo ON bo.parentid=a.objid
   INNER JOIN tblBusinessLine bl ON bo.strBusinessLineID=bl.objid 
   INNER JOIN sysTblAssessmentType stat ON a.inttype=stat.objid 
   LEFT JOIN etracs25_capture_lob cl ON bl.objid=cl.oldlob_objid 
WHERE a.objid=$P{objid} AND a.inttype NOT IN ( 2,10 )


[getReceivables]
SELECT * FROM (
   SELECT 
      tb.objid,
      case stat.strtype 
         WHEN 'RENEWAL' THEN 'RENEW'
         ELSE stat.strtype 
      end AS assessmenttype, 
      b.strBusinessId AS businessid,
      CASE tfa.stracctType 
         WHEN 'OTHER CHARGE' THEN 'OTHERCHARGE' 
         WHEN 'FEE' THEN 'REGFEE' 
         WHEN 'TAX' THEN 'TAX' 
      END AS taxfeetype,
      ta.intyear AS yearapplied, ta.intyear AS [year],
      bl.objid AS oldlob_objid, bl.strbusinessline AS oldlob_name,
      clob.lob_objid, clob.lob_name, 
      tb.strAcctID AS oldaccount_objid,
      tfa.strAcctCode AS oldaccount_code,
      tfa.strDescription AS oldaccount_title,
      ca.account_objid, ca.account_title,
      tb.curAmount AS amount, 
      tb.curAmtPaid AS amtpaid, 
      (tb.curAmount-tb.curAmtPaid) AS balance 
   FROM tblassessment ta 
      INNER JOIN tblBPLedgerBill tb ON ta.objid=tb.strassessmentid 
      INNER JOIN tblBPLedger b ON b.objid=tb.parentid 
      INNER JOIN sysTblAssessmentType stat ON ta.inttype=stat.objid 
      INNER JOIN tblTaxFeeAccount tfa ON tfa.objid=tb.strAcctID 
      LEFT JOIN tblBusinessLine bl ON bl.objid=tb.strBusinessLineID 
      LEFT JOIN etracs25_capture_lob clob ON bl.objid=clob.oldlob_objid 
      LEFT JOIN etracs25_capture_account ca ON tb.stracctid=ca.oldaccount_objid 
   WHERE ta.objid=$P{objid} 
)bt 
WHERE bt.balance > 0 
ORDER BY bt.year DESC
