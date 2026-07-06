package controllers;

import dal.CategoryDAO;
import dal.OrderDAO;
import dal.OrderDetailDAO;
import dal.ProductDAO;
import dal.RoleDAO;
import dal.UserDAO;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Category;
import model.Order;
import model.OrderDetail;
import model.Product;
import model.Role;
import model.User;

@WebServlet(name = "AdminServlet", urlPatterns = {"/admin"})
// Main admin entry point: loads one management section and renders it inside the dashboard page.
public class AdminServlet extends AdminGuardServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = requireAdminUser(request, response);
        if (user == null) {
            return;
        }

        String service = request.getParameter("service");
        if (service == null || service.isBlank()) {
            service = "dashboard";
        }

        moveAdminFlashToRequest(session, request);

        // Pick which manager section should be loaded into the dashboard page.
        switch (service) {
            case "productManager":
                loadProducts(request);
                request.setAttribute("selectedManager", "productManager");
                break;
            case "orderManager":
                loadOrders(request);
                request.setAttribute("selectedManager", "orderManager");
                break;
            case "userManager":
                loadUsers(request);
                request.setAttribute("selectedManager", "userManager");
                break;
            case "roleManager":
                loadRoles(request);
                request.setAttribute("selectedManager", "roleManager");
                break;
            case "categoryManager":
                loadCategories(request);
                request.setAttribute("selectedManager", "categoryManager");
                break;
            default:
                request.setAttribute("selectedManager", null);
                break;
        }

        request.getRequestDispatcher("jsp/admin/adminIndex.jsp").forward(request, response);
    }

    private void loadProducts(HttpServletRequest request) {
        // Loads product data for the embedded product manager section.
        ProductDAO dao = new ProductDAO();
        String productName = escapeLike(request.getParameter("productName"));
        Vector<Product> products = productName.isEmpty()
                ? dao.getAllProducts()
                : dao.searchProductsByName(productName);
        request.setAttribute("products", products);
    }

    private void loadOrders(HttpServletRequest request) {
        // Loads order data and optional order details for the embedded order manager section.
        OrderDAO orderDAO = new OrderDAO();
        String userID = escapeLike(request.getParameter("userID"));
        Vector<Order> orders = userID.isEmpty()
                ? orderDAO.getAllOrdersSortedByLatest()
                : orderDAO.searchOrdersByUserId(userID);
        request.setAttribute("orders", orders);

        String orderIdRaw = request.getParameter("pId");
        if (orderIdRaw != null && orderIdRaw.matches("\\d+")) {
            int orderId = Integer.parseInt(orderIdRaw);
            UserDAO userDAO = new UserDAO();
            OrderDetailDAO detailDAO = new OrderDetailDAO();
            Vector<OrderDetail> details = detailDAO.getOrderDetailsByOrderId(orderId);
            ProductDAO productDAO = new ProductDAO();
            Map<Integer, Product> detailProducts = new HashMap<>();

            for (OrderDetail detail : details) {
                Product product = productDAO.searchProduct(detail.getProductID());
                if (product != null) {
                    detailProducts.put(detail.getProductID(), product);
                }
            }

            Order selectedOrder = orderDAO.searchOrder(orderId);
            request.setAttribute("selectedOrder", selectedOrder);
            if (selectedOrder != null) {
                request.setAttribute("selectedCustomer", userDAO.searchUser(selectedOrder.getUserID()));
            }
            request.setAttribute("details", details);
            request.setAttribute("detailProducts", detailProducts);
        }
    }

    private void loadUsers(HttpServletRequest request) {
        // Loads user data for the embedded user manager section.
        UserDAO dao = new UserDAO();
        RoleDAO roleDAO = new RoleDAO();
        String fullName = escapeLike(request.getParameter("fullName"));
        Vector<User> users = fullName.isEmpty()
                ? dao.getAllUsers()
                : dao.searchUsersByFullName(fullName);
        Map<String, Integer> orderCounts = new HashMap<>();
        for (User user : users) {
            orderCounts.put(user.getUserID(), dao.getNumOrderByUser(user.getUserID()));
        }
        request.setAttribute("users", users);
        request.setAttribute("roles", roleDAO.getAllRoles());
        request.setAttribute("orderCounts", orderCounts);
    }

    private void loadRoles(HttpServletRequest request) {
        // Loads role data for the embedded role manager section.
        RoleDAO dao = new RoleDAO();
        String roleName = escapeLike(request.getParameter("roleName"));
        Vector<Role> roles = roleName.isEmpty()
                ? dao.getAllRoles()
                : dao.searchRolesByName(roleName);
        request.setAttribute("roles", roles);
    }

    private void loadCategories(HttpServletRequest request) {
        // Loads category data for the embedded category manager section.
        CategoryDAO dao = new CategoryDAO();
        String categoryName = escapeLike(request.getParameter("categoryName"));
        Vector<Category> categories = categoryName.isEmpty()
                ? dao.getAllCategories()
                : dao.searchCategoriesByName(categoryName);
        request.setAttribute("categories", categories);
    }

    private String escapeLike(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().replace("'", "''");
    }

    private void moveAdminFlashToRequest(HttpSession session, HttpServletRequest request) {
        Object adminMessage = session.getAttribute("adminMessage");
        if (adminMessage != null) {
            request.setAttribute("adminMessage", adminMessage);
            request.setAttribute("adminMessageType", session.getAttribute("adminMessageType"));
            session.removeAttribute("adminMessage");
            session.removeAttribute("adminMessageType");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Admin Controller";
    }
}
