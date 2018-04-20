/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.DatabaseAmazon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author sswu
 */
public class CreateMysqlConnection {
    
    private Connection _mysqlDB = null;
    private String user;
    private String password;
    //private String server = "phpmyadmin.cs.ksu.edu";
    private String serverDriver = "com.mysql.jdbc.Driver";
    public String serverUrl = "jdbc:mysql://mysql.cis.ksu.edu/";
    public CreateMysqlConnection(String u, String p) {
        user = u;
        password = p;
        serverUrl += user;
    }
    
    public Connection openConnection() {
        
        try {
            /* load jdbc driver for mysql */
            Class.forName(serverDriver).newInstance();
        
            /* open a connection to the database */
            _mysqlDB = DriverManager.getConnection(serverUrl, user, password);
           
        } catch (SQLException e) {
            System.out.println("Open mysql database connection error");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return _mysqlDB;
    }
    
    public void closeConnections() {
        try {
            _mysqlDB.close();
        } catch (Exception e) {
            System.out.println("Close mysql connection error");
        }
    }
    
    
}
