import express from "express";
import mongoose from "mongoose";
import cors from "cors";

const app = express();
app.use(cors());
app.use(express.json());

// Connect to MongoDB
mongoose.connect("mongodb://localhost:27017/yourDBname", {
  useNewUrlParser: true,
  useUnifiedTopology: true,
});

// Product Schema
const productSchema = new mongoose.Schema({
  name: String,
  description: String,
  category: String,
  price: Number,
  inStock: Boolean,
});
const Profeduct = mongoose.model("Product", productSchema);

// --- GET /products?search=&category=&minPrice=&maxPrice= ---
app.get("/products", async (req, res) => {
  try {
    const { search, category, minPrice, maxPrice } = req.query;

    const query = {};

    // Search by name or description
    if (search) {
      query.$or = [
        { name: { $regex: search, $options: "i" } },
        { description: { $regex: search, $options: "i" } },
      ];
    }

    // Filter by category
    if (category) query.category = category;

    // Filter by price range
    if (minPrice || maxPrice) {
      query.price = {};
      if (minPrice) query.price.$gte = Number(minPrice);
      if (maxPrice) query.price.$lte = Number(maxPrice);
    }

    const products = await Product.find(query);
    res.json(products);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: "Failed to fetch products" });
  }
});

app.listen(5000, () => console.log("✅ Server running on port 5000"));
