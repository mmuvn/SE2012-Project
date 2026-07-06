/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controllers;

import dal.CartDAO;
import dal.OrderDAO;
import dal.ProductDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Vector;
import model.Cart;
import model.Order;
import model.Product;
import model.User;

/**
 *
 * @author admin
 */
@WebServlet(name="CartServlet", urlPatterns={"/cart"})
// Manages cart actions such as add, remove, quantity update, cart display, and checkout.
public class CartServlet extends HttpServlet {
   
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
        CartDAO dao = new CartDAO();
        ProductDAO pDao = new ProductDAO();
        String service = request.getParameter("service");
        if (service == null || service.isBlank()) {
            service = "showCart";
        }

        User user = getLoggedInUser(session);
        if (requiresLoggedInUser(service) && user == null) {
            response.sendRedirect("indexjsp?service=login");
            return;
        }

        // Update one cart item's quantity and cap it at current stock.
        if (service.equals("updateQuantity")) {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            Cart cart = (Cart) session.getAttribute(productId + "");

            if (cart != null) {
                Product p = pDao.searchProduct(productId);

                if (p == null || p.getStatus() == 0 || p.getQuantity() <= 0) {
                    session.removeAttribute(productId + "");
                    session.setAttribute("cartMessage", "A sold out product was removed from your cart.");
                    session.setAttribute("cartMessageType", "error");
                    response.sendRedirect("cart");
                    return;
                }

                int maxQuantity = p.getQuantity();

                if (quantity > maxQuantity) {
                    quantity = maxQuantity;
                    session.setAttribute("cartMessage", "Only " + maxQuantity + " item(s) are available for this product.");
                    session.setAttribute("cartMessageType", "error");
                }

                if (quantity < 1) {
                    quantity = 1;
                    session.setAttribute("cartMessage", "Invalid input. Quantity is automatically set to 1.");
                    session.setAttribute("cartMessageType", "error");
                }

                cart.setQuantity(quantity);
                cart.setAvailableStock(maxQuantity);
                session.setAttribute(productId + "", cart);
            }

            response.sendRedirect("cart");
            return;
        }
        
        // Create an order from the current session cart and reduce product stock.
        if (service.equals("checkout")) {
            Vector<Cart> vector = getCartItems(session);

            // Recheck stock right before creating the order.
            for (Cart c : vector) {
                Product currentProduct = pDao.searchProduct(c.getProductID());
                if (currentProduct == null || currentProduct.getStatus() == 0 || currentProduct.getQuantity() <= 0) {
                    session.removeAttribute(c.getProductID() + "");
                    session.setAttribute("cartMessage", "A sold out product was removed from your cart before checkout.");
                    session.setAttribute("cartMessageType", "error");
                    response.sendRedirect("cart");
                    return;
                }
                if (c.getQuantity() > currentProduct.getQuantity()) {
                    c.setQuantity(currentProduct.getQuantity());
                    session.setAttribute(c.getProductID() + "", c);
                    session.setAttribute("cartMessage", "Only " + currentProduct.getQuantity() + " item(s) remain in stock for " + currentProduct.getProductName() + ".");
                    session.setAttribute("cartMessageType", "error");
                    response.sendRedirect("cart");
                    return;
                }
            }

            double total = 0;
            for (Cart c : vector) {
                total += c.getPrice() * c.getQuantity();
            }

            OrderDAO oDao = new OrderDAO();

            Order order = new Order(
                    new java.sql.Date(System.currentTimeMillis()),
                    total,
                    user.getUserID(),
                    Order.STATUS_PENDING
            );

            int orderID = oDao.checkoutOrder(order, vector);
            if (orderID == 0) {
                session.setAttribute("cartMessage", "Checkout stopped because one product is no longer available.");
                session.setAttribute("cartMessageType", "error");
                response.sendRedirect("cart");
                return;
            }

            // Clear the cart after a successful order.
            for (Cart c : vector) {
                session.removeAttribute(c.getProductID() + "");
            }

            session.setAttribute("cartMessage", "Check out successful! Check your order history to see your order.");
            session.setAttribute("cartMessageType", "success");
            response.sendRedirect("cart?service=showCart");
            return;
        }

