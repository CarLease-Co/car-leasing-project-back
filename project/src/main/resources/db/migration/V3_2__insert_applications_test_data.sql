INSERT INTO applications (user_id,
                          monthly_income,
                          loan_duration,
                          financial_obligations,
                          loan_amount,
                          car_id,
                          manufacture_date,
                          free_text_explanation,
                          start_date,
                          end_date)
VALUES (1, 5000.00, 60, 1500.00, 20000, 1, 2013, 'Need the loan for a new car', 'PENDING', '2024-01-01', NULL),
       (1, 6000.00, 48, 2000.00, 30000, 2, 2018, 'Upgrading to a larger vehicle', 'APPROVED', '2024-02-01',
        '2029-02-01'),
       (1, 4500.00, 36, 1200.00, 35000, 3, 2021, 'Car loan for daily commute', 'REVIEW_DECLINED', NULL, NULL);
