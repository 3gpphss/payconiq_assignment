# payconiq_assignment
Stock REST API Assignment with basic operations create, update, find and delete

Application basic details:
1. Stock details and User details will shore in MySQL Relational database.
2. Spring boot REST APIs using HTTP and REST standards
3. Basic Token based athentication for accessing resources.
4. Swagger UI for API's Documenation
5. MicroServices and Docker container based approach.
6. Docker compose

Security:
Token based security mandatory to access the resource.

API to generate token: POST /user?user=testuser&password=password

Application APIs:

GET /api/stocks (get a list of stocks)

POST /api/stocks (create a stock)

GET /api/stocks/1 (get one stock from the list)

PATCH /api/stocks/1 (update the price of a single stock)

DELETE/api/stocks/1 (delete a single stock)

More information related to application please read https://github.com/3gpphss/payconiq_assignment/blob/main/ApplicationDocumentation.docx document.


