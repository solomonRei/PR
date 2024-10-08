# Product Filtering API Documentation

This documentation explains how to use the filtering functionality of the product API. The API allows you to filter products based on various criteria using different comparison operators. You can create custom queries by specifying the fields and conditions, and the results will return products that match the specified criteria.

## API Endpoint

GET http://localhost:8080/products/filter-custom

### Query Parameter: `search`

The `search` query parameter allows you to specify filtering conditions in the following format:
```<field>:<operator><value>```


Multiple conditions can be chained together using commas (`,`).

### Supported Fields

- **price**: Filter products based on their price.

### Supported Operators

The API supports the following comparison operators:

| Operator Symbol | Description                   | Example            |
|-----------------|-------------------------------|--------------------|
| `>`             | Greater than                  | `price:>100`       |
| `<`             | Less than                     | `price:<1000`      |
| `=`             | Equal to                      | `price:=500`       |
| `!=`            | Not equal to                  | `price:!=100`      |
| `>=`            | Greater than or equal to      | `price:>=500`      |
| `<=`            | Less than or equal to         | `price:<=500`      |

### Example Queries

Here are a few examples of how you can structure your queries using the `search` parameter:

1. **Filter products where price is greater than or equal to 529:**

```http://localhost:8080/products/filter-custom?search=price:>=529```

2. **Filter products where price is less than 6000:**

```http://localhost:8080/products/filter-custom?search=price:<6000```

3. **Filter products where price is greater than or equal to 529 and less than 6000:**

```http://localhost:8080/products/filter-custom?search=price:>=529,price:<6000```
