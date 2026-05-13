INSERT INTO users (
    id,
    name,
    email,
    password,
    role,
    active
)
SELECT
    gen_random_uuid(),
    'System Admin',
    'admin@clinic.com',
    '$2a$12$xHeq7Y.GMIgMqVWfQ8H97.d1AqSUjXL0EDSm.i9EKhX4N08AvsdVu',
    'ADMIN',
    true
    WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'admin@clinic.com'
);