// server.js
const express = require('express');
const cors = require('cors');
require('dotenv').config();

const app = express();
app.use(cors());
app.use(express.json());

// Routes
app.use('/shops', require('./routes/shops'));
app.use('/menu', require('./routes/menu'));
app.use('/orders', require('./routes/orders'));

app.get('/', (req, res) => {
  res.send('🚀 CampusBites API is working!');
});

app.listen(3000, () => {
  console.log('🚀 Server running on http://localhost:3000');
});
