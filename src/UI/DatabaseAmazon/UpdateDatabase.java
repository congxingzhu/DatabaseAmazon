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
public class UpdateDatabase {
    
    private Connection _mysqlDB;
    
    private String _search_cat_by_name = "select * from category where name like ?";
    private PreparedStatement _cat_by_name_statement;
    
    private String _search_product_by_type = "select * from product where type = ? limit 10";
    private PreparedStatement _product_by_type_statement;
    
    private String _get_cat_by_asin = "select c.cat_id, c.name from category c join belongTo b " + 
                                        "on c.cat_id = b.cat_id where b.asin = ?";
    private PreparedStatement _cat_by_asin_statement;
    
    private String _delete_cat_by_id = "delete from category where cat_id = ?";
    private PreparedStatement _delete_cat_by_id_statement;
    
    private String _insert_cat = "insert into category values(?, ?)";
    private PreparedStatement _insert_cat_statement;
    
    private String _insert_belongto = "insert into belongTo values(?, ?)";
    private PreparedStatement _insert_belongto_statement;
    
    private String _update_cat = "update category set cat_id = ? where name = ?";
    private PreparedStatement _update_cat_statement;
    
    private String _update_belongto = "update belongTo set asin = ? where cat_id = ?";
    private PreparedStatement _update_belongto_statement;
    
    private String _begin_transaction_sql = "START TRANSACTION";
    private PreparedStatement _begin_transaction_statement;
    
    private String _commit = "COMMIT";
    private PreparedStatement _commit_statement;
    
    private String _rollback = "ROLLBACK";
    private PreparedStatement _rollback_statement;
    
    public UpdateDatabase(Connection mysqlDB) {
        _mysqlDB = mysqlDB;
    }
    
    public void PrepardStatements() {
        try {
            _cat_by_name_statement = _mysqlDB.prepareStatement(_search_cat_by_name);
            _product_by_type_statement = _mysqlDB.prepareStatement(_search_product_by_type);
            _cat_by_asin_statement = _mysqlDB.prepareStatement(_get_cat_by_asin);
            _delete_cat_by_id_statement = _mysqlDB.prepareStatement(_delete_cat_by_id);
            _insert_cat_statement = _mysqlDB.prepareStatement(_insert_cat);
            _insert_belongto_statement = _mysqlDB.prepareStatement(_insert_belongto);
            _update_cat_statement = _mysqlDB.prepareStatement(_update_cat);
            _update_belongto_statement = _mysqlDB.prepareStatement(_update_belongto);
            
            _begin_transaction_statement = _mysqlDB.prepareStatement(_begin_transaction_sql);
            _commit_statement = _mysqlDB.prepareStatement(_commit);
            _rollback_statement = _mysqlDB.prepareStatement(_rollback);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Boolean updateCategory(String name, int id) {
        try {
            _update_cat_statement.clearParameters();
            _update_cat_statement.setInt(1, id);
            _update_cat_statement.setString(2, name);
            _begin_transaction_statement.executeUpdate();
            if (_update_cat_statement.executeUpdate() != 0) {
                _commit_statement.executeUpdate();
                return true;
            } else {
                _rollback_statement.executeUpdate();
                return false;
            }
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Boolean updateBelongTo(String asin, int id){
        try {
            _update_belongto_statement.clearParameters();
            _update_belongto_statement.setString(1, asin);
            _update_belongto_statement.setInt(2, id);
            _begin_transaction_statement.executeUpdate();
            if (_update_belongto_statement.executeUpdate() != 0) {
                _commit_statement.executeUpdate();
                return true;
            } else {
                _rollback_statement.executeUpdate();
                return false;
            }
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Boolean insertBelongTo(String asin, int id) {
        try {
            _insert_belongto_statement.clearParameters();
            _insert_belongto_statement.setString(1, asin);
            _insert_belongto_statement.setInt(2, id);
            _begin_transaction_statement.executeUpdate();
            if (_insert_belongto_statement.executeUpdate() != 0) {
                _commit_statement.executeUpdate();
                return true;
            } else {
                _rollback_statement.executeUpdate();
                return false;
            }
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Boolean insertCategory(int id, String name) {
        try {
            _insert_cat_statement.clearParameters();
            _insert_cat_statement.setInt(1, id);
            _insert_cat_statement.setString(2, name);
            _begin_transaction_statement.executeUpdate();
            if (_insert_cat_statement.executeUpdate() != 0) {
                _commit_statement.executeUpdate();
                return true;
            } else {
                _rollback_statement.executeUpdate();
                return false;
            }
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Boolean deleteCategoryById(int id) {
        try {
            _delete_cat_by_id_statement.clearParameters();
            _delete_cat_by_id_statement.setInt(1, id);
            _begin_transaction_statement.executeUpdate();
            if (_delete_cat_by_id_statement.executeUpdate() != 0) {
                _commit_statement.executeUpdate();
                return true;
            } else {
                _rollback_statement.executeUpdate();
                return false;
            }
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public LinkedList<Category> getCategoryByAsin(String asin) {
        LinkedList<Category> result = new LinkedList<Category>();
        Category c;
        try {
            _cat_by_asin_statement.clearParameters();
            _cat_by_asin_statement.setString(1, asin);
            ResultSet cat_set = _cat_by_asin_statement.executeQuery();
            while (cat_set.next()) {
                c = new Category();
                c.id = cat_set.getInt(1);
                c.name = cat_set.getString(2);
                result.add(c);
            }
            cat_set.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    public LinkedList<Product> searchProductByType(String type) {
        LinkedList<Product> result = new LinkedList<Product>();
        Product p;
        try {
            _product_by_type_statement.clearParameters();
            _product_by_type_statement.setString(1, type);
            ResultSet product_set = _product_by_type_statement.executeQuery();
            while (product_set.next()) {
               String asin = product_set.getString(1);
                String t = product_set.getString(2);
                int salerank = product_set.getInt(3);
                p = new Product(asin, t, salerank, type);
                result.add(p);
            }
            product_set.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public LinkedList<Category> searchCatByName(String name) {
        
        LinkedList<Category> result = new LinkedList<Category>();
        Category c;
        try {
            _cat_by_name_statement.clearParameters();
            _cat_by_name_statement.setString(1,'%' + name + '%');
            ResultSet cat_set = _cat_by_name_statement.executeQuery();
            while (cat_set.next()) {
                c = new Category();
                c.id = cat_set.getInt(1);
                c.name = cat_set.getString(2);
                result.add(c);
            }
            cat_set.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
}
