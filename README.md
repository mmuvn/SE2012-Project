# SE2012-Project

An entry-level shopping management system built with JSP, Servlets, Java, and SQL Server.

## Overview

This project is a small e-commerce web application for a course submission. It includes a customer storefront and an admin dashboard for managing the main shopping data.

## Main Features

- Customer registration and login
- Product listing, search, and detail pages
- Shopping cart and checkout flow
- Order history for customers
- Admin management screens for products, orders, users, roles, and categories
- Custom error page for common web errors

## Tech Stack

- Java Servlet/JSP
- JSTL and Expression Language
- SQL Server
- NetBeans Ant project structure
- Apache Tomcat
- JUnit tests for core model behavior

## Project Structure

- `src/java/controllers` - servlet request handling
- `src/java/dal` - database access layer
- `src/java/model` - application domain models
- `web/jsp` - JSP views
- `web/css` - shared styling
- `database` - SQL Server database script
- `test` - unit tests

## Local Configuration

The real database configuration file is intentionally ignored by Git. Copy `src/java/ConnectDB.properties.example` to `src/java/ConnectDB.properties`, then update it with your SQL Server username, password, and database URL.
