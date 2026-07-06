package controllers;

import dal.OrderDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import model.Order;
import model.User;

@WebServlet(name="OrderJSP", urlPatterns={"/orderjsp"})
// Handles admin-side order CRUD operations and viewing order detail lines.
public class OrderJSP extends AdminGuardServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = requireAdminUser(request, response);
        if (user == null) {
            return;
        }

        OrderDAO dao = new OrderDAO();
        String service = request.getParameter("service");

        if (service == null || service.equals("listOfOrder")) {
            response.sendRedirect("admin?service=orderManager");
            return;
        }

        // Delete one order from the admin side.
        if (service.equals("deleteOrder")) {

            int pId = Integer.parseInt(request.getParameter("pId"));
            Order currentOrder = dao.searchOrder(pId);
            if (currentOrder == null) {
                session.setAttribute("adminMessage", "Order not found.");
                session.setAttribute("adminMessageType", "error");
            } else if (currentOrder.getStatus() == Order.STATUS_APPROVED) {
                session.setAttribute("adminMessage", "Approved orders cannot be deleted.");
                session.setAttribute("adminMessageType", "error");
            } else if (dao.deletePendingOrCancelledOrder(pId)) {
                session.setAttribute("adminMessage", "Order deleted.");
                session.setAttribute("adminMessageType", "success");
            } else {
                session.setAttribute("adminMessage", "Only pending or cancelled orders can be deleted.");
                session.setAttribute("adminMessageType", "error");
            }

            response.sendRedirect("admin?service=orderManager");
            return;
        }

        // Approve a pending order from the admin side.
        if (service.equals("approveOrder")) {
            int orderID = Integer.parseInt(request.getParameter("pId"));
            Order currentOrder = dao.searchOrder(orderID);

            if (currentOrder != null && currentOrder.getStatus() == Order.STATUS_PENDING) {
                dao.updateOrderStatus(orderID, Order.STATUS_PENDING, Order.STATUS_APPROVED);
                session.setAttribute("adminMessage", "Order #" + orderID + " was approved.");
                session.setAttribute("adminMessageType", "success");
            } else {
                session.setAttribute("adminMessage", "Only pending orders can be approved.");
                session.setAttribute("adminMessageType", "error");
            }

            response.sendRedirect("admin?service=orderManager&pId=" + orderID + "#manager-panel");
            return;
        }

        // Order updates are disabled in the current admin flow.
        if (service.equals("updateOrder")) {
            session.setAttribute("adminMessage", "Order update is disabled.");
            session.setAttribute("adminMessageType", "error");
            response.sendRedirect("admin?service=orderManager");
            return;
        }

        // Show the insert form first, then create a new order.
        if (service.equals("addOrder")) {

            String submit = request.getParameter("submit");

            // Show insert form
            if (submit == null) {

                request.getRequestDispatcher("jsp/insert/insertOrder.jsp")
                        .forward(request, response);
                return;
            }

            // Insert database
            else {

                Date orderDate = Date.valueOf(request.getParameter("orderDate"));
                double total = Double.parseDouble(request.getParameter("total"));
                String userID = request.getParameter("userID");

                Order o = new Order(orderDate, total, userID, Order.STATUS_PENDING);

                dao.insertOrder(o);

                response.sendRedirect("admin?service=orderManager");
                return;
            }
        }
        // Redirect back to the admin dashboard and open the chosen order's detail section.
if (service.equals("showDetail")) {
    String orderID = request.getParameter("pId");
    response.sendRedirect("admin?service=orderManager&pId=" + orderID + "#manager-panel");
    return;
}
        response.sendRedirect("admin?service=orderManager");
        return;
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
        return "Order Controller";
    }
}
