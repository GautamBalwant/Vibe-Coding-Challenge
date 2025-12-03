const express = require('express');
const router = express.Router();
const axios = require('axios');
const PDFDocument = require('pdfkit');
const ExcelJS = require('exceljs');

const BACKEND_URL = process.env.BACKEND_URL || 'http://localhost:8080';

// Admin dashboard
router.get('/dashboard', async (req, res) => {
  try {
    // Check if user is logged in and is admin
    if (!req.session.user || req.session.user.role !== 'admin') {
      return res.redirect('/auth/login?role=admin&error=Please login as admin');
    }
    
    const adminUser = req.session.user;
    
    // Fetch properties and brokers data
    const [propertiesRes, brokersRes] = await Promise.all([
      axios.get(`${BACKEND_URL}/api/properties`),
      axios.get(`${BACKEND_URL}/api/brokers`).catch(() => ({ data: [] }))
    ]);
    
    res.render('admin/dashboard', { 
      pageTitle: 'Admin Dashboard',
      adminUser,
      properties: propertiesRes.data || [],
      brokers: brokersRes.data || [],
      stats: {
        totalProperties: propertiesRes.data?.length || 0,
        activeProperties: propertiesRes.data?.filter(p => p.status === 'Active').length || 0,
        totalBrokers: brokersRes.data?.length || 0
      }
    });
  } catch (error) {
    console.error('Dashboard error:', error);
    res.render('admin/dashboard', { 
      pageTitle: 'Admin Dashboard',
      adminUser: req.session.user,
      properties: [],
      brokers: [],
      stats: { totalProperties: 0, activeProperties: 0, totalBrokers: 0 },
      error: 'Failed to load dashboard data'
    });
  }
});

// Add Property - GET form
router.get('/property/add', (req, res) => {
  if (!req.session.user || req.session.user.role !== 'admin') {
    return res.redirect('/auth/login?role=admin&error=Please login as admin');
  }
  const adminUser = req.session.user;
  res.render('admin/add-property', { 
    pageTitle: 'Add Property',
    adminUser,
    error: null,
    success: null
  });
});

// Add Property - POST
router.post('/property/add', async (req, res) => {
  if (!req.session.user || req.session.user.role !== 'admin') {
    return res.redirect('/auth/login?role=admin&error=Please login as admin');
  }
  const adminUser = req.session.user;
  
  try {
    const propertyData = {
      id: Date.now(), // Simple ID generation
      title: req.body.title,
      description: req.body.description,
      propertyType: req.body.propertyType,
      price: parseFloat(req.body.price),
      currency: 'INR',
      bedrooms: parseInt(req.body.bedrooms),
      bathrooms: parseInt(req.body.bathrooms),
      areaSqft: parseInt(req.body.areaSqft),
      status: req.body.status || 'Active',
      featured: req.body.featured === 'on',
      address: {
        line: req.body.address,
        city: req.body.city,
        state: req.body.state,
        pincode: req.body.pincode,
        country: 'India'
      },
      amenities: req.body.amenities ? req.body.amenities.split(',').map(a => a.trim()) : [],
      brokerId: req.body.brokerId ? parseInt(req.body.brokerId) : null,
      media: []
    };
    
    // Save to backend
    await axios.post(`${BACKEND_URL}/api/properties`, propertyData);
    
    res.render('admin/add-property', { 
      pageTitle: 'Add Property',
      adminUser,
      error: null,
      success: 'Property added successfully!'
    });
  } catch (error) {
    console.error('Add property error:', error);
    res.render('admin/add-property', { 
      pageTitle: 'Add Property',
      adminUser,
      error: 'Failed to add property: ' + error.message,
      success: null
    });
  }
});

// Add Broker - GET form
router.get('/broker/add', (req, res) => {
  const adminUser = req.cookies.admin_user;
  if (!adminUser) {
    return res.redirect('/admin/login');
  }
  res.render('admin/add-broker', { 
    pageTitle: 'Add Broker',
    adminUser,
    error: null,
    success: null
  });
});

