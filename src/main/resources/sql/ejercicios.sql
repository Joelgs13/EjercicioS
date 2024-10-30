DROP SCHEMA IF EXISTS `ejercicios`;
CREATE SCHEMA `ejercicios`;

USE `ejercicios`;

CREATE TABLE `Animales` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(80) DEFAULT NULL,
  `especie` varchar(80) DEFAULT NULL,
  `raza` varchar(40) DEFAULT NULL,
  `sexo` varchar(20) DEFAULT NULL,
  `edad` int(3) DEFAULT NULL,
  `peso` int(4) DEFAULT NULL,
  `observaciones` varchar(200) DEFAULT NULL,
  `fecha_primera_consulta` date DEFAULT NULL,
  `foto` blob DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;