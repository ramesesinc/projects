[getIdleLandSharesAbstract]
SELECT 
    b.name AS barangay,
    SUM(CASE WHEN cra.revperiod = 'current' AND cra.revtype = 'basicidle' THEN cra.amount ELSE 0 END) AS brgycurr,
    SUM(CASE WHEN cra.revperiod IN ('previous', 'prior') AND cra.revtype = 'basicidle' THEN cra.amount ELSE 0 END) AS brgyprev,
    SUM(0.0) AS brgypenalty,
    SUM(CASE WHEN cra.revperiod <> 'advance' AND cra.revtype = 'basicidle' THEN cra.amount ELSE 0 END) AS brgytotal,

    SUM(CASE WHEN cra.revperiod = 'current' AND cra.revtype = 'basicidle' AND cra.sharetype = 'municipality' THEN cra.amount ELSE 0 END) AS municurrshare,
    SUM(CASE WHEN cra.revperiod IN ('previous', 'prior') AND cra.revtype = 'basicidle' AND cra.sharetype = 'municipality' THEN cra.amount ELSE 0 END) AS muniprevshare,
    SUM(0.0) AS munipenaltyshare,
    SUM(CASE WHEN cra.revperiod <> 'advance' AND cra.revtype = 'basicidle' AND cra.sharetype = 'municipality' THEN cra.amount ELSE 0 END) AS munisharetotal,

    SUM(CASE WHEN cra.revperiod = 'current' AND cra.revtype = 'basicidle' AND cra.sharetype = 'province' THEN cra.amount ELSE 0 END) AS provcurrshare,
    SUM(CASE WHEN cra.revperiod IN ('previous', 'prior') AND cra.revtype = 'basicidle' AND cra.sharetype = 'province' THEN cra.amount ELSE 0 END) AS provprevshare,
    SUM(0.0) AS provpenaltyshare,
    SUM(CASE WHEN cra.revperiod <> 'advance' AND  cra.revtype = 'basicidle' AND cra.sharetype = 'province' THEN cra.amount ELSE 0 END) AS provsharetotal
FROM remittance rem 
    inner join liquidation_remittance liqr on rem.objid = liqr.objid 
    inner join remittance_cashreceipt remc on rem.objid = remc.remittanceid 
    inner join cashreceipt cr on remc.objid = cr.objid 
    INNER JOIN cashreceiptitem_rpt_account cra ON cr.objid = cra.rptreceiptid 
    INNER JOIN rptledger rl ON cra.rptledgerid = rl.objid
    INNER JOIN barangay b on rl.barangayid = b.objid 
where rem.remittancedate >= $P{fromdate} AND rem.remittancedate < $P{todate}   
    and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid) 
    AND cra.revtype = 'basicidle'
    AND cra.amount > 0
GROUP BY b.name 


[getIdleLandShares]
SELECT 
    SUM(CASE WHEN cra.revperiod = 'current' AND cra.revtype = 'basicidle' AND cra.sharetype = 'municipality' THEN cra.amount ELSE 0 END) AS municurrshare,
    SUM(CASE WHEN cra.revperiod IN ('previous', 'prior') AND cra.revtype = 'basicidle' AND cra.sharetype = 'municipality' THEN cra.amount ELSE 0 END) AS muniprevshare,
    SUM(0.0) AS munipenaltyshare,
    SUM(CASE WHEN cra.revperiod <> 'advance' AND cra.revtype = 'basicidle' AND cra.sharetype = 'municipality' THEN cra.amount ELSE 0 END) AS munisharetotal,

    SUM(CASE WHEN cra.revperiod = 'current' AND cra.revtype = 'basicidle' AND cra.sharetype = 'province' THEN cra.amount ELSE 0 END) AS provcurrshare,
    SUM(CASE WHEN cra.revperiod IN ('previous', 'prior') AND cra.revtype = 'basicidle' AND cra.sharetype = 'province' THEN cra.amount ELSE 0 END) AS provprevshare,
    SUM(0.0) AS provpenaltyshare,
    SUM(CASE WHEN cra.revperiod <> 'advance' AND  cra.revtype = 'basicidle' AND cra.sharetype = 'province' THEN cra.amount ELSE 0 END) AS provsharetotal
