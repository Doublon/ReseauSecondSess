package database.utilities;

import java.sql.*;

public class BeanBDMySQL extends BeanBD
{ 
    public BeanBDMySQL(String host, String port, String login, String password, String schema)
    {
        super(host,port,login,password,schema);
        setDriver("com.mysql.jdbc.Driver");
    }
    
    @Override
    public void ConnexionDB() throws SQLException, ClassNotFoundException
    {
        String chaineConnexion = "jdbc:mysql://" + host + ":" + port + "/" + schema + "?autoReconnect=true&useSSL=false";
        
        Class.forName(this.driver);
            
        connection = DriverManager.getConnection(chaineConnexion,login,password);
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);       
        //statement = (Statement) connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
    }  
}
