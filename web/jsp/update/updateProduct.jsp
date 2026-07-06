<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- Admin form page for editing an existing product record. --%>
<c:if test="${empty sessionScope.user or sessionScope.user.roleID != 1}">
    <c:redirect url="/indexjsp"/>
</c:if>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Update Product</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/storefront.css?v=20260413-1">
    </head>
    <body>
        <div class="site-shell admin-form-shell">
            <section class="auth-card admin-form-card">
                <span class="brand-mark">ADMIN</span>
                <h1>Update product</h1>
                <p>Edit product details, stock, category, and storefront availability.</p>

                <form action="productjsp" method="post" enctype="multipart/form-data" class="admin-form">
                    <div class="admin-form-grid">
                        <div class="admin-field">
                            <label for="txtId">Product ID</label>
                        <input type="text" name="productId" id="txtId" value="${p.productID}" placeholder="Product ID" readonly>
                        </div>
                        <div class="admin-field">
                            <label for="txtName">Product name</label>
                        <input type="text" name="productName" id="txtName" value="${p.productName}" placeholder="Enter product name">
                        </div>
                        <div class="admin-field">
                            <label for="txtimage">Replace image</label>
                        <input type="file" name="imageFile" id="txtimage" accept="image/*">
                        <small class="field-help">Current image: <span class="inline-code">${p.image}</span>. Leave this empty to keep it.</small>
                        <input type="hidden" name="currentImage" value="${p.image}">
                        </div>
                        <div class="admin-field">
                            <label for="txtprice">Price</label>
                        <input type="text" name="price" id="txtprice" value="${p.price}" placeholder="Enter price">
                        </div>
                        <div class="admin-field">
                            <label for="txtquantity">Quantity</label>
                        <input type="text" name="quantity" id="txtquantity" value="${p.quantity}" placeholder="Enter stock quantity">
                        </div>
                        <div class="admin-field">
                            <label for="categoryID">Category</label>
                            <select name="categoryID" id="categoryID">
                                <c:forEach var="category" items="${cVector}">
                                    <option value="${category.categoryID}" ${category.categoryID eq p.categoryID ? 'selected' : ''}>
                                        ${category.categoryName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="admin-field full">
                            <label for="txtdescription">Description</label>
                        <textarea name="description" id="txtdescription" rows="4" placeholder="Enter a short product description">${p.description}</textarea>
                        </div>
                    </div>

                    <div class="form-actions">
                        <input type="submit" name="submit" value="Update product">
                        <a class="btn btn-ghost" href="admin?service=productManager">Cancel</a>
                        <input type="hidden" name="service" value="updateProduct">
                    </div>
                </form>
            </section>
        </div>
    </body>
</html>
