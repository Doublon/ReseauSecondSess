/* BD_AIRPORT du : 12/01 à 14h58 */

DROP TABLE Comptes;
DROP TABLE Agents;
DROP TABLE Bagages;
DROP TABLE Billets;
DROP TABLE Reserve;
DROP TABLE Vols;
DROP TABLE Avions;
DROP TABLE Clients;
DROP TABLE Pistes;

DROP PROCEDURE procedureVerifTimeOut;
DROP EVENT verifTimeOut;

CREATE TABLE Pistes
(
	numPiste INT PRIMARY KEY,
    libre INT
);


CREATE TABLE Clients 
(
	numClient INT AUTO_INCREMENT,
	nom VARCHAR(50),
	prenom VARCHAR(50),
	sexe CHAR,
	login VARCHAR(50) UNIQUE,
	password VARCHAR(50),
	PRIMARY KEY (numClient),
	UNIQUE (login)
)
ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE Avions 
(
	numAvion VARCHAR(10),
	typeAvion VARCHAR(20),
	compagnie VARCHAR(25),
	PRIMARY KEY (numAvion)
)
ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE Vols 
(
	numVol VARCHAR(20),
	destination VARCHAR(50),
	zone VARCHAR(50),
	nombrePlaces NUMERIC,
	placesRestantes NUMERIC,
	distance DOUBLE,
	heureDepart DATETIME,
	heureArrivee DATETIME,
	prixParPersonne DOUBLE,
	numAvion VARCHAR(10),
	PRIMARY KEY (numVol),
	FOREIGN KEY (numAvion) REFERENCES Avions(numAvion)
)
ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE Billets 
(
	numBillet VARCHAR(17),
	nombreAccompagnants NUMERIC,
	numClient INT NULL,
	numPlace INT NULL,
	numVol VARCHAR(20),
	PRIMARY KEY (numBillet),
	FOREIGN KEY (numClient) REFERENCES Clients(numClient),
	FOREIGN KEY (numVol) REFERENCES Vols(numVol)
)
ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE Reserve
(
	numCommande INT AUTO_INCREMENT,
	nombreAccompagnants NUMERIC,
	numClient INT,
	numPlace INT,
	numVol VARCHAR(20),
	heureReservation DATETIME,
	PRIMARY KEY (numCommande),
	FOREIGN KEY (numClient) REFERENCES Clients(numClient),
	FOREIGN KEY (numVol) REFERENCES Vols(numVol)
)
ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE Bagages 
(
	numBagage VARCHAR(30),
	poids DOUBLE,
	valise CHAR(1),
	receptionne CHAR(1),
	chargeSoute CHAR(1),
	verifieDouane CHAR(1),
	remarques VARCHAR(50),
	numBillet VARCHAR(17),
	PRIMARY KEY (numBagage),
	FOREIGN KEY (numBillet) REFERENCES Billets(numBillet)
)
ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE Agents 
(
	numAgent VARCHAR(10),
	nom VARCHAR(50),
	prenom VARCHAR(50),
	poste VARCHAR(50),
	login VARCHAR(50) UNIQUE,
	password VARCHAR(50),
	PRIMARY KEY (numAgent)
)
ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE Comptes
(
	numCompte VARCHAR(20),
	balance DOUBLE,
	numClient INT,
	PRIMARY KEY (numCompte),
	FOREIGN KEY (numClient) REFERENCES Clients(numClient),
	CHECK(balance >= 0)
)
ENGINE=INNODB DEFAULT CHARSET=utf8;


INSERT INTO pistes VALUES (1, 0);
INSERT INTO pistes VALUES (2, 0);
INSERT INTO pistes VALUES (3, 0);
INSERT INTO pistes VALUES (4, 0);
INSERT INTO pistes VALUES (5, 0);


