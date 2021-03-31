-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th3 22, 2021 lúc 01:54 PM
-- Phiên bản máy phục vụ: 10.4.18-MariaDB
-- Phiên bản PHP: 8.0.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `gym_equipment`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `equipment_details`
--

CREATE TABLE `equipment_details` (
  `id` varchar(5) NOT NULL,
  `name` varchar(100) NOT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `price` int(15) NOT NULL,
  `warranty_time` date NOT NULL DEFAULT current_timestamp(),
  `supplier_id` varchar(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `gym_equipments`
--

CREATE TABLE `gym_equipments` (
  `id` varchar(5) NOT NULL,
  `status` varchar(20) NOT NULL,
  `detail_id` varchar(5) NOT NULL,
  `import_id` varchar(5) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `import_details`
--

CREATE TABLE `import_details` (
  `id` varchar(5) NOT NULL,
  `equipment_id` varchar(5) NOT NULL,
  `user_id` tinyint(3) NOT NULL,
  `date_import` datetime NOT NULL DEFAULT current_timestamp(),
  `amount` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `login_info`
--

CREATE TABLE `login_info` (
  `userName` varchar(15) NOT NULL,
  `password` varchar(24) NOT NULL,
  `userId` tinyint(3) NOT NULL,
  `role_id` tinyint(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `role`
--

CREATE TABLE `role` (
  `id` tinyint(3) NOT NULL,
  `role` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `suppliers`
--

CREATE TABLE `suppliers` (
  `id` varchar(5) NOT NULL,
  `name` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `phone_number` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `id` tinyint(3) NOT NULL,
  `firstName` varchar(10) NOT NULL,
  `lastName` varchar(30) NOT NULL,
  `birthDay` date NOT NULL,
  `email` varchar(100) NOT NULL,
  `contactNumber` varchar(12) NOT NULL,
  `profilePicture` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `equipment_details`
--
ALTER TABLE `equipment_details`
  ADD PRIMARY KEY (`id`),
  ADD KEY `supplier_id` (`supplier_id`);

--
-- Chỉ mục cho bảng `gym_equipments`
--
ALTER TABLE `gym_equipments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `import_id` (`import_id`),
  ADD KEY `detail_id` (`detail_id`),
  ADD KEY `import_detail_id` (`import_id`);

--
-- Chỉ mục cho bảng `import_details`
--
ALTER TABLE `import_details`
  ADD PRIMARY KEY (`id`,`equipment_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `equipment_id` (`equipment_id`);

--
-- Chỉ mục cho bảng `login_info`
--
ALTER TABLE `login_info`
  ADD PRIMARY KEY (`userName`),
  ADD KEY `userId` (`userId`),
  ADD KEY `role_id` (`role_id`);

--
-- Chỉ mục cho bảng `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `suppliers`
--
ALTER TABLE `suppliers`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `id` tinyint(3) NOT NULL AUTO_INCREMENT;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `equipment_details`
--
ALTER TABLE `equipment_details`
  ADD CONSTRAINT `equipment_details_ibfk_1` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`id`) ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `gym_equipments`
--
ALTER TABLE `gym_equipments`
  ADD CONSTRAINT `gym_equipments_ibfk_4` FOREIGN KEY (`detail_id`) REFERENCES `equipment_details` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `gym_equipments_ibfk_5` FOREIGN KEY (`import_id`) REFERENCES `import_details` (`id`) ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `import_details`
--
ALTER TABLE `import_details`
  ADD CONSTRAINT `import_details_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `import_details_ibfk_2` FOREIGN KEY (`equipment_id`) REFERENCES `equipment_details` (`id`) ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `login_info`
--
ALTER TABLE `login_info`
  ADD CONSTRAINT `login_info_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `login_info_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
