# Database Setup

This project uses SQL Server and the `HE200133` database script included in the repository.

## 1. Create The Database

Open SQL Server Management Studio or another SQL Server client, then run:

```sql
CREATE DATABASE HE200133;
GO
```

After creating the database, execute the script at:

```text
database/HE200133.sql
```

The script creates the required tables and inserts sample data for products, categories, roles, users, orders, and order details.

## 2. Configure The Java Connection

Copy the example configuration file:

```text
src/java/ConnectDB.properties.example
```

Create a local file with this name:

```text
src/java/ConnectDB.properties
```

Update the values for your local SQL Server instance:

```properties
userID=your_sqlserver_user
password=your_sqlserver_password
url=jdbc:sqlserver://localhost:1433;databaseName=HE200133;encrypt=true;trustServerCertificate=true
```

## 3. Notes

- `ConnectDB.properties` is ignored by Git because it contains local credentials.
- If SQL Server runs on a different port, update the `url` value.
- If the database name changes, update both SQL Server and the JDBC URL.