// Add Broker - POST
router.post('/broker/add', async (req, res) => {
  const adminUser = req.cookies.admin_user;
  if (!adminUser) {
    return res.redirect('/admin/login');
  }
  
  try {
    const brokerData = {
      id: Date.now(), // Simple ID generation
      name: req.body.name,
      email: req.body.email,
      phone: req.body.phone,
      licenseNumber: req.body.licenseNumber,
      specialization: req.body.specialization,
      yearsOfExperience: parseInt(req.body.yearsOfExperience) || 0,
      rating: parseFloat(req.body.rating) || 0,
      totalSales: 0,
      isActive: true,
      joinedDate: new Date().toISOString(),
      bio: req.body.bio || ''
    };
    
    // Save to backend
    await axios.post(`${BACKEND_URL}/api/brokers`, brokerData);
    
    res.render('admin/add-broker', { 
      pageTitle: 'Add Broker',
      adminUser,
      error: null,
      success: 'Broker added successfully!'
    });
  } catch (error) {
    console.error('Add broker error:', error);
    res.render('admin/add-broker', { 
      pageTitle: 'Add Broker',
      adminUser,
      error: 'Failed to add broker: ' + error.message,
      success: null
    });
  }
});

// Reports page
router.get('/reports', async (req, res) => {
  const adminUser = req.cookies.admin_user;
  if (!adminUser) {
    return res.redirect('/admin/login');
  }
  
  try {
    // Fetch properties and brokers data
    const [propertiesRes, brokersRes] = await Promise.all([
      axios.get(`${BACKEND_URL}/api/properties`),
      axios.get(`${BACKEND_URL}/api/brokers`).catch(() => ({ data: [] }))
    ]);
    
    const properties = propertiesRes.data || [];
    const brokers = brokersRes.data || [];
    
    // Calculate statistics
    const totalProperties = properties.length;
    const activeProperties = properties.filter(p => p.status === 'Active').length;
    const underConstructionProperties = properties.filter(p => p.status === 'Under Construction').length;
    const soldProperties = properties.filter(p => p.status === 'Sold').length;
    const featuredProperties = properties.filter(p => p.featured).length;
    
    // Property type breakdown
    const propertyTypes = {};
    properties.forEach(p => {
      propertyTypes[p.propertyType] = (propertyTypes[p.propertyType] || 0) + 1;
    });
    
    // City breakdown
    const cities = {};
    properties.forEach(p => {
      if (p.address && p.address.city) {
        cities[p.address.city] = (cities[p.address.city] || 0) + 1;
      }
    });
    
    // Price statistics
    const prices = properties.map(p => p.price).filter(p => p > 0);
    const totalValue = prices.reduce((sum, price) => sum + price, 0);
    const avgPrice = prices.length > 0 ? totalValue / prices.length : 0;
    const maxPrice = prices.length > 0 ? Math.max(...prices) : 0;
    const minPrice = prices.length > 0 ? Math.min(...prices) : 0;
    
    // Broker statistics
    const totalBrokers = brokers.length;
    const activeBrokers = brokers.filter(b => b.isActive).length;
    
    res.render('admin/reports', { 
      pageTitle: 'Reports & Analytics',
      adminUser,
      stats: {
        totalProperties,
        activeProperties,
        underConstructionProperties,
        soldProperties,
        featuredProperties,
        propertyTypes,
        cities,
        totalValue,
        avgPrice,
        maxPrice,
        minPrice,
        totalBrokers,
        activeBrokers
      },
      properties,
      brokers
    });
  } catch (error) {
    console.error('Reports error:', error);
    res.render('admin/reports', { 
      pageTitle: 'Reports & Analytics',
      adminUser,
      stats: {},
      properties: [],
      brokers: [],
      error: 'Failed to load reports data'
    });
  }
});

