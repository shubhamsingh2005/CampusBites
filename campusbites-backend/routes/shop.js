// routes/shops.js
const express = require('express');
const router = express.Router();
const db = require('../db');

router.get('/', async (req, res) => {
  try {
    const [shops] = await db.query('SELECT * FROM shops');
    res.json(shops);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

module.exports = router;