FROM remittance rem 
    inner join liquidation_remittance liqr on rem.objid = liqr.objid 
    inner join remittance_cashreceipt remc on rem.objid = remc.remittanceid 
    inner join cashreceipt cr on remc.objid = cr.objid 
    INNER JOIN cashreceiptitem_rpt_account cra ON cr.objid = cra.rptreceiptid 
WHERE rem.remittancedate >= $P{fromdate} AND rem.remittancedate < $P{todate} 
    and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid) 


[getBasicSharesAbstract]
SELECT
    b.name AS barangay,
    SUM(t.brgycurr) AS brgycurr,
    SUM(t.brgyprev) AS brgyprev,
    SUM(t.brgypenalty) AS brgypenalty,
    SUM(t.brgycurrshare) AS brgycurrshare,
    SUM(t.brgyprevshare) AS brgyprevshare,
    SUM(t.brgypenaltyshare) AS brgypenaltyshare,
    SUM(t.provmunicurrshare) AS provmunicurrshare,
    SUM(t.provmuniprevshare) AS provmuniprevshare,
    SUM(t.provmunipenaltyshare) AS provmunipenaltyshare
FROM (
    SELECT 
        b.objid AS barangayid,
        CASE WHEN cra.revperiod = 'current' AND cra.revtype = 'basic' THEN cra.amount ELSE 0 END AS brgycurr,
        CASE WHEN cra.revperiod IN ('previous', 'prior') AND cra.revtype = 'basic' THEN cra.amount ELSE 0 END AS brgyprev,
        CASE WHEN cra.revtype = 'basicint' THEN cra.amount ELSE 0 END AS brgypenalty,
        
        CASE WHEN cra.revperiod = 'current' AND cra.revtype = 'basic' AND cra.sharetype = 'barangay' THEN cra.amount ELSE 0 END AS brgycurrshare,
        CASE WHEN cra.revperiod IN ('previous', 'prior') AND cra.revtype = 'basic' AND cra.sharetype = 'barangay' THEN cra.amount ELSE 0 END AS brgyprevshare,
        CASE WHEN cra.revtype = 'basicint' AND cra.sharetype = 'barangay' THEN cra.amount ELSE 0 END AS brgypenaltyshare,

        CASE WHEN cra.revperiod = 'current' AND cra.revtype = 'basic' AND cra.sharetype IN ('province', 'municipality') THEN cra.amount ELSE 0 END AS provmunicurrshare,
        CASE WHEN cra.revperiod IN ('previous', 'prior') AND cra.revtype = 'basic' AND cra.sharetype IN ('province', 'municipality') THEN cra.amount ELSE 0 END AS provmuniprevshare,
        CASE WHEN cra.revtype = 'basicint' AND cra.sharetype IN ('province', 'municipality') THEN cra.amount ELSE 0 END AS provmunipenaltyshare
    FROM remittance rem 
        inner join liquidation_remittance liqr on rem.objid = liqr.objid 
        inner join remittance_cashreceipt remc on rem.objid = remc.remittanceid 
        inner join cashreceipt cr on remc.objid = cr.objid 
        INNER JOIN cashreceiptitem_rpt_account cra ON cr.objid = cra.rptreceiptid 
        INNER JOIN rptledger rl ON cra.rptledgerid = rl.objid
        INNER JOIN barangay b on rl.barangayid = b.objid 
    WHERE rem.remittancedate >= $P{fromdate} AND rem.remittancedate < $P{todate} 
        and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid) 
        AND cra.revperiod <> 'advance' 
        AND cra.revtype IN ('basic', 'basicint')

    UNION ALL

    SELECT  
        ( SELECT MIN(crn.barangay_objid) FROM cashreceiptitem_rpt_noledger crn 
                INNER JOIN cashreceiptitem_rpt_online cro ON crn.objid = cro.objid 
            WHERE cro.rptreceiptid = cr.objid 
        ) AS barangayid,
        CASE WHEN cra.revperiod = 'current' AND cra.revtype = 'basic' THEN cra.amount ELSE 0 END AS brgycurr,
        CASE WHEN cra.revperiod IN ('previous', 'prior') AND cra.revtype = 'basic' THEN cra.amount ELSE 0 END AS brgyprev,
        CASE WHEN cra.revtype = 'basicint' THEN cra.amount ELSE 0 END AS brgypenalty,
                        
        CASE WHEN cra.revperiod = 'current' AND cra.revtype = 'basic' AND cra.sharetype = 'barangay' THEN cra.amount ELSE 0 END AS brgycurrshare,
        CASE WHEN cra.revperiod IN ('previous', 'prior') AND cra.revtype = 'basic' AND cra.sharetype = 'barangay' THEN cra.amount ELSE 0 END AS brgyprevshare,
        CASE WHEN cra.revtype = 'basicint' AND cra.sharetype = 'barangay' THEN cra.amount ELSE 0 END AS brgypenaltyshare,

        CASE WHEN cra.revperiod = 'current' AND cra.revtype = 'basic' AND cra.sharetype IN ('province', 'municipality') THEN cra.amount ELSE 0 END AS provmunicurrshare,
        CASE WHEN cra.revperiod IN ('previous', 'prior') AND cra.revtype = 'basic' AND cra.sharetype IN ('province', 'municipality') THEN cra.amount ELSE 0 END AS provmuniprevshare,
        CASE WHEN cra.revtype = 'basicint' AND cra.sharetype IN ('province', 'municipality') THEN cra.amount ELSE 0 END AS provmunipenaltyshare
    FROM remittance rem 
        inner join liquidation_remittance liqr on rem.objid = liqr.objid 
        inner join remittance_cashreceipt remc on rem.objid = remc.remittanceid 
        inner join cashreceipt cr on remc.objid = cr.objid 
        INNER JOIN cashreceipt_rpt crr ON cr.objid = crr.objid 
        INNER JOIN cashreceiptitem_rpt_account cra ON cr.objid = cra.rptreceiptid 
    WHERE rem.remittancedate >= $P{fromdate} AND rem.remittancedate < $P{todate} 
        and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid) 
        AND crr.txntype = 'noledger'
        AND cra.revperiod <> 'advance' 
        AND cra.revtype IN ('basic', 'basicint')
)t 
INNER JOIN barangay b ON t.barangayid = b.objid 
GROUP BY b.name 


