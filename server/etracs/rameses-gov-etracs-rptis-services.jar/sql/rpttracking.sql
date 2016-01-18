[insertTracking]
INSERT INTO rpttracking (
  objid, filetype, trackingno, msg
)
VALUES(
  $P{objid}, $P{filetype}, $P{trackingno}, $P{msg}
)

[deleteTracking]
DELETE FROM rpttracking WHERE objid = $P{objid}


[updateMsg]
UPDATE rpttracking SET msg = $P{msg} WHERE objid = $P{objid}

[findById]
SELECT * FROM rpttracking WHERE objid = $P{objid}

