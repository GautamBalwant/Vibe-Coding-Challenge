const express = require('express');
const axios = require('axios');
const router = express.Router();

const BACKEND_URL = process.env.BACKEND_URL || 'http://localhost:8080';

// API endpoint for real-time filtering (returns JSON)
router.get('/explore', async (req, res) => {
  try {
    console.log('API /api/explore called with params:', req.query);
    
    // Get filter parameters from query string
    const { search, city, propertyType, minPrice, maxPrice, bedrooms, status, page, limit } = req.query;
    
    // Build query parameters
    const params = new URLSearchParams();
    if (city) params.append('city', city);
    if (propertyType) params.append('propertyType', propertyType);
    if (minPrice) params.append('minPrice', minPrice);
    if (maxPrice) params.append('maxPrice', maxPrice);
    if (bedrooms) params.append('bedrooms', bedrooms);
    if (page) params.append('page', page);
    if (limit) params.append('limit', limit);
    
    // Fetch properties with filters from backend API
    const url = `${BACKEND_URL}/api/properties${params.toString() ? '?' + params.toString() : ''}`;
    console.log('Fetching from backend:', url);
    
    const response = await axios.get(url);
    let properties = response.data || [];
    
    console.log(`Received ${properties.length} properties from backend`);
    
    // Apply text search filter on title, description, and location (client-side)
    if (search) {
      const searchLower = search.toLowerCase();
      properties = properties.filter(p => 
        (p.title && p.title.toLowerCase().includes(searchLower)) ||
        (p.description && p.description.toLowerCase().includes(searchLower)) ||
        (p.address?.city && p.address.city.toLowerCase().includes(searchLower)) ||
        (p.address?.state && p.address.state.toLowerCase().includes(searchLower)) ||
        (p.address?.line && p.address.line.toLowerCase().includes(searchLower)) ||
        (p.propertyType && p.propertyType.toLowerCase().includes(searchLower))
      );
      console.log(`After search filter: ${properties.length} properties`);
    }
    
    // Apply status filter (client-side)
    if (status) {
      properties = properties.filter(p => p.status === status);
      console.log(`After status filter: ${properties.length} properties`);
    }
    
    // Return JSON response
    res.json({
      success: true,
      properties,
      count: properties.length
    });
  } catch (error) {
    console.error('Error in API /api/explore:', error.message);
    res.status(500).json({
      success: false,
      properties: [],
      count: 0,
      error: error.message
    });
  }
});

module.exports = router;
