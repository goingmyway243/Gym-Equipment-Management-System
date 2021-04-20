-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th4 15, 2021 lúc 03:17 PM
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
  `warranty_time` tinyint(2) DEFAULT NULL,
  `supplier_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Đang đổ dữ liệu cho bảng `equipment_details`
--

INSERT INTO `equipment_details` (`id`, `name`, `picture`, `price`, `warranty_time`, `supplier_id`) VALUES
('MCB01', 'Máy chạy bộ Kingsport', NULL, 13500000, 2, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `gym_equipments`
--

CREATE TABLE `gym_equipments` (
  `id` varchar(7) NOT NULL,
  `status` varchar(20) NOT NULL,
  `detail_id` varchar(5) NOT NULL,
  `import_id` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Đang đổ dữ liệu cho bảng `gym_equipments`
--

INSERT INTO `gym_equipments` (`id`, `status`, `detail_id`, `import_id`, `created_at`, `updated_at`) VALUES
('ASD-001', 'Đang hoạt động', 'MCB01', 1, '2021-04-07 15:48:53', '2021-04-14 16:13:16'),
('TES-001', 'Bị hỏng', 'MCB01', 2, '2021-04-14 16:13:30', '2021-04-14 16:13:30');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `import_details`
--

CREATE TABLE `import_details` (
  `id` int(11) NOT NULL,
  `user_id` tinyint(3) NOT NULL,
  `date_import` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Đang đổ dữ liệu cho bảng `import_details`
--

INSERT INTO `import_details` (`id`, `user_id`, `date_import`) VALUES
(1, 1, '2021-04-07'),
(2, 1, '2021-04-14');

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

--
-- Đang đổ dữ liệu cho bảng `login_info`
--

INSERT INTO `login_info` (`userName`, `password`, `userId`, `role_id`) VALUES
('admin', 'admin', 1, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `role`
--

CREATE TABLE `role` (
  `id` tinyint(4) NOT NULL,
  `role` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Đang đổ dữ liệu cho bảng `role`
--

INSERT INTO `role` (`id`, `role`) VALUES
(1, 'Quản lý'),
(2, 'Nhân viên');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `suppliers`
--

CREATE TABLE `suppliers` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `phone_number` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Đang đổ dữ liệu cho bảng `suppliers`
--

INSERT INTO `suppliers` (`id`, `name`, `address`, `phone_number`) VALUES
(1, 'Kingsport', '384 Điện Biên Phủ, P.17, Q.Bình Thạnh', '0936211210'),
(2, 'Gym Kingdom', '77 Đinh Bộ Lĩnh, P.11, Q.Bình Thạnh', '0903666808');

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
  `contactNumber` varchar(10) NOT NULL,
  `profilePicture` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`id`, `firstName`, `lastName`, `birthDay`, `email`, `contactNumber`, `profilePicture`, `created_at`, `updated_at`) VALUES
(1, 'Đăng', 'Nguyễn Hải', '2000-08-24', 'nguyenhaidang240800@gmail.com', '0961362843', '', '2021-03-29 15:20:05', '2021-03-29 15:21:00');

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
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

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
-- AUTO_INCREMENT cho bảng `import_details`
--
ALTER TABLE `import_details`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `role`
--
ALTER TABLE `role`
  MODIFY `id` tinyint(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `suppliers`
--
ALTER TABLE `suppliers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `id` tinyint(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

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
  ADD CONSTRAINT `import_details_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON UPDATE CASCADE;

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
