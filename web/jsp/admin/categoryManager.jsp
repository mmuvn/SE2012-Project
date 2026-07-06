<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%-- Admin category manager fragment: edits the storefront category structure and descriptions. --%>

<div class="content-stack">
    <div class="section-heading">
        <div>
            <h2>Category manager</h2>
            <p>${fn:length(categories)} category record(s) shown</p>
        </div>
        <a class="btn btn-primary" href="categoryjsp?service=addCategory">Add category</a>
    </div>

    <section class="search-panel">
        <h2>Search categories</h2>
        <form action="admin#manager-panel" method="get" class="search-form admin-search-form">
            <div class="field-group">
                <input type="text" name="categoryName" value="${param.categoryName}" placeholder="Search by category name">
            </div>
            <input type="submit" value="Search">
            <input type="hidden" name="service" value="categoryManager">
        </form>
    </section>

    <div class="admin-table-wrap admin-table-scroll">
        <table class="admin-table">
            <thead>
                <tr>
                    <th>Category ID</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="category" items="${categories}">
                    <tr>
                        <td class="inline-code">${category.categoryID}</td>
                        <td>${category.categoryName}</td>
                        <td class="admin-text-cell">${category.describe}</td>
                        <td>
                            <div class="admin-action-group">
                                <a class="btn btn-danger btn-small" href="categoryjsp?service=deleteCategory&pId=${category.categoryID}">Delete</a>
                                <a class="btn btn-secondary btn-small" href="categoryjsp?service=updateCategory&pId=${category.categoryID}">Update</a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>
