const express = require('express');
const router = express.Router();
const axios = require('axios');

const API_URL = 'http://localhost:8080/api';

// Authentication middleware
function requireAuth(req, res, next) {
    console.log('Buyer Dashboard - Session check:', req.session);
    console.log('Buyer Dashboard - User in session:', req.session && req.session.user);
    
    if (req.session && req.session.user && req.session.user.role === 'buyer') {
        next();
    } else {
        console.log('Buyer Dashboard - Auth failed, redirecting to login');
        res.redirect('/auth/login?role=buyer&error=Please login first');
    }
}

// Buyer dashboard
router.get('/dashboard', requireAuth, async (req, res) => {
    try {
        console.log('Loading buyer dashboard for user:', req.session.user);
        
        // Get buyerId from session
        const buyerId = req.session.user.id;
        
        // Fetch buyer details
        const buyerResponse = await axios.get(`${API_URL}/buyers/${buyerId}`);
        const buyer = buyerResponse.data;
        
        // Fetch all properties
        const propertiesResponse = await axios.get(`${API_URL}/properties`);
        const allProperties = propertiesResponse.data;
        
        // Filter saved properties
        const savedProperties = allProperties.filter(p => 
            buyer.savedProperties && buyer.savedProperties.includes(p.id)
        );
        
        // Filter viewed properties
        const viewedProperties = allProperties.filter(p => 
            buyer.viewedProperties && buyer.viewedProperties.includes(p.id)
        );
        
        // Match properties based on preferences
        let recommendedProperties = allProperties;
        if (buyer.preferences) {
            const prefs = buyer.preferences;
            recommendedProperties = allProperties.filter(p => {
                let matches = true;
                
                // City match
                if (prefs.preferredCities && prefs.preferredCities.length > 0) {
                    matches = matches && prefs.preferredCities.some(city => 
                        p.address && p.address.city && p.address.city.toLowerCase().includes(city.toLowerCase())
                    );
                }
                
                // Property type match
                if (prefs.propertyTypes && prefs.propertyTypes.length > 0) {
                    matches = matches && prefs.propertyTypes.includes(p.propertyType);
                }
                
                // Bedrooms match
                if (prefs.minBedrooms) {
                    matches = matches && p.bedrooms >= prefs.minBedrooms;
                }
                if (prefs.maxBedrooms) {
                    matches = matches && p.bedrooms <= prefs.maxBedrooms;
                }
                
                // Price match
                if (prefs.minPrice) {
                    matches = matches && p.price >= prefs.minPrice;
                }
                if (prefs.maxPrice) {
                    matches = matches && p.price <= prefs.maxPrice;
                }
                
                // Area match
                if (prefs.minArea) {
                    matches = matches && p.areaSqft >= prefs.minArea;
                }
                if (prefs.maxArea) {
                    matches = matches && p.areaSqft <= prefs.maxArea;
                }
                
                return matches;
            });
        }
        
        // Fetch inquiries by this buyer
        const inquiriesResponse = await axios.get(`${API_URL}/inquiries/buyer/${buyerId}`);
        const inquiries = inquiriesResponse.data;
        
        res.render('buyer-dashboard', {
            title: 'Buyer Dashboard',
            pageTitle: 'Buyer Dashboard',
            buyer: buyer,
            savedProperties: savedProperties,
            viewedProperties: viewedProperties,
            recommendedProperties: recommendedProperties.slice(0, 20), // Limit to 20
            inquiries: inquiries
        });
    } catch (error) {
        console.error('Error fetching buyer dashboard:', error.message);
        res.status(500).send('Error loading buyer dashboard');
    }
});

module.exports = router;
