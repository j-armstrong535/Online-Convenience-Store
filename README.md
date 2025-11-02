# Online-Convenience-Store
Prototype of an online convenience store for an already existing store looking to expand itself online

# Required dependencies

JDK 21
Install via:
(Mac) 
>brew install openjdk@21
(Linux) 
>sudo apt install openjdk-21-jdk
(Windows) 
>winget install Eclipse Adoptium.Temurin.21.jdk

Node.js18+ (incl. npm)
Install via:
(Mac) 
>brew install node@20
(Linux)
Use the NodeSource setup script
(Windows)
>winget install OpenJS.NodeJS.LTS

Project Node modules (fetch frontend dependencies once)
>cd frontend && npm install

Auto-start shell script
(Mac/Linux)
See section below.
(Windows)
Same command, but must run from either Git Bash or WSL.

# Start the app yourself (after installing required dependencies)
1. Clone the GitHub repository onto your machine.
2. Open the repository in your IDE, and start a new terminal session in your IDE.
3. Navigate to the root of the repository
4. Run the following command
    >./start-dev.sh


# MongoDB info

## Product info
Products populate the MongoDB instance via the following format:
*---------*
_id: ObjectId('')
name: "string"
category: "string"
price: Float/Double
stock: Int
*---------*
The following product categories populate the DB:
Groceries, Candy, Snacks, Drinks, Personal Care, Alchohol, Frozen food, Desserts, Deli, Condiments & Spices, Household & Cleaning, Pet Care.

## Cart info

## Account info