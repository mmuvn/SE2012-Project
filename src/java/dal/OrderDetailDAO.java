package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.OrderDetail;

public class OrderDetailDAO extends DBContext {

    public Vector<OrderDetail> getAllOrderDetail(String sql) {
        Vector<OrderDetail> vector = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                OrderDetail detail = new OrderDetail(
                        rs.getInt("detailID"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getInt("orderID"),
                        rs.getInt("productID"));
                vector.add(detail);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderDetailDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vector;
    }

    public OrderDetail searchOrderDetail(int detailID) {
        String sql = "SELECT * FROM tblOrderDetails where detailID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, detailID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()) {
                return new OrderDetail(
                        detailID,
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getInt("orderID"),
                        rs.getInt("productID"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderDetailDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int insertOrderDetail(OrderDetail detail) {
        int n = 0;
        String sql = "INSERT INTO [dbo].[tblOrderDetails]\n" +
                "           ([price]\n" +
                "           ,[quantity]\n" +
                "           ,[orderID]\n" +
                "           ,[productID])" +
                "     VALUES (?,?,?,?)";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setDouble(1, detail.getPrice());
            ptm.setInt(2, detail.getQuantity());
            ptm.setInt(3, detail.getOrderID());
            ptm.setInt(4, detail.getProductID());
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(OrderDetailDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }

    public Vector<OrderDetail> getOrderDetailsByOrderId(int orderID) {
        Vector<OrderDetail> vector = new Vector<>();
        String sql = "SELECT * FROM tblOrderDetails WHERE orderID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, orderID);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                vector.add(new OrderDetail(
                        rs.getInt("detailID"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getInt("orderID"),
                        rs.getInt("productID")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderDetailDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vector;
    }

    public int updateOrderDetail(OrderDetail detail) {
        int n = 0;
        String sql = "UPDATE [dbo].[tblOrderDetails]\n" +
                "   SET [price] = ?\n" +
                "      ,[quantity] = ?\n" +
                "      ,[orderID] = ?\n" +
                "      ,[productID] = ?\n" +
                " WHERE detailID =?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setDouble(1, detail.getPrice());
            ptm.setInt(2, detail.getQuantity());
            ptm.setInt(3, detail.getOrderID());
            ptm.setInt(4, detail.getProductID());
            ptm.setInt(5, detail.getDetailID());
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(OrderDetailDAO.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(OrderDetailDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
}
