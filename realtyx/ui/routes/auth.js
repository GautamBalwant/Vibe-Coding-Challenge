const express = require('express');
const router = express.Router();
const axios = require('axios');
const bcrypt = require('bcryptjs');

const API_URL = 'http://localhost:8080/api';

// Login page - unified for all roles including admin
router.get('/login', (req, res) => {
    const role = req.query.role || 'buyer'; // Default to buyer
    res.render('auth/login', {
        title: 'Login - RealtyX',
        role: role,
        error: req.query.error,
        query: req.query,
        layout: false
    });
});

// Signup pages - role specific
router.get('/signup/buyer', (req, res) => {
    res.render('auth/signup-buyer', {
        title: 'Buyer Signup - RealtyX',
        error: req.query.error,
        layout: false
    });
});

router.get('/signup/seller', (req, res) => {
    res.render('auth/signup-seller', {
        title: 'Seller Signup - RealtyX',
        error: req.query.error,
        layout: false
    });
});

router.get('/signup/agent', (req, res) => {
    res.render('auth/signup-agent', {
        title: 'Agent Signup - RealtyX',
        error: req.query.error,
        layout: false
    });
});

// Login POST handler
router.post('/login', async (req, res) => {
    try {
        const { email, password, role } = req.body;

        console.log('=== LOGIN ATTEMPT ===');
        console.log('Email:', email);
        console.log('Role:', role);
        console.log('Body:', req.body);

        if (!email || !password || !role) {
            console.log('Missing required fields');
            return res.redirect('/auth/login?error=Please fill in all fields');
        }

        let apiEndpoint = '';
        let redirectUrl = '';
        
        // Determine API endpoint based on role
        switch(role) {
            case 'buyer':
                apiEndpoint = `${API_URL}/buyers`;
                break;
            case 'seller':
                apiEndpoint = `${API_URL}/sellers`;
                break;
            case 'agent':
                apiEndpoint = `${API_URL}/agents`;
                break;
            case 'admin':
                apiEndpoint = `${API_URL}/admin-users`;
                break;
            default:
                console.log('Invalid role:', role);
                return res.redirect('/auth/login?error=Invalid role');
        }

        console.log('Fetching from:', apiEndpoint);

        // Fetch all users of this role
        const response = await axios.get(apiEndpoint);
        const users = response.data;

        console.log(`Found ${users.length} users for role ${role}`);

        // Find user - admin uses email, others use email
        let user;
        if (role === 'admin') {
            user = users.find(u => u.email === email || u.username === email);
        } else {
            user = users.find(u => u.email === email);
        }

        if (!user) {
            console.log('User not found with email:', email);
            return res.redirect(`/auth/login?role=${role}&error=Invalid email or password`);
        }

        console.log('User found:', user.name || user.fullName, user.email);

        // Check if user is active
        if (!user.isActive) {
            console.log('User account is inactive');
            return res.redirect(`/auth/login?role=${role}&error=Account is inactive`);
        }

        // Verify password - admin uses passwordHash field, others use password field
        console.log('Verifying password...');
        const passwordField = role === 'admin' ? user.passwordHash : user.password;
        const passwordMatch = await bcrypt.compare(password, passwordField);

        console.log('Password match result:', passwordMatch);

        if (!passwordMatch) {
            console.log('Password does not match');
            return res.redirect(`/auth/login?role=${role}&error=Invalid email or password`);
        }

        console.log('Login successful for:', user.email);

        // Set session
        req.session.user = {
            id: user.id,
            name: user.name || user.fullName,
            email: user.email,
            role: role,
            profileImage: user.profileImage || `https://ui-avatars.com/api/?name=${encodeURIComponent(user.name || user.fullName)}&background=667eea&color=fff`
        };

        console.log('Session created:', req.session.user);

        // Redirect to appropriate dashboard
        switch(role) {
            case 'buyer':
                redirectUrl = `/buyer/dashboard`;
                break;
            case 'seller':
                redirectUrl = `/seller/dashboard`;
                break;
            case 'agent':
                redirectUrl = `/agent/${user.id}`;
                break;
            case 'admin':
                redirectUrl = `/admin/dashboard`;
                break;
        }

        console.log('Redirecting to:', redirectUrl);
        res.redirect(redirectUrl);

    } catch (error) {
        console.error('=== LOGIN ERROR ===');
        console.error('Error message:', error.message);
        console.error('Error stack:', error.stack);
        if (error.response) {
            console.error('API Response status:', error.response.status);
            console.error('API Response data:', error.response.data);
        }
        res.redirect(`/auth/login?role=${req.body.role || 'buyer'}&error=Login failed. Please try again.`);
    }
});