INSERT INTO Clients VALUES (1,"Verwimp","Jim","H","client1","password1");
INSERT INTO Clients VALUES (2,"Rorive","Olivier","H","client2","password2");
INSERT INTO Clients VALUES (3,"Tusset","Quentin","H",null,null);
INSERT INTO Clients VALUES (4,"Dubois","Philippe","H",null,null);
INSERT INTO Clients VALUES (5,"Hazee","Claire","F",null,null);
INSERT INTO Clients VALUES (6,"Cannella","Claude","F",null,null);
INSERT INTO Clients VALUES (7,"Charlet","Christophe","H",null,null);
INSERT INTO Clients VALUES (8,"Vilvens","Claude","H",null,null);
INSERT INTO Clients VALUES (9,"Romio","Alfonso","H",null,null);
INSERT INTO Clients VALUES (10,"Herbiet","Laurence","F",null,null);

INSERT INTO Avions VALUES ("1","AIRBUS","POWDER-AIRLINES");
INSERT INTO Avions VALUES ("2","BOEING","MACHIN-AIRWAYS");
INSERT INTO Avions VALUES ("3","AIRBUS","FRANCE-AIR");
INSERT INTO Avions VALUES ("4","AIRBUS","EMIRATES");
INSERT INTO Avions VALUES ("5","BOEING","QATAR-AIRWAYS");

INSERT INTO Vols VALUES ("361","New York","AM-N",180,175,5890.72,'2017-11-16 06:12:00','2017-11-16 10:11:00',null,"2");
INSERT INTO Vols VALUES ("362","Peshawar","AS",250,244,7422,'2017-11-16 14:36:00','2017-11-16 18:11:00',null,"1");
INSERT INTO Vols VALUES ("68","Majorque","EUR",185,183,1261.33,'2016-10-26 06:28:00','2016-10-26 09:03:00',null,"2");
INSERT INTO Vols VALUES ("69","Madrid","EUR",185,183,1261.33,'2017-10-26 06:28:00','2017-10-26 09:03:00',null,"2");
INSERT INTO Vols VALUES ("119","Lisbonne","EUR",45,43,10461,'2016-11-09 09:06:00','2016-11-07 09:26:00',null,"2");
INSERT INTO Vols VALUES ("120","Porto","EUR",61,59,10461,'2017-11-09 09:06:00','2017-11-07 09:26:00',null,"2");
INSERT INTO Vols VALUES ("121","Barcelone","EUR",5,3,10461,'2017-12-09 09:06:00','2017-12-07 09:26:00',null,"2");
INSERT INTO Vols VALUES ("258","Maroc","AFR-N",37,34,2872,'2017-01-20 07:35:00','2017-11-07 10:48:00',null,"1");
INSERT INTO Vols VALUES ("404","Marseille","EUR",100,99,1043.60,'2016-11-13 08:20:00','2017-11-13 10:20:00',null,"4");
INSERT INTO Vols VALUES ("405","Paris","EUR",100,98,1043.60,'2017-11-17 08:20:00','2017-11-17 10:20:00',null,"4");
INSERT INTO Vols VALUES ("566","Melbourne","OCEA",4,4,16620,'2017-11-30 05:21:00','2017-11-30 14:56:00',null,"1");
INSERT INTO Vols VALUES ("567","Sidney","OCEA",32,30,16620,'2017-12-07 05:21:00','2017-12-07 14:56:00',null,"1");
INSERT INTO Vols VALUES ("568","Brisbane","OCEA",3,1,16620,'2017-10-15 03:33:00','2017-10-15 15:15:00',null,"1");
INSERT INTO Vols VALUES ("569","Wellington","OCEA",26,24,16620,'2017-11-08 05:21:00','2017-11-08 14:56:00',null,"1");

