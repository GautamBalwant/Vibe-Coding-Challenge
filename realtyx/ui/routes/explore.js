const express = require('express');
const axios = require('axios');
const router = express.Router();

const BACKEND_URL = process.env.BACKEND_URL || 'http://localhost:8080';

router.get('/', async (req, res) => {
  try {
    // Get filter parameters from query string
    const { search, city, propertyType, minPrice, maxPrice, bedrooms, status, page } = req.query;
    
    // Pagination settings
    const itemsPerPage = 15;
    const currentPage = parseInt(page) || 1;
    
    // Build query parameters
    const params = new URLSearchParams();
    if (city) params.append('city', city);
    if (propertyType) params.append('propertyType', propertyType);
    if (minPrice) params.append('minPrice', minPrice);
    if (maxPrice) params.append('maxPrice', maxPrice);
    if (bedrooms) params.append('bedrooms', bedrooms);
    
    // Fetch properties with filters from backend API
    const url = `${BACKEND_URL}/api/properties${params.toString() ? '?' + params.toString() : ''}`;
    console.log('Fetching properties from:', url);
    const response = await axios.get(url);
    let properties = response.data || [];
    
    // Apply text search filter on title and description (client-side)
    if (search) {
      const searchLower = search.toLowerCase();
      properties = properties.filter(p => 
        (p.title && p.title.toLowerCase().includes(searchLower)) ||
        (p.description && p.description.toLowerCase().includes(searchLower))
      );
    }
    
    // Apply status filter (client-side)
    if (status) {
      properties = properties.filter(p => p.status === status);
    }
    
    // Calculate pagination
    const totalProperties = properties.length;
    const totalPages = Math.ceil(totalProperties / itemsPerPage);
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const paginatedProperties = properties.slice(startIndex, endIndex);
    
    // Get unique values for filter dropdowns
    const allProperties = response.data || [];
    const cities = [...new Set(allProperties.map(p => p.address?.city).filter(Boolean))];
    const propertyTypes = [...new Set(allProperties.map(p => p.propertyType).filter(Boolean))];
    const statuses = [...new Set(allProperties.map(p => p.status).filter(Boolean))];
    
    res.render('explore', { 
      pageTitle: 'Explore Properties', 
      properties: paginatedProperties,
      pagination: {
        currentPage,
        totalPages,
        totalProperties,
        itemsPerPage,
        hasNextPage: currentPage < totalPages,
        hasPrevPage: currentPage > 1
      },
      filters: {
        search: search || '',
        city: city || '',
        propertyType: propertyType || '',
        minPrice: minPrice || '',
        maxPrice: maxPrice || '',
        bedrooms: bedrooms || '',
        status: status || ''
      },
      filterOptions: {
        cities,
        propertyTypes,
        statuses
      }
    });
  } catch (error) {
    console.error('Error fetching properties:', error.message);
    res.render('explore', { 
      pageTitle: 'Explore Properties', 
      properties: [],
      pagination: {
        currentPage: 1,
        totalPages: 0,
        totalProperties: 0,
        itemsPerPage: 15,
        hasNextPage: false,
        hasPrevPage: false
      },
      filters: {},
      filterOptions: { cities: [], propertyTypes: [], statuses: [] }
    });
  }
});

module.exports = router;
