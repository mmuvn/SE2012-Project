<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%-- Admin product manager fragment: search, review, and maintain product records inside the dashboard. --%>

<div class="content-stack">
    <div class="section-heading">
        <div>
            <h2>Product manager</h2>
            <p>${fn:length(products)} product(s) shown</p>
        </div>
        <a class="btn btn-primary" href="productjsp?service=addProduct">Add product</a>
    </div>

    <section class="search-panel">
        <h2>Search products</h2>
        <form action="admin#manager-panel" method="get" class="search-form admin-search-form">
            <div class="field-group">
                <input type="text" name="productName" value="${param.productName}" placeholder="Search by product name">
            </div>
            <input type="submit" value="Search">
            <input type="hidden" name="service" value="productManager">
        </form>
    </section>

    <div class="admin-table-wrap admin-table-scroll">
        <table class="admin-table">
            <thead>
                <tr>
                    <th>Product ID</th>
                    <th>Name</th>
                    <th>Image</th>
                    <th>Price</th>
                    <th>Quantity</th>
                    <th>Category</th>
                    <th>Description</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="product" items="${products}">
                    <tr>
                        <td class="inline-code">${product.productID}</td>
                        <td>${product.productName}</td>
                        <td>
                            <img class="admin-thumb" src="images/${product.image}" alt="${product.productName}">
                        </td>
                        <td>${product.price}</td>
                        <td>${product.quantity}</td>
                        <td class="inline-code">${product.categoryID}</td>
                        <td class="admin-text-cell">${product.description}</td>
                        <td>
                            <span class="status-pill ${product.status == 1 ? 'status-active' : 'status-inactive'}">
                                ${product.status == 1 ? 'Active' : 'Inactive'}
                            </span>
                        </td>
                        <td>
                            <div class="admin-action-group">
                                <a class="btn btn-danger btn-small" href="productjsp?service=deleteProduct&pId=${product.productID}">Delete</a>
                                <a class="btn btn-secondary btn-small" href="productjsp?service=updateProduct&pId=${product.productID}">Update</a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>
