CREATE TABLE IF NOT EXISTS quantity_measurements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    this_value DOUBLE NOT NULL,
    this_unit VARCHAR(50) NOT NULL,
    this_measurement_type VARCHAR(50) NOT NULL,
    that_value DOUBLE,
    that_unit VARCHAR(50),
    that_measurement_type VARCHAR(50),
    operation VARCHAR(20) NOT NULL,
    result_value DOUBLE,
    result_unit VARCHAR(50),
    result_measurement_type VARCHAR(50),
    result_string VARCHAR(255),
    error_message VARCHAR(500),
    is_error BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_quantity_measurements_operation
    ON quantity_measurements(operation);

CREATE INDEX IF NOT EXISTS idx_quantity_measurements_measurement_type
    ON quantity_measurements(this_measurement_type);

CREATE INDEX IF NOT EXISTS idx_quantity_measurements_created_at
    ON quantity_measurements(created_at);

CREATE INDEX IF NOT EXISTS idx_quantity_measurements_is_error
    ON quantity_measurements(is_error);

CREATE TABLE IF NOT EXISTS app_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    full_name VARCHAR(150),
    password_hash VARCHAR(255),
    auth_provider VARCHAR(20) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS app_user_roles (
    user_id BIGINT NOT NULL,
    role_name VARCHAR(50) NOT NULL,
    CONSTRAINT fk_app_user_roles_user FOREIGN KEY (user_id) REFERENCES app_users(id)
);

CREATE INDEX IF NOT EXISTS idx_app_users_username
    ON app_users(username);

CREATE INDEX IF NOT EXISTS idx_app_users_email
    ON app_users(email);
