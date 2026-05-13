CREATE TABLE schedules
(
    id          UUID PRIMARY KEY,
    doctor_id   UUID        NOT NULL,
    day_of_week VARCHAR(20) NOT NULL,
    start_time  TIME        NOT NULL,
    end_time    TIME        NOT NULL,
    active      BOOLEAN     NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_schedule_doctor
        FOREIGN KEY (doctor_id) REFERENCES doctors (id)
);