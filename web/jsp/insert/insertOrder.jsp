<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- Admin form page for manually creating an order record. --%>
<c:if test="${empty sessionScope.user or sessionScope.user.roleID != 1}">
    <c:redirect url="/indexjsp"/>
</c:if>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Add Order</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/storefront.css?v=20260322-3">
    </head>
    <body>
        <div class="site-shell admin-form-shell">
            <section class="auth-card admin-form-card">
                <span class="brand-mark">ADMIN</span>
                <h1>Add order</h1>
                <p>Create an order record manually for admin-side management.</p>

                <form action="orderjsp" method="post" class="admin-form">
                    <div class="admin-form-grid">
                        <div class="admin-field">
                            <label for="orderDate">Order date</label>
                            <input type="date" name="orderDate" id="orderDate">
                        </div>
                        <div class="admin-field">
                            <label for="total">Total</label>
                        <input type="text" name="total" id="total" placeholder="Enter order total">
                        </div>
                        <div class="admin-field full">
                            <label for="userID">User ID</label>
                        <input type="text" name="userID" id="userID" placeholder="Enter the user ID">
                        </div>
                    </div>

                    <div class="form-actions">
                        <input type="submit" name="submit" value="Add order">
                        <a class="btn btn-ghost" href="admin?service=orderManager">Cancel</a>
                        <input type="hidden" name="service" value="addOrder">
                    </div>
                </form>
            </section>
        </div>
    </body>
</html>
