-- MySQL dump 10.13  Distrib 8.0.20, for Win64 (x86_64)
--
-- Host: eu-cdbr-west-03.cleardb.net    Database: heroku_47fd00a889de629
-- ------------------------------------------------------
-- Server version	5.6.47-log

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
-- Table structure for table `clients`
--

DROP TABLE IF EXISTS `clients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clients` (
  `date` date NOT NULL,
  `client_id` varchar(10) NOT NULL,
  `time_slot` varchar(8) NOT NULL,
  `status` varchar(10) DEFAULT 'booked',
  `start_time` varchar(6) DEFAULT '--:--',
  `end_time` varchar(6) DEFAULT '--:--',
  `specialist_name` varchar(45) DEFAULT ' ',
  UNIQUE KEY `ClientId_UNIQUE` (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clients`
--

LOCK TABLES `clients` WRITE;
/*!40000 ALTER TABLE `clients` DISABLE KEYS */;
INSERT INTO `clients` VALUES ('2020-09-01','1091759','6','occurred','19:25','19:25','Vytautas'),('2020-09-02','1099506','14','occurred','19:28','19:28','Vytautas'),('2020-09-02','1109045','6','canceled','--:--','--:--','Vytautas'),('2020-09-01','1522576','3','occurred','21:41','21:41','Vytautas'),('2020-09-02','1532186','5','occurred','21:41','21:41','Vytautas'),('2020-10-10','1584968','14','booked','--:--','--:--','Petras'),('2020-09-02','1607687','14','canceled','--:--','--:--','Vytautas'),('2020-09-01','1620721','13','canceled','--:--','--:--','Vytautas'),('2020-09-01','7096660','5','canceled','--:--','--:--','Vytautas'),('2020-09-01','7232878','3','canceled','--:--','--:--','Vytautas'),('2020-09-01','7321692','5','canceled','--:--','--:--','Vytautas'),('2020-09-01','7350365','4','canceled','--:--','--:--','Vytautas'),('2020-09-01','7363732','4','occurred','19:13','19:20','Vytautas'),('2020-09-01','8676054','7','occurred','21:41','21:41','Vytautas'),('2020-09-01','8680764','8','booked','--:--','--:--','Vytautas'),('2020-09-01','8684104','5','booked','--:--','--:--','Vytautas'),('2020-09-01','8686016','8','booked','--:--','--:--','Vytautas'),('2020-09-01','8687716','9','booked','--:--','--:--','Vytautas'),('2020-09-01','8689698','10','booked','--:--','--:--','Vytautas'),('2020-09-01','8693325','2','booked','--:--','--:--','Vytautas'),('2020-09-01','9236279','12','booked','--:--','--:--','Vytautas'),('2020-10-10','9240219','6','booked','--:--','--:--','Kazimieras'),('2020-10-10','9244759','6','booked','--:--','--:--','Petras'),('2020-10-10','9249194','7','canceled','--:--','--:--','Petras'),('2020-10-12','9257425','7','canceled','--:--','--:--','Stasys'),('2020-10-10','9264425','7','booked','--:--','--:--','Petras'),('2020-10-10','9556489','6','canceled','--:--','--:--','Vytautas'),('2020-10-10','9860733','7','canceled','--:--','--:--','Vytautas');
/*!40000 ALTER TABLE `clients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `specialists`
--

DROP TABLE IF EXISTS `specialists`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `specialists` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `specialist_name` varchar(45) NOT NULL,
  `current_client` int(11) DEFAULT '0',
  `number` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8 KEY_BLOCK_SIZE=4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `specialists`
--

LOCK TABLES `specialists` WRITE;
/*!40000 ALTER TABLE `specialists` DISABLE KEYS */;
INSERT INTO `specialists` VALUES (11,'Vytautas',0,101),(21,'Kazimieras',0,102),(31,'Petras',0,104),(41,'Stasys',0,105);
/*!40000 ALTER TABLE `specialists` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `time_table`
--

DROP TABLE IF EXISTS `time_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `time_table` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `specialist_name` varchar(45) NOT NULL,
  `slot_1` varchar(45) DEFAULT 'empty',
  `slot_2` varchar(45) DEFAULT 'empty',
  `slot_3` varchar(45) DEFAULT 'empty',
  `slot_4` varchar(45) DEFAULT 'empty',
  `slot_5` varchar(45) DEFAULT 'empty',
  `slot_6` varchar(45) DEFAULT 'empty',
  `slot_7` varchar(45) DEFAULT 'empty',
  `slot_8` varchar(45) DEFAULT 'empty',
  `slot_9` varchar(45) DEFAULT 'empty',
  `slot_10` varchar(45) DEFAULT 'empty',
  `slot_11` varchar(45) DEFAULT 'empty',
  `slot_12` varchar(45) DEFAULT 'empty',
  `slot_13` varchar(45) DEFAULT 'empty',
  `slot_14` varchar(45) DEFAULT 'empty',
  `slot_15` varchar(45) DEFAULT 'empty',
  `slot_16` varchar(45) DEFAULT 'empty',
  PRIMARY KEY (`id`),
  KEY `specialist_name_idx` (`specialist_name`)
) ENGINE=InnoDB AUTO_INCREMENT=151 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `time_table`
--

LOCK TABLES `time_table` WRITE;
/*!40000 ALTER TABLE `time_table` DISABLE KEYS */;
INSERT INTO `time_table` VALUES (11,'2020-10-10','Vytautas','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty'),(21,'2020-10-10','Kazimieras','empty','empty','empty','empty','empty','9240219','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty'),(31,'2020-10-10','Petras','empty','empty','empty','empty','empty','9244759','9264425','empty','empty','empty','empty','empty','empty','1584968','empty','empty'),(41,'2020-10-12','Vytautas','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty'),(81,'2020-10-12','Stasys','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty'),(91,'2020-08-30','Vytautas','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty'),(101,'2020-09-01','Vytautas','empty','8693325','occurred','occurred','8684104','occurred','occurred','8686016','8687716','8689698','empty','9236279','empty','empty','empty','empty'),(111,'2020-09-02','Vytautas','empty','empty','empty','empty','occurred','empty','empty','empty','empty','empty','empty','empty','empty','occurred','empty','empty'),(121,'2020-09-03','Vytautas','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty'),(131,'2020-09-04','Vytautas','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty'),(141,'2020-09-05','Vytautas','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty','empty');
/*!40000 ALTER TABLE `time_table` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-08-31 21:55:40
