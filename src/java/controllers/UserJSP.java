/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controllers;

import dal.RoleDAO;
import dal.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

/**
 *
 * @author admin
 */
@WebServlet(name="UserJSP", urlPatterns={"/userjsp"})
// Handles admin-side user CRUD operations and user search.
public class UserJSP extends AdminGuardServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        User loginUser = requireAdminUser(request, response);
        if (loginUser == null) {
            return;
        }

        UserDAO dao = new UserDAO();
        String service = request.getParameter("service");

        if (service == null || service.equals("listOfUser")) {
            response.sendRedirect("admin?service=userManager");
            return;
        }

        // Toggle one user's active status from the admin side.
        if (service.equals("toggleUserStatus")) {

            String userID = request.getParameter("pId");
            User user = dao.searchUser(userID);
            if (user != null) {
                boolean newStatus = !user.isActivate();
                int updated = dao.updateUserStatus(userID, newStatus);
                if (updated > 0) {
                    session.setAttribute("adminMessage", newStatus ? "User activated successfully." : "User deactivated successfully.");
                    session.setAttribute("adminMessageType", "success");
                } else {
                    session.setAttribute("adminMessage", "User status could not be changed.");
                    session.setAttribute("adminMessageType", "error");
                }
            } else {
                session.setAttribute("adminMessage", "User not found.");
                session.setAttribute("adminMessageType", "error");
            }

            response.sendRedirect("admin?service=userManager");
            return;
        }

        // Show the update form first, then save the edited user account.
        if (service.equals("updateUser")) {

            String submit = request.getParameter("submit");

            if (submit == null) {

                String userID = request.getParameter("pId");

                User user = dao.searchUser(userID);
                RoleDAO roleDAO = new RoleDAO();

                request.setAttribute("user", user);
                request.setAttribute("roles", roleDAO.getAllRoles());

                request.getRequestDispatcher("jsp/update/updateUser.jsp")
                        .forward(request, response);

                return;
            }

            String userID = request.getParameter("userID");
            String fullName = request.getParameter("fullName");
            String password = request.getParameter("password");
            int roleID = Integer.parseInt(request.getParameter("roleID"));
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            User currentUser = dao.searchUser(userID);
            boolean activate = currentUser.isActivate();

            if (currentUser != null && currentUser.getRoleID() == 1 && roleID != 1) {
                session.setAttribute("adminMessage", "Admin role cannot be changed for admin accounts.");
                session.setAttribute("adminMessageType", "error");
                response.sendRedirect("admin?service=userManager");
                return;
            }

            User user = new User(userID, fullName, password, roleID, address, phone, email, activate);

            int updated = dao.updateUser(user);
            if (updated > 0) {
                session.setAttribute("adminMessage", "Updated successfully.");
                session.setAttribute("adminMessageType", "success");
            } else {
                session.setAttribute("adminMessage", "Update failed.");
                session.setAttribute("adminMessageType", "error");
            }
int totalOrders = dao.getNumOrderByUser(userID);
request.setAttribute("totalOrders", totalOrders);
            response.sendRedirect("admin?service=userManager");
            return;
        }

        // Show the insert form first, then create a new user account.
        if (service.equals("addUser")) {
            response.sendRedirect("admin?service=userManager");
            return;
        }
        response.sendRedirect("admin?service=userManager");
        return;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "User Controller";
    }// </editor-fold>

}
