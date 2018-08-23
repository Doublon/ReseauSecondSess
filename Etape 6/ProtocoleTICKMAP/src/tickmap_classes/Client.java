package tickmap_classes;

import java.io.Serializable;

public class Client implements Serializable
{
    public static final char MASCULIN = 'H';
    public static final char FEMININ = 'F';
    
    private int numClient;
    private String nom;
    private String prenom;
    private String login;
    private String password;
    private char sexe;
    
    public Client()
    {
        
    }
    
    public Client(int nC)
    {
        numClient = nC;
    }
    
    public Client(String n, String p, String l, String pass)
    {
        nom = n;
        prenom = p;
        login = l;
        password = pass;
    }
    
    public Client(int nC, String n, String p, String l, String pass, char s)
    {
        numClient = nC;
        nom = n;
        prenom = p;
        login = l;
        password = pass;
        sexe = s;
    }

    /**
     * @return the numClient
     */
    public int getNumClient()
    {
        return numClient;
    }

    /**
     * @param numClient the numClient to set
     */
    public void setNumClient(int numClient)
    {
        this.numClient = numClient;
    }

    /**
     * @return the nom
     */
    public String getNom()
    {
        return nom;
    }

    /**
     * @param nom the nom to set
     */
    public void setNom(String nom)
    {
        this.nom = nom;
    }

    /**
     * @return the prenom
     */
    public String getPrenom()
    {
        return prenom;
    }

    /**
     * @param prenom the prenom to set
     */
    public void setPrenom(String prenom)
    {
        this.prenom = prenom;
    }

    /**
     * @return the login
     */
    public String getLogin()
    {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login)
    {
        this.login = login;
    }

    /**
     * @return the password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * @return the sexe
     */
    public char getSexe()
    {
        return sexe;
    }

    /**
     * @param sexe the sexe to set
     */
    public void setSexe(char sexe)
    {
        this.sexe = sexe;
    }
    
    @Override
    public String toString()
    {
        return numClient + " -> " + nom +  " " + prenom;
    }
}
