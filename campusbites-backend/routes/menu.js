// routes/menu.js
const express = require('express');
const router = express.Router();
const db = require('../db');

router.get('/:shop_id', async (req, res) => {
  const { shop_id } = req.params;
  try {
    const [items] = await db.query('SELECT * FROM menu_items WHERE shop_id = ?', [shop_id]);
    res.json(items);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

module.exports = router;