[getBasicShares]
SELECT
    b.name AS barangay,
    SUM(t.brgytotalshare) AS brgytotalshare,
    SUM(t.municurrshare) AS municurrshare,
    SUM(t.muniprevshare) AS muniprevshare,
    SUM(t.munipenaltyshare) AS munipenaltyshare,
    SUM(t.provcurrshare) AS provcurrshare,
    SUM(t.provprevshare) AS provprevshare,
    SUM(t.provpenaltyshare) AS provpenaltyshare,
    SUM(t.brgytotalshare + t.municurrshare + t.muniprevshare + t.munipenaltyshare +
            t.provcurrshare + t.provprevshare + t.provpenaltyshare
    ) AS grandtotal
FROM (
    SELECT 
        b.objid AS barangayid,
        CASE WHEN cra.sharetype = 'barangay' THEN cra.amount ELSE 0 END AS brgytotalshare,

        CASE WHEN cra.revperiod = 'current' AND cra.revtype = 'basic' AND cra.sharetype = 'municipality' THEN cra.amount ELSE 0 END AS municurrshare,
        CASE WHEN cra.revperiod IN ('previous', 'prior') AND cra.revtype = 'basic' AND cra.sharetype = 'municipality' THEN cra.amount ELSE 0 END AS muniprevshare,
        CASE WHEN cra.revtype = 'basicint' AND cra.sharetype = 'municipality' THEN cra.amount ELSE 0 END AS munipenaltyshare,

        CASE WHEN cra.revperiod = 'current' AND cra.revtype = 'basic' AND cra.sharetype = 'province' THEN cra.amount ELSE 0 END AS provcurrshare,
        CASE WHEN cra.revperiod IN ('previous', 'prior') AND cra.revtype = 'basic' AND cra.sharetype = 'province' THEN cra.amount ELSE 0 END AS provprevshare,
        CASE WHEN cra.revtype = 'basicint' AND cra.sharetype = 'province' THEN cra.amount ELSE 0 END AS provpenaltyshare
    FROM remittance rem 
        inner join liquidation_remittance liqr on rem.objid = liqr.objid 
        inner join remittance_cashreceipt remc on rem.objid = remc.remittanceid 
        inner join cashreceipt cr on remc.objid = cr.objid 
        INNER JOIN cashreceiptitem_rpt_account cra ON cr.objid = cra.rptreceiptid 
        INNER JOIN rptledger rl ON cra.rptledgerid = rl.objid
        INNER JOIN barangay b on rl.barangayid = b.objid 
    WHERE rem.remittancedate >= $P{fromdate} AND rem.remittancedate < $P{todate} 
        and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid) 
        AND cra.revperiod <> 'advance' 
        AND cra.revtype IN ('basic', 'basicint')

    UNION ALL

    SELECT  
        ( SELECT MIN(crn.barangay_objid) 
                FROM cashreceiptitem_rpt_noledger crn 
                INNER JOIN cashreceiptitem_rpt_online cro ON crn.objid = cro.objid 
                WHERE cro.rptreceiptid = cr.objid 
         ) AS barangayid,

        CASE WHEN cra.sharetype = 'barangay' THEN cra.amount ELSE 0 END AS brgytotalshare,

        CASE WHEN cra.revperiod = 'current' AND cra.revtype = 'basic' AND cra.sharetype = 'municipality' THEN cra.amount ELSE 0 END AS municurrshare,
        CASE WHEN cra.revperiod IN ('previous', 'prior') AND cra.revtype = 'basic' AND cra.sharetype = 'municipality' THEN cra.amount ELSE 0 END AS muniprevshare,
        CASE WHEN cra.revtype = 'basicint' AND cra.sharetype = 'municipality' THEN cra.amount ELSE 0 END AS munipenaltyshare,

        CASE WHEN cra.revperiod = 'current' AND cra.revtype = 'basic' AND cra.sharetype = 'province' THEN cra.amount ELSE 0 END AS provcurrshare,
        CASE WHEN cra.revperiod IN ('previous', 'prior') AND cra.revtype = 'basic' AND cra.sharetype = 'province' THEN cra.amount ELSE 0 END AS provprevshare,
        CASE WHEN cra.revtype = 'basicint' AND cra.sharetype = 'province' THEN cra.amount ELSE 0 END AS provpenaltyshare
    FROM remittance rem 
        inner join liquidation_remittance liqr on rem.objid = liqr.objid 
        inner join remittance_cashreceipt remc on rem.objid = remc.remittanceid 
        inner join cashreceipt cr on remc.objid = cr.objid 
        INNER JOIN cashreceipt_rpt crr ON cr.objid = crr.objid 
        INNER JOIN cashreceiptitem_rpt_account cra ON cr.objid = cra.rptreceiptid 
    WHERE rem.remittancedate >= $P{fromdate} AND rem.remittancedate < $P{todate} 
        and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid) 
        AND crr.txntype = 'noledger'
        AND cra.revperiod <> 'advance' 
        AND cra.revtype IN ('basic', 'basicint')
) t
INNER JOIN barangay b ON t.barangayid = b.objid 
GROUP BY b.name 


