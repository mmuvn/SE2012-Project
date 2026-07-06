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
import java.sql.Date;
import java.sql.Connection;
import model.Cart;
import model.Order;
public class OrderDAO extends DBContext{
    private Order mapOrder(ResultSet rs) throws SQLException {
        return new Order(
                rs.getInt("orderID"),
                rs.getDate("orderDate"),
                rs.getDouble("total"),
                rs.getString("userID"),
                rs.getInt("status"));
    }

    public Vector<Order> getAllOrder(String sql){
        Vector<Order> vector = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while(rs.next()){
                vector.add(mapOrder(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vector;
    }
    public Vector<Order> getAllOrdersSortedByLatest() {
        return getAllOrder("SELECT * FROM tblOrders ORDER BY orderDate DESC, orderID DESC");
    }

    public Vector<Order> searchOrdersByUserId(String userIDKeyword) {
        Vector<Order> vector = new Vector<>();
        String sql = "SELECT * FROM tblOrders WHERE userID LIKE ? ORDER BY orderDate DESC, orderID DESC";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, "%" + userIDKeyword + "%");
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                vector.add(mapOrder(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vector;
    }

    public Order searchOrder(int orderID){
        String sql ="SELECT * FROM tblOrders where orderID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, orderID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()){
                 return mapOrder(rs);
            } 
        } catch (SQLException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
   public int insertOrder(Order p){
    int orderID = 0;

    String sql = "INSERT INTO tblOrders (orderDate, total, userID, status) VALUES (?,?,?,?)";

    try {
        PreparedStatement ptm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ptm.setDate(1, p.getOrderDate());
        ptm.setDouble(2, p.getTotal());
        ptm.setString(3, p.getUserID());
        ptm.setInt(4, p.getStatus());

        ptm.executeUpdate();

        ResultSet rs = ptm.getGeneratedKeys();
        if(rs.next()){
            orderID = rs.getInt(1);
        }

    } catch (SQLException ex) {
        Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
    }

    return orderID;
}

   public int checkoutOrder(Order order, Vector<Cart> items) {
        int orderID = 0;
        boolean previousAutoCommit = true;

        String insertOrderSql = "INSERT INTO tblOrders (orderDate, total, userID, status) VALUES (?,?,?,?)";
        String reduceStockSql = "UPDATE dbo.tblProducts\n" +
                "SET quantity = quantity - ?,\n" +
                "    status = CASE WHEN quantity - ? <= 0 THEN 0 ELSE status END\n" +
                "WHERE productID = ? AND status = 1 AND quantity >= ?";
        String insertDetailSql = "INSERT INTO dbo.tblOrderDetails ([price],[quantity],[orderID],[productID]) VALUES (?,?,?,?)";

        try {
            previousAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            try (PreparedStatement orderPtm = connection.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                orderPtm.setDate(1, order.getOrderDate());
                orderPtm.setDouble(2, order.getTotal());
                orderPtm.setString(3, order.getUserID());
                orderPtm.setInt(4, order.getStatus());
                orderPtm.executeUpdate();

                try (ResultSet rs = orderPtm.getGeneratedKeys()) {
                    if (rs.next()) {
                        orderID = rs.getInt(1);
                    }
                }
            }

            if (orderID == 0) {
                connection.rollback();
                return 0;
            }

            try (PreparedStatement stockPtm = connection.prepareStatement(reduceStockSql);
                 PreparedStatement detailPtm = connection.prepareStatement(insertDetailSql)) {
                for (Cart item : items) {
                    stockPtm.setInt(1, item.getQuantity());
                    stockPtm.setInt(2, item.getQuantity());
                    stockPtm.setInt(3, item.getProductID());
                    stockPtm.setInt(4, item.getQuantity());

                    if (stockPtm.executeUpdate() == 0) {
                        connection.rollback();
                        return 0;
                    }

                    detailPtm.setDouble(1, item.getPrice());
                    detailPtm.setInt(2, item.getQuantity());
                    detailPtm.setInt(3, orderID);
                    detailPtm.setInt(4, item.getProductID());
                    detailPtm.executeUpdate();
                }
            }

            connection.commit();
            return orderID;
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, rollbackEx);
            }
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.setAutoCommit(previousAutoCommit);
            } catch (SQLException ex) {
                Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }
           public int updateOrder(Order p){
        int n =0;
        String sql ="UPDATE [dbo].[tblOrders]\n" +
"   SET [orderDate] = ?\n" +
"      ,[total] = ?\n" +
"      ,[userID] = ?\n" +
"      ,[status] = ?\n" +
" WHERE orderID =?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setDate(1, p.getOrderDate());
            ptm.setDouble(2, p.getTotal());
            ptm.setString(3, p.getUserID());
            ptm.setInt(4, p.getStatus());
            ptm.setInt(5,p.getOrderID());
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }
    public int deleteOrder (int orderID){
        int n =0;
        String sql ="""
                    DELETE FROM [dbo].[tblOrders]
                          WHERE orderID =?""";
        PreparedStatement ptm;
        try {
            ptm = connection.prepareStatement(sql);
            ptm.setInt(1, orderID);
            n=ptm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }

    public boolean deletePendingOrCancelledOrder(int orderID) {
        boolean previousAutoCommit = true;
        String selectStatusSql = "SELECT status FROM dbo.tblOrders WHERE orderID = ?";
        String selectDetailsSql = "SELECT productID, quantity FROM dbo.tblOrderDetails WHERE orderID = ?";
        String restoreStockSql = "UPDATE dbo.tblProducts\n" +
                "SET quantity = quantity + ?,\n" +
                "    status = CASE WHEN quantity + ? > 0 THEN 1 ELSE status END\n" +
                "WHERE productID = ?";
        String deleteDetailsSql = "DELETE FROM dbo.tblOrderDetails WHERE orderID = ?";
        String deleteOrderSql = "DELETE FROM dbo.tblOrders WHERE orderID = ? AND status IN (?, ?)";

        try {
            previousAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            int currentStatus = -1;
            try (PreparedStatement statusPtm = connection.prepareStatement(selectStatusSql)) {
                statusPtm.setInt(1, orderID);
                try (ResultSet rs = statusPtm.executeQuery()) {
                    if (rs.next()) {
                        currentStatus = rs.getInt("status");
                    }
                }
            }

            if (currentStatus != Order.STATUS_PENDING && currentStatus != Order.STATUS_CANCELLED) {
                connection.rollback();
                return false;
            }

            if (currentStatus == Order.STATUS_PENDING) {
                try (PreparedStatement detailsPtm = connection.prepareStatement(selectDetailsSql);
                     PreparedStatement restorePtm = connection.prepareStatement(restoreStockSql)) {
                    detailsPtm.setInt(1, orderID);
                    try (ResultSet rs = detailsPtm.executeQuery()) {
                        while (rs.next()) {
                            int productID = rs.getInt("productID");
                            int quantity = rs.getInt("quantity");
                            restorePtm.setInt(1, quantity);
                            restorePtm.setInt(2, quantity);
                            restorePtm.setInt(3, productID);
                            restorePtm.executeUpdate();
                        }
                    }
                }
            }

            try (PreparedStatement deleteDetailsPtm = connection.prepareStatement(deleteDetailsSql);
                 PreparedStatement deleteOrderPtm = connection.prepareStatement(deleteOrderSql)) {
                deleteDetailsPtm.setInt(1, orderID);
                deleteDetailsPtm.executeUpdate();

                deleteOrderPtm.setInt(1, orderID);
                deleteOrderPtm.setInt(2, Order.STATUS_PENDING);
                deleteOrderPtm.setInt(3, Order.STATUS_CANCELLED);

                if (deleteOrderPtm.executeUpdate() == 0) {
                    connection.rollback();
                    return false;
                }
            }

            connection.commit();
            return true;
        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, rollbackEx);
            }
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.setAutoCommit(previousAutoCommit);
            } catch (SQLException ex) {
                Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public Vector<Order> getOrdersByUser(String userID) {
        Vector<Order> vector = new Vector<>();
        String sql = "SELECT * FROM tblOrders WHERE userID = ? ORDER BY orderDate DESC, orderID DESC";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, userID);
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                vector.add(mapOrder(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vector;
    }

    public boolean updateOrderStatus(int orderID, int newStatus) {
        String sql = "UPDATE dbo.tblOrders SET status = ? WHERE orderID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, newStatus);
            ptm.setInt(2, orderID);
            return ptm.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean updateOrderStatus(int orderID, int expectedStatus, int newStatus) {
        String sql = "UPDATE dbo.tblOrders SET status = ? WHERE orderID = ? AND status = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, newStatus);
            ptm.setInt(2, orderID);
            ptm.setInt(3, expectedStatus);
            return ptm.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
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
           Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
}