INSERT INTO Billets VALUES ("68-26102016-0001",1,2,null,"68");
INSERT INTO Billets VALUES ("68-26102016-0002",null,2,null,"68");
INSERT INTO Billets VALUES ("69-26102017-0001",1,2,null,"69");
INSERT INTO Billets VALUES ("69-26102017-0002",null,2,null,"69");
INSERT INTO Billets VALUES ("119-09112016-0001",1,3,null,"119");
INSERT INTO Billets VALUES ("119-09112016-0002",null,3,null,"119");
INSERT INTO Billets VALUES ("120-09112017-0001",1,6,null,"120");
INSERT INTO Billets VALUES ("120-09112017-0002",null,6,null,"120");
INSERT INTO Billets VALUES ("121-09122017-0001",1,2,null,"121");
INSERT INTO Billets VALUES ("121-09122017-0002",null,2,null,"121");
INSERT INTO Billets VALUES ("258-20012017-0001",2,5,null,"258");
INSERT INTO Billets VALUES ("258-20012017-0002",null,5,null,"258");
INSERT INTO Billets VALUES ("258-20012017-0003",null,5,null,"258");
INSERT INTO Billets VALUES ("361-16112017-0001",3,2,null,"361");
INSERT INTO Billets VALUES ("361-16112017-0002",null,2,null,"361");
INSERT INTO Billets VALUES ("361-16112017-0003",null,2,null,"361");
INSERT INTO Billets VALUES ("361-16112017-0004",null,2,null,"361");
INSERT INTO Billets VALUES ("361-16112017-0005",0,5,null,"361");
INSERT INTO Billets VALUES ("362-16112017-0001",1,1,null,"362");
INSERT INTO Billets VALUES ("362-16112017-0002",null,1,null,"362");
INSERT INTO Billets VALUES ("362-16112017-0003",3,3,null,"362");
INSERT INTO Billets VALUES ("362-16112017-0004",null,3,null,"362");
INSERT INTO Billets VALUES ("362-16112017-0005",null,3,null,"362");
INSERT INTO Billets VALUES ("362-16112017-0006",null,3,null,"362");
INSERT INTO Billets VALUES ("404-13112016-0001",0,4,null,"404");
INSERT INTO Billets VALUES ("405-17112017-0001",1,2,null,"405");
INSERT INTO Billets VALUES ("405-17112017-0002",null,2,null,"405");
INSERT INTO Billets VALUES ("667-07122017-0001",1,2,null,"567");
INSERT INTO Billets VALUES ("668-15102017-0001",1,2,null,"568");
INSERT INTO Billets VALUES ("669-08112017-0001",1,2,null,"569");