[getBasicSharesSummary]   
SELECT xx.*, 
    (brgytotalshare + munitotalshare + provtotalshare) as totalshare 
FROM ( 
    SELECT 
        SUM(CASE WHEN cra.revperiod = 'current' AND cra.revtype = 'basic' AND cra.sharetype = 'barangay' THEN cra.amount ELSE 0 END) AS brgycurrshare,
        SUM(CASE WHEN cra.revperiod IN ('previous', 'prior') AND cra.revtype = 'basic' AND cra.sharetype = 'barangay' THEN cra.amount ELSE 0 END) AS brgyprevshare,
        SUM(CASE WHEN cra.revtype = 'basicint' AND cra.sharetype = 'barangay' THEN cra.amount ELSE 0 END) AS  brgypenaltyshare,
        SUM(CASE WHEN cra.sharetype = 'barangay' THEN cra.amount ELSE 0 END) AS  brgytotalshare,

        SUM(CASE WHEN cra.revperiod = 'current' AND cra.revtype = 'basic' AND cra.sharetype = 'municipality' THEN cra.amount ELSE 0 END) AS  municurrshare,
        SUM(CASE WHEN cra.revperiod IN ('previous', 'prior') AND cra.revtype = 'basic' AND cra.sharetype = 'municipality' THEN cra.amount ELSE 0 END) AS  muniprevshare,
        SUM(CASE WHEN cra.revtype = 'basicint' AND cra.sharetype = 'municipality' THEN cra.amount ELSE 0 END) AS  munipenaltyshare,
        SUM(CASE WHEN cra.sharetype = 'municipality' THEN cra.amount ELSE 0 END) AS  munitotalshare,

        SUM(CASE WHEN cra.revperiod = 'current' AND cra.revtype = 'basic' AND cra.sharetype = 'province' THEN cra.amount ELSE 0 END) AS  provcurrshare,
        SUM(CASE WHEN cra.revperiod IN ('previous', 'prior') AND cra.revtype = 'basic' AND cra.sharetype = 'province' THEN cra.amount ELSE 0 END) AS  provprevshare,
        SUM(CASE WHEN cra.revtype = 'basicint' AND cra.sharetype = 'province' THEN cra.amount ELSE 0 END) AS  provpenaltyshare,
        SUM(CASE WHEN cra.sharetype = 'province' THEN cra.amount ELSE 0 END) AS  provtotalshare 
    FROM remittance rem 
        inner join liquidation_remittance liqr on rem.objid = liqr.objid 
        inner join remittance_cashreceipt remc on rem.objid = remc.remittanceid 
        inner join cashreceipt cr on remc.objid = cr.objid 
        inner join cashreceiptitem_rpt_account cra ON cr.objid = cra.rptreceiptid 
    where rem.remittancedate >= $P{fromdate} AND rem.remittancedate < $P{todate}   
        and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid) 
        and cra.revperiod <> 'advance' 
        and cra.revtype IN ('basic', 'basicint')
)xx 


