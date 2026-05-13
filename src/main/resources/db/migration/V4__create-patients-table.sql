CREATE TABLE patients (
    id UUID PRIMARY KEY,

    user_id UUID NOT NULL UNIQUE,

    cpf VARCHAR(11) NOT NULL UNIQUE,

    phone VARCHAR(20) NOT NULL,

    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_patient_user
        FOREIGN KEY (user_id) REFERENCES users(id)
);