        // Add one product to the cart unless it is sold out or already at max stock.
        if (service.equals("add2Cart")) {
            int pId = Integer.parseInt(request.getParameter("pId"));
            Cart cart = dao.getCart(pId);
            Product product = pDao.searchProduct(pId);

            if (cart == null || product == null || product.getStatus() == 0 || product.getQuantity() <= 0) {
                session.setAttribute("cartMessage", "This product is sold out.");
                session.setAttribute("cartMessageType", "error");
                response.sendRedirect("indexjsp");
                return;
            }

            if (session.getAttribute(pId + "") == null) {
                cart.setQuantity(1);
                cart.setAvailableStock(product.getQuantity());
                session.setAttribute(pId + "", cart);
            } else {
                cart = (Cart) session.getAttribute(pId + "");
                if (cart.getQuantity() >= product.getQuantity()) {
                    session.setAttribute("cartMessage", "Only " + product.getQuantity() + " item(s) are available for " + product.getProductName() + ".");
                    session.setAttribute("cartMessageType", "error");
                    response.sendRedirect("cart");
                    return;
                }
                cart.setQuantity(cart.getQuantity() + 1);
                cart.setAvailableStock(product.getQuantity());
                session.setAttribute(pId + "", cart);
            }
            response.sendRedirect("cart");
            return;
        }

        // Remove one product from the session cart.
        if (service.equals("removeItem")) {
            int productId = Integer.parseInt(request.getParameter("productId"));
            session.removeAttribute(productId + "");
            response.sendRedirect("cart");
            return;
        }

        // Remove every cart item stored in session.
        if (service.equals("removeAll")) {
            Enumeration enu = session.getAttributeNames();
            while (enu.hasMoreElements()) {
                String key = (String) enu.nextElement();
                Object obj = session.getAttribute(key);

                if (obj instanceof Cart) {
                    session.removeAttribute(key);
                }
            }
            response.sendRedirect("cart");
            return;
        }
        
        // Show the cart page with current items and one-time flash messages.
        if (service.equals("showCart")) {
            Vector<Cart> vector = getCartItems(session);
            for (Cart item : vector) {
                Product currentProduct = pDao.searchProduct(item.getProductID());
                item.setAvailableStock(currentProduct != null ? currentProduct.getQuantity() : 0);
            }
            moveCartFlashToRequest(session, request);
            request.setAttribute("vector", vector);
            request.getRequestDispatcher("jsp/cart.jsp").forward(request, response);
            return;
        }
        response.sendRedirect("cart");
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
        return "Cart Controller";
    }// </editor-fold>

    private Vector<Cart> getCartItems(HttpSession session) {
        // Collects every Cart object stored in session so the cart page can render all current items.
        Vector<Cart> vector = new Vector<>();
        Enumeration enu = session.getAttributeNames();
        while (enu.hasMoreElements()) {
            String key = (String) enu.nextElement();
            Object obj = session.getAttribute(key);

            if (obj instanceof Cart) {
                vector.add((Cart) obj);
            }
        }
        return vector;
    }

    private User getLoggedInUser(HttpSession session) {
        // Returns the current logged-in user stored in session.
        return (User) session.getAttribute("user");
    }

    private boolean requiresLoggedInUser(String service) {
        // Every cart action in this project belongs to the current logged-in user.
        return "showCart".equals(service)
                || "updateQuantity".equals(service)
                || "checkout".equals(service)
                || "add2Cart".equals(service)
                || "removeItem".equals(service)
                || "removeAll".equals(service);
    }

    private void moveCartFlashToRequest(HttpSession session, HttpServletRequest request) {
        // Moves one-time cart messages from session to request for the cart JSP.
        String cartMessage = (String) session.getAttribute("cartMessage");
        String cartMessageType = (String) session.getAttribute("cartMessageType");

        if (cartMessage != null) {
            request.setAttribute("cartMessage", cartMessage);
            request.setAttribute("cartMessageType", cartMessageType);
            session.removeAttribute("cartMessage");
            session.removeAttribute("cartMessageType");
        }
    }

}
