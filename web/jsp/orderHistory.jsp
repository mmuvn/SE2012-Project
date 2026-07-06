<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="/indexjsp?service=login"/>
</c:if>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Order History</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/storefront.css?v=20260413-4">
    </head>
    <body>
        <div class="site-shell">
            <section class="cart-layout">
                <div class="cart-summary">
                    <div>
                        <h1>Order history</h1>
                        <p>Review your recent orders and cancel pending ones before they are approved.</p>
                    </div>
                    <div class="cart-total">${fn:length(orders)} order(s)</div>
                </div>

                <section class="panel">
                    <div class="section-heading">
                        <div>
                            <h2>Your orders</h2>
                            <p>${sessionScope.user.fullName}</p>
                        </div>
                    </div>

                    <c:if test="${not empty historyMessage}">
                        <div class="message ${historyMessageType == 'success' ? 'message-success' : 'message-error'}">${historyMessage}</div>
                    </c:if>

                    <c:choose>
                        <c:when test="${not empty orders}">
                            <div class="table-wrap">
                                <table class="shop-table">
                                    <thead>
                                        <tr>
                                            <th>Order date</th>
                                            <th>Items</th>
                                            <th>Total</th>
                                            <th>Status</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="order" items="${orders}">
                                            <tr>
                                                <td>${order.orderDate}</td>
                                                <td class="admin-text-cell">${orderItems[order.orderID]}</td>
                                                <td>$${order.total}</td>
                                                <td>
                                                    <span class="status-pill ${order.status == 2 ? 'status-approved' : order.status == 1 ? 'status-cancelled' : 'status-pending'}">
                                                        ${order.statusLabel}
                                                    </span>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${order.status == 0}">                                                            <a class="btn btn-danger btn-small" href="indexjsp?service=cancelOrder&orderID=${order.orderID}" onclick="return confirm('Do you want to cancel this order? Once cancelled, it will stay in your history.');">Cancel order</a>
                                                        </c:when>
                                                        <c:when test="${order.status == 1}">
                                                            <a class="btn btn-secondary btn-small" href="indexjsp?service=clearCancelledOrder&orderID=${order.orderID}" onclick="return confirm('Clear this cancelled order from your history?');">Clear order</a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="btn btn-disabled btn-small">Locked</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <h3>No orders yet</h3>
                                <p>Your completed checkouts will appear here once you place them.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>

                    <div class="page-actions">
                        <a class="btn btn-ghost" href="indexjsp">Back to shop</a>
                        <a class="btn btn-primary" href="cart?service=showCart">Show Cart</a>
                    </div>
                </section>
            </section>
        </div>
    </body>
</html>
