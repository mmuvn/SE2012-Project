/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Product;

/**
 *
 * @author admin
 */
public class ProductDAO extends DBContext {
    private Product mapProduct(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("productID"),
                rs.getString("productName"),
                rs.getString("image"),
                rs.getDouble("price"),
                rs.getInt("quantity"),
                rs.getString("categoryID"),
                rs.getString("description"),
                rs.getInt("status"));
    }

    public Vector<Product> getAllProduct(String sql) {
        Vector<Product> vector = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                vector.add(mapProduct(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vector;
    }

    public Product searchProduct(int productID) {
        String sql = "SELECT * FROM tblProducts where productID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, productID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                return mapProduct(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Vector<Product> getAllProducts() {
        return getAllProduct("SELECT * FROM tblProducts");
    }

    public Vector<Product> searchProductsByName(String keyword) {
        Vector<Product> vector = new Vector<>();
        String sql = "SELECT * FROM tblProducts WHERE productName LIKE ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, "%" + keyword + "%");
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                vector.add(mapProduct(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vector;
    }

    public Vector<Product> getProductsByCategory(String categoryID) {
        Vector<Product> vector = new Vector<>();
        String sql = "SELECT * FROM tblProducts WHERE categoryID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, categoryID);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                vector.add(mapProduct(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vector;
    }

    public int insertProduct(Product p) {
        int n = 0;
        String sql = "INSERT INTO [dbo].[tblProducts]\n" +
                "           ([productName]\n" +
                "           ,[image]\n" +
                "           ,[price]\n" +
                "           ,[quantity]\n" +
                "           ,[categoryID]\n" +
                "           ,[description]\n" +
                "           ,[status])\n" +
                "     VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, p.getProductName());
            ptm.setString(2, p.getImage());
            ptm.setDouble(3, p.getPrice());
            ptm.setInt(4, p.getQuantity());
            ptm.setString(5, p.getCategoryID());
            ptm.setString(6, p.getDescription());
            ptm.setInt(7, p.getStatus());
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }

    public int updateProduct(Product p) {
        int n = 0;
        String sql = "UPDATE [dbo].[tblProducts]\n" +
                "   SET [productName] = ?\n" +
                "      ,[image] = ?\n" +
                "      ,[price] = ?\n" +
                "      ,[quantity] = ?\n" +
                "      ,[categoryID] = ?\n" +
                "      ,[description] = ?\n" +
                "      ,[status] = ?\n" +
                " WHERE productID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, p.getProductName());
            ptm.setString(2, p.getImage());
            ptm.setDouble(3, p.getPrice());
            ptm.setInt(4, p.getQuantity());
            ptm.setString(5, p.getCategoryID());
            ptm.setString(6, p.getDescription());
            ptm.setInt(7, p.getStatus());
            ptm.setInt(8, p.getProductID());
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }

    public ResultSet getData(String sql) {
        ResultSet rs = null;
        Statement state;
        try {
            state = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = state.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public void changeStatus(int productID, int newStatus) {
        String sql = "UPDATE [dbo].[tblProducts]\n" +
                "   SET [status] = ?\n" +
                " WHERE productID = " + productID;
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, newStatus);
            ptm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int deleteProduct(int productID) {
        int n = 0;
        String sql = """
                    DELETE FROM [dbo].[tblProducts]
                          WHERE productID =?""";
        PreparedStatement ptm;
        try {
            ResultSet rs = getData("SELECT * from tblOrderDetails where productID = " + productID);
            if (rs.next()) {
                changeStatus(productID, 0);
                return n;
            }
            ptm = connection.prepareStatement(sql);
            ptm.setInt(1, productID);
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }

    public boolean reduceStockAfterCheckout(int productID, int purchasedQuantity) {
        String sql = "UPDATE dbo.tblProducts\n" +
                "SET quantity = quantity - ?,\n" +
                "    status = CASE WHEN quantity - ? <= 0 THEN 0 ELSE status END\n" +
                "WHERE productID = ? AND status = 1 AND quantity >= ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, purchasedQuantity);
            ptm.setInt(2, purchasedQuantity);
            ptm.setInt(3, productID);
            ptm.setInt(4, purchasedQuantity);
            return ptm.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean restoreStockAfterCancellation(int productID, int restoredQuantity) {
        String sql = "UPDATE dbo.tblProducts\n" +
                "SET quantity = quantity + ?,\n" +
                "    status = CASE WHEN quantity + ? > 0 THEN 1 ELSE status END\n" +
                "WHERE productID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, restoredQuantity);
            ptm.setInt(2, restoredQuantity);
            ptm.setInt(3, productID);
            return ptm.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public Vector<Product> getBestSellingProducts(int limit) {
        Vector<Product> vector = new Vector<>();
        int safeLimit = Math.max(limit, 1);
        String sql = "SELECT TOP " + safeLimit + " p.*, SUM(od.quantity) AS totalSold\n" +
                "FROM dbo.tblProducts p\n" +
                "JOIN dbo.tblOrderDetails od ON p.productID = od.productID\n" +
                "JOIN dbo.tblOrders o ON od.orderID = o.orderID\n" +
                "WHERE o.status = 2\n" +
                "GROUP BY p.productID, p.productName, p.image, p.price, p.quantity, p.categoryID, p.description, p.status\n" +
                "ORDER BY SUM(od.quantity) DESC, p.productName ASC";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                Product product = mapProduct(rs);
                product.setTotalSold(rs.getInt("totalSold"));
                vector.add(product);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vector;
    }

    public Vector<Product> getRandomProducts(int limit, int excludedProductId) {
        Vector<Product> vector = new Vector<>();
        int safeLimit = Math.max(limit, 1);
        String sql = "SELECT TOP " + safeLimit + " *\n" +
                "FROM dbo.tblProducts\n" +
                "WHERE productID <> ? AND status = 1\n" +
                "ORDER BY NEWID()";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, excludedProductId);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                vector.add(mapProduct(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vector;
    }
}
