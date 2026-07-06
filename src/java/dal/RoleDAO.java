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
import java.sql.Statement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Role;
/**
 *
 * @author admin
 */
public class RoleDAO extends DBContext{
    public Vector<Role> getAllRoles() {
        return getAllRole("SELECT * FROM tblRoles");
    }

    public Vector<Role> searchRolesByName(String roleName) {
        Vector<Role> vector = new Vector<>();
        String sql = "SELECT * FROM tblRoles WHERE roleName LIKE ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, "%" + roleName + "%");
            ResultSet rs = ptm.executeQuery();
            while (rs.next()) {
                vector.add(new Role(rs.getInt("roleID"), rs.getString("roleName")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vector;
    }

    public Vector<Role> getAllRole(String sql){
        Vector<Role> vector = new Vector<>();
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ResultSet rs = ptm.executeQuery();
            while(rs.next()){
                Role p = new Role(rs.getInt(1), rs.getString(2));
                vector.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vector;
    }
            public Role searchRole(int roleID){
        String sql ="SELECT * FROM tblRoles where roleID = ?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setInt(1, roleID);
            ResultSet rs = ptm.executeQuery();
            if (rs.next()){
                 Role p = new Role(roleID, rs.getString(2));
                 return p;
            } 
        } catch (SQLException ex) {
            Logger.getLogger(RoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public int insertRole(Role p){
        int n = 0;
        String sql = "INSERT INTO tblRoles(roleName) VALUES (?)";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, p.getRoleName());
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }
                   public int updateRole(Role p){
        int n =0;
        String sql ="UPDATE [dbo].[tblRoles]\n" +
"   SET [roleName] = ?\n" +
" WHERE roleID =?";
        try {
            PreparedStatement ptm = connection.prepareStatement(sql);
            ptm.setString(1, p.getRoleName());
            ptm.setInt(2, p.getRoleID());
            n = ptm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }
                    public int deleteRole (int roleID){
        int n =0;
        String sql ="""
                    DELETE FROM [dbo].[tblRoles]
                          WHERE roleID =?""";
        PreparedStatement ptm;
        try {
            ptm = connection.prepareStatement(sql);
            ptm.setInt(1, roleID);
            n=ptm.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }
                        public ResultSet getData(String sql){
        ResultSet rs = null;
        Statement state;
        try {
            state= connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rs = state.executeQuery(sql);
        } catch (SQLException ex) {
           Logger.getLogger(RoleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }
}
