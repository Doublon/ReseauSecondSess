package database.utilities;

import java.sql.*;

public class BeanBDOracle extends BeanBD
{
    public BeanBDOracle(String host, String port, String login, String password) 
    {
        super(host,port,login,password);
        setDriver("oracle.jdbc.driver.OracleDriver");
    }
    
    @Override
    public void ConnexionDB() throws SQLException, ClassNotFoundException 
    {
        String url = "jdbc:oracle:thin:@//localhost:" + port + "/xe";
        
        Class.forName(this.driver);

        connection = (Connection) DriverManager.getConnection(url, login, password);
        //connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        //statement = (Statement) getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
    }
}
