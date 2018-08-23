package tickmap_classes;

import java.io.Serializable;
import java.util.List;

public class Reservation implements Serializable
{
    private Vol vol;
    private Client client;
    private List<Client> listeAccompagnants;
    
    public Reservation()
    {
        
    }
    
    public Reservation(Vol v, Client c, List<Client> lA)
    {
        vol = v;
        client = c;
        listeAccompagnants = lA;
    }

    /**
     * @return the vol
     */
    public Vol getVol()
    {
        return vol;
    }

    /**
     * @param vol the vol to set
     */
    public void setVol(Vol vol)
    {
        this.vol = vol;
    }

    /**
     * @return the client
     */
    public Client getClient()
    {
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(Client client)
    {
        this.client = client;
    }

    /**
     * @return the listeAccompagnants
     */
    public List<Client> getListeAccompagnants()
    {
        return listeAccompagnants;
    }

    /**
     * @param listeAccompagnants the listeAccompagnants to set
     */
    public void setListeAccompagnants(List<Client> listeAccompagnants)
    {
        this.listeAccompagnants = listeAccompagnants;
    }
}
