
CREATE DATABASE IF NOT EXISTS  `socialbookdb`;
USE `socialbookdb`;


--
-- Table structure for table `users`
--




CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_name` text DEFAULT NULL,
  `password` text DEFAULT NULL,
  `email` text DEFAULT NULL,
  PRIMARY KEY (`id`)
);


CREATE TABLE `user_profile` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` TEXT NOT NULL,
  `fullname` TEXT DEFAULT NULL,
  `address`text DEFAULT NULL,
  `age` INT DEFAULT NULL,
  `phone_number` VARCHAR(10) DEFAULT NULL,
  `user_id` INT DEFAULT NULL, 
  `postcode` text DEFAULT NULL,
  `state` text DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
  );



CREATE TABLE `Author` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `book_category` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` TEXT NOT NULL,
  PRIMARY KEY (`id`)
);



INSERT INTO `book_category` (`name`) VALUES
('Art'),
('Biography'),
('Business'),
('Chick Lit'),
("Children's"),
('Classics'),
('Comics'),
('Contemporary'),
('Cookbooks'),
('Crime'),
('Ebooks'),
('Fiction'),
('Graphic Novels'),
('Historical Fiction'),
('History'),
('Horror'),
('Humor and Comedy'),
('Manga'),
('Memoir'),
('Music'),
('Mystery'),
('Nonfiction'),
('Paranormal'),
('Philosophy'),
('Poetry'),
('Psychology'),
('Religion'),
('Science'),
('Romance'),
('Science Fiction'),
('Self Help'),
('Suspense'),
('Spirituality'),
('Sports'),
('Thriller'),
('Travel'),
('Young Adult'),
('Other');




CREATE TABLE `Book` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` text NOT NULL,
  `summary` text NOT NULL,
  `category_id` INT DEFAULT NULL,
  `userprofile_id` INT DEFAULT NULL,
  `publish_date` TEXT DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`category_id`) REFERENCES `book_category` (`id`),
  FOREIGN KEY (`userprofile_id`) REFERENCES `user_profile` (`id`)
);



CREATE TABLE `notification` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` TEXT NULL,
  `book_title` TEXT NULL,
  `created_date` TEXT NULL,
  PRIMARY KEY (`id`));




CREATE TABLE `authors_book` (
  `author_id` int DEFAULT NULL,
  `book_id` int DEFAULT NULL,
  FOREIGN KEY (`author_id`) REFERENCES `Author` (`id`),
  FOREIGN KEY (`book_id`) REFERENCES `Book` (`id`)
);

CREATE TABLE `userprofiles_authors` (
  `userprofile_id` int DEFAULT NULL,
  `author_id` int DEFAULT NULL,
  FOREIGN KEY (`userprofile_id`) REFERENCES `user_profile` (`id`),
  FOREIGN KEY (`author_id`) REFERENCES `Author` (`id`)
);


CREATE TABLE `userprofiles_favebookcateg` (
  `userprofile_id` int DEFAULT NULL,
  `bookcategory_id` int DEFAULT NULL,
  FOREIGN KEY (`userprofile_id`) REFERENCES `user_profile` (`id`),
  FOREIGN KEY (`bookcategory_id`) REFERENCES `book_category` (`id`)
);


CREATE TABLE `request_books` (
  `book_id` int DEFAULT NULL,
  `user_profile_id` int DEFAULT NULL,
  FOREIGN KEY (`book_id`) REFERENCES `Book` (`id`),
  FOREIGN KEY (`user_profile_id`) REFERENCES `user_profile` (`id`)
);



CREATE TABLE `notification_userprofiles` (
  `notification_id` int DEFAULT NULL,
  `user_profile_id` int DEFAULT NULL,
  FOREIGN KEY (`notification_id`) REFERENCES `notification` (`id`),
  FOREIGN KEY (`user_profile_id`) REFERENCES `user_profile` (`id`)
);



