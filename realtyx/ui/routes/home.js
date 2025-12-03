const express = require('express');
const axios = require('axios');
const router = express.Router();

const BACKEND_URL = process.env.BACKEND_URL || 'http://localhost:8080';

router.get('/', async (req, res) => {
  try {
    const page = parseInt(req.query.page) || 1;
    const itemsPerPage = 15;
    
    console.log('Fetching from:', `${BACKEND_URL}/api/properties`);
    // Fetch all properties from backend API (not just featured)
    const response = await axios.get(`${BACKEND_URL}/api/properties`);
    console.log('Received properties:', response.data?.length || 0);
    const allProperties = response.data || [];
    
    // Calculate pagination
    const totalProperties = allProperties.length;
    const totalPages = Math.ceil(totalProperties / itemsPerPage);
    const startIndex = (page - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const paginatedProperties = allProperties.slice(startIndex, endIndex);
    
    // Pass user session and pagination to the view
    res.render('index', { 
      pageTitle: 'Home', 
      properties: paginatedProperties,
      pagination: {
        currentPage: page,
        totalPages: totalPages,
        totalProperties: totalProperties,
        itemsPerPage: itemsPerPage,
        hasNextPage: page < totalPages,
        hasPrevPage: page > 1
      },
      user: req.session && req.session.user ? req.session.user : null
    });
  } catch (error) {
    console.error('Error fetching properties:');
    console.error('Status:', error.response?.status);
    console.error('Data:', error.response?.data);
    console.error('Message:', error.message);
    console.error('Full error:', error);
    // Render with empty properties on error
    res.render('index', { 
      pageTitle: 'Home', 
      properties: [],
      pagination: {
        currentPage: 1,
        totalPages: 0,
        totalProperties: 0,
        itemsPerPage: 15,
        hasNextPage: false,
        hasPrevPage: false
      },
      user: req.session && req.session.user ? req.session.user : null
    });
  }
});

module.exports = router;
