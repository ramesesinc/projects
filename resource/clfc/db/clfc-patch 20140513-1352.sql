USE clfc2;

ALTER TABLE `loan_route`
ADD COLUMN `prefix` VARCHAR(2);

UPDATE `loan_route` SET prefix = '01'
WHERE `code` = 'R-983795553';

UPDATE `loan_route` SET prefix = '02'
WHERE `code` = 'R-983794636';

UPDATE `loan_route` SET prefix = '03'
WHERE `code` = 'R-983794689';

UPDATE `loan_route` SET prefix = '04'
WHERE `code` = 'R-983795645';

UPDATE `loan_route` SET prefix = '05'
WHERE `code` = 'R-983795587';

UPDATE `loan_route` SET prefix = '06'
WHERE `code` = 'R-983795616';

UPDATE `loan_route` SET prefix = '07'
WHERE `code` = 'R483284788';

UPDATE `loan_route` SET prefix = '08'
WHERE `code` = 'R-983795524';

UPDATE `loan_route` SET prefix = '09'
WHERE `code` = 'R1696945384';



