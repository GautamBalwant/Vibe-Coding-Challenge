// Express app entry point for RealtyX UI
const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const session = require('cookie-session');
const helmet = require('helmet');
const expressLayouts = require('express-ejs-layouts');
require('dotenv').config();

const app = express();
const PORT = process.env.PORT || 3001;

// Middleware
app.use(helmet({ contentSecurityPolicy: false }));
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(cookieParser());

// Session middleware - must be after body parsers
app.use(session({
  name: 'realtyx-session',
  keys: ['realtyx-secret-key-2025'],
  maxAge: 24 * 60 * 60 * 1000, // 24 hours
  httpOnly: true,
  sameSite: 'lax'
}));

// Static files
app.use(express.static(path.join(__dirname, 'public')));

// View engine setup
app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views'));
app.use(expressLayouts);
app.set('layout', '_layout');

// Routes
app.use('/', require('./routes/home'));
app.use('/explore', require('./routes/explore'));
app.use('/directory', require('./routes/directory')); // Directory - Search builders, agents, sellers
app.use('/api', require('./routes/api')); // API endpoints for real-time filtering
app.use('/property', require('./routes/property'));
app.use('/admin', require('./routes/admin'));
app.use('/api/chatbot', require('./routes/chatbot'));
app.use('/auth', require('./routes/auth')); // Authentication routes
app.use('/builder', require('./routes/builder'));
app.use('/agent', require('./routes/agent'));
app.use('/buyer', require('./routes/buyer'));
app.use('/seller', require('./routes/seller'));

// 404 handler
app.use((req, res) => {
  res.status(404).send('Page not found');
});

// Error handler
app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(500).send('Something broke!');
});

// Start server
app.listen(PORT, () => {
  console.log(`RealtyX UI running on http://localhost:${PORT}`);
});
