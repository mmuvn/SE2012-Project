package controllers;

import java.io.IOException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

// Shared admin guard for servlets that require an authenticated admin account.
public abstract class AdminGuardServlet extends HttpServlet {

    protected User requireAdminUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;

        if (user == null) {
            response.sendRedirect("indexjsp?service=login");
            return null;
        }

        if (user.getRoleID() != 1) {
            response.sendRedirect("indexjsp");
            return null;
        }

        return user;
    }
}