// Buyer Signup POST handler
router.post('/signup/buyer', async (req, res) => {
    try {
        const { name, email, phone, password, confirmPassword, preferredCities, propertyTypes, minBedrooms, maxBedrooms, minPrice, maxPrice } = req.body;

        // Validate passwords match
        if (password !== confirmPassword) {
            return res.redirect('/auth/signup/buyer?error=Passwords do not match');
        }

        // Check if email already exists
        const response = await axios.get(`${API_URL}/buyers`);
        const existingBuyer = response.data.find(b => b.email === email);

        if (existingBuyer) {
            return res.redirect('/auth/signup/buyer?error=Email already registered');
        }

        // Hash password
        const passwordHash = await bcrypt.hash(password, 10);

        // Create buyer object
        const newBuyer = {
            name: name,
            email: email,
            phone: phone,
            password: passwordHash,
            profileImage: `https://i.pravatar.cc/150?u=${email}`,
            isActive: true,
            registeredDate: new Date().toISOString().split('T')[0],
            preferences: {
                preferredCities: preferredCities ? preferredCities.split(',').map(c => c.trim()) : [],
                propertyTypes: propertyTypes ? propertyTypes.split(',').map(t => t.trim()) : [],
                minBedrooms: parseInt(minBedrooms) || 1,
                maxBedrooms: parseInt(maxBedrooms) || 5,
                minPrice: parseFloat(minPrice) || 0,
                maxPrice: parseFloat(maxPrice) || 100000000,
                minArea: 500,
                maxArea: 5000,
                mustHaveAmenities: [],
                moveInDate: ""
            },
            savedProperties: [],
            viewedProperties: []
        };

        // Create buyer via API
        await axios.post(`${API_URL}/buyers`, newBuyer);

        // Redirect to login
        res.redirect('/auth/login?role=buyer&success=Account created successfully');

    } catch (error) {
        console.error('Buyer signup error:', error.message);
        res.redirect('/auth/signup/buyer?error=Signup failed. Please try again.');
    }
});

// Seller Signup POST handler
router.post('/signup/seller', async (req, res) => {
    try {
        const { name, email, phone, password, confirmPassword } = req.body;

        // Validate passwords match
        if (password !== confirmPassword) {
            return res.redirect('/auth/signup/seller?error=Passwords do not match');
        }

        // Check if email already exists
        const response = await axios.get(`${API_URL}/sellers`);
        const existingSeller = response.data.find(s => s.email === email);

        if (existingSeller) {
            return res.redirect('/auth/signup/seller?error=Email already registered');
        }

        // Hash password
        const passwordHash = await bcrypt.hash(password, 10);

        // Create seller object
        const newSeller = {
            name: name,
            email: email,
            phone: phone,
            password: passwordHash,
            profileImage: `https://i.pravatar.cc/150?u=${email}`,
            isActive: true,
            registeredDate: new Date().toISOString().split('T')[0],
            propertyIds: [],
            totalPropertiesSold: 0,
            activeListings: 0
        };

        // Create seller via API
        await axios.post(`${API_URL}/sellers`, newSeller);

        // Redirect to login
        res.redirect('/auth/login?role=seller&success=Account created successfully');

    } catch (error) {
        console.error('Seller signup error:', error.message);
        res.redirect('/auth/signup/seller?error=Signup failed. Please try again.');
    }
});

// Agent Signup POST handler
router.post('/signup/agent', async (req, res) => {
    try {
        const { name, email, phone, password, confirmPassword, licenseNumber, specialization, yearsOfExperience, bio } = req.body;

        // Validate passwords match
        if (password !== confirmPassword) {
            return res.redirect('/auth/signup/agent?error=Passwords do not match');
        }

        // Check if email already exists
        const response = await axios.get(`${API_URL}/agents`);
        const existingAgent = response.data.find(a => a.email === email);

        if (existingAgent) {
            return res.redirect('/auth/signup/agent?error=Email already registered');
        }

        // Hash password
        const passwordHash = await bcrypt.hash(password, 10);

        // Create agent object
        const newAgent = {
            name: name,
            email: email,
            phone: phone,
            password: passwordHash,
            licenseNumber: licenseNumber,
            specialization: specialization,
            yearsOfExperience: parseInt(yearsOfExperience) || 0,
            rating: 0.0,
            totalSales: 0,
            activeListings: 0,
            isActive: true,
            joinedDate: new Date().toISOString().split('T')[0],
            profileImage: `https://i.pravatar.cc/150?u=${email}`,
            bio: bio || '',
            reviews: []
        };

        // Create agent via API
        await axios.post(`${API_URL}/agents`, newAgent);

        // Redirect to login
        res.redirect('/auth/login?role=agent&success=Account created successfully');

    } catch (error) {
        console.error('Agent signup error:', error.message);
        res.redirect('/auth/signup/agent?error=Signup failed. Please try again.');
    }
});

// Logout handler
router.get('/logout', (req, res) => {
    req.session = null;
    res.redirect('/');
});

module.exports = router;
