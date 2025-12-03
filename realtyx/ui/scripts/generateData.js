const fs = require('fs');
const path = require('path');

// Indian cities with their states
const cities = [
  { city: 'Mumbai', state: 'Maharashtra', pincodes: ['400001', '400012', '400050', '400070', '400092'] },
  { city: 'Pune', state: 'Maharashtra', pincodes: ['411001', '411038', '411057', '411014', '411045'] },
  { city: 'Bangalore', state: 'Karnataka', pincodes: ['560001', '560034', '560066', '560100', '560078'] },
  { city: 'Delhi', state: 'Delhi', pincodes: ['110001', '110017', '110085', '110064', '110092'] },
  { city: 'Hyderabad', state: 'Telangana', pincodes: ['500001', '500034', '500081', '500016', '500072'] },
  { city: 'Chennai', state: 'Tamil Nadu', pincodes: ['600001', '600028', '600041', '600096', '600034'] },
  { city: 'Kolkata', state: 'West Bengal', pincodes: ['700001', '700019', '700053', '700091', '700156'] },
  { city: 'Ahmedabad', state: 'Gujarat', pincodes: ['380001', '380015', '380054', '380061', '380013'] },
  { city: 'Surat', state: 'Gujarat', pincodes: ['395001', '395007', '395009', '395017', '394210'] },
  { city: 'Jaipur', state: 'Rajasthan', pincodes: ['302001', '302012', '302017', '302021', '302033'] },
  { city: 'Lucknow', state: 'Uttar Pradesh', pincodes: ['226001', '226010', '226016', '226022', '226028'] },
  { city: 'Kanpur', state: 'Uttar Pradesh', pincodes: ['208001', '208012', '208014', '208022', '208027'] },
  { city: 'Nagpur', state: 'Maharashtra', pincodes: ['440001', '440008', '440013', '440022', '440033'] },
  { city: 'Indore', state: 'Madhya Pradesh', pincodes: ['452001', '452010', '452016', '452018', '453441'] },
  { city: 'Thane', state: 'Maharashtra', pincodes: ['400601', '400604', '400607', '400610', '400615'] },
  { city: 'Bhopal', state: 'Madhya Pradesh', pincodes: ['462001', '462016', '462023', '462039', '462042'] },
  { city: 'Visakhapatnam', state: 'Andhra Pradesh', pincodes: ['530001', '530016', '530022', '530040', '530045'] },
  { city: 'Patna', state: 'Bihar', pincodes: ['800001', '800013', '800020', '800025', '801503'] },
  { city: 'Vadodara', state: 'Gujarat', pincodes: ['390001', '390007', '390015', '390019', '390024'] },
  { city: 'Ghaziabad', state: 'Uttar Pradesh', pincodes: ['201001', '201009', '201012', '201014', '201301'] }
];

const propertyTypes = [
  { type: 'Apartment', weight: 40 },
  { type: 'Villa', weight: 15 },
  { type: 'Penthouse', weight: 8 },
  { type: 'Row House', weight: 12 },
  { type: 'Commercial', weight: 10 },
  { type: 'Plot', weight: 10 },
  { type: 'Studio', weight: 5 }
];

const statuses = [
  { status: 'Active', weight: 70 },
  { status: 'Under Construction', weight: 25 },
  { status: 'Sold', weight: 5 }
];

const amenitiesList = [
  'Parking', 'Swimming Pool', 'Gym', 'Garden', 'Security', 'Power Backup',
  'Elevator', 'Club House', 'Children Play Area', 'Jogging Track',
  'Indoor Games', 'Party Hall', 'Intercom', 'Gas Pipeline', 'Maintenance Staff',
  'Water Supply', 'Rainwater Harvesting', 'Wifi', 'CCTV', 'Gated Community',
  'Sports Facility', 'Multipurpose Hall', 'Visitor Parking', 'Landscaped Gardens',
  'Fire Safety', 'Waste Management', 'Cafeteria', 'Library', 'Shopping Center'
];

