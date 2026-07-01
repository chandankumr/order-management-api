# API Demo

## 1. Create a STANDARD Order
**Request:**
```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{"customerId": "CUST001", "customerType": "STANDARD", "amount": 100.00}'

  