[getSefShares]
SELECT 
    SUM(CASE WHEN cra.revperiod = 'current' AND cra.revtype = 'sef' AND cra.sharetype = 'municipality' THEN cra.amount ELSE 0 END) AS municurrshare,
    SUM(CASE WHEN cra.revperiod IN ('previous', 'prior') AND cra.revtype = 'sef' AND cra.sharetype = 'municipality' THEN cra.amount ELSE 0 END) AS muniprevshare,
    SUM(CASE WHEN cra.revtype = 'sefint' AND cra.sharetype = 'municipality' THEN cra.amount ELSE 0 END) AS munipenaltyshare,
    SUM(CASE WHEN cra.revtype IN ('sef', 'sefint') AND cra.sharetype = 'municipality' THEN cra.amount ELSE 0 END) AS munisharetotal,

    SUM(CASE WHEN cra.revperiod = 'current' AND cra.revtype = 'sef' AND cra.sharetype = 'province' THEN cra.amount ELSE 0 END) AS provcurrshare,
    SUM(CASE WHEN cra.revperiod IN ('previous', 'prior') AND cra.revtype = 'sef' AND cra.sharetype = 'province' THEN cra.amount ELSE 0 END) AS provprevshare,
    SUM(CASE WHEN cra.revtype = 'sefint' AND cra.sharetype = 'province' THEN cra.amount ELSE 0 END) AS provpenaltyshare,
    SUM(CASE WHEN cra.revtype IN ('sef', 'sefint') AND cra.sharetype = 'province' THEN cra.amount ELSE 0 END) AS provsharetotal
