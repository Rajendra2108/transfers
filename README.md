Coding Exercise - Transfer money between tow accounts

Setup:
Used Spring Boot to create the project with in-memory h2 database for storing accounts and transactions.

To run code:
./mvnw clean install


To test try with the below request body:
http://localhost:8080/swagger-ui/#/transfers-controller/transferFundsUsingPOST

{
  "amount": 10,
  "currency": "GBP",
  "id": 10,
  "sourceAccountId": 1,
  "targetAccountId": 2
}


Feature Improvements:
- Error handling with global exception handler
- Improve error handling
- Add performance tests

