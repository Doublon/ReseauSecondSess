.SILENT : 

CHEMIN = $(HOME)/InpresAirport/CheckIn/

ALL : Application_CheckIn Serveur_CheckIn

SocketUtilities.o: $(CHEMIN)/SocketUtilities/SocketUtilities.c $(CHEMIN)/SocketUtilities/SocketUtilities.h
	echo Creation de SocketUtilities.o ...
	gcc -c $(CHEMIN)/SocketUtilities/SocketUtilities.c

LibrairieCSV.o : $(CHEMIN)/LibrairieCSV/LibrairieCSV.c $(CHEMIN)/LibrairieCSV/LibrairieCSV.h
	       echo Creation de LibrairieCSV.o ...
	       gcc -c $(CHEMIN)/LibrairieCSV/LibrairieCSV.c

LibrairieServeur.o : $(CHEMIN)/LibrairieServeur/LibrairieServeur.c $(CHEMIN)/LibrairieServeur/LibrairieServeur.h $(CHEMIN)/SocketUtilities/SocketUtilities.h
		     echo Creation de LibrairieServeur.o ...
		     gcc -c $(CHEMIN)/LibrairieServeur/LibrairieServeur.c $(CHEMIN)/SocketUtilities/SocketUtilities.c

UtileCheckIn.o : UtileCheckIn.c
		 echo Creation de UtileCheckIn.o ...
		 gcc -c UtileCheckIn.c

Application_CheckIn : Application_CheckIn.c SocketUtilities.o UtileCheckIn.o
	echo Creation de Application_CheckIn ...
	gcc -o Application_CheckIn Application_CheckIn.c SocketUtilities.o UtileCheckIn.o -lnsl -lsocket -lrt

Serveur_CheckIn : Serveur_CheckIn.c SocketUtilities.o LibrairieCSV.o UtileCheckIn.o LibrairieServeur.o
	echo Creation de Serveur_CheckIn ...
	gcc -o Serveur_CheckIn Serveur_CheckIn.c SocketUtilities.o LibrairieCSV.o UtileCheckIn.o LibrairieServeur.o -lnsl -lsocket -lrt
