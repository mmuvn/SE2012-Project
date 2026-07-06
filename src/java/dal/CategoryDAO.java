/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

/**
 *
 * @author admin
 */

    import dal.DBContext;
import java.sql.Statement;
import java.util.Vector;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Category;
public class CategoryDAO extends DBContext{
    public Vector<Category> getAllCategories() {
        return getAllCategory("SELECT * FROM tblCategories");
    }

    public Vector<Category> searchCategoriesByName(String categoryName) {
        Vector<Category> vector = new Vector<>();
        String sql = "SELECT * FROM tblCategories WHERE categoryName LIKE ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, "%" + categoryName + "%");
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                vector.add(new Category(rs.getString("categoryID"), rs.getString("categoryName"), rs.getString("describe")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vector;
    }

    public Vector<Category> getAllCategory(String sql){
        Vector<Category> vector = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while(rs.next()){
                Category p = new Category(rs.getString("categoryID"), rs.getString("categoryName"), rs.getString("describe"));
                vector.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vector;
    }
        public Category searchCategory(String categoryID){
        String sql ="SELECT * FROM tblCategories where categoryID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, categoryID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()){
                 Category p = new Category(categoryID,rs.getString(2), rs.getString(3));
                 return p;
            } 
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public int insertCategory(Category p){
        int n = 0;
        String sql = "INSERT INTO [dbo].[tblCategories]\n" +
"           ([categoryID]\n" +
"           ,[categoryName]\n" +
"           ,[describe])" +
"     VALUES (?,?,?)";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, p.getCategoryID());
            ptm.setString(2, p.getCategoryName());
            ptm.setString(3, p.getDescribe());
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }
        public int updateCategory(Category p){
        int n =0;
        String sql ="UPDATE [dbo].[tblCategories]\n" +
"   SET [categoryID] = ?\n" +
"      ,[categoryName] = ?\n" +
"      ,[describe] = ?\n" +
" WHERE categoryID =?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, p.getCategoryID());
            ptm.setString(2, p.getCategoryName());
            ptm.setString(3, p.getDescribe());
            ptm.setString(4, p.getCategoryID());
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }
    public int deleteCategory (String categoryID){
        int n =0;
        String sql ="""
                    DELETE FROM [dbo].[tblCategories]
                          WHERE categoryID =?""";
        PreparedStatement ptm;
        try {
            ptm = connection.prepareStatement(sql);
            ptm.setString(1, categoryID);
            n=ptm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }

    public boolean isCategoryInUse(String categoryID) {
        String sql = "SELECT TOP 1 1 FROM dbo.tblProducts WHERE categoryID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, categoryID);
            ResultSet rs = ptm.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
             public ResultSet getData(String sql){
        ResultSet rs = null;
        Statement state;
        try {
            state= connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rs = state.executeQuery(sql);
        } catch (SQLException ex) {
           Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
             
}