const localities = {
  'Mumbai': ['Andheri', 'Bandra', 'Juhu', 'Powai', 'Goregaon', 'Malad', 'Borivali', 'Worli', 'Lower Parel', 'Thane'],
  'Pune': ['Baner', 'Hinjewadi', 'Kharadi', 'Wakad', 'Aundh', 'Viman Nagar', 'Koregaon Park', 'Hadapsar', 'Pimpri', 'Kalyani Nagar'],
  'Bangalore': ['Whitefield', 'Electronic City', 'Marathahalli', 'Koramangala', 'Indiranagar', 'HSR Layout', 'Hebbal', 'Yelahanka', 'JP Nagar', 'Sarjapur'],
  'Delhi': ['Dwarka', 'Rohini', 'Vasant Kunj', 'Saket', 'Greater Kailash', 'Karol Bagh', 'Janakpuri', 'Pitampura', 'Connaught Place', 'Defence Colony'],
  'Hyderabad': ['Gachibowli', 'Hitech City', 'Madhapur', 'Kondapur', 'Banjara Hills', 'Jubilee Hills', 'Miyapur', 'Kukatpally', 'Secunderabad', 'Begumpet'],
  'Chennai': ['Anna Nagar', 'T Nagar', 'Velachery', 'OMR', 'Adyar', 'Mylapore', 'Nungambakkam', 'Guindy', 'Porur', 'Tambaram'],
  'default': ['Sector 1', 'Sector 2', 'Sector 3', 'Civil Lines', 'Model Town', 'Cantonment', 'City Center', 'Ring Road', 'Station Road', 'Park Street']
};

const builderNames = [
  'Prestige Group', 'DLF Limited', 'Godrej Properties', 'Sobha Limited', 'Brigade Group',
  'Mahindra Lifespaces', 'Oberoi Realty', 'Lodha Group', 'Puravankara', 'Shriram Properties',
  'Embassy Group', 'Phoenix Mills', 'Kolte Patil', 'Salarpuria Sattva', 'Mantri Developers',
  'Nitesh Estates', 'Total Environment', 'Provident Housing', 'Assetz Property', 'Century Real Estate'
];

// Property image URLs (using Unsplash for real property images)
const propertyImageCategories = {
  'Apartment': [
    'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267',
    'https://images.unsplash.com/photo-1545324418-cc1a3fa10c00',
    'https://images.unsplash.com/photo-1502672260066-6bc476e4e2ca',
    'https://images.unsplash.com/photo-1512917774080-9991f1c4c750'
  ],
  'Villa': [
    'https://images.unsplash.com/photo-1564013799919-ab600027ffc6',
    'https://images.unsplash.com/photo-1600596542815-ffad4c1539a9',
    'https://images.unsplash.com/photo-1600585154340-be6161a56a0c',
    'https://images.unsplash.com/photo-1600607687939-ce8a6c25118c'
  ],
  'Penthouse': [
    'https://images.unsplash.com/photo-1600607687644-aac4c3eac7f4',
    'https://images.unsplash.com/photo-1600607687920-4e2a09cf159d',
    'https://images.unsplash.com/photo-1600607688960-e095ff83135e',
    'https://images.unsplash.com/photo-1600607688969-a5bfcd646154'
  ],
  'Commercial': [
    'https://images.unsplash.com/photo-1497366216548-37526070297c',
    'https://images.unsplash.com/photo-1497366811353-6870744d04b2',
    'https://images.unsplash.com/photo-1486406146926-c627a92ad1ab',
    'https://images.unsplash.com/photo-1497366754035-f200968a6e72'
  ],
  'default': [
    'https://images.unsplash.com/photo-1560518883-ce09059eeffa',
    'https://images.unsplash.com/photo-1570129477492-45c003edd2be',
    'https://images.unsplash.com/photo-1605146769289-440113cc3d00',
    'https://images.unsplash.com/photo-1582268611958-ebfd161ef9cf'
  ]
};

function getRandomElement(array) {
  return array[Math.floor(Math.random() * array.length)];
}

function getWeightedRandom(items) {
  const totalWeight = items.reduce((sum, item) => sum + item.weight, 0);
  let random = Math.random() * totalWeight;
  
  for (const item of items) {
    random -= item.weight;
    if (random <= 0) {
      return item.type || item.status;
    }
  }
  return items[0].type || items[0].status;
}

