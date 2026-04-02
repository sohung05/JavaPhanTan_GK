//create unique constraints
CREATE CONSTRAINT unique_supplier IF NOT EXISTS FOR (s:Supplier) REQUIRE s.supplier_id IS UNIQUE;
CREATE CONSTRAINT unique_product IF NOT EXISTS FOR (p:Product) REQUIRE p.product_id IS UNIQUE;
CREATE CONSTRAINT unique_order IF NOT EXISTS FOR (o:Order) REQUIRE o.order_id IS UNIQUE;


//Loading data from CSV files
LOAD CSV WITH HEADERS FROM 'file:///northwind/suppliers.csv' AS row
WITH row WHERE row.supplier_id IS NOT NULL
MERGE (s:Supplier {supplier_id: row.supplier_id})
SET s.company_name = row.company_name,
    s.contact_name = row.contact_name,
    s.country = row.country;


LOAD CSV WITH HEADERS FROM 'file:///northwind/products.csv' AS row
WITH row WHERE row.product_id IS NOT null
MERGE (p:Product {product_id: row.product_id})
SET p.product_name = row.product_name,
    p.unit = row.unit,
    p.unit_price = toFloat(row.unit_price),
    p.units_in_stock = toInteger(row.units_in_stock)
WITH row, p
MATCH (s:Supplier {supplier_id: row.supplier_id})
MERGE (s)-[:SUPPLIES]->(p);


LOAD CSV WITH HEADERS FROM 'file:///northwind/orders.csv' AS row
WITH row WHERE row.order_id IS NOT NULL
MERGE (o:Order {order_id: row.order_id})
SET o.customer_name = row.customer_name,
    o.employee_name = row.employee_name,
    o.order_date = date(row.order_date),
    o.status = row.status;

LOAD CSV WITH HEADERS FROM 'file:///northwind/order_details.csv' AS row
WITH row WHERE row.order_id IS NOT NULL AND row.product_id IS NOT NULL
MATCH (o:Order {order_id: row.order_id})
MATCH (p:Product {product_id: row.product_id})
MERGE (o)-[od:ORDERS{quantity: toInteger(row.quantity), unit_price: toFloat(row.unit_price), discount: toFloat(row.discount)}]->(p);


