<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- Admin form page for creating a new product record. --%>
<c:if test="${empty sessionScope.user or sessionScope.user.roleID != 1}">
    <c:redirect url="/indexjsp"/>
</c:if>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Add Product</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/storefront.css?v=20260413-1">
    </head>
    <body>
        <div class="site-shell admin-form-shell">
            <section class="auth-card admin-form-card">
                <span class="brand-mark">ADMIN</span>
                <h1>Add product</h1>
                <p>Create a new product for the storefront catalog.</p>

                <c:if test="${not empty error}">
                    <div class="message message-error">${error}</div>
                </c:if>

                <form action="productjsp" method="post" enctype="multipart/form-data" class="admin-form">
                    <div class="admin-form-grid">
                        <div class="admin-field full">
                            <label for="txtName">Product name</label>
                        <input type="text" name="productName" id="txtName" value="${param.productName}" placeholder="Enter product name">
                        </div>
                        <div class="admin-field">
                            <label for="txtimage">Product image</label>
                        <input type="file" name="imageFile" id="txtimage" accept="image/*" required>
                        <small class="field-help">Choose an image from your device. The filename will be saved automatically.</small>
                        </div>
                        <div class="admin-field">
                            <label for="txtprice">Price</label>
                        <input type="text" name="price" id="txtprice" value="${param.price}" placeholder="Enter price">
                        </div>
                        <div class="admin-field">
                            <label for="txtquantity">Quantity</label>
                        <input type="text" name="quantity" id="txtquantity" value="${param.quantity}" placeholder="Enter stock quantity">
                        </div>
                        <div class="admin-field">
                            <label for="categoryID">Category</label>
                            <select name="categoryID" id="categoryID">
                                <c:forEach var="category" items="${cVector}">
                                    <option value="${category.categoryID}" ${param.categoryID eq category.categoryID ? 'selected' : ''}>${category.categoryName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="admin-field full">
                            <label for="txtdescription">Description</label>
                        <textarea name="description" id="txtdescription" rows="4" placeholder="Enter a short product description">${param.description}</textarea>
                        </div>
                    </div>

                    <div class="form-actions">
                        <input type="submit" name="submit" value="Add product">
                        <a class="btn btn-ghost" href="admin?service=productManager">Cancel</a>
                        <input type="hidden" name="service" value="addProduct">
                    </div>
                </form>
            </section>
        </div>
    </body>
</html>