// Export Reports to PDF
router.get('/reports/export/pdf', async (req, res) => {
  const adminUser = req.cookies.admin_user;
  if (!adminUser) {
    return res.redirect('/admin/login');
  }
  
  try {
    // Fetch data
    const [propertiesRes, brokersRes] = await Promise.all([
      axios.get(`${BACKEND_URL}/api/properties`),
      axios.get(`${BACKEND_URL}/api/brokers`).catch(() => ({ data: [] }))
    ]);
    
    const properties = propertiesRes.data || [];
    const brokers = brokersRes.data || [];
    
    // Calculate statistics
    const totalProperties = properties.length;
    const activeProperties = properties.filter(p => p.status === 'Active').length;
    const underConstructionProperties = properties.filter(p => p.status === 'Under Construction').length;
    const soldProperties = properties.filter(p => p.status === 'Sold').length;
    
    const prices = properties.map(p => p.price).filter(p => p > 0);
    const totalValue = prices.reduce((sum, price) => sum + price, 0);
    const avgPrice = prices.length > 0 ? totalValue / prices.length : 0;
    
    // Create PDF
    const doc = new PDFDocument({ margin: 50 });
    
    // Set response headers
    res.setHeader('Content-Type', 'application/pdf');
    res.setHeader('Content-Disposition', 'attachment; filename=realtyx-report.pdf');
    
    // Pipe PDF to response
    doc.pipe(res);
    
    // Add content
    doc.fontSize(24).text('RealtyX Reports & Analytics', { align: 'center' });
    doc.moveDown();
    doc.fontSize(12).text(`Generated on: ${new Date().toLocaleDateString()}`, { align: 'center' });
    doc.moveDown(2);
    
    // Property Statistics
    doc.fontSize(16).text('Property Statistics', { underline: true });
    doc.moveDown();
    doc.fontSize(12);
    doc.text(`Total Properties: ${totalProperties}`);
    doc.text(`Active Properties: ${activeProperties}`);
    doc.text(`Under Construction: ${underConstructionProperties}`);
    doc.text(`Sold Properties: ${soldProperties}`);
    doc.moveDown(2);
    
    // Financial Overview
    doc.fontSize(16).text('Financial Overview', { underline: true });
    doc.moveDown();
    doc.fontSize(12);
    doc.text(`Total Portfolio Value: ₹${(totalValue / 10000000).toFixed(2)} Crores`);
    doc.text(`Average Property Price: ₹${(avgPrice / 100000).toFixed(2)} Lacs`);
    doc.moveDown(2);
    
    // Broker Statistics
    doc.fontSize(16).text('Broker Statistics', { underline: true });
    doc.moveDown();
    doc.fontSize(12);
    doc.text(`Total Brokers: ${brokers.length}`);
    doc.text(`Active Brokers: ${brokers.filter(b => b.isActive).length}`);
    doc.moveDown(2);
    
    // Property List
    doc.addPage();
    doc.fontSize(16).text('Property List', { underline: true });
    doc.moveDown();
    doc.fontSize(10);
    
    properties.slice(0, 20).forEach((property, index) => {
      doc.text(`${index + 1}. ${property.title}`);
      doc.text(`   Type: ${property.propertyType} | Price: ₹${(property.price / 100000).toFixed(2)} Lacs | Status: ${property.status}`);
      doc.moveDown(0.5);
    });
    
    // Finalize PDF
    doc.end();
    
  } catch (error) {
    console.error('PDF export error:', error);
    res.status(500).send('Failed to generate PDF report');
  }
});

