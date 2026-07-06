# Development Notes

## Running The Project

This is a NetBeans Ant web project intended to run on Apache Tomcat.

Typical local workflow:

1. Open the project folder in NetBeans.
2. Configure a Tomcat server in NetBeans.
3. Make sure SQL Server is running.
4. Create `src/java/ConnectDB.properties` from the example file.
5. Run the project from NetBeans.

## Build Files

The main Ant entry file is:

```text
build.xml
```

NetBeans-generated project metadata is stored under:

```text
nbproject/
```

The `nbproject/private/` folder is ignored because it stores machine-specific IDE settings.

## Testing

Unit tests are stored under:

```text
test/
```

Current tests focus on model behavior, especially cart calculations.

## Code Organization

- Controllers handle request routing and validation.
- DAO classes own SQL queries and database access.
- Model classes represent products, users, carts, orders, and related records.
- JSP files render the storefront and admin pages.

## Repository Hygiene

The repository excludes generated build output, scanner cache files, IDE private settings, and local database credentials. This keeps the GitHub repository focused on source code and documentation.
