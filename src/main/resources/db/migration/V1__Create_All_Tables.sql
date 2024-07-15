-- Create Users table
CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    registration_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create Role table
CREATE TABLE role (
    id UUID PRIMARY KEY,
    name_role VARCHAR(100) NOT NULL
);

-- Create UserRole table (many-to-many relationship between Users and Role)
CREATE TABLE user_role (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE
);

-- Create Category table
CREATE TABLE category (
    id UUID PRIMARY KEY,
    name_category VARCHAR(100) NOT NULL
);

-- Create PaymentService table
CREATE TABLE payment_service (
    id UUID PRIMARY KEY,
    payment_service_name VARCHAR(100) NOT NULL,
    description TEXT
);

-- Create Bill table
CREATE TABLE bill (
    id UUID PRIMARY KEY,
    due_date DATE NOT NULL,
    payment_date DATE,
    amount DECIMAL(10, 2) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL,
    category_id UUID,
    payment_service_id UUID,
    user_id UUID,
    FOREIGN KEY (category_id) REFERENCES category (id) ON DELETE SET NULL,
    FOREIGN KEY (payment_service_id) REFERENCES payment_service (id) ON DELETE SET NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL
);
