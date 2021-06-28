DROP SCHEMA IF EXISTS SocialMeals;
DROP USER IF EXISTS 'userSocialMeals'@'%';

CREATE SCHEMA SocialMeals;

CREATE USER 'userSocialMeals'@'%' IDENTIFIED BY 'pwSocialMeals';
GRANT ALL PRIVILEGES ON SocialMeals . * TO 'userSocialMeals'@'%';