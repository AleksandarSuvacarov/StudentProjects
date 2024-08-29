-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: zzp_v2
-- ------------------------------------------------------
-- Server version	8.0.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `korisnik`
--

DROP TABLE IF EXISTS `korisnik`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `korisnik` (
  `idKor` int NOT NULL AUTO_INCREMENT,
  `korIme` varchar(45) NOT NULL,
  `lozinka` varchar(45) NOT NULL,
  `idUlo` int NOT NULL,
  PRIMARY KEY (`idKor`),
  KEY `FK_idUlo_idx` (`idUlo`),
  CONSTRAINT `FK_idUlo` FOREIGN KEY (`idUlo`) REFERENCES `uloga` (`idUlo`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `kviz`
--

DROP TABLE IF EXISTS `kviz`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kviz` (
  `idKviz` int NOT NULL AUTO_INCREMENT,
  `idTem` int NOT NULL,
  `idOrg` int NOT NULL,
  `naslov` varchar(255) NOT NULL,
  `opis` longtext NOT NULL,
  `datumVreme` datetime NOT NULL,
  `adresa` varchar(255) NOT NULL,
  `kotizacija` int NOT NULL,
  `kapacitet` int NOT NULL,
  `zauzetost` int NOT NULL,
  `slika` longblob,
  PRIMARY KEY (`idKviz`),
  KEY `FK_kviz_idTem_idx` (`idTem`),
  KEY `FK_kviz_idOrg_idx` (`idOrg`),
  CONSTRAINT `FK_kviz_idOrg` FOREIGN KEY (`idOrg`) REFERENCES `organizator` (`idOrg`) ON UPDATE CASCADE,
  CONSTRAINT `FK_kviz_idTem` FOREIGN KEY (`idTem`) REFERENCES `tema` (`idTem`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `organizator`
--

DROP TABLE IF EXISTS `organizator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `organizator` (
  `opis` longtext NOT NULL,
  `brKvizova` int NOT NULL,
  `slika` longblob,
  `naziv` varchar(255) NOT NULL,
  `idOrg` int NOT NULL,
  `odobren` tinyint NOT NULL,
  PRIMARY KEY (`idOrg`),
  CONSTRAINT `FK_organizator_idOrg` FOREIGN KEY (`idOrg`) REFERENCES `korisnik` (`idKor`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `prijava`
--

DROP TABLE IF EXISTS `prijava`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prijava` (
  `idPri` int NOT NULL AUTO_INCREMENT,
  `idKviz` int NOT NULL,
  `idKor` int NOT NULL,
  `nazivTima` varchar(45) NOT NULL,
  `brClanova` int NOT NULL,
  PRIMARY KEY (`idPri`),
  KEY `FK_prijava_idKviz_idx` (`idKviz`),
  KEY `FK_prijava_idKor_idx` (`idKor`),
  CONSTRAINT `FK_prijava_idKor` FOREIGN KEY (`idKor`) REFERENCES `korisnik` (`idKor`) ON UPDATE CASCADE,
  CONSTRAINT `FK_prijava_idKviz` FOREIGN KEY (`idKviz`) REFERENCES `kviz` (`idKviz`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `recenzija`
--

DROP TABLE IF EXISTS `recenzija`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recenzija` (
  `idRec` int NOT NULL AUTO_INCREMENT,
  `idOrgRec` int NOT NULL,
  `idKorRec` int NOT NULL,
  `ocena` int NOT NULL,
  `komentar` longtext NOT NULL,
  `datumVreme` datetime NOT NULL,
  PRIMARY KEY (`idRec`),
  KEY `FK_recenzija_idx` (`idOrgRec`),
  KEY `FK_recenzija_idOrgKor_idx` (`idKorRec`),
  CONSTRAINT `FK_recenzija_idOrgKor` FOREIGN KEY (`idKorRec`) REFERENCES `korisnik` (`idKor`) ON UPDATE CASCADE,
  CONSTRAINT `FK_recenzija_idOrgRec` FOREIGN KEY (`idOrgRec`) REFERENCES `organizator` (`idOrg`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tema`
--

DROP TABLE IF EXISTS `tema`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tema` (
  `idTem` int NOT NULL AUTO_INCREMENT,
  `naziv` varchar(45) NOT NULL,
  PRIMARY KEY (`idTem`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `uloga`
--

DROP TABLE IF EXISTS `uloga`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uloga` (
  `idUlo` int NOT NULL AUTO_INCREMENT,
  `naziv` varchar(45) NOT NULL,
  PRIMARY KEY (`idUlo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-04-20 12:39:15
