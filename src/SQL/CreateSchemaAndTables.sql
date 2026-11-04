CREATE SCHEMA `codecvbackend` ;
use codecvbackend;
CREATE TABLE roles (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(255),
    role_description VARCHAR(255)
);

CREATE TABLE employees (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    role_id BIGINT NOT NULL,
    CONSTRAINT fk_employees_role
       FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE shifts (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    department VARCHAR(255),
    start_time DATETIME,
    end_time DATETIME,
    amount_of_employees_needed BIGINT
);

CREATE TABLE employees_on_shifts (
    employee_id BIGINT NOT NULL,
    shift_id BIGINT NOT NULL,
    PRIMARY KEY (employee_id, shift_id),
    CONSTRAINT fk_eos_employee
     FOREIGN KEY (employee_id) REFERENCES employees(id),
    CONSTRAINT fk_eos_shift
     FOREIGN KEY (shift_id) REFERENCES shifts(id)
);