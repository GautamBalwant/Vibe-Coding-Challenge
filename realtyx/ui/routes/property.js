const express = require('express');
const axios = require('axios');
const router = express.Router();

const BACKEND_URL = process.env.BACKEND_URL || 'http://localhost:8080';

router.get('/:id', async (req, res) => {
  try {
    const propertyId = req.params.id;
    // Fetch property details from backend API
    const response = await axios.get(`${BACKEND_URL}/api/properties/${propertyId}`);
    const property = response.data;
    res.render('property', { pageTitle: property.title || 'Property Details', property });
  } catch (error) {
    console.error('Error fetching property:', error.message);
    res.render('property', { pageTitle: 'Property Details', property: null });
  }
});

module.exports = router;
