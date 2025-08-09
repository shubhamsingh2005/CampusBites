// db.js
const mysql = require('mysql2');
require('dotenv').config();

const db = mysql.createConnection({
    host: 'localhost',
    user: process.env.DB_USER,
    password: process.env.DB_PASSWORD,
    database: 'campusbites',
});

db.connect((err) => {
    if (err) throw err;
    console.log('✅ MySQL connected');
});

module.exports = db;
