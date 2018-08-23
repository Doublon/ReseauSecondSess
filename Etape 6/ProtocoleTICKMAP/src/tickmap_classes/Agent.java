package tickmap_classes;

import java.io.Serializable;

public class Agent implements Serializable
{
    private String numAgent;
    private String nom;
    private String prenom;
    private String poste;
    private String login;
    private String password;
    
    public Agent()
    {
        
    }
    
    /**
     * @return the numAgent
     */
    public String getNumAgent()
    {
        return numAgent;
    }

    /**
     * @param numAgent the numAgent to set
     */
    public void setNumAgent(String numAgent)
    {
        this.numAgent = numAgent;
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
     * @return the poste
     */
    public String getPoste()
    {
        return poste;
    }

    /**
     * @param poste the poste to set
     */
    public void setPoste(String poste)
    {
        this.poste = poste;
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
    
    @Override
    public String toString()
    {
        return numAgent + " | " + nom + " " + prenom + " | " + poste  + " | " + login;
    }
}
