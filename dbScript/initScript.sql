
ALTER TABLE user MODIFY password text;
INSERT INTO `user` (`id`, `created`, `created_by`, `modified`, `modified_by`, `is_active`, `address`, `age`, `currentSalary`, `date_of_birth`, `email`, `father_name`, `joining_date`, `max_discount_percent`, `mother_name`, `name`, `national_id_no`, `password`, `profile_picture_path`, `religion`, `roles`, `sex`, `starting_salary`, `user_name`, `user_type`, `role`) VALUES (NULL, '2015-07-01 00:00:00', 'SUPER_ADMIN', NULL, NULL, b'1', NULL, '26', NULL, '2015-07-01 00:00:00', 'habib.rahman.dsi@gmail.com', NULL, '2015-07-01 00:00:00', '100', NULL, 'Md. Habibur Rahman', NULL, '123', NULL, 'Islam', 'SUPER_ADMIN', 'Male', NULL, 'SUPER_ADMIN', NULL, 'ROLE_SUPER_ADMIN');

UPDATE `product` SET `sale_rate`=0.0 WHERE sale_rate is null;
UPDATE `product` SET `purchase_rate`=0.0 WHERE purchase_rate is null;
UPDATE `product` SET `total_quantity`=0.0 WHERE total_quantity is null;