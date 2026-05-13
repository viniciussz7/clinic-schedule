CREATE TABLE doctors (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE,
    specialty_id UUID NOT NULL,
    crm VARCHAR(30) NOT NULL UNIQUE,
    bio VARCHAR(1000),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_doctor_user
        FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_doctor_specialty
        FOREIGN KEY (specialty_id) REFERENCES specialties(id)
)