-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: zzp_baza
-- ------------------------------------------------------
-- Server version	8.0.33

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
-- Dumping data for table `auth_group`
--

LOCK TABLES `auth_group` WRITE;
/*!40000 ALTER TABLE `auth_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `auth_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `auth_group_permissions`
--

LOCK TABLES `auth_group_permissions` WRITE;
/*!40000 ALTER TABLE `auth_group_permissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `auth_group_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `auth_permission`
--

LOCK TABLES `auth_permission` WRITE;
/*!40000 ALTER TABLE `auth_permission` DISABLE KEYS */;
INSERT INTO `auth_permission` VALUES (1,'Can add log entry',1,'add_logentry'),(2,'Can change log entry',1,'change_logentry'),(3,'Can delete log entry',1,'delete_logentry'),(4,'Can view log entry',1,'view_logentry'),(5,'Can add permission',2,'add_permission'),(6,'Can change permission',2,'change_permission'),(7,'Can delete permission',2,'delete_permission'),(8,'Can view permission',2,'view_permission'),(9,'Can add group',3,'add_group'),(10,'Can change group',3,'change_group'),(11,'Can delete group',3,'delete_group'),(12,'Can view group',3,'view_group'),(13,'Can add content type',4,'add_contenttype'),(14,'Can change content type',4,'change_contenttype'),(15,'Can delete content type',4,'delete_contenttype'),(16,'Can view content type',4,'view_contenttype'),(17,'Can add session',5,'add_session'),(18,'Can change session',5,'change_session'),(19,'Can delete session',5,'delete_session'),(20,'Can view session',5,'view_session'),(21,'Can add korisnik',6,'add_korisnik'),(22,'Can change korisnik',6,'change_korisnik'),(23,'Can delete korisnik',6,'delete_korisnik'),(24,'Can view korisnik',6,'view_korisnik'),(25,'Can add kviz',7,'add_kviz'),(26,'Can change kviz',7,'change_kviz'),(27,'Can delete kviz',7,'delete_kviz'),(28,'Can view kviz',7,'view_kviz'),(29,'Can add tema',8,'add_tema'),(30,'Can change tema',8,'change_tema'),(31,'Can delete tema',8,'delete_tema'),(32,'Can view tema',8,'view_tema'),(33,'Can add uloga',9,'add_uloga'),(34,'Can change uloga',9,'change_uloga'),(35,'Can delete uloga',9,'delete_uloga'),(36,'Can view uloga',9,'view_uloga'),(37,'Can add organizator',10,'add_organizator'),(38,'Can change organizator',10,'change_organizator'),(39,'Can delete organizator',10,'delete_organizator'),(40,'Can view organizator',10,'view_organizator'),(41,'Can add prijava',11,'add_prijava'),(42,'Can change prijava',11,'change_prijava'),(43,'Can delete prijava',11,'delete_prijava'),(44,'Can view prijava',11,'view_prijava'),(45,'Can add recenzija',12,'add_recenzija'),(46,'Can change recenzija',12,'change_recenzija'),(47,'Can delete recenzija',12,'delete_recenzija'),(48,'Can view recenzija',12,'view_recenzija');
/*!40000 ALTER TABLE `auth_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `django_admin_log`
--

LOCK TABLES `django_admin_log` WRITE;
/*!40000 ALTER TABLE `django_admin_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `django_admin_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `django_content_type`
--

LOCK TABLES `django_content_type` WRITE;
/*!40000 ALTER TABLE `django_content_type` DISABLE KEYS */;
INSERT INTO `django_content_type` VALUES (1,'admin','logentry'),(3,'auth','group'),(2,'auth','permission'),(4,'contenttypes','contenttype'),(5,'sessions','session'),(6,'zzp','korisnik'),(7,'zzp','kviz'),(10,'zzp','organizator'),(11,'zzp','prijava'),(12,'zzp','recenzija'),(8,'zzp','tema'),(9,'zzp','uloga');
/*!40000 ALTER TABLE `django_content_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `django_migrations`
--

LOCK TABLES `django_migrations` WRITE;
/*!40000 ALTER TABLE `django_migrations` DISABLE KEYS */;
INSERT INTO `django_migrations` VALUES (1,'contenttypes','0001_initial','2023-05-29 18:25:37.653540'),(2,'contenttypes','0002_remove_content_type_name','2023-05-29 18:25:37.672057'),(3,'auth','0001_initial','2023-05-29 18:25:37.729949'),(4,'auth','0002_alter_permission_name_max_length','2023-05-29 18:25:37.743829'),(5,'auth','0003_alter_user_email_max_length','2023-05-29 18:25:37.746830'),(6,'auth','0004_alter_user_username_opts','2023-05-29 18:25:37.748950'),(7,'auth','0005_alter_user_last_login_null','2023-05-29 18:25:37.751773'),(8,'auth','0006_require_contenttypes_0002','2023-05-29 18:25:37.752774'),(9,'auth','0007_alter_validators_add_error_messages','2023-05-29 18:25:37.754877'),(10,'auth','0008_alter_user_username_max_length','2023-05-29 18:25:37.757877'),(11,'auth','0009_alter_user_last_name_max_length','2023-05-29 18:25:37.760578'),(12,'auth','0010_alter_group_name_max_length','2023-05-29 18:25:37.766386'),(13,'auth','0011_update_proxy_permissions','2023-05-29 18:25:37.769895'),(14,'auth','0012_alter_user_first_name_max_length','2023-05-29 18:25:37.772581'),(15,'zzp','0001_initial','2023-05-29 18:25:37.975602'),(16,'admin','0001_initial','2023-05-29 18:25:38.011543'),(17,'admin','0002_logentry_remove_auto_add','2023-05-29 18:25:38.015554'),(18,'admin','0003_logentry_add_action_flag_choices','2023-05-29 18:25:38.020066'),(19,'sessions','0001_initial','2023-05-29 18:25:38.030134'),(20,'zzp','0002_organizator_teme','2023-06-04 11:46:21.789758'),(21,'zzp','0003_alter_korisnik_slika','2023-06-04 11:46:21.793761'),(22,'zzp','0004_alter_kviz_zauzetost','2023-06-04 11:46:21.798168'),(23,'zzp','0005_alter_organizator_brkvizova','2023-06-04 14:56:16.245812'),(24,'zzp','0006_alter_organizator_odobren','2023-06-04 14:56:16.254322'),(25,'zzp','0007_organizator_predstojecikviz','2023-06-05 23:37:42.398647'),(26,'zzp','0008_korisnik_odobren','2023-06-06 09:47:04.575817'),(27,'zzp','0009_remove_korisnik_slika','2023-06-06 09:47:04.589324'),(28,'zzp','0010_alter_kviz_idorg_alter_organizator_predstojecikviz_and_more','2023-06-06 10:26:00.535639'),(29,'zzp','0011_alter_organizator_idorg','2023-06-06 10:29:09.263453');
/*!40000 ALTER TABLE `django_migrations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `django_session`
--

LOCK TABLES `django_session` WRITE;
/*!40000 ALTER TABLE `django_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `django_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `korisnik`
--

LOCK TABLES `korisnik` WRITE;
/*!40000 ALTER TABLE `korisnik` DISABLE KEYS */;
INSERT INTO `korisnik` VALUES (1,'pbkdf2_sha256$600000$ak1wX03snlse7EfcCvbldU$xGIeF2IGxQnoDU14g5i6guzE6oyyXVb/6Ng6udqn87M=','2023-06-06 18:54:02.211290',1,'admin','','','',1,1,'2023-05-29 18:27:33.742250',1,0),(18,'pbkdf2_sha256$600000$6YSsL0X4an3vWxE3B5qUgM$GY8fXK7gvVdpnERRwLEWvfPbLGR9RW2SUdrKNIvyYzM=','2023-06-06 19:28:20.250266',0,'pera','','','',0,1,'2023-06-06 18:58:13.649651',4,0),(19,'pbkdf2_sha256$600000$dxqNZOVrBZyQNJqQrGq1r5$cGplCveTrCm9Uj+eqWYPtcFa016B4g1v/Wkf7Y2/4Rw=','2023-06-06 19:29:20.137475',0,'mika','','','',0,1,'2023-06-06 18:58:29.456041',4,0),(20,'pbkdf2_sha256$600000$xnHpFGtDEYycggkFxmrtNw$eyHwfBy3xM6Zo+nGMsGnj7zqjjjXXsQKdHAvMGmjBmI=','2023-06-06 19:32:35.136264',0,'zika','','','',0,1,'2023-06-06 18:58:45.481770',4,0),(21,'pbkdf2_sha256$600000$PFeINVFedX377pV9bikTmT$ZuzP9P4Oq84L+9TtMcBefi7pKQ6F74UPh5Bd/3mm/xQ=','2023-06-06 19:17:04.450853',0,'konquiztador','','','',0,1,'2023-06-06 19:06:55.773797',3,1),(22,'pbkdf2_sha256$600000$tAVTjJ0QJqdyh5XnvFHfEg$n2GKcb4jdgDBTGtQ8w7XANi/yxXiSsmRJdTNdPUSH/s=','2023-06-06 19:16:31.704833',0,'moderator','','','',0,1,'2023-06-06 19:07:32.812114',2,0),(23,'pbkdf2_sha256$600000$FqUtldG6pU8Hqv4K3jH2tk$UBIL3AJ+EwYU5pXhGp1Ni/NXMWnpy97TIjSANaovip8=','2023-06-06 19:35:25.682182',0,'tresarina','','','',0,1,'2023-06-06 19:13:39.847113',3,1),(24,'pbkdf2_sha256$600000$qjaILAP4Tq3AfYstG7wPsW$oReeWnXi7bR/5X1Wj+Vm2ptzkN1AcmZGxWbYup5OmGE=','2023-06-06 19:16:16.983498',0,'doublesat','','','',0,1,'2023-06-06 19:16:16.865937',3,0),(26,'pbkdf2_sha256$600000$s6Y4cxY6FXVUcprjxQLy4a$fzGUvUEfeCgP/0AMHoCkElh5zsrCEc2DmLyy7CLHfJI=','2023-06-06 19:41:23.315341',0,'postao_admin','','','',0,1,'2023-06-06 19:41:04.040257',1,0);
/*!40000 ALTER TABLE `korisnik` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `korisnik_groups`
--

LOCK TABLES `korisnik_groups` WRITE;
/*!40000 ALTER TABLE `korisnik_groups` DISABLE KEYS */;
/*!40000 ALTER TABLE `korisnik_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `korisnik_user_permissions`
--

LOCK TABLES `korisnik_user_permissions` WRITE;
/*!40000 ALTER TABLE `korisnik_user_permissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `korisnik_user_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `kviz`
--

LOCK TABLES `kviz` WRITE;
/*!40000 ALTER TABLE `kviz` DISABLE KEYS */;
INSERT INTO `kviz` VALUES (7,'Istorija sveta','Proverite svoje znanje iz oblasti poznavanja istorije sveta kroz različite epohe.','2023-06-30 20:00:00.000000','Merime Njegomir 13',250,50,5,'imgs/history.png',1,21),(8,'Naruto kviz','Proverite svoje poznavanja tajni sela lišća i okolnih zaseoka.','2023-07-03 19:30:00.000000','Jašara Ahmedovskog 56',300,45,5,'imgs/naruto.jpg',12,21),(9,'Drzavni posao','Šta li rade Baba, Šljivovička, Žika (težak alkoholičar) i naša omiljena tri arhivatora.\r\n*Vratićeš se kasno kući, iza slagalice...*','2023-07-27 19:00:00.000000','Pejićevi salaši BB',600,60,2,'imgs/drzavniPosao.jpeg',14,23),(10,'Premier league','Koliko daleko seže istorija fudbala sa čuvenog ostrva Evrope.','2023-07-06 21:00:00.000000','Ronaldinja Gauča 10',150,18,18,'imgs/football.png',7,23);
/*!40000 ALTER TABLE `kviz` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `organizator`
--

LOCK TABLES `organizator` WRITE;
/*!40000 ALTER TABLE `organizator` DISABLE KEYS */;
INSERT INTO `organizator` VALUES ('Prvi onlajn kviz na Balkanu, a sad smo vodeci pub kviz organizator.',2,'KonQuiztador',21,1,'imgs/konquiztador-low-resolution-color-logo_1.png','Opšti, Sport, Anime',8),('Najtreš kvizovi koje možete da zamislite. ČEKAMO VAS!',2,'Treš kviz',23,1,'imgs/tres-kviz-high-resolution-color-logo_1.png','Treš, Muzika, Sport',10),('Opis organizacije koja je  u zahtevima inicijalno.',0,'DoubleSat',24,0,'imgs/double-sat-low-resolution-color-logo_1.png','Opsti, Sport, Muzika',NULL);
/*!40000 ALTER TABLE `organizator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `prijava`
--

LOCK TABLES `prijava` WRITE;
/*!40000 ALTER TABLE `prijava` DISABLE KEYS */;
INSERT INTO `prijava` VALUES (26,'Liverpul',6,18,10),(27,'Perina ekipa',5,18,7),(28,'Pet kagea',5,19,8),(29,'Baraba i Princeza',2,19,9),(30,'Arsenal',6,19,10),(31,'Čelzi',6,20,10);
/*!40000 ALTER TABLE `prijava` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `recenzija`
--

LOCK TABLES `recenzija` WRITE;
/*!40000 ALTER TABLE `recenzija` DISABLE KEYS */;
INSERT INTO `recenzija` VALUES (5,5,'NAJJAČI BREEEE','2023-06-06 21:32:19.732795',19,23),(6,4,'Pokrali su me za prvo mesto!!!','2023-06-06 21:33:32.206606',20,21),(7,2,'Otkazali u poslednjem trenutku...','2023-06-06 21:34:08.575735',20,23);
/*!40000 ALTER TABLE `recenzija` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `tema`
--

LOCK TABLES `tema` WRITE;
/*!40000 ALTER TABLE `tema` DISABLE KEYS */;
INSERT INTO `tema` VALUES (1,'Opsti'),(7,'Sport'),(8,'Muzika'),(9,'Film'),(10,'Kaladont'),(11,'Na slovo'),(12,'Anime'),(13,'Treš'),(14,'Kultura');
/*!40000 ALTER TABLE `tema` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `uloga`
--

LOCK TABLES `uloga` WRITE;
/*!40000 ALTER TABLE `uloga` DISABLE KEYS */;
INSERT INTO `uloga` VALUES (1,'admin'),(2,'moderator'),(3,'organizator'),(4,'korisnik');
/*!40000 ALTER TABLE `uloga` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-06 21:46:47