function getPropertyImage(propertyType, id) {
  const category = propertyImageCategories[propertyType] || propertyImageCategories['default'];
  const baseUrl = category[id % category.length];
  return `${baseUrl}?w=800&h=600&fit=crop&q=80`;
}

function generateRandomAmenities() {
  const count = Math.floor(Math.random() * 10) + 5; // 5-15 amenities
  const shuffled = [...amenitiesList].sort(() => 0.5 - Math.random());
  return shuffled.slice(0, count);
}

function generateProperty(id, brokerId) {
  const cityData = getRandomElement(cities);
  const propertyType = getWeightedRandom(propertyTypes);
  const status = getWeightedRandom(statuses);
  const locality = getRandomElement(localities[cityData.city] || localities['default']);
  const builder = getRandomElement(builderNames);
  
  // Generate bedrooms based on property type
  let bedrooms, bathrooms, areaSqft, priceRange;
  
  if (propertyType === 'Studio') {
    bedrooms = 1;
    bathrooms = 1;
    areaSqft = Math.floor(Math.random() * 200) + 300; // 300-500
    priceRange = [2000000, 5000000]; // 20-50 lacs
  } else if (propertyType === 'Commercial' || propertyType === 'Plot') {
    bedrooms = 0;
    bathrooms = Math.floor(Math.random() * 3) + 1;
    areaSqft = Math.floor(Math.random() * 5000) + 1000; // 1000-6000
    priceRange = [5000000, 50000000]; // 50 lacs - 5 crores
  } else if (propertyType === 'Penthouse') {
    bedrooms = Math.floor(Math.random() * 2) + 4; // 4-5
    bathrooms = bedrooms;
    areaSqft = Math.floor(Math.random() * 2000) + 3000; // 3000-5000
    priceRange = [20000000, 100000000]; // 2-10 crores
  } else if (propertyType === 'Villa') {
    bedrooms = Math.floor(Math.random() * 2) + 3; // 3-4
    bathrooms = bedrooms;
    areaSqft = Math.floor(Math.random() * 2000) + 2500; // 2500-4500
    priceRange = [15000000, 80000000]; // 1.5-8 crores
  } else { // Apartment, Row House
    bedrooms = Math.floor(Math.random() * 4) + 1; // 1-4
    bathrooms = Math.min(bedrooms, Math.floor(Math.random() * 2) + 1);
    areaSqft = Math.floor(Math.random() * 1500) + 600; // 600-2100
    priceRange = [3000000, 25000000]; // 30 lacs - 2.5 crores
  }
  
  const price = Math.floor(Math.random() * (priceRange[1] - priceRange[0])) + priceRange[0];
  const featured = Math.random() < 0.15; // 15% featured
  
  const propertyNames = [
    `${builder} ${locality} ${propertyType}`,
    `Premium ${bedrooms > 0 ? bedrooms + ' BHK' : ''} ${propertyType} in ${locality}`,
    `Luxury ${propertyType} at ${locality}`,
    `${builder} Elite ${propertyType}`,
    `${locality} Heights ${propertyType}`,
    `${builder} ${locality} Residency`
  ];
  
  return {
    id: id,
    title: getRandomElement(propertyNames),
    description: `Beautiful ${propertyType.toLowerCase()} in ${locality}, ${cityData.city}. This property offers ${bedrooms > 0 ? bedrooms + ' spacious bedrooms' : 'excellent commercial space'} with modern amenities and great connectivity. Developed by ${builder}, known for quality construction and timely delivery. Perfect for ${propertyType === 'Commercial' ? 'business ventures' : 'families looking for a comfortable lifestyle'}. ${status === 'Under Construction' ? 'Ready to move in by 2026.' : 'Ready for immediate possession.'}`,
    propertyType: propertyType,
    price: price,
    currency: 'INR',
    bedrooms: bedrooms,
    bathrooms: bathrooms,
    areaSqft: areaSqft,
    status: status,
    featured: featured,
    address: {
      line: `${Math.floor(Math.random() * 500) + 1}, ${locality}`,
      city: cityData.city,
      state: cityData.state,
      pincode: getRandomElement(cityData.pincodes),
      country: 'India'
    },
    amenities: generateRandomAmenities(),
    brokerId: (brokerId % 50) + 1, // Assign to brokers 1-50
    media: [
      {
        type: 'image',
        url: getPropertyImage(propertyType, id),
        altText: `${propertyType} view ${id}`,
        isPrimary: true
      }
    ],
    builder: builder,
    builderRating: (Math.random() * 2 + 3).toFixed(1), // 3.0-5.0
    agentRating: (Math.random() * 2 + 3).toFixed(1), // 3.0-5.0
    propertyRating: (Math.random() * 2 + 3).toFixed(1), // 3.0-5.0
    createdAt: new Date(Date.now() - Math.floor(Math.random() * 365 * 24 * 60 * 60 * 1000)).toISOString(),
    updatedAt: new Date().toISOString()
  };
}

