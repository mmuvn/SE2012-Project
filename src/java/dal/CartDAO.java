/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.SQLException;
import model.Cart;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author admin
 */
public class CartDAO extends DBContext {
    public Cart getCart(int productID){
        Cart cart = null;
        String sql = "SELECT [productID]\n" +
"      ,[productName]\n" +
"      ,[price]\n" +
"      ,[quantity]\n" +
"  FROM [dbo].[tblProducts]\n" +
"  WHERE productID = ? AND status = 1 AND quantity > 0";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, productID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()){
                cart = new Cart(rs.getInt(1),rs.getString(2),rs.getDouble(3),rs.getInt(4));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CartDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cart;
    }

             public ResultSet getData(String sql){
        ResultSet rs = null;
        Statement state;
        try {
            state= connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rs = state.executeQuery(sql);
        } catch (SQLException ex) {
           Logger.getLogger(CartDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
}
