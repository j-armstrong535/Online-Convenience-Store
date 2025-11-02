# Alterations from Object Design document to implementation

What evidence was produced to justify decisions?
Who did what?
Why were shortcuts taken?
Why were tradeoffs made?
What assumptions did you make?

1. Gianni - Changed the collaborator for Inventory-R4, now collaborates with DB to retrieve the stock levels of a product.  

2. Gianni - Removed ProductCategory class, as products can simply have a string property and Inventory can retrieve it and serve it to the user or admin

3. renamed ShoppingCart to Cart for readability, as the word 'Shopping' doesn't add too much to the meaning of the name