function generateBrokers(count) {
  const brokers = [];
  const firstNames = ['Rahul', 'Priya', 'Amit', 'Sneha', 'Rajesh', 'Kavita', 'Vikram', 'Anjali', 'Suresh', 'Pooja'];
  const lastNames = ['Sharma', 'Patel', 'Kumar', 'Singh', 'Reddy', 'Verma', 'Mehta', 'Gupta', 'Joshi', 'Nair'];
  const specializations = ['Residential Sales', 'Commercial Sales', 'Rental Properties', 'Luxury Properties', 'Investment Properties'];
  
  for (let i = 1; i <= count; i++) {
    const firstName = getRandomElement(firstNames);
    const lastName = getRandomElement(lastNames);
    const experience = Math.floor(Math.random() * 20) + 1;
    
    brokers.push({
      id: i,
      name: `${firstName} ${lastName}`,
      email: `${firstName.toLowerCase()}.${lastName.toLowerCase()}${i}@realtyx.com`,
      phone: `${Math.floor(Math.random() * 9000000000) + 1000000000}`,
      licenseNumber: `RERA-${getRandomElement(['MH', 'KA', 'DL', 'TN', 'TG'])}-${new Date().getFullYear()}-${String(i).padStart(5, '0')}`,
      specialization: getRandomElement(specializations),
      yearsOfExperience: experience,
      rating: (Math.random() * 2 + 3).toFixed(1), // 3.0-5.0
      totalSales: Math.floor(Math.random() * 200) + 10,
      isActive: Math.random() > 0.1, // 90% active
      joinedDate: new Date(Date.now() - experience * 365 * 24 * 60 * 60 * 1000).toISOString(),
      bio: `${experience}+ years of experience in real estate. Specialized in ${getRandomElement(specializations).toLowerCase()}.`
    });
  }
  
  return brokers;
}

console.log('Generating 10,000 properties and 50 brokers...');

// Generate brokers
const brokers = generateBrokers(50);
console.log(`Generated ${brokers.length} brokers`);

// Generate properties in batches
const properties = [];
const batchSize = 1000;
const totalProperties = 10000;

for (let i = 0; i < totalProperties; i++) {
  properties.push(generateProperty(i + 1, i));
  
  if ((i + 1) % batchSize === 0) {
    console.log(`Generated ${i + 1} properties...`);
  }
}

console.log(`Generated ${properties.length} properties`);

// Write to files
const dataDir = path.join(__dirname, '..', '..', 'data');

fs.writeFileSync(
  path.join(dataDir, 'properties.json'),
  JSON.stringify(properties, null, 2)
);

fs.writeFileSync(
  path.join(dataDir, 'brokers.json'),
  JSON.stringify(brokers, null, 2)
);

console.log('Data generation completed!');
console.log(`- Properties: ${properties.length}`);
console.log(`- Brokers: ${brokers.length}`);
console.log(`- Featured Properties: ${properties.filter(p => p.featured).length}`);
console.log(`- Active Properties: ${properties.filter(p => p.status === 'Active').length}`);
console.log(`- Total Portfolio Value: ₹${(properties.reduce((sum, p) => sum + p.price, 0) / 10000000).toFixed(2)} Crores`);
