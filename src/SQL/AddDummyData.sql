use codecvbackend;

INSERT INTO roles (role_name, role_description)
VALUES
    ('admin', 'Administrator with full access'),
    ('employee', 'Regular employee');

INSERT INTO employees (first_name, last_name, email, password, role_id)
VALUES
    ('Alice', 'Morgan', 'alice@example.com', 'password123', 1),
    ('Ben', 'Carter', 'ben@example.com', 'password456', 2),
    ('Chloe', 'Reed', 'chloe@example.com', 'password789', 2);

INSERT INTO shifts (department, start_time, end_time, amount_of_employees_needed)
VALUES
    ('Front Desk', '2026-10-08 08:00:00', '2026-10-08 16:00:00', 2),
    ('Kitchen', '2026-10-08 10:00:00', '2026-10-08 18:00:00', 2),
    ('Front Desk', '2026-10-09 08:00:00', '2026-10-09 16:00:00', 1);

INSERT INTO employees_on_shifts (employee_id, shift_id)
VALUES
    (1, 1),
    (2, 1),
    (2, 2),
    (3, 3);