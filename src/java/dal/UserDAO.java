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
import java.util.Vector;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;
/**
 *
 * @author admin
 */
public class UserDAO extends DBContext{
    private User buildUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getString("userID"),
                rs.getString("fullName"),
                rs.getString("password"),
                rs.getInt("roleID"),
                rs.getString("address"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getBoolean("activate"));
    }

    public Vector<User> getAllUser(String sql){
        Vector<User> vector = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while(rs.next()){
                User p = new User(rs.getString(1),rs.getString("fullName"), rs.getString(3), rs.getInt(4),
                        rs.getString(5), rs.getString(6), rs.getString(7), rs.getBoolean(8));
                vector.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vector;
    }

    public Vector<User> getAllUsers() {
        return getAllUser("SELECT * FROM tblUsers");
    }

    public Vector<User> searchUsersByFullName(String fullName) {
        Vector<User> vector = new Vector<>();
        String sql = "SELECT * FROM tblUsers WHERE fullName LIKE ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, "%" + fullName + "%");
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                vector.add(buildUser(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vector;
    }

                public User searchUser(String userID){
        String sql ="SELECT * FROM tblUsers where userID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, userID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()){
                User p = new User(userID, rs.getString(2), rs.getString(3), rs.getInt(4),
                        rs.getString(5), rs.getString(6), rs.getString(7), rs.getBoolean(8));
                 return p;
            } 
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public User searchUserByEmail(String email){
        String sql ="SELECT * FROM tblUsers where email = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, email);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()){
                return buildUser(rs);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
public int getNumOrderByUser(String userID) {
    String sql = "SELECT COUNT(o.orderID) AS totalOrders " +
                 "FROM dbo.tblOrders o " +
                 "WHERE o.userID = ? AND o.status = 2";

    try {
        PreparedStatement ptm = connection.prepareStatement(sql);
        ptm.setString(1, userID);
        ResultSet rs = ptm.executeQuery();

        if (rs.next()) {
            return rs.getInt("totalOrders");
        }
    } catch (SQLException ex) {
        Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return 0;
}
    public String generateNextUserID() {
        String sql = "SELECT MAX(CAST(SUBSTRING(userID, 2, LEN(userID) - 1) AS INT)) AS maxId " +
                "FROM tblUsers WHERE userID LIKE 'U[0-9]%'";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            int nextId = 1;
            if (rs.next()) {
                nextId = rs.getInt("maxId") + 1;
                if (rs.wasNull()) {
                    nextId = 1;
                }
            }
            return String.format("U%03d", nextId);
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "U001";
    }
    public int insertUser(User p){
        int n = 0;
        String sql = "INSERT INTO [dbo].[tblUsers]\n" +
"           ([userID]\n" +
"           ,[fullName]\n" +
"           ,[password]\n" +
"           ,[roleID]\n" +
"           ,[address]\n" +
"           ,[phone]\n" +
"           ,[email]\n" +
"           ,[activate])" +
"     VALUES (?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, p.getUserID());
            ptm.setString(2, p.getFullName());
            ptm.setString(3, p.getPassword());
            ptm.setInt(4, p.getRoleID());
            ptm.setString(5, p.getAddress());
            ptm.setString(6, p.getPhone());
            ptm.setString(7, p.getEmail());
            ptm.setBoolean(8, p.isActivate());
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }
                       public int updateUser(User p){
        int n =0;
        String sql ="UPDATE [dbo].[tblUsers]\n" +
"   SET [userID] = ?\n" +
"      ,[fullName] = ?\n" +
"      ,[password] = ?\n" +
"      ,[roleID] = ?\n" +
"      ,[address] = ?\n" +
"      ,[phone] = ?\n" +
"      ,[email] = ?\n" +
"      ,[activate] = ?\n" +
" WHERE userID =?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, p.getUserID());
            ptm.setString(2, p.getFullName());
            ptm.setString(3, p.getPassword());
            ptm.setInt(4, p.getRoleID());
            ptm.setString(5, p.getAddress());
            ptm.setString(6, p.getPhone());
            ptm.setString(7, p.getEmail());
            ptm.setBoolean(8, p.isActivate());
            ptm.setString(9, p.getUserID());
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }

    public int deleteUser (String userID){
        int n =0;
        String sql ="DELETE FROM [dbo].[tblUsers]\n" +
"      WHERE userID =?";
        PreparedStatement ptm;
        try {
            ptm = connection.prepareStatement(sql);
            ptm.setString(1, userID);
            n=ptm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }

    public int updateUserStatus(String userID, boolean activate) {
        int n = 0;
        String sql = "UPDATE [dbo].[tblUsers]\n" +
                "   SET [activate] = ?\n" +
                " WHERE userID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setBoolean(1, activate);
            ptm.setString(2, userID);
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }
      public ResultSet getData(String sql){
    ResultSet rs = null;
    try {
        java.sql.Statement state =
            connection.createStatement(
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);

        rs = state.executeQuery(sql); // ✅
    } catch (SQLException ex) {
        Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return rs;
}
    //check user login
    public User checkLogin(String email, String password) {
    String sql = "SELECT * FROM tblUsers WHERE email = ? AND password = ? AND activate = 1";
    try {
        PreparedStatement ptm = connection.prepareStatement(sql);
        ptm.setString(1, email);
        ptm.setString(2, password);
        ResultSet rs = ptm.executeQuery();
        if (rs.next()) {
            return buildUser(rs);
        }
    } catch (SQLException ex) {
        Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
}
    
}
