# payconiq_assignment
Stock REST API Assignment with basic operations create, update, find and delete

Security:
Token based security mandatory to access the resource.

API to generate token: POST /user?user=testuser&password=password

Application APIs:

GET /api/stocks (get a list of stocks)

POST /api/stocks (create a stock)

GET /api/stocks/1 (get one stock from the list)

PATCH /api/stocks/1 (update the price of a single stock)

DELETE/api/stocks/1 (delete a single stock)

Docker and Docker-compose also support.
