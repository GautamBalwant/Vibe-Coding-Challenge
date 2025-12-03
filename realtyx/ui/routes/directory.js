const express = require('express');
const router = express.Router();
const axios = require('axios');

const API_URL = 'http://localhost:8080/api';

// Directory page - Search for builders, agents, sellers
router.get('/', async (req, res) => {
    try {
        const searchQuery = req.query.search || '';
        const roleFilter = req.query.role || 'all'; // all, builder, agent, seller
        const cityFilter = req.query.city || '';
        const specializationFilter = req.query.specialization || '';

        // Fetch all data
        const [buildersRes, agentsRes, sellersRes] = await Promise.all([
            axios.get(`${API_URL}/builders`),
            axios.get(`${API_URL}/agents`),
            axios.get(`${API_URL}/sellers`)
        ]);

        let builders = buildersRes.data || [];
        let agents = agentsRes.data || [];
        let sellers = sellersRes.data || [];

        // Apply filters
        if (searchQuery) {
            const query = searchQuery.toLowerCase();
            builders = builders.filter(b => 
                b.companyName.toLowerCase().includes(query) ||
                b.name.toLowerCase().includes(query) ||
                (b.description && b.description.toLowerCase().includes(query))
            );
            agents = agents.filter(a => 
                a.name.toLowerCase().includes(query) ||
                a.email.toLowerCase().includes(query) ||
                (a.specialization && a.specialization.toLowerCase().includes(query)) ||
                (a.bio && a.bio.toLowerCase().includes(query))
            );
            sellers = sellers.filter(s => 
                s.name.toLowerCase().includes(query) ||
                s.email.toLowerCase().includes(query)
            );
        }

        // Filter by specialization (for agents)
        if (specializationFilter && roleFilter === 'agent') {
            agents = agents.filter(a => 
                a.specialization && a.specialization.toLowerCase().includes(specializationFilter.toLowerCase())
            );
        }

        // Filter by active status
        builders = builders.filter(b => b.isActive);
        agents = agents.filter(a => a.isActive);
        sellers = sellers.filter(s => s.isActive);

        // Prepare results based on role filter
        let results = [];
        if (roleFilter === 'all' || roleFilter === 'builder') {
            results.push(...builders.map(b => ({ ...b, type: 'builder' })));
        }
        if (roleFilter === 'all' || roleFilter === 'agent') {
            results.push(...agents.map(a => ({ ...a, type: 'agent' })));
        }
        if (roleFilter === 'all' || roleFilter === 'seller') {
            results.push(...sellers.map(s => ({ ...s, type: 'seller' })));
        }

        res.render('directory', {
            title: 'Directory',
            pageTitle: 'Directory',
            builders: roleFilter === 'all' || roleFilter === 'builder' ? builders : [],
            agents: roleFilter === 'all' || roleFilter === 'agent' ? agents : [],
            sellers: roleFilter === 'all' || roleFilter === 'seller' ? sellers : [],
            searchQuery,
            roleFilter,
            specializationFilter,
            user: req.session && req.session.user ? req.session.user : null
        });
    } catch (error) {
        console.error('Error fetching directory data:', error.message);
        res.status(500).send('Error loading directory');
    }
});

module.exports = router;