// Export Reports to Excel
router.get('/reports/export/excel', async (req, res) => {
  const adminUser = req.cookies.admin_user;
  if (!adminUser) {
    return res.redirect('/admin/login');
  }
  
  try {
    // Fetch data
    const [propertiesRes, brokersRes] = await Promise.all([
      axios.get(`${BACKEND_URL}/api/properties`),
      axios.get(`${BACKEND_URL}/api/brokers`).catch(() => ({ data: [] }))
    ]);
    
    const properties = propertiesRes.data || [];
    const brokers = brokersRes.data || [];
    
    // Create workbook
    const workbook = new ExcelJS.Workbook();
    
    // Properties Sheet
    const propertiesSheet = workbook.addWorksheet('Properties');
    propertiesSheet.columns = [
      { header: 'ID', key: 'id', width: 15 },
      { header: 'Title', key: 'title', width: 30 },
      { header: 'Type', key: 'propertyType', width: 15 },
      { header: 'Price (₹)', key: 'price', width: 15 },
      { header: 'Bedrooms', key: 'bedrooms', width: 10 },
      { header: 'Bathrooms', key: 'bathrooms', width: 10 },
      { header: 'Area (sqft)', key: 'areaSqft', width: 12 },
      { header: 'Status', key: 'status', width: 15 },
      { header: 'City', key: 'city', width: 15 }
    ];
    
    properties.forEach(property => {
      propertiesSheet.addRow({
        id: property.id,
        title: property.title,
        propertyType: property.propertyType,
        price: property.price,
        bedrooms: property.bedrooms,
        bathrooms: property.bathrooms,
        areaSqft: property.areaSqft,
        status: property.status,
        city: property.address?.city || 'N/A'
      });
    });
    
    // Style header row
    propertiesSheet.getRow(1).font = { bold: true };
    propertiesSheet.getRow(1).fill = {
      type: 'pattern',
      pattern: 'solid',
      fgColor: { argb: 'FF4472C4' }
    };
    propertiesSheet.getRow(1).font = { bold: true, color: { argb: 'FFFFFFFF' } };
    
    // Brokers Sheet
    const brokersSheet = workbook.addWorksheet('Brokers');
    brokersSheet.columns = [
      { header: 'ID', key: 'id', width: 10 },
      { header: 'Name', key: 'name', width: 25 },
      { header: 'Email', key: 'email', width: 30 },
      { header: 'Phone', key: 'phone', width: 15 },
      { header: 'License Number', key: 'licenseNumber', width: 20 },
      { header: 'Specialization', key: 'specialization', width: 20 },
      { header: 'Experience (years)', key: 'yearsOfExperience', width: 15 },
      { header: 'Rating', key: 'rating', width: 10 },
      { header: 'Active', key: 'isActive', width: 10 }
    ];
    
    brokers.forEach(broker => {
      brokersSheet.addRow({
        id: broker.id,
        name: broker.name,
        email: broker.email,
        phone: broker.phone,
        licenseNumber: broker.licenseNumber,
        specialization: broker.specialization,
        yearsOfExperience: broker.yearsOfExperience,
        rating: broker.rating,
        isActive: broker.isActive ? 'Yes' : 'No'
      });
    });
    
    // Style header row
    brokersSheet.getRow(1).font = { bold: true };
    brokersSheet.getRow(1).fill = {
      type: 'pattern',
      pattern: 'solid',
      fgColor: { argb: 'FF70AD47' }
    };
    brokersSheet.getRow(1).font = { bold: true, color: { argb: 'FFFFFFFF' } };
    
    // Statistics Sheet
    const statsSheet = workbook.addWorksheet('Statistics');
    statsSheet.columns = [
      { header: 'Metric', key: 'metric', width: 30 },
      { header: 'Value', key: 'value', width: 20 }
    ];
    
    const prices = properties.map(p => p.price).filter(p => p > 0);
    const totalValue = prices.reduce((sum, price) => sum + price, 0);
    const avgPrice = prices.length > 0 ? totalValue / prices.length : 0;
    
    statsSheet.addRows([
      { metric: 'Total Properties', value: properties.length },
      { metric: 'Active Properties', value: properties.filter(p => p.status === 'Active').length },
      { metric: 'Under Construction', value: properties.filter(p => p.status === 'Under Construction').length },
      { metric: 'Sold Properties', value: properties.filter(p => p.status === 'Sold').length },
      { metric: 'Total Portfolio Value (₹)', value: totalValue },
      { metric: 'Average Price (₹)', value: Math.round(avgPrice) },
      { metric: 'Total Brokers', value: brokers.length },
      { metric: 'Active Brokers', value: brokers.filter(b => b.isActive).length }
    ]);
    
    statsSheet.getRow(1).font = { bold: true };
    statsSheet.getRow(1).fill = {
      type: 'pattern',
      pattern: 'solid',
      fgColor: { argb: 'FFFFC000' }
    };
    statsSheet.getRow(1).font = { bold: true, color: { argb: 'FF000000' } };
    
    // Set response headers
    res.setHeader('Content-Type', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet');
    res.setHeader('Content-Disposition', 'attachment; filename=realtyx-report.xlsx');
    
    // Write to response
    await workbook.xlsx.write(res);
    res.end();
    
  } catch (error) {
    console.error('Excel export error:', error);
    res.status(500).send('Failed to generate Excel report');
  }
});

// Admin logout
router.get('/logout', (req, res) => {
  res.clearCookie('admin_user');
  res.redirect('/admin/login');
});

module.exports = router;
