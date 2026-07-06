<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- Admin form page for creating a new category. --%>
<c:if test="${empty sessionScope.user or sessionScope.user.roleID != 1}">
    <c:redirect url="/indexjsp"/>
</c:if>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Add Category</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/storefront.css?v=20260322-3">
    </head>
    <body>
        <div class="site-shell admin-form-shell">
            <section class="auth-card admin-form-card">
                <span class="brand-mark">ADMIN</span>
                <h1>Add category</h1>
                <p>Create a new category for the storefront navigation.</p>

                <form action="categoryjsp" method="post" class="admin-form">
                    <div class="admin-form-grid">
                        <div class="admin-field">
                            <label for="categoryID">Category ID</label>
                        <input type="text" name="categoryID" id="categoryID" placeholder="Enter category ID, for example C011">
                        </div>
                        <div class="admin-field">
                            <label for="categoryName">Category name</label>
                        <input type="text" name="categoryName" id="categoryName" placeholder="Enter category name">
                        </div>
                        <div class="admin-field full">
                            <label for="describe">Description</label>
                        <input type="text" name="describe" id="describe" placeholder="Enter a short category description">
                        </div>
                    </div>

                    <div class="form-actions">
                        <input type="submit" name="submit" value="Add category">
                        <a class="btn btn-ghost" href="admin?service=categoryManager">Cancel</a>
                        <input type="hidden" name="service" value="addCategory">
                    </div>
                </form>
            </section>
        </div>
    </body>
</html>
