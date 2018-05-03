/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.DatabaseAmazon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 *
 * @author sswu
 */
public class AdvancedQueryNoSQL {
    
    Connection _mysqlDB;
    
    private String _highest_salerank = "select product.title from product where product.type = ? and " + 
            "product.salerank >= All(select product.salerank from product where product.type = ?)";
    private PreparedStatement _highest_salerank_statement;
    
    private String _avg_rating = "select avg(rating) from review where cid = ? group by cid";
    private PreparedStatement _avg_rating_statement;
    
    private String _cat_of_highest_salerank = "select c.cat_id, c.name from category c join belongTo b on b.cat_id = c.cat_id " + 
            "where b.asin = (select asin from product where type = 'Book' " + 
            "and salerank >= All(select p.salerank from product p where p.type = 'Book'))";
    private PreparedStatement _cat_of_highest_salerank_statement;
    
    private String _highest_salerank_similar = "Select max(Z.salerank) as m, Z.name from (select distinct p.asin, p.salerank as salerank, p.title as name "
            + "from product p, (select asin2 from similarTo where asin1 = ?) X," 
            + "(select asin1 from similarTo where asin2 = ?) Y where p.asin = X.asin2 or p.asin = Y.asin1 or p.asin = ?) Z";
    private PreparedStatement _highest_salerank_similar_statement;
    
    public AdvancedQueryNoSQL(Connection mysqlDB) {
        _mysqlDB = mysqlDB;
    }
    
    public void prepareStatements() {
        try {
            _highest_salerank_statement = _mysqlDB.prepareStatement(_highest_salerank);
            _avg_rating_statement = _mysqlDB.prepareStatement(_avg_rating);
            _cat_of_highest_salerank_statement = _mysqlDB.prepareStatement(_cat_of_highest_salerank);
            _highest_salerank_similar_statement = _mysqlDB.prepareStatement(_highest_salerank_similar);
            //_search_similar_group_statement = _mysqlDB.prepareStatement(_search_similar_group);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public String getHighestSalerankInSimilarGroup(String asin) {
        
        String name = "";
        
        try {
            _highest_salerank_similar_statement.clearParameters();
            _highest_salerank_similar_statement.setString(1, asin);
            _highest_salerank_similar_statement.setString(2, asin);
            _highest_salerank_similar_statement.setString(3, asin);
            
            ResultSet product_set = _highest_salerank_similar_statement.executeQuery();
            while (product_set.next()) {
                name = product_set.getString(2);
            }
            product_set.close();
            
        } catch(SQLException e) {
            e.printStackTrace();
        }
        
        return name;
    }
    
    public LinkedList<Category> getHighestSalerankOfCategory() {
        LinkedList<Category> list = new LinkedList<Category>();
        
        try {      
            ResultSet cat_set = _cat_of_highest_salerank_statement.executeQuery();
            while (cat_set.next()) {
                int id = cat_set.getInt(1);
                String name = cat_set.getString(2);
                Category c = new Category(id, name);
                list.add(c);
            }
            cat_set.close();
            
        } catch(SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }
    
    public double getAvgRatingByCustomerId(String id) {
        double avg = 0.0;
        
        try {
            _avg_rating_statement.clearParameters();
            _avg_rating_statement.setString(1, id);
            
            ResultSet avg_set = _avg_rating_statement.executeQuery();
            while (avg_set.next()) {
                avg = avg_set.getFloat(1);
            }
            avg_set.close();
            
        } catch(SQLException e) {
            e.printStackTrace();
        }
        
        return avg;
    }
    
    public String getHighestSalerank(String type) {
        
        String name = "";
        
        try {
            _highest_salerank_statement.clearParameters();
            _highest_salerank_statement.setString(1, type);
            _highest_salerank_statement.setString(2, type);
            
            
            ResultSet product_set = _highest_salerank_statement.executeQuery();
            while (product_set.next()) {
                name = product_set.getString(1);
            }
            product_set.close();
            
        } catch(SQLException e) {
            e.printStackTrace();
        }
        
        return name;
    }
}