FROM remittance rem 
    inner join liquidation_remittance liqr on rem.objid = liqr.objid 
    inner join remittance_cashreceipt remc on rem.objid = remc.remittanceid 
    inner join cashreceipt cr on remc.objid = cr.objid 
    INNER JOIN cashreceiptitem_rpt_account cra ON cr.objid = cra.rptreceiptid 
WHERE rem.remittancedate >= $P{fromdate} AND rem.remittancedate < $P{todate} 
    and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid) 
    AND cra.revperiod <> 'advance'
    AND cra.revtype IN ('sef', 'sefint')


[getBrgySharesStandard]
SELECT  
    MIN(b.name) AS brgyname, 
    SUM(CASE WHEN cra.revperiod='current' AND revtype='basic' THEN cra.amount + cra.discount ELSE 0.0 END )AS basiccurrentamt,     
    SUM(CASE WHEN cra.revperiod='current' AND revtype='basic' THEN cra.discount ELSE 0.0 END )AS basiccurrentdiscamt,     
    SUM(CASE WHEN cra.revperiod = 'current' AND revtype ='basicint' THEN cra.amount ELSE 0.0 END) AS basiccurrentintamt,
    SUM(CASE WHEN cra.revperiod IN ('previous', 'prior') AND revtype ='basic' THEN cra.amount ELSE 0.0 END) AS basicprevamt,    
    SUM(CASE WHEN cra.revperiod IN ('previous', 'prior') AND revtype ='basicint' THEN cra.amount ELSE 0.0 END) AS basicprevintamt   
FROM remittance rem 
    inner join liquidation_remittance liqr on rem.objid = liqr.objid 
    inner join remittance_cashreceipt remc on rem.objid = remc.remittanceid 
    inner join cashreceipt cr on remc.objid = cr.objid 
    INNER JOIN cashreceiptitem_rpt_account cra ON cr.objid = cra.rptreceiptid 
    INNER JOIN rptledger rl ON cra.rptledgerid = rl.objid
    INNER JOIN barangay b ON rl.barangayid = b.objid 
WHERE rem.remittancedate >= $P{fromdate} AND rem.remittancedate < $P{todate} 
    and cr.objid not in (select receiptid from cashreceipt_void where receiptid=cr.objid) 
    AND cra.sharetype ='barangay'
GROUP BY b.objid


[getBrgySharesAdvance]
SELECT  
    MIN(b.name) AS brgyname, 
    SUM(CASE WHEN cra.revperiod='advance' AND revtype='basic' THEN cra.amount ELSE 0.0 END )AS basiccurrentamt,     
    SUM(CASE WHEN cra.revperiod = 'advance' AND revtype ='basicint' THEN cra.amount ELSE 0.0 END) AS basiccurrentintamt,
FROM cashreceipt cr 
    INNER JOIN cashreceiptitem_rpt_account cra ON cr.objid = cra.rptreceiptid 
    LEFT JOIN cashreceipt_void cv ON cr.objid = cv.receiptid 
    INNER JOIN rptledger rl ON cra.rptledgerid = rl.objid
    INNER JOIN barangay b ON rl.barangayid = b.objid 
    INNER JOIN remittance_cashreceipt rc ON cr.objid = rc.objid
WHERE cr.receiptdate >= $P{fromdate} AND cr.receiptdate < $P{todate}
    AND cra.sharetype ='barangay'
     AND cv.objid IS NULL  
GROUP BY b.objid  

