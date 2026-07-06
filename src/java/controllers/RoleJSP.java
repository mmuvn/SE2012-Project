/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controllers;

import dal.RoleDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Role;
import model.User;

/**
 *
 * @author admin
 */
@WebServlet(name="RoleJSP", urlPatterns={"/rolejsp"})
// Handles admin-side role CRUD operations and role search.
public class RoleJSP extends AdminGuardServlet {
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
        User user = requireAdminUser(request, response);
        if (user == null) {
            return;
        }
           RoleDAO dao = new RoleDAO();
        String service = request.getParameter("service");

        if (service == null || service.equals("listOfRole")) {
            response.sendRedirect("admin?service=roleManager");
            return;
        }

        // Delete one role from the admin side.
        if (service.equals("deleteRole")) {
            int pId = Integer.parseInt(request.getParameter("pId"));
            int deleted = dao.deleteRole(pId);
            if (deleted > 0) {
                session.setAttribute("adminMessage", "Role deleted successfully.");
                session.setAttribute("adminMessageType", "success");
            } else {
                session.setAttribute("adminMessage", "Role could not be deleted.");
                session.setAttribute("adminMessageType", "error");
            }
            response.sendRedirect("admin?service=roleManager");
            return;
        }

        // Show the update form first, then save the edited role.
        if (service.equals("updateRole")) {
            String submit = request.getParameter("submit");

            if (submit == null) { // show update page
                int pId = Integer.parseInt(request.getParameter("pId"));
                request.setAttribute("pId", pId);

                Role p = dao.searchRole(pId);
                request.setAttribute("p", p);

                request.getRequestDispatcher("jsp/update/updateRole.jsp")
                        .forward(request, response);
                return;
            } else { // update database
                int roleID = Integer.parseInt(request.getParameter("roleID"));
                String roleName = request.getParameter("roleName");

                Role p = new Role(roleID, roleName);
                int updated = dao.updateRole(p);
                if (updated > 0) {
                    session.setAttribute("adminMessage", "Updated successfully.");
                    session.setAttribute("adminMessageType", "success");
                } else {
                    session.setAttribute("adminMessage", "Update failed.");
                    session.setAttribute("adminMessageType", "error");
                }

                response.sendRedirect("admin?service=roleManager");
                return;
            }
        }

        // Show the insert form first, then create a new role.
        if (service.equals("addRole")) {
            String submit = request.getParameter("submit");

            if (submit == null) {
                request.getRequestDispatcher("jsp/insert/insertRole.jsp")
                        .forward(request, response);
                return;
            }
            String roleName = request.getParameter("roleName");

            Role o = new Role(roleName);
            int inserted = dao.insertRole(o);
            if (inserted > 0) {
                session.setAttribute("adminMessage", "Role added successfully.");
                session.setAttribute("adminMessageType", "success");
            } else {
                session.setAttribute("adminMessage", "Role could not be added.");
                session.setAttribute("adminMessageType", "error");
            }

            response.sendRedirect("admin?service=roleManager");
            return;
        }

        response.sendRedirect("admin?service=roleManager");
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
        return "Role Controller";
    }// </editor-fold>

}
