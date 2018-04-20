/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.DatabaseAmazon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author sswu
 */
public class BasicQuery {
    
    private Connection _mysqlDB;
    
    private String _search_by_asin = "Select * from product where asin = ?";
    private PreparedStatement _search_by_asin_statement;
    
    public BasicQuery(Connection c) {
        _mysqlDB = c;
    }
    
    public void prepareStatements() {
        try {
            _search_by_asin_statement = _mysqlDB.prepareStatement(_search_by_asin);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void search(String asin) {
        try {
            _search_by_asin_statement.clearParameters();
            _search_by_asin_statement.setString(1, asin);
            
            ResultSet product_set = _search_by_asin_statement.executeQuery();
            while (product_set.next()) {
                String title = product_set.getString(2);
                System.out.println(title);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
