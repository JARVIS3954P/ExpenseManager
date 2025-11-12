-- V1__Initial_Schema.sql

-- Create the User table
CREATE TABLE "user" (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    role VARCHAR(50)
);

-- Create the Expense table
CREATE TABLE expense (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    amount NUMERIC(19, 2),
    expense_date DATE,
    category VARCHAR(50),
    status VARCHAR(50),
    attachment_url VARCHAR(255),
    user_id BIGINT,
    CONSTRAINT fk_expense_user FOREIGN KEY (user_id) REFERENCES "user"(id)
);

-- Create the Approval table
CREATE TABLE approval (
    id BIGSERIAL PRIMARY KEY,
    approved BOOLEAN,
    rejection_reason VARCHAR(255),
    approved_at TIMESTAMP,
    remarks VARCHAR(255),
    approved_by_id BIGINT,
    expense_id BIGINT UNIQUE,
    CONSTRAINT fk_approval_user FOREIGN KEY (approved_by_id) REFERENCES "user"(id),
    CONSTRAINT fk_approval_expense FOREIGN KEY (expense_id) REFERENCES expense(id)
);