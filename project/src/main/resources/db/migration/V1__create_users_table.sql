CREATE TABLE applications (
                              application_id INT AUTO_INCREMENT NOT NULL,
                              applicant_id INT NOT NULL,
                              user_name VARCHAR(255) NOT NULL,
                              user_surname VARCHAR(255) NOT NULL,
                              monthly_income DECIMAL(19,4) NOT NULL,
                              financial_obligations DECIMAL(19,4) NOT NULL,
                              car_id INT NOT NULL,
                              loan_amount DECIMAL(19,4) NOT NULL,
                              loan_duration INT NOT NULL CHECK (loan_duration BETWEEN 1 AND 120),
                              free_text_explanation VARCHAR(255),
                              is_submitted BOOLEAN NOT NULL,
                              status VARCHAR(255) NOT NULL,
                              start_date DATE,
                              end_date DATE,
                              PRIMARY KEY (application_id)
);

INSERT INTO applications (
    applicant_id,
    user_name,
    user_surname,
    monthly_income,
    financial_obligations,
    car_id,
    loan_amount,
    loan_duration,
    free_text_explanation,
    is_submitted,
    status,
    start_date,
    end_date
) VALUES
      (1, 'Jane', 'Doe', 5000.00, 1500.00, 101, 25000.00, 60, 'Need the loan for a new car', TRUE, 'PENDING', '2024-01-01', NULL),
      (2, 'John', 'Smith', 6000.00, 2000.00, 102, 30000.00, 72, 'Upgrading to a larger vehicle', TRUE, 'APPROVED', '2024-02-01', '2029-02-01'),
      (3, 'Emily', 'Jones', 4500.00, 1200.00, 103, 20000.00, 48, 'Car loan for daily commute', FALSE, 'NEW', NULL, NULL);
