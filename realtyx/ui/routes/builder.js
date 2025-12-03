const express = require('express');
const router = express.Router();
const axios = require('axios');

const API_URL = 'http://localhost:8080/api';

// Authentication middleware (optional for builder - can be viewed by anyone)
function checkAuth(req, res, next) {
    // Builder profiles can be viewed by anyone, but we'll pass session data if available
    next();
}

// Builder profile page
router.get('/:id', checkAuth, async (req, res) => {
    try {
        const builderId = req.params.id;
        
        // Fetch builder details
        const builderResponse = await axios.get(`${API_URL}/builders/${builderId}`);
        const builder = builderResponse.data;
        
        // Fetch reviews for this builder
        const reviewsResponse = await axios.get(`${API_URL}/reviews/target/${builderId}?targetType=builder`);
        const reviews = reviewsResponse.data;
        
        // Fetch properties by this builder
        const propertiesResponse = await axios.get(`${API_URL}/properties`);
        const allProperties = propertiesResponse.data;
        const builderProperties = allProperties.filter(p => p.builderId == builderId);
        
        res.render('builder-profile', {
            title: `${builder.companyName} - Builder Profile`,
            pageTitle: `${builder.companyName} - Builder Profile`,
            builder: builder,
            reviews: reviews,
            properties: builderProperties,
            user: req.session && req.session.user ? req.session.user : null
        });
    } catch (error) {
        console.error('Error fetching builder details:', error.message);
        res.status(500).send('Error loading builder profile');
    }
});

module.exports = router;
