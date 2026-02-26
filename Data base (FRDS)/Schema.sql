CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'ACTIVE'
);

CREATE TABLE transactions (
    transaction_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    category VARCHAR(100) NOT NULL,
    transaction_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fraud_probability DOUBLE PRECISION DEFAULT 0.0,
    anomaly_score DOUBLE PRECISION DEFAULT 0.0,
    risk_score DOUBLE PRECISION DEFAULT 0.0,
    status VARCHAR(30) DEFAULT 'PENDING',
    CONSTRAINT fk_user FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
);

CREATE TABLE alerts (
    alert_id BIGSERIAL PRIMARY KEY,
    transaction_id BIGINT NOT NULL,
    alert_type VARCHAR(50) NOT NULL,
    status VARCHAR(20) DEFAULT 'UNREAD',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transaction FOREIGN KEY (transaction_id)
        REFERENCES transactions(transaction_id)
        ON DELETE CASCADE
);

CREATE TABLE model_performance (
    model_id BIGSERIAL PRIMARY KEY,
    accuracy DOUBLE PRECISION NOT NULL,
    "precision" DOUBLE PRECISION NOT NULL,
    recall DOUBLE PRECISION NOT NULL,
    f1_score DOUBLE PRECISION NOT NULL,
    roc_auc DOUBLE PRECISION NOT NULL,
    training_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);