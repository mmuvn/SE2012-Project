<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- Admin dashboard shell: the cards select which manager fragment is included below on the same page. --%>
<c:if test="${empty sessionScope.user or sessionScope.user.roleID != 1}">
    <c:redirect url="/indexjsp"/>
</c:if>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Dashboard</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/storefront.css?v=20260413-4">
    </head>
    <body>
        <div class="site-shell admin-shell">
            <header class="hero-card admin-hero">
                <div class="hero-copy">
                    <span class="brand-mark">ADMIN</span>
                    <h1>Admin dashboard</h1>
                    <p>Manage products, orders, users, and categories from one clean control center.</p>
                </div>
                <div class="hero-actions">
                    <a class="btn btn-secondary" href="indexjsp">Back to shop</a>
                </div>
            </header>

            <section class="admin-nav-grid">
                <a class="admin-nav-card ${selectedManager eq 'productManager' ? 'is-active' : ''}" href="admin?service=productManager#manager-panel">
                    <h2>Product manager</h2>
                    <p>Update catalog details, stock, images, and storefront availability.</p>
                    <span class="btn btn-primary">Open</span>
                </a>
                <a class="admin-nav-card ${selectedManager eq 'orderManager' ? 'is-active' : ''}" href="admin?service=orderManager#manager-panel">
                    <h2>Order manager</h2>
                    <p>Review orders, inspect line items, and maintain order records.</p>
                    <span class="btn btn-primary">Open</span>
                </a>
                <a class="admin-nav-card ${selectedManager eq 'userManager' ? 'is-active' : ''}" href="admin?service=userManager#manager-panel">
                    <h2>User manager</h2>
                    <p>Create, update, or remove customer and admin accounts.</p>
                    <span class="btn btn-primary">Open</span>
                </a>
                <a class="admin-nav-card ${selectedManager eq 'categoryManager' ? 'is-active' : ''}" href="admin?service=categoryManager#manager-panel">
                    <h2>Category manager</h2>
                    <p>Shape the storefront structure with category names and descriptions.</p>
                    <span class="btn btn-primary">Open</span>
                </a>
            </section>

            <section id="manager-panel" class="panel admin-manager-panel">
                <c:if test="${not empty adminMessage}">
                    <div class="message ${adminMessageType == 'success' ? 'message-success' : 'message-error'}">${adminMessage}</div>
                </c:if>
                <c:choose>
                    <c:when test="${selectedManager eq 'productManager'}">
                        <jsp:include page="productManager.jsp"/>
                    </c:when>
                    <c:when test="${selectedManager eq 'orderManager'}">
                        <jsp:include page="orderManager.jsp"/>
                    </c:when>
                    <c:when test="${selectedManager eq 'userManager'}">
                        <jsp:include page="userManager.jsp"/>
                    </c:when>
                    <c:when test="${selectedManager eq 'roleManager'}">
                        <jsp:include page="roleManager.jsp"/>
                    </c:when>
                    <c:when test="${selectedManager eq 'categoryManager'}">
                        <jsp:include page="categoryManager.jsp"/>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-state">
                            <h2>Choose a manager</h2>
                            <p class="dashboard-hint">Pick one of the cards above and the selected manager will open right here under the dashboard.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </section>
        </div>
    </body>
</html>
