<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%-- Admin order manager fragment: lists orders and can reveal detail lines for one selected order. --%>

<div class="content-stack">
    <div class="section-heading">
        <div>
            <h2>Order manager</h2>
            <p>${fn:length(orders)} order(s) shown</p>
        </div>
    </div>

    <section class="search-panel">
        <h2>Search orders</h2>
        <form action="admin#manager-panel" method="get" class="search-form admin-search-form">
            <div class="field-group">
                <input type="text" name="userID" value="${param.userID}" placeholder="Search by user ID">
            </div>
            <input type="submit" value="Search">
            <input type="hidden" name="service" value="orderManager">
        </form>
    </section>

    <div class="admin-table-wrap admin-table-scroll">
        <table class="admin-table">
            <thead>
                <tr>
                    <th>Order date</th>
                    <th>Status</th>
                    <th>Total</th>
                    <th>User ID</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="order" items="${orders}">
                    <tr>
                        <td>${order.orderDate}</td>
                        <td>
                            <span class="status-pill ${order.status == 2 ? 'status-approved' : order.status == 1 ? 'status-cancelled' : 'status-pending'}">
                                ${order.statusLabel}
                            </span>
                        </td>
                        <td>${order.total}</td>
                        <td class="inline-code">${order.userID}</td>
                        <td>
                            <div class="admin-action-group">
                                <c:if test="${order.status == 0}">
                                    <a class="btn btn-primary btn-small" href="orderjsp?service=approveOrder&pId=${order.orderID}" onclick="return confirm('Approve this pending order?');">Approve</a>
                                    <a class="btn btn-danger btn-small" href="orderjsp?service=deleteOrder&pId=${order.orderID}" onclick="return confirm('Delete this pending order? Stock will be restored.');">Delete</a>
                                </c:if>
                                <c:if test="${order.status == 1}">
                                    <a class="btn btn-danger btn-small" href="orderjsp?service=deleteOrder&pId=${order.orderID}" onclick="return confirm('Delete this cancelled order?');">Delete</a>
                                </c:if>
                                <a class="btn btn-ghost btn-small" href="admin?service=orderManager&pId=${order.orderID}#manager-panel">Show details</a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <c:if test="${not empty details}">
        <section class="panel">
            <div class="section-heading">
                <div>
                    <h2>Order details</h2>
                    <p>Selected order line items</p>
                </div>
            </div>

            <c:if test="${not empty selectedOrder}">
                <div class="order-summary-grid">
                    <article class="detail-meta-card">
                        <h3>Order date</h3>
                        <p>${selectedOrder.orderDate}</p>
                    </article>
                    <article class="detail-meta-card">
                        <h3>Status</h3>
                        <p>
                            <span class="status-pill ${selectedOrder.status == 2 ? 'status-approved' : selectedOrder.status == 1 ? 'status-cancelled' : 'status-pending'}">
                                ${selectedOrder.statusLabel}
                            </span>
                        </p>
                    </article>
                    <article class="detail-meta-card">
                        <h3>Customer</h3>
                        <p>
                            <c:choose>
                                <c:when test="${not empty selectedCustomer}">
                                    ${selectedCustomer.fullName}
                                </c:when>
                                <c:otherwise>
                                    ${selectedOrder.userID}
                                </c:otherwise>
                            </c:choose>
                        </p>
                    </article>
                    <article class="detail-meta-card">
                        <h3>Total</h3>
                        <p>$${selectedOrder.total}</p>
                    </article>
                </div>
            </c:if>

            <div class="order-detail-list">
                <c:forEach var="detail" items="${details}">
                    <c:set var="product" value="${detailProducts[detail.productID]}" />
                    <article class="order-detail-card">
                        <div class="order-detail-media">
                            <c:choose>
                                <c:when test="${not empty product and not empty product.image}">
                                    <img class="admin-thumb" src="${pageContext.request.contextPath}/images/${product.image}" alt="${product.productName}">
                                </c:when>
                                <c:otherwise>
                                    <div class="product-initial">${empty product ? detail.productID : fn:substring(product.productName, 0, 1)}</div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="order-detail-main">
                            <h3>
                                <c:choose>
                                    <c:when test="${not empty product}">
                                        ${product.productName}
                                    </c:when>
                                    <c:otherwise>
                                        Product #${detail.productID}
                                    </c:otherwise>
                                </c:choose>
                            </h3>
                            <p class="product-meta">Product ID: ${detail.productID}</p>
                            <div class="order-detail-stats">
                                <div>
                                    <span class="order-detail-label">Quantity</span>
                                    <strong>${detail.quantity}</strong>
                                </div>
                                <div>
                                    <span class="order-detail-label">Unit price</span>
                                    <strong>$${detail.price}</strong>
                                </div>
                                <div>
                                    <span class="order-detail-label">Subtotal</span>
                                    <strong>$${detail.price * detail.quantity}</strong>
                                </div>
                            </div>
                        </div>
                    </article>
                </c:forEach>
            </div>
        </section>
    </c:if>
</div>
