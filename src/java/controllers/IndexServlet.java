/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controllers;
import dal.CategoryDAO;
import dal.OrderDAO;
import dal.OrderDetailDAO;
import dal.ProductDAO;
import dal.UserDAO;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.StringJoiner;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Vector;
import model.Category;
import model.Order;
import model.OrderDetail;
import model.Product;
import model.User;

/**
 *
 * @author admin
 */
@WebServlet(name="IndexServlet", urlPatterns={"/indexjsp"})
// Handles the customer-facing storefront: home page, login, register, filters, and product detail.
public class IndexServlet extends HttpServlet {
    private static final int PAGE_SIZE = 15;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{9,11}$");
   
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
        HttpSession session = request.getSession(true);
        String service = request.getParameter("service");
        if (service == null || service.isBlank()) {
            service = "display";
        }
        switch (service) {
            case "login":
                handleLogin(request, response, session);
                return;
            case "logOut":
                handleLogout(response, session);
                return;
            case "register":
                handleRegister(request, response);
                return;
            case "orderHistory":
                handleOrderHistory(request, response, session);
                return;
            case "cancelOrder":
                handleCancelOrder(request, response, session);
                return;
            case "clearCancelledOrder":
                handleClearCancelledOrder(request, response, session);
                return;
            case "productDetail":
                handleProductDetail(request, response);
                return;
            case "listOfProduct":
                handleProductSearch(request, response, session);
                return;
            case "showItem":
                handleCategoryFilter(request, response, session);
                return;
            case "display":
                handleDisplay(request, response, session);
                return;
            default:
                response.sendRedirect("indexjsp");
                return;
        }
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
        return "Storefront Controller";
    }// </editor-fold>

    private void prepareStorefront(HttpServletRequest request, Vector<Product> allProducts, Vector<Category> categories, String service) {
        // Applies server-side pagination and sends the current storefront data to index.jsp.
        int currentPage = parsePage(request.getParameter("page"));
        int totalProducts = allProducts != null ? allProducts.size() : 0;
        int totalPages = Math.max(1, (int) Math.ceil(totalProducts / (double) PAGE_SIZE));

        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        Vector<Product> pagedProducts = new Vector<>();
        if (allProducts != null && !allProducts.isEmpty()) {
            int start = (currentPage - 1) * PAGE_SIZE;
            int end = Math.min(start + PAGE_SIZE, allProducts.size());
            pagedProducts = new Vector<>(allProducts.subList(start, end));
        }

        request.setAttribute("products", pagedProducts);
        request.setAttribute("categories", categories);
        request.setAttribute("totalProductCount", totalProducts);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", PAGE_SIZE);
        request.setAttribute("currentService", service);
    }

    private int parsePage(String pageValue) {
        // Keeps page parsing safe and falls back to page 1 when the input is missing or invalid.
        if (pageValue == null || pageValue.isBlank()) {
            return 1;
        }

        try {
            int page = Integer.parseInt(pageValue);
            return Math.max(page, 1);
        } catch (NumberFormatException ex) {
            return 1;
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        // Shows the login page first, then authenticates the submitted email/password.
        UserDAO userDAO = new UserDAO();
        String email = trimToEmpty(request.getParameter("email"));
        String password = request.getParameter("password");

        if (email.isBlank() || password == null || password.isBlank()) {
            request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
            return;
        }

        User user = userDAO.checkLogin(email, password);
        if (user != null) {
            session.setAttribute("user", user);
            response.sendRedirect("indexjsp");
            return;
        }

        request.setAttribute("mess", "Incorrect email or password!");
        request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
    }

    private void handleLogout(HttpServletResponse response, HttpSession session) throws IOException {
        // Logs out the current session user and returns to the storefront.
        session.invalidate();
        response.sendRedirect("indexjsp");
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Shows the register page first, then validates and creates a new customer account.
        UserDAO userDAO = new UserDAO();
        String submit = request.getParameter("submit");

        if (submit == null) {
            request.getRequestDispatcher("jsp/register.jsp").forward(request, response);
            return;
        }

        String fullName = trimToEmpty(request.getParameter("fullName"));
        String password = trimToEmpty(request.getParameter("password"));
        String address = trimToEmpty(request.getParameter("address"));
        String phone = trimToEmpty(request.getParameter("phone"));
        String email = trimToEmpty(request.getParameter("email"));

        String validationError = validateRegisterInput(fullName, password, address, phone, email);
        if (validationError != null) {
            request.setAttribute("error", validationError);
            request.getRequestDispatcher("jsp/register.jsp").forward(request, response);
            return;
        }

        User existEmail = userDAO.searchUserByEmail(email);
        if (existEmail != null) {
            request.setAttribute("error", "Email is already in use.");
            request.getRequestDispatcher("jsp/register.jsp").forward(request, response);
            return;
        }

        String userID = generateUniqueUserId(userDAO);
        User user = new User(userID, fullName, password, 2, address, phone, email, true);
        userDAO.insertUser(user);
        response.sendRedirect("indexjsp");
    }

    private void handleOrderHistory(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        // Loads the signed-in user's order history page.
        User user = getSessionUser(session);
        if (user == null) {
            response.sendRedirect("indexjsp?service=login");
            return;
        }

        loadOrderHistory(request, session, user.getUserID());
        request.getRequestDispatcher("jsp/orderHistory.jsp").forward(request, response);
    }

    private void handleCancelOrder(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        // Cancels one pending order owned by the current user and restores its stock.
        User user = getSessionUser(session);
        if (user == null) {
            response.sendRedirect("indexjsp?service=login");
            return;
        }

        String orderIdRaw = request.getParameter("orderID");
        if (orderIdRaw == null || !orderIdRaw.matches("\\d+")) {
            session.setAttribute("historyMessage", "Invalid order selected.");
            session.setAttribute("historyMessageType", "error");
            response.sendRedirect("indexjsp?service=orderHistory");
            return;
        }

        int orderID = Integer.parseInt(orderIdRaw);
        OrderDAO orderDAO = new OrderDAO();
        ProductDAO productDAO = new ProductDAO();
        OrderDetailDAO detailDAO = new OrderDetailDAO();
        Order order = orderDAO.searchOrder(orderID);

        if (order == null || !user.getUserID().equals(order.getUserID())) {
            session.setAttribute("historyMessage", "Order not found.");
            session.setAttribute("historyMessageType", "error");
            response.sendRedirect("indexjsp?service=orderHistory");
            return;
        }

        if (order.getStatus() != Order.STATUS_PENDING) {
            session.setAttribute("historyMessage", "Only pending orders can be cancelled.");
            session.setAttribute("historyMessageType", "error");
            response.sendRedirect("indexjsp?service=orderHistory");
            return;
        }

        boolean cancelled = orderDAO.updateOrderStatus(orderID, Order.STATUS_PENDING, Order.STATUS_CANCELLED);
        if (!cancelled) {
            session.setAttribute("historyMessage", "This order could not be cancelled.");
            session.setAttribute("historyMessageType", "error");
            response.sendRedirect("indexjsp?service=orderHistory");
            return;
        }

        Vector<OrderDetail> details = detailDAO.getOrderDetailsByOrderId(orderID);
        for (OrderDetail detail : details) {
            productDAO.restoreStockAfterCancellation(detail.getProductID(), detail.getQuantity());
        }

        session.setAttribute("historyMessage", "Order cancelled.");
        session.setAttribute("historyMessageType", "success");
        response.sendRedirect("indexjsp?service=orderHistory");
    }

    private void handleClearCancelledOrder(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        // Permanently removes one cancelled order owned by the current user from order history.
        User user = getSessionUser(session);
        if (user == null) {
            response.sendRedirect("indexjsp?service=login");
            return;
        }

        String orderIdRaw = request.getParameter("orderID");
        if (orderIdRaw == null || !orderIdRaw.matches("\\d+")) {
            session.setAttribute("historyMessage", "Invalid order selected.");
            session.setAttribute("historyMessageType", "error");
            response.sendRedirect("indexjsp?service=orderHistory");
            return;
        }

        int orderID = Integer.parseInt(orderIdRaw);
        OrderDAO orderDAO = new OrderDAO();
        Order order = orderDAO.searchOrder(orderID);

        if (order == null || !user.getUserID().equals(order.getUserID())) {
            session.setAttribute("historyMessage", "Order not found.");
            session.setAttribute("historyMessageType", "error");
            response.sendRedirect("indexjsp?service=orderHistory");
            return;
        }

        if (order.getStatus() != Order.STATUS_CANCELLED) {
            session.setAttribute("historyMessage", "Only cancelled orders can be cleared.");
            session.setAttribute("historyMessageType", "error");
            response.sendRedirect("indexjsp?service=orderHistory");
            return;
        }

        boolean deleted = orderDAO.deletePendingOrCancelledOrder(orderID);
        if (!deleted) {
            session.setAttribute("historyMessage", "This cancelled order could not be cleared.");
            session.setAttribute("historyMessageType", "error");
            response.sendRedirect("indexjsp?service=orderHistory");
            return;
        }

        session.setAttribute("historyMessage", "Cancelled order cleared.");
        session.setAttribute("historyMessageType", "success");
        response.sendRedirect("indexjsp?service=orderHistory");
    }

    private void handleProductDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Loads one product, its category, and random recommendations for the detail page.
        ProductDAO productDAO = new ProductDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        String productIdRaw = request.getParameter("pId");
        if (productIdRaw == null || productIdRaw.isBlank()) {
            response.sendRedirect("indexjsp");
            return;
        }

        int productId = Integer.parseInt(productIdRaw);
        Product product = productDAO.searchProduct(productId);
        if (product == null) {
            response.sendRedirect("indexjsp");
            return;
        }

        Category category = categoryDAO.searchCategory(product.getCategoryID());
        request.setAttribute("product", product);
        request.setAttribute("category", category);
        request.setAttribute("recommendedProducts", productDAO.getRandomProducts(4, productId));
        request.getRequestDispatcher("jsp/productDetail.jsp").forward(request, response);
    }

    private void handleProductSearch(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        // Loads all products or filtered search results for the storefront.
        ProductDAO productDAO = new ProductDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        String submit = request.getParameter("submit");
        String productName = request.getParameter("productName");

        Vector<Product> products = submit == null
                ? productDAO.getAllProducts()
                : productDAO.searchProductsByName(productName);
        Vector<Category> categories = categoryDAO.getAllCategories();

        moveCartFlashToRequest(session, request);
        request.setAttribute("currentSearchKeyword", productName);
        request.setAttribute("bestSellers", productDAO.getBestSellingProducts(4));
        prepareStorefront(request, products, categories, "listOfProduct");
        request.getRequestDispatcher("jsp/index.jsp").forward(request, response);
    }

    private void handleCategoryFilter(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        // Loads products from one category and keeps the storefront pagination flow.
        ProductDAO productDAO = new ProductDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        String categoryID = request.getParameter("cId");

        Vector<Product> products = productDAO.getProductsByCategory(categoryID);
        Vector<Category> categories = categoryDAO.getAllCategories();

        moveCartFlashToRequest(session, request);
        request.setAttribute("currentCategoryId", categoryID);
        request.setAttribute("bestSellers", productDAO.getBestSellingProducts(4));
        prepareStorefront(request, products, categories, "showItem");
        request.getRequestDispatcher("jsp/index.jsp").forward(request, response);
    }

    private void handleDisplay(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        // Loads the default storefront with all categories, all products, and best sellers.
        ProductDAO productDAO = new ProductDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        Vector<Product> products = productDAO.getAllProducts();
        Vector<Category> categories = categoryDAO.getAllCategories();

        moveCartFlashToRequest(session, request);
        request.setAttribute("bestSellers", productDAO.getBestSellingProducts(4));
        prepareStorefront(request, products, categories, "display");
        request.getRequestDispatcher("jsp/index.jsp").forward(request, response);
    }

    private void moveCartFlashToRequest(HttpSession session, HttpServletRequest request) {
        // Moves one-time cart messages from session to request so JSP can show them after redirects.
        String cartMessage = (String) session.getAttribute("cartMessage");
        String cartMessageType = (String) session.getAttribute("cartMessageType");

        if (cartMessage != null) {
            request.setAttribute("cartMessage", cartMessage);
            request.setAttribute("cartMessageType", cartMessageType);
            session.removeAttribute("cartMessage");
            session.removeAttribute("cartMessageType");
        }
    }

    private String trimToEmpty(String value) {
        // Normalizes nullable input so the controller can validate fields safely.
        return value == null ? "" : value.trim();
    }

    private String validateRegisterInput(String fullName, String password,
            String address, String phone, String email) {
        // Keeps the register rules in one place for easier maintenance.
        if (fullName.isBlank()) {
            return "Full name is required.";
        }
        String passwordError = validatePassword(password);
        if (passwordError != null) {
            return passwordError;
        }
        if (address.isBlank()) {
            return "Address is required.";
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            return "Phone must contain 9 to 11 digits.";
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return "Please enter a valid email address.";
        }
        return null;
    }

    private String validatePassword(String password) {
        // Keeps password rules readable and gives users a specific reason when validation fails.
        if (password == null || password.length() < 8) {
            return "Password must be at least 8 characters.";
        }
        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter.";
        }
        if (!password.matches(".*[^A-Za-z0-9].*")) {
            return "Password must contain at least one special character.";
        }
        return null;
    }

    private String generateUniqueUserId(UserDAO userDAO) {
        // Generates the next available customer ID for self-registration.
        String generatedId = userDAO.generateNextUserID();
        while (userDAO.searchUser(generatedId) != null) {
            generatedId = userDAO.generateNextUserID();
        }
        return generatedId;
    }

    private void loadOrderHistory(HttpServletRequest request, HttpSession session, String userID) {
        // Loads the current user's orders and a short item summary for each row.
        OrderDAO orderDAO = new OrderDAO();
        OrderDetailDAO detailDAO = new OrderDetailDAO();
        ProductDAO productDAO = new ProductDAO();
        Vector<Order> orders = orderDAO.getOrdersByUser(userID);
        Map<Integer, String> orderItems = new HashMap<>();

        for (Order order : orders) {
            Vector<OrderDetail> details = detailDAO.getOrderDetailsByOrderId(order.getOrderID());
            orderItems.put(order.getOrderID(), buildOrderItemSummary(details, productDAO));
        }

        request.setAttribute("orders", orders);
        request.setAttribute("orderItems", orderItems);
        moveHistoryFlashToRequest(session, request);
    }

    private String buildOrderItemSummary(Vector<OrderDetail> details, ProductDAO productDAO) {
        // Builds a compact item summary like "Product A x1, Product B x2" for order history.
        if (details == null || details.isEmpty()) {
            return "No items";
        }

        StringJoiner joiner = new StringJoiner(", ");
        for (OrderDetail detail : details) {
            Product product = productDAO.searchProduct(detail.getProductID());
            String productName = product != null ? product.getProductName() : "Product #" + detail.getProductID();
            joiner.add(productName + " x" + detail.getQuantity());
        }
        return joiner.toString();
    }

    private void moveHistoryFlashToRequest(HttpSession session, HttpServletRequest request) {
        // Moves one-time order history messages from session to request.
        String historyMessage = (String) session.getAttribute("historyMessage");
        String historyMessageType = (String) session.getAttribute("historyMessageType");

        if (historyMessage != null) {
            request.setAttribute("historyMessage", historyMessage);
            request.setAttribute("historyMessageType", historyMessageType);
            session.removeAttribute("historyMessage");
            session.removeAttribute("historyMessageType");
        }
    }

    private User getSessionUser(HttpSession session) {
        // Reads the current logged-in user from session.
        return (User) session.getAttribute("user");
    }

}
