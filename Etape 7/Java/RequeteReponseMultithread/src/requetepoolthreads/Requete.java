package requetepoolthreads;

import java.net.*;

public interface Requete
{
    public void TraiterRequete(Socket sock, ConsoleServeur cs);
}