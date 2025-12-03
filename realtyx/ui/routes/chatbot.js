const express = require('express');
const router = express.Router();
const axios = require('axios');

const BACKEND_URL = process.env.BACKEND_URL || 'http://localhost:8080';

// Chatbot endpoint
router.post('/chat', async (req, res) => {
  try {
    const { message } = req.body;
    
    if (!message) {
      return res.json({ 
        response: "Hello! I'm RealtyX assistant. How can I help you find your dream property today?",
        suggestions: [
          "Show me apartments in Mumbai",
          "I need a 3 BHK under 1 crore",
          "Show properties near good schools",
          "What's the most expensive property?"
        ]
      });
    }
    
    // Fetch all data
    const [propertiesRes, brokersRes] = await Promise.all([
      axios.get(`${BACKEND_URL}/api/properties`),
      axios.get(`${BACKEND_URL}/api/brokers`).catch(() => ({ data: [] }))
    ]);
    
    const properties = propertiesRes.data || [];
    const brokers = brokersRes.data || [];
    
    // Simple NLP-like intent detection
    const messageLower = message.toLowerCase();
    let response = '';
    let propertyResults = [];
    let suggestions = [];
    
    // Extract intent and entities
    const intents = {
      greeting: /^(hi|hello|hey|good morning|good evening)/i,
      propertySearch: /(show|find|search|looking for|need|want)/i,
      priceQuery: /(price|cost|expensive|cheap|budget|affordable)/i,
      locationQuery: /(in|at|near|location|city|area)/i,
      bedroomQuery: /(\d+)\s*(bhk|bedroom|bed)/i,
      propertyType: /(apartment|villa|penthouse|commercial|plot|house)/i,
      brokerQuery: /(broker|agent|realtor)/i,
      ratingQuery: /(rating|review|rated)/i,
      featuredQuery: /(featured|best|top|popular)/i
    };
    
    // Greeting
    if (intents.greeting.test(messageLower)) {
      response = "Hello! I'm your RealtyX assistant. I can help you find properties based on location, price, bedrooms, property type, and more. What are you looking for?";
      suggestions = [
        "Show me apartments in Mumbai",
        "Properties under 50 lakhs",
        "3 BHK apartments",
        "Featured properties"
      ];
    }
    // Featured properties query
    else if (intents.featuredQuery.test(messageLower)) {
      propertyResults = properties.filter(p => p.featured);
      response = `Here are our ${propertyResults.length} featured properties with top ratings!`;
      suggestions = ["Show me more details", "Filter by location", "What's the price range?"];
    }
    // Property type query
    else if (intents.propertyType.test(messageLower)) {
      const typeMatch = messageLower.match(intents.propertyType);
      const propertyType = typeMatch[1].charAt(0).toUpperCase() + typeMatch[1].slice(1);
      propertyResults = properties.filter(p => 
        p.propertyType && p.propertyType.toLowerCase().includes(typeMatch[1])
      );
      response = `Found ${propertyResults.length} ${propertyType} properties.`;
      suggestions = [`Filter by price`, `Show ${propertyType}s in specific city`, `What amenities are available?`];
    }
    // Bedroom query
    else if (intents.bedroomQuery.test(messageLower)) {
      const bedroomMatch = messageLower.match(intents.bedroomQuery);
      const bedrooms = parseInt(bedroomMatch[1]);
      propertyResults = properties.filter(p => p.bedrooms === bedrooms);
      response = `Found ${propertyResults.length} properties with ${bedrooms} bedrooms.`;
      suggestions = [`Filter by city`, `Show price range`, `What's the average price?`];
    }
    // Location query
    else if (intents.locationQuery.test(messageLower)) {
      const cities = ['mumbai', 'pune', 'bangalore', 'delhi', 'hyderabad', 'chennai'];
      const foundCity = cities.find(city => messageLower.includes(city));
      
      if (foundCity) {
        propertyResults = properties.filter(p => 
          p.address && p.address.city && p.address.city.toLowerCase() === foundCity
        );
        response = `Found ${propertyResults.length} properties in ${foundCity.charAt(0).toUpperCase() + foundCity.slice(1)}.`;
        suggestions = [`Filter by price`, `Show 2 BHK only`, `What property types are available?`];
      } else {
        const availableCities = [...new Set(properties.map(p => p.address?.city).filter(Boolean))];
        response = `I can help you find properties in these cities: ${availableCities.join(', ')}. Which city interests you?`;
        suggestions = availableCities.slice(0, 3);
      }
    }
    // Price query
    else if (intents.priceQuery.test(messageLower)) {
      // Extract price if mentioned
      const priceMatch = messageLower.match(/(\d+)\s*(lakh|crore|lac)/i);
      
      if (priceMatch) {
        const amount = parseFloat(priceMatch[1]);
        const unit = priceMatch[2].toLowerCase();
        const priceInRupees = unit.includes('crore') ? amount * 10000000 : amount * 100000;
        
        if (messageLower.includes('under') || messageLower.includes('below') || messageLower.includes('less')) {
          propertyResults = properties.filter(p => p.price <= priceInRupees);
          response = `Found ${propertyResults.length} properties under ₹${amount} ${unit}.`;
        } else if (messageLower.includes('above') || messageLower.includes('more') || messageLower.includes('over')) {
          propertyResults = properties.filter(p => p.price >= priceInRupees);
          response = `Found ${propertyResults.length} properties above ₹${amount} ${unit}.`;
        } else {
          propertyResults = properties.filter(p => Math.abs(p.price - priceInRupees) < priceInRupees * 0.2);
          response = `Found ${propertyResults.length} properties around ₹${amount} ${unit}.`;
        }
        suggestions = [`Filter by location`, `Show property details`, `What amenities are included?`];
      } else {
        const prices = properties.map(p => p.price).filter(p => p > 0);
        const avgPrice = prices.reduce((a, b) => a + b, 0) / prices.length;
        const minPrice = Math.min(...prices);
        const maxPrice = Math.max(...prices);
        
        response = `Our properties range from ₹${(minPrice / 100000).toFixed(2)} Lacs to ₹${(maxPrice / 100000).toFixed(2)} Lacs. Average price is ₹${(avgPrice / 100000).toFixed(2)} Lacs.`;
        suggestions = [`Show properties under 50 lakhs`, `Show properties above 1 crore`, `Show mid-range properties`];
      }
    }
    // Broker query
    else if (intents.brokerQuery.test(messageLower)) {
      const topBrokers = brokers
        .filter(b => b.isActive && b.rating)
        .sort((a, b) => b.rating - a.rating)
        .slice(0, 5);
      
      response = `We have ${brokers.length} professional brokers. Here are our top-rated agents:\n\n`;
      topBrokers.forEach((broker, i) => {
        response += `${i + 1}. ${broker.name} - ${broker.specialization} (Rating: ${broker.rating || 'N/A'}/5, Experience: ${broker.yearsOfExperience || 0} years)\n`;
      });
      
      suggestions = [`Show properties by top agents`, `Contact an agent`, `What are their specializations?`];
    }
    // Rating query
    else if (intents.ratingQuery.test(messageLower)) {
      const topRatedBrokers = brokers
        .filter(b => b.rating && b.rating >= 4)
        .sort((a, b) => b.rating - a.rating);
      
      response = `We have ${topRatedBrokers.length} highly-rated brokers with 4+ star ratings. `;
      response += `Our brokers specialize in various areas including ${[...new Set(brokers.map(b => b.specialization).filter(Boolean))].join(', ')}.`;
      
      suggestions = [`Show top-rated properties`, `Contact best agents`, `What's the average rating?`];
    }
    // General property search
    else if (intents.propertySearch.test(messageLower)) {
      propertyResults = properties.slice(0, 10);
      response = `I found ${properties.length} properties available. Here are some options for you. You can filter by location, price, bedrooms, or property type.`;
      suggestions = [`Filter by city`, `Show 3 BHK only`, `Properties under 1 crore`, `Featured properties`];
    }
    // Default response
    else {
      response = "I can help you find properties! Try asking me about:\n";
      response += "• Properties in specific cities (Mumbai, Pune, Bangalore, etc.)\n";
      response += "• Properties by price range\n";
      response += "• Properties by bedrooms (1 BHK, 2 BHK, 3 BHK, etc.)\n";
      response += "• Property types (Apartment, Villa, Penthouse, etc.)\n";
      response += "• Top-rated brokers and agents\n";
      response += "• Featured properties";
      
      suggestions = [
        "Show featured properties",
        "Properties in Mumbai",
        "3 BHK apartments",
        "Top-rated agents"
      ];
    }
    
    res.json({ 
      response,
      properties: propertyResults.slice(0, 6), // Limit to 6 results
      totalFound: propertyResults.length,
      suggestions
    });
    
  } catch (error) {
    console.error('Chatbot error:', error);
    res.status(500).json({ 
      response: "I'm having trouble processing your request. Please try again.",
      properties: [],
      suggestions: ["Try again", "Show all properties"]
    });
  }
});

module.exports = router;
