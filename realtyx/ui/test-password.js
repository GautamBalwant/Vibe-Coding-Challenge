const bcrypt = require('bcryptjs');

// Test the password hash
const password = 'password123';
const hash = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy';

console.log('Testing password:', password);
console.log('Against hash:', hash);

bcrypt.compare(password, hash).then(result => {
    console.log('Password match result:', result);
    if (result) {
        console.log('✅ Password is correct!');
    } else {
        console.log('❌ Password does not match!');
    }
}).catch(err => {
    console.error('Error:', err);
});
