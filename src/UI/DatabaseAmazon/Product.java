/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.DatabaseAmazon;

/**
 *
 * @author sswu
 */
public class Product {
    
    public Product(){}
    public Product(String asin, String title, int salerank, String type) {
        this.asin = asin;
        this.title = title;
        this.salerank = salerank;
        this.type = type;
    }
    
    public String asin;
    public String title;
    public int salerank;
    public String type;
}
