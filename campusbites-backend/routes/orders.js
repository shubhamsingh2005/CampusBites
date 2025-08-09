// routes/orders.js
const express = require('express');
const router = express.Router();
const db = require('../db');

// POST: Place new order
router.post('/', async (req, res) => {
  const { user_id, shop_id, total_amount, status } = req.body;
  try {
    const [result] = await db.query(
      'INSERT INTO orders (user_id, shop_id, total_amount, status) VALUES (?, ?, ?, ?)',
      [user_id, shop_id, total_amount, status]
    );
    res.json({ order_id: result.insertId, message: "Order placed" });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// GET: All orders of a user
router.get('/:user_id', async (req, res) => {
  const { user_id } = req.params;
  try {
    const [orders] = await db.query('SELECT * FROM orders WHERE user_id = ?', [user_id]);
    res.json(orders);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

module.exports = router;
