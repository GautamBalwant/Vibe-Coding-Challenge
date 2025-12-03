const express = require('express');
const router = express.Router();
const axios = require('axios');

const API_URL = 'http://localhost:8080/api';

// Authentication middleware
function requireAuth(req, res, next) {
    console.log('Seller Dashboard - Session check:', req.session);
    console.log('Seller Dashboard - User in session:', req.session && req.session.user);
    
    if (req.session && req.session.user && req.session.user.role === 'seller') {
        next();
    } else {
        console.log('Seller Dashboard - Auth failed, redirecting to login');
        res.redirect('/auth/login?role=seller&error=Please login first');
    }
}

// Seller dashboard
router.get('/dashboard', requireAuth, async (req, res) => {
    try {
        console.log('Loading seller dashboard for user:', req.session.user);
        
        // Get sellerId from session
        const sellerId = req.session.user.id;
        
        // Fetch seller details
        const sellerResponse = await axios.get(`${API_URL}/sellers/${sellerId}`);
        const seller = sellerResponse.data;
        
        // Fetch all properties
        const propertiesResponse = await axios.get(`${API_URL}/properties`);
        const allProperties = propertiesResponse.data;
        
        // Filter seller's properties
        const sellerProperties = allProperties.filter(p => 
            seller.propertyIds && seller.propertyIds.includes(p.id)
        );
        
        // Fetch inquiries for seller's properties
        const inquiriesResponse = await axios.get(`${API_URL}/inquiries/seller/${sellerId}`);
        const inquiries = inquiriesResponse.data;
        
        // Count new inquiries
        const newInquiriesCount = inquiries.filter(i => i.status === 'new').length;
        
        res.render('seller-dashboard', {
            title: 'Seller Dashboard',
            pageTitle: 'Seller Dashboard',
            seller: seller,
            properties: sellerProperties,
            inquiries: inquiries,
            newInquiriesCount: newInquiriesCount
        });
    } catch (error) {
        console.error('Error fetching seller dashboard:', error.message);
        res.status(500).send('Error loading seller dashboard');
    }
});

module.exports = router;
