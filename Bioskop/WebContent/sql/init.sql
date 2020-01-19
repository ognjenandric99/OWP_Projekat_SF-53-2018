DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Filmovi;
DROP TABLE IF EXISTS Karta;
DROP TABLE IF EXISTS Projekcije;
DROP TABLE IF EXISTS Sale;
DROP TABLE IF EXISTS Sedista;
DROP TABLE IF EXISTS Tipovi_Projekcija;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `Bioskop`
--

-- --------------------------------------------------------

--
-- Table structure for table `Filmovi`
--

CREATE TABLE `Filmovi` (
  `ID` integer PRIMARY KEY,
  `Naziv` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Reziser` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Glumci` varchar(2555) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Zanrovi` varchar(2555) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Trajanje` int(255) NOT NULL,
  `Distributer` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Zemlja_Porekla` int(255) NOT NULL,
  `Godina_Proizvodnje` int(4) NOT NULL,
  `Opis` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL
);

-- --------------------------------------------------------

--
-- Table structure for table `Karta`
--

CREATE TABLE `Karta` (
  `ID` integer PRIMARY KEY,
  `ID_Projekcije` int(255) NOT NULL,
  `ID_Sedista` int(255) NOT NULL,
  `VremeProdaje` datetime NOT NULL,
  `Korisnik` varchar(255) COLLATE utf8_unicode_ci NOT NULL
);

-- --------------------------------------------------------

--
-- Table structure for table `Projekcije`
--

CREATE TABLE `Projekcije` (
  `ID` integer PRIMARY KEY,
  `ID_Filma` int(255) NOT NULL,
  `ID_Tipa_Projekcije` int(255) NOT NULL,
  `ID_Sale` int(255) NOT NULL,
  `Termin` datetime NOT NULL,
  `CenaKarte` int(255) NOT NULL,
  `Administrator` varchar(255) COLLATE utf8_unicode_ci NOT NULL
);

-- --------------------------------------------------------

--
-- Table structure for table `Sale`
--

CREATE TABLE `Sale` (
  `ID` integer PRIMARY KEY,
  `Naziv` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `ID_Tipova_Projekcija` varchar(255) COLLATE utf8_unicode_ci NOT NULL
);

-- --------------------------------------------------------

--
-- Table structure for table `Sedista`
--

CREATE TABLE `Sedista` (
  `ID` integer PRIMARY KEY,
  `ID_Sale` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Broj_Sedista` int(255) NOT NULL
);

-- --------------------------------------------------------

--
-- Table structure for table `Tipovi_Projekcija`
--

CREATE TABLE `Tipovi_Projekcija` (
  `ID` integer PRIMARY KEY,
  `Naziv` varchar(255) COLLATE utf8_unicode_ci NOT NULL
);

-- --------------------------------------------------------

--
-- Table structure for table `Users`
--

CREATE TABLE `Users` (
  `ID` integer PRIMARY KEY,
  `Username` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Password` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `DatumRegistracije` date NOT NULL,
  `Uloga` varchar(255) COLLATE utf8_unicode_ci NOT NULL
);

INSERT INTO Filmovi(Naziv,Reziser,Glumci,Zanrovi,Trajanje,Distributer,Zemlja_Porekla,Godina_Proizvodnje,Opis) VALUES("RED","Robert Chventele","Bruce Willis;Morgan F;","Akcija;Avantura",150,"Delta Video","Amerika",2010,"Agent u penziji.");