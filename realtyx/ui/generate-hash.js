const bcrypt = require('bcryptjs');

const password = 'password123';

bcrypt.hash(password, 10).then(hash => {
    console.log('Generated hash for "password123":');
    console.log(hash);
    
    // Verify it works
    bcrypt.compare(password, hash).then(result => {
        console.log('\nVerification:', result ? '✅ Correct' : '❌ Failed');
    });
}).catch(err => {
    console.error('Error:', err);
});