INSERT INTO Bagages VALUES ("68-Rorive-26102016-0001-001",8.86,'N','N','N','N',"Bagage solitaire","68-26102016-0001");
INSERT INTO Bagages VALUES ("68-Rorive-26102016-0001-002",8.86,'N','N','N','N',"","68-26102016-0001");
INSERT INTO Bagages VALUES ("69-Rorive-26102017-0001-001",4.98,'N','N','N','N',"Suspect","69-26102017-0001");
INSERT INTO Bagages VALUES ("69-Rorive-26102017-0001-002",5.96,'N','N','N','N',"","69-26102017-0001");
INSERT INTO Bagages VALUES ("119-Dupont-09112016-0001-001",3.14,'N','N','N','N',"","119-09112016-0001");
INSERT INTO Bagages VALUES ("119-Dupont-09112016-0001-002",3.14,'N','N','N','N',"","119-09112016-0001");
INSERT INTO Bagages VALUES ("120-Cannella-09112017-0001-001",5.65,'N','N','N','N',"","120-09112017-0001");
INSERT INTO Bagages VALUES ("120-Cannella-09112017-0001-002",5.65,'N','N','N','N',"","120-09112017-0001");
INSERT INTO Bagages VALUES ("121-Rorive-09122017-0001-001",5.65,'N','N','N','N',"","121-09122017-0001");
INSERT INTO Bagages VALUES ("121-Rorive-09122017-0001-002",5.65,'N','N','N','N',"","121-09122017-0001");
INSERT INTO Bagages VALUES ("258-Hazee-20012017-0001-001",7.56,'N','N','N','N',"","258-20012017-0001");
INSERT INTO Bagages VALUES ("258-Hazee-20012017-0001-002",7.56,'N','N','N','N',"","258-20012017-0001");
INSERT INTO Bagages VALUES ("361-Rorive-16112017-0001-001",14.28,'N','N','N','N',"Bagage inutile","361-16112017-0001");
INSERT INTO Bagages VALUES ("361-Rorive-16112017-0001-002",14.28,'N','N','N','N',"","361-16112017-0001");
INSERT INTO Bagages VALUES ("361-Rorive-16112017-0001-003",14.28,'N','N','N','N',"","361-16112017-0001");
INSERT INTO Bagages VALUES ("361-Hazee-16112017-0005-001",7.56,'N','N','N','N',"Beau bagage","361-16112017-0005");
INSERT INTO Bagages VALUES ("361-Hazee-16112017-0005-002",7.56,'N','N','N','N',"","361-16112017-0005");
INSERT INTO Bagages VALUES ("362-Verwimp-16112017-0001-001",14.28,'O','N','N','N',"","362-16112017-0001");
INSERT INTO Bagages VALUES ("362-Verwimp-16112017-0001-002",6.69,'N','N','N','N',"Deuxième bagage","362-16112017-0001");
INSERT INTO Bagages VALUES ("362-Verwimp-16112017-0001-003",2.22,'O','N','N','N',"Sachet","362-16112017-0001");
INSERT INTO Bagages VALUES ("362-Dupont-16112017-0003-001",11.97,'O','N','N','N',"Un pont du pont","362-16112017-0003");
INSERT INTO Bagages VALUES ("362-Dupont-16112017-0003-002",4.45,'N','N','N','N',"Foin","362-16112017-0003");
INSERT INTO Bagages VALUES ("404-Dubois-13112016-0001-001",5.65,'N','N','N','N',"","404-13112016-0001");
INSERT INTO Bagages VALUES ("405-Rorive-17112017-0001-001",11.97,'N','N','N','N',"","405-17112017-0001");
INSERT INTO Bagages VALUES ("405-Rorive-17112017-0001-002",11.97,'N','N','N','N',"","405-17112017-0001");
INSERT INTO Bagages VALUES ("467-Rorive-07122017-0001-001",5.65,'N','N','N','N',"","667-07122017-0001");


INSERT INTO Agents VALUES ("4697","Cielle","Aiguille","Aiguilleur du ciel",null,null);
INSERT INTO Agents VALUES ("2138","Vieux","Michel","Bagagiste","user1","password1");
INSERT INTO Agents VALUES ("1283","Jeune","Albert","Bagagiste","user2","password2");
INSERT INTO Agents VALUES ("7777","Tusset","Quentin","Bagagiste","tusque","123Soleil");
INSERT INTO Agents VALUES ("1234","Rorive","Olivier","Bagagiste","roro","123Soleil");

COMMIT;

delimiter |

CREATE PROCEDURE procedureVerifTimeOut()
BEGIN
		DECLARE done INT DEFAULT FALSE;
        DECLARE vol VARCHAR(20);
        DECLARE nbrPlaces INT;
        DECLARE commande INT;
		
		DECLARE curseur CURSOR FOR SELECT numCommande, numVol, nombreAccompagnants FROM Reserve WHERE TIMESTAMPDIFF(MINUTE,heureReservation,current_timestamp) >= 1;
		DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

		OPEN curseur;
        
        read_loop: LOOP
			FETCH curseur INTO commande, vol, nbrPlaces;
            IF done THEN
				LEAVE read_loop;
			END IF;
            SET nbrPlaces = nbrPlaces + 1;
            UPDATE Vols set placesRestantes = placesRestantes + nbrPlaces where numVol = vol;
            DELETE FROM Reserve WHERE numCommande = commande;
        END LOOP;
END |

delimiter ;

CREATE EVENT verifTimeOut
ON SCHEDULE EVERY 1 MINUTE
COMMENT 'Retirer les billets réservés si timeout (Web_Applic_Billets)'
DO
	CALL procedureVerifTimeOut();

COMMIT;