[findByObjid]
SELECT * FROM borrower WHERE objid=$P{objid} 

[getChildren]
SELECT * FROM borrowerindividual_children WHERE borrowerid=$P{borrowerid} 

[removeChildren]
DELETE FROM borrowerindividual_children WHERE borrowerid=$P{borrowerid} 

[getEmployments]
SELECT * FROM employment WHERE refid=$P{refid} 

[removeEmployments]
DELETE FROM employment WHERE refid=$P{refid} 

[getOtherIncomes]
SELECT * FROM sourceofincome WHERE refid=$P{refid} 

[removeOtherIncomes]
DELETE FROM sourceofincome WHERE refid=$P{refid} 

[getEducations]
SELECT * FROM borrowerindividual_education WHERE borrowerid=$P{borrowerid} 

[removeEducations]
DELETE FROM borrowerindividual_education WHERE borrowerid=$P{borrowerid} 

[findParent]
SELECT * FROM borrowerindividual_parent WHERE objid=$P{objid} 

[removeParent]
DELETE FROM borrowerindividual_parent WHERE objid=$P{objid} 

[getSiblings]
SELECT * FROM borrowerindividual_sibling WHERE borrowerid=$P{borrowerid} 

[removeSiblings]
DELETE FROM borrowerindividual_sibling WHERE borrowerid=$P{borrowerid} 

[findBankAcct]
SELECT * FROM borrower_bankacct 
WHERE borrowerid=$P{borrowerid} AND type=$P{type} 

[getBankAccts]
SELECT * FROM borrower_bankacct 
WHERE borrowerid=$P{borrowerid} AND type=$P{type} 

[removeBankAccts]
DELETE FROM borrower_bankacct 
WHERE borrowerid=$P{borrowerid} AND type=$P{type} 

[removeAllBankAccts]
DELETE FROM borrower_bankacct WHERE borrowerid=$P{borrowerid} 