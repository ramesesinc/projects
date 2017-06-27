alter table consolidationaffectedrpu modify column newsuffix int null;

ALTER TABLE `consolidation_task` DROP FOREIGN KEY `consolidation_task_ibfk_1`;

ALTER TABLE `subdivision_task` DROP FOREIGN KEY `subdivision_task_ibfk_1`;

