package database.utilities;

import java.io.Serializable;
import java.sql.*;

abstract public class BeanBD implements Serializable
{
    protected String driver;
    protected String host;
    protected String port;
    protected String login;
    protected String password;
    protected String schema;
    protected Connection connection;
    protected Statement statement;
    protected ResultSet resultat;
    
    public BeanBD(String host, String port, String login, String password)
    {
        setHost(host);
        setPort(port);
        setLogin(login);
        setPassword(password);
    }
    
    public BeanBD(String host, String port, String login, String password, String schema) 
    {
        setHost(host);
        setPort(port);
        setLogin(login);
        setPassword(password);
        setSchema(schema);
    }
    
    abstract public void ConnexionDB() throws SQLException, ClassNotFoundException;
    
    public void DeconnexionDB() throws SQLException
    {
        if(connection != null)
            connection.close();
    
        if(statement != null)
            statement.close();
    }
    
    synchronized public ResultSet executerRequete(String requete) throws SQLException
    {
        statement = getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        resultat = statement.executeQuery(requete);
        
        return resultat;
    }
    
    synchronized public int executerUpdate(String requete) throws SQLException
    {
        //statement = getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        statement = getConnection().createStatement();
        return statement.executeUpdate(requete);
    }
    
    public ResultSet getResultat()
    {
        return resultat;
    }

    public void setResultat(ResultSet rs)
    {
        this.resultat = rs;
    }
    
    public void setDriver(String driver) 
    {
        this.driver = driver;
    }
    
    public String getDriver()
    {
        return driver;
    }

    public String getHost() {
        return host;
    }

    public final void setHost(String host) 
    {
        this.host = host;
    }

    public String getPort() 
    {
        return port;
    }

    public final void setPort(String port) 
    {
        this.port = port;
    }
    
    public String getSchema() 
    {
        return schema;
    }

    public final void setSchema(String schema) 
    {
        this.schema = schema;
    }

    public String getLogin() 
    {
        return login;
    }

    public final void setLogin(String login) 
    {
        this.login = login;
    }

    public String getPassword() 
    {
        return password;
    }

    public final void setPassword(String password) 
    {
        this.password = password;
    }
    
    public Connection getConnection() 
    {
        return connection;
    }

    public void setConnection(Connection connection) 
    {
        this.connection = connection;
    }
    
    public void commit() throws SQLException
    {
        this.connection.commit();
    }
    
    public void rollback() throws SQLException
    {
        this.connection.rollback();
    }
    
    public void setAutoCommit(boolean ac) throws SQLException
    {
        this.connection.setAutoCommit(ac);
    }
}
