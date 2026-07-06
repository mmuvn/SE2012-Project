<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- Admin form page for editing an order record. --%>
<c:if test="${empty sessionScope.user or sessionScope.user.roleID != 1}">
    <c:redirect url="/indexjsp"/>
</c:if>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Update Order</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/storefront.css?v=20260322-3">
    </head>
    <body>
        <div class="site-shell admin-form-shell">
            <section class="auth-card admin-form-card">
                <span class="brand-mark">ADMIN</span>
                <h1>Update order</h1>
                <p>Edit the selected order record.</p>

                <form action="orderjsp" method="post" class="admin-form">
                    <div class="admin-form-grid">
                        <div class="admin-field">
                            <label for="orderID">Order ID</label>
                        <input type="text" name="orderID" id="orderID" value="${p.orderID}" placeholder="Enter order ID">
                        </div>
                        <div class="admin-field">
                            <label for="orderDate">Order date</label>
                            <input type="date" name="orderDate" id="orderDate" value="${p.orderDate}">
                        </div>
                        <div class="admin-field">
                            <label for="total">Total</label>
                        <input type="text" name="total" id="total" value="${p.total}" placeholder="Enter order total">
                        </div>
                        <div class="admin-field">
                            <label for="userID">User ID</label>
                        <input type="text" name="userID" id="userID" value="${p.userID}" placeholder="Enter user ID">
                        </div>
                    </div>

                    <div class="form-actions">
                        <input type="submit" name="submit" value="Update order">
                        <a class="btn btn-ghost" href="admin?service=orderManager">Cancel</a>
                        <input type="hidden" name="service" value="updateOrder">
                    </div>
                </form>
            </section>
        </div>
    </body>
</html>
