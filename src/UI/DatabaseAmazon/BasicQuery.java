/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.DatabaseAmazon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

/**
 *
 * @author sswu
 */
public class BasicQuery {
    
    private Connection _mysqlDB;
    
    private String _search_by_asin = "select * from product where asin = ?";
    private PreparedStatement _search_by_asin_statement;
    
    private String _search_by_title = "select * from product where title like ?";
    private PreparedStatement _search_by_title_statement;
    
    private String _search_review_by_asin = "select * from review where asin = ?";
    private PreparedStatement _search_review_by_asin_statement;
    
    private String _search_by_cid = "select * from review where cid = ?";
    private PreparedStatement _search_by_cid_statement;
    
    private String _search_similar_group = "select distinct p.asin, p.title, p.salerank, p.type from product p, " +
                                            "(select asin2 from similarTo where asin1 = ?) X, " +
                                            "(select asin1 from similarTo where asin2 = ?) Y " +
                                            "where p.asin = X.asin2 or p.asin = Y.asin1 or p.asin = ?;";
    private PreparedStatement _search_similar_group_statement;
    
    public BasicQuery(Connection c) {
        _mysqlDB = c;
    }
    
    public void prepareStatements() {
        try {
            _search_by_asin_statement = _mysqlDB.prepareStatement(_search_by_asin);
            _search_by_title_statement = _mysqlDB.prepareStatement(_search_by_title);
            _search_review_by_asin_statement = _mysqlDB.prepareStatement(_search_review_by_asin);
            _search_by_cid_statement = _mysqlDB.prepareStatement(_search_by_cid);
            _search_similar_group_statement = _mysqlDB.prepareStatement(_search_similar_group);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public LinkedList<Product> searchSimilarGroup(String asin) {
        LinkedList<Product> result = new LinkedList<Product>();
        Product p;
        try {
            _search_similar_group_statement.clearParameters();
            _search_similar_group_statement.setString(1, asin);
            _search_similar_group_statement.setString(2, asin);
            _search_similar_group_statement.setString(3, asin);
            ResultSet product_set = _search_similar_group_statement.executeQuery();
            while (product_set.next()) {
                p = new Product();
                p.asin = product_set.getString(1);
                p.title = product_set.getString(2);
                p.salerank = product_set.getInt(3);
                p.type = product_set.getString(4);
                result.add(p);
            }
            
            product_set.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public LinkedList<Review> searchReviewByCid(String cid) {
        LinkedList<Review> result = new LinkedList<Review>();
        Review r;
        try {
            _search_by_cid_statement.clearParameters();
            _search_by_cid_statement.setString(1, cid);
            ResultSet review_set = _search_by_cid_statement.executeQuery();
            while (review_set.next()) {
                r = new Review();
                r.asin = review_set.getString(2);
                r.cid = cid;
                r.date = review_set.getString(4);
                r.rating = review_set.getInt(5);
                r.vote = review_set.getInt(6);
                result.add(r);
            }
            review_set.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public LinkedList<Review> searchReviewByAsin(String asin) {
        LinkedList<Review> result = new LinkedList<Review>();
        Review r;
        try {
            _search_review_by_asin_statement.clearParameters();
            _search_review_by_asin_statement.setString(1, asin);
            ResultSet review_set = _search_review_by_asin_statement.executeQuery();
            while (review_set.next()) {
                r = new Review();
                r.asin = asin;
                r.cid = review_set.getString(3);
                r.date = review_set.getString(4);
                r.rating = review_set.getInt(5);
                r.vote = review_set.getInt(6);
                result.add(r);
            }
            review_set.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public LinkedList<Product> searchByTitle(String title) {
        LinkedList<Product> result = new LinkedList<Product>();
        Product p;
        
        try {
            _search_by_title_statement.clearParameters();
            _search_by_title_statement.setString(1, '%' + title + '%');
            ResultSet product_set = _search_by_title_statement.executeQuery();
            while (product_set.next()) {
                String asin = product_set.getString(1);
                String t = product_set.getString(2);
                int salerank = product_set.getInt(3);
                String type = product_set.getString(4);
                p = new Product(asin, t, salerank, type);
                result.add(p);
            }
            product_set.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
       
        
        return result;
    }
    
    public LinkedList<Product> searchByAsin(String asin) {
       
        LinkedList<Product> result = new LinkedList<Product>();
       
        Product p;
        try {
            _search_by_asin_statement.clearParameters();
            _search_by_asin_statement.setString(1, asin);
            
            ResultSet product_set = _search_by_asin_statement.executeQuery();
            while (product_set.next()) {
                
                String title = product_set.getString(2);
                int salerank = product_set.getInt(3);
                String type = product_set.getString(4);
                p = new Product(asin, title, salerank, type);
                result.add(p);
                System.out.println(title + " " + asin + " " + type );
            }
            
            product_set.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
}
