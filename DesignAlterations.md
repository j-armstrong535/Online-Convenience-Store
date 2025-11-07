# Alterations from Object Design document to implementation

What evidence was produced to justify decisions?
Who did what?
Why were shortcuts taken?
Why were tradeoffs made?
What assumptions did you make?

1. Gianni - Changed the collaborator for Inventory-R4, now collaborates with DB to retrieve the stock levels of a product.  

2. renamed ShoppingCart to Cart for readability, as the word 'Shopping' doesn't add anything to the meaning of the name; a Cart still means for shopping

3. Added a small enum class FulfilmentMethod to denote the state of the checkout order

The rest of the Design Alterations can be seen in the Design Implementation and Reflection document.