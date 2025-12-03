const express = require('express');
const router = express.Router();
const axios = require('axios');

const API_URL = 'http://localhost:8080/api';

// Authentication middleware
function checkAuth(req, res, next) {
    // Agent profiles can be viewed by anyone, but we'll pass session data if available
    next();
}

// Agent profile page
router.get('/:id', checkAuth, async (req, res) => {
    try {
        const agentId = req.params.id;
        
        // Fetch agent details
        const agentResponse = await axios.get(`${API_URL}/agents/${agentId}`);
        const agent = agentResponse.data;
        
        // Fetch reviews for this agent
        const reviewsResponse = await axios.get(`${API_URL}/reviews/target/${agentId}?targetType=agent`);
        const reviews = reviewsResponse.data;
        
        // Fetch properties by this agent
        const propertiesResponse = await axios.get(`${API_URL}/properties`);
        const allProperties = propertiesResponse.data;
        const agentProperties = allProperties.filter(p => p.agentId == agentId);
        
        res.render('agent-profile', {
            title: `${agent.name} - Agent Profile`,
            pageTitle: `${agent.name} - Agent Profile`,
            agent: agent,
            reviews: reviews,
            properties: agentProperties,
            user: req.session && req.session.user ? req.session.user : null
        });
    } catch (error) {
        console.error('Error fetching agent details:', error.message);
        res.status(500).send('Error loading agent profile');
    }
});

module.exports = router;
