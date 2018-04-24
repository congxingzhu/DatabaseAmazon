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
public class Review {
    
    public Review(){}
    
    public Review(String asin, String cid, String date, int rating, int vote) {
        this.asin = asin;
        this.cid = cid;
        this.date = date;
        this.rating = rating;
        this.vote = vote;
    }
    
    public String asin;
    public String cid;
    public String date;
    public int rating;
    public int vote;
    
}
