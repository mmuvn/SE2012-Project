<%-- 
    Document   : productDetail
    Created on : Mar 21, 2026
    Author     : Codex
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%-- Product detail page: shows one product with image, stock, description, category, and add-to-cart action. --%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>${product.productName}</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/storefront.css?v=20260414-1">
    </head>
    <body>
        <div class="site-shell">
            <div class="page-actions detail-top-actions">
                    <a class="btn btn-ghost" href="indexjsp">Back to shop</a>
                <c:if test="${not empty category}">
                    <a class="btn btn-secondary" href="indexjsp?service=showItem&cId=${category.categoryID}">More in ${category.categoryName}</a>
                </c:if>
            </div>

            <section class="detail-layout">
                <div class="detail-media panel">
                    <div class="detail-image-frame">
                        <img class="detail-image" src="${pageContext.request.contextPath}/images/${product.image}" alt="${product.productName}" onerror="this.style.display='none'; this.nextElementSibling.style.display='grid';">
                        <div class="detail-image-fallback">${fn:substring(empty product.productName ? 'P' : product.productName, 0, 1)}</div>
                    </div>
                </div>

                <div class="detail-content panel">
                    <c:if test="${not empty category}">
                        <span class="tag">${category.categoryName}</span>
                    </c:if>
                    <h1 class="detail-title">${product.productName}</h1>
                    <p class="detail-price">$${product.price}</p>
                    <p class="detail-stock ${product.quantity <= 0 or product.status == 0 ? 'stock-sold-out' : ''}">
                        <c:choose>
                        <c:when test="${product.quantity <= 0 or product.status == 0}">Sold out.</c:when>
                        <c:otherwise>Stock available: ${product.quantity}</c:otherwise>
                        </c:choose>
                    </p>

                    <div class="page-actions">
                        <c:choose>
                            <c:when test="${product.quantity <= 0 or product.status == 0}">
                                <span class="btn btn-disabled">Sold out</span>
                            </c:when>
                            <c:otherwise>
                                <a class="btn btn-primary" href="cart?service=add2Cart&pId=${product.productID}" onclick="return confirm('Add to cart?');">Add to cart</a>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="detail-section">
                    <h2>Description</h2>
                        <c:choose>
                            <c:when test="${not empty product.description}">
                                <p>${product.description}</p>
                            </c:when>
                            <c:otherwise>
                            <p>This product does not have a description yet. You can add one from the product manager after updating the database schema.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="detail-meta-grid">
                        <div class="detail-meta-card">
                            <h3>Category</h3>
                            <p><c:out value="${category.categoryName}" default="Unknown" /></p>
                        </div>
                        <div class="detail-meta-card">
                            <h3>Stock</h3>
                            <p>${product.quantity}</p>
                        </div>
                    </div>
                </div>
            </section>

            <c:if test="${not empty recommendedProducts}">
                <section class="panel recommendation-panel">
                    <div class="section-heading">
                        <div>
                            <h2>You may also like</h2>
                        </div>
                    </div>
                    <div class="product-grid product-grid-compact">
                        <c:forEach var="item" items="${recommendedProducts}">
                            <c:set var="displayName" value="${empty item.productName ? 'Product' : item.productName}" />
                            <article class="product-card">
                                <a class="product-link" href="indexjsp?service=productDetail&pId=${item.productID}">
                                    <div class="product-visual">
                                        <div>
                                            <img class="product-image" src="${pageContext.request.contextPath}/images/${item.image}" alt="${displayName}" onerror="this.style.display='none'; this.nextElementSibling.style.display='grid';">
                                            <div class="product-initial product-fallback">${fn:substring(displayName, 0, 1)}</div>
                                        </div>
                                    </div>
                                </a>
                                <div>
                                    <h3><a class="product-title-link" href="indexjsp?service=productDetail&pId=${item.productID}">${displayName}</a></h3>
                                </div>
                                <div class="product-footer">
                                    <div>
                                        <div class="product-price">$${item.price}</div>
                                        <p class="product-meta ${item.quantity <= 0 or item.status == 0 ? 'stock-sold-out' : ''}">
                                            <c:choose>
                                                <c:when test="${item.quantity <= 0 or item.status == 0}">Sold out.</c:when>
                                                <c:otherwise>Stock: ${item.quantity}</c:otherwise>
                                            </c:choose>
                                        </p>
                                    </div>
                                    <div class="product-actions">
                                        <a class="btn btn-ghost" href="indexjsp?service=productDetail&pId=${item.productID}">View details</a>
                                    </div>
                                </div>
                            </article>
                        </c:forEach>
                    </div>
                </section>
            </c:if>
        </div>
    </body>
</html>
