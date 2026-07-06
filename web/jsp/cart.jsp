<%-- 
    Document   : cart
    Created on : Mar 6, 2026, 8:49:41 AM
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%-- Customer cart page: shows session cart items, quantity updates, totals, and checkout actions. --%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Your Cart</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/storefront.css?v=20260323-2">
    </head>
    <body>
        <c:set var="total" value="${0}" />
        <c:forEach var="item" items="${vector}">
            <c:set var="total" value="${total + (item.quantity * item.price)}" />
        </c:forEach>

        <div class="site-shell">
            <section class="cart-layout">
                <div class="cart-summary">
                    <div>
                <h1>Your cart</h1>
                <p>Review items, update quantity, or move straight to checkout.</p>
                    </div>
                    <div class="cart-total">$${total}</div>
                </div>

                <section class="panel">
                    <div class="section-heading">
                        <div>
                    <h2>Cart items</h2>
                    <p>${fn:length(vector)} item(s) in your order</p>
                        </div>
                    </div>

                    <c:if test="${not empty cartMessage}">
                        <div class="message ${cartMessageType == 'success' ? 'message-success' : 'message-error'}">${cartMessage}</div>
                    </c:if>

                    <c:choose>
                        <c:when test="${not empty vector}">
                            <div class="table-wrap">
                                <table class="shop-table">
                                    <thead>
                                        <tr>
                            <th>Product ID</th>
                            <th>Product Name</th>
                            <th>Price</th>
                            <th>Quantity</th>
                            <th>Sub total</th>
                            <th>Remove</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="item" items="${vector}">
                                            <tr>
                                                <td>${item.productID}</td>
                                                <td>${item.productName}</td>
                                                <td>$${item.price}</td>
                                                <td>
                                                    <form action="cart" method="get" class="inline-form">
                                                        <input type="number" name="quantity" value="${item.quantity}" min="1" max="${item.availableStock}">
                                                        <input type="hidden" name="service" value="updateQuantity">
                                                        <input type="hidden" name="productId" value="${item.productID}">
                                                        <input type="submit" value="Update">
                                                    </form>
                                                </td>
                                                <td>$${item.quantity * item.price}</td>
                                    <td><a class="small-link" href="cart?service=removeItem&productId=${item.productID}">Remove</a></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                            <h3>Your cart is empty</h3>
                            <p>Add a few products from the shop, then come back here to checkout.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>

                    <div class="page-actions">
                        <c:if test="${not empty vector}">
                            <a class="btn btn-danger" href="cart?service=removeAll" onclick="return confirm('Are you sure you want to remove all product from cart?');">Remove All Cart</a>
                            <a class="btn btn-primary" href="cart?service=checkout" onclick="return confirm('Checkout?')";>Checkout</a>
                        </c:if>
                        <c:if test="${not empty sessionScope.user}">
                            <a class="btn btn-secondary" href="indexjsp?service=orderHistory">Order History</a>
                        </c:if>
                        <a class="btn btn-ghost" href="indexjsp">Continue shopping</a>
                    </div>
                </section>
            </section>
        </div>
    </body>
</html>
