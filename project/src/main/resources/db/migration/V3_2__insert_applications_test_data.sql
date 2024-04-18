INSERT INTO applications (user_id,
                          monthly_income,
                          financial_obligations,
                          car_id,
                          manufacture_date,
                          free_text_explanation,
                          is_submitted,
                          status,
                          start_date,
                          end_date)
VALUES (1, 5000.00, 1500.00, 1, 2013, 'Need the loan for a new car', TRUE, 'PENDING', '2024-01-01', NULL),
       (1, 6000.00, 2000.00, 2, 2018, 'Upgrading to a larger vehicle', TRUE, 'APPROVED', '2024-02-01',
        '2029-02-01'),
       (1, 4500.00, 1200.00, 3, 2021, 'Car loan for daily commute', FALSE, 'IN_REVIEW', NULL, NULL);
