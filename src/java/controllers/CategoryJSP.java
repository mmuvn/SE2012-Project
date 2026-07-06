/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controllers;

import dal.CategoryDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Category;
import model.User;


/**
 *
 * @author admin
 */
@WebServlet(name="CategoryJSP", urlPatterns={"/categoryjsp"})
// Handles admin-side category CRUD operations and category search.
public class CategoryJSP extends AdminGuardServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = requireAdminUser(request, response);
        if (user == null) {
            return;
        }
        CategoryDAO dao = new CategoryDAO();
        String service = request.getParameter("service");
        if (service==null || service.equals("listOfCategory")){
            response.sendRedirect("admin?service=categoryManager");
            return;
        }
if (service.equals("deleteCategory")){
            // Delete one category from the admin side.
            String pId = request.getParameter("pId");
            if (dao.isCategoryInUse(pId)) {
                session.setAttribute("adminMessage", "Category cannot be deleted because products are still using it.");
                session.setAttribute("adminMessageType", "error");
                response.sendRedirect("admin?service=categoryManager");
                return;
            }
            dao.deleteCategory(pId);
            session.setAttribute("adminMessage", "Category deleted.");
            session.setAttribute("adminMessageType", "success");
            response.sendRedirect("admin?service=categoryManager");
            return;
        }
if (service.equals("updateCategory")) {
            // Show the update form first, then save the edited category.
            String submit = request.getParameter("submit");
            if (submit == null) {//show updateProduct.jsp
                String pId = request.getParameter("pId");
                request.setAttribute("pId", pId);
                Category p = dao.searchCategory(pId);

                request.setAttribute("p", p);
                request.getRequestDispatcher("jsp/update/updateCategory.jsp").forward(request, response);
                return;
            } else {//update
            String categoryID = request.getParameter("categoryID");
            String categoryName = request.getParameter("categoryName");
            String describe = request.getParameter("describe");
Category p = new Category(categoryID, categoryName, describe);
int n = dao.updateCategory(p);
                if (n > 0) {
                    session.setAttribute("adminMessage", "Updated successfully.");
                    session.setAttribute("adminMessageType", "success");
                } else {
                    session.setAttribute("adminMessage", "Update failed.");
                    session.setAttribute("adminMessageType", "error");
                }
                response.sendRedirect("admin?service=categoryManager");
                return;
            }
        }
        if (service.equals("addCategory")){
            // Show the insert form first, then create a new category.
            String submit = request.getParameter("submit");
            if (submit ==null)//insert product.jsp
            {
          
                request.getRequestDispatcher("jsp/insert/insertCategory.jsp").forward(request, response);
                return;
            }
            String categoryID = request.getParameter("categoryID");
            String categoryName = request.getParameter("categoryName");
            String describe = request.getParameter("describe");
Category p = new Category(categoryID, categoryName, describe);
int n = dao.insertCategory(p);
            if (n > 0) {
                session.setAttribute("adminMessage", "Category added successfully.");
                session.setAttribute("adminMessageType", "success");
            } else {
                session.setAttribute("adminMessage", "Category could not be added.");
                session.setAttribute("adminMessageType", "error");
            }
            response.sendRedirect("admin?service=categoryManager");
            return;
        }
        response.sendRedirect("admin?service=categoryManager");
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
        return "Category Controller";
    }// </editor-fold>

}
