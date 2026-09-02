-- ============================================================
-- Library Management System - Database Setup Script
-- Run this entire script in MySQL Workbench or the mysql CLI.
-- ============================================================

CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

-- ------------------------------------------------------------
-- Table: members
-- Stores both Student and Faculty members (member_type distinguishes them).
-- ------------------------------------------------------------
DROP TABLE IF EXISTS issue_records;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS members;

CREATE TABLE members (
    member_id      VARCHAR(10)  NOT NULL,
    name           VARCHAR(100) NOT NULL,
    contact_number VARCHAR(20),
    member_type    VARCHAR(20)  NOT NULL,   -- 'Student' or 'Faculty'
    extra_info     VARCHAR(150),            -- roll no/dept or designation/dept
    PRIMARY KEY (member_id)
);

-- ------------------------------------------------------------
-- Table: books
-- ------------------------------------------------------------
CREATE TABLE books (
    book_id          VARCHAR(10)  NOT NULL,
    title            VARCHAR(150) NOT NULL,
    author           VARCHAR(100) NOT NULL,
    isbn             VARCHAR(30),
    total_copies     INT NOT NULL DEFAULT 1,
    available_copies INT NOT NULL DEFAULT 1,
    PRIMARY KEY (book_id)
);

-- ------------------------------------------------------------
-- Table: issue_records
-- One row per "book issued to a member" transaction.
-- Foreign keys enforce that a record can only reference an
-- existing member and an existing book.
-- ------------------------------------------------------------
CREATE TABLE issue_records (
    issue_id     INT AUTO_INCREMENT NOT NULL,
    member_id    VARCHAR(10) NOT NULL,
    book_id      VARCHAR(10) NOT NULL,
    issue_date   DATE NOT NULL,
    due_date     DATE NOT NULL,
    return_date  DATE NULL,
    fine_amount  DECIMAL(8,2) NOT NULL DEFAULT 0.00,
    status       VARCHAR(20) NOT NULL DEFAULT 'ISSUED',  -- 'ISSUED' or 'RETURNED'
    PRIMARY KEY (issue_id),
    CONSTRAINT fk_issue_member FOREIGN KEY (member_id)
        REFERENCES members(member_id) ON DELETE CASCADE,
    CONSTRAINT fk_issue_book FOREIGN KEY (book_id)
        REFERENCES books(book_id) ON DELETE CASCADE
);

-- ------------------------------------------------------------
-- Sample data
-- ------------------------------------------------------------
INSERT INTO members (member_id, name, contact_number, member_type, extra_info) VALUES
('S1', 'Arun Kumar',   '9876500001', 'Student', 'Roll:CS21B01 Dept:CSE'),
('S2', 'Divya Shree',  '9876500003', 'Student', 'Roll:CS21B02 Dept:CSE'),
('F1', 'Dr. Meena Rao','9876500002', 'Faculty', 'Designation:Professor Dept:CSE'),
('F2', 'Dr. Karthik S','9876500004', 'Faculty', 'Designation:Assoc. Professor Dept:IT');

INSERT INTO books (book_id, title, author, isbn, total_copies, available_copies) VALUES
('B1', 'Introduction to Algorithms', 'Cormen',        '978-0262033848', 2, 2),
('B2', 'Effective Java',             'Joshua Bloch',  '978-0134685991', 1, 1),
('B3', 'Clean Code',                 'Robert Martin', '978-0132350884', 3, 3),
('B4', 'Design Patterns',            'GoF',            '978-0201633610', 2, 2),
('B5', 'Database System Concepts',   'Silberschatz',  '978-0073523323', 2, 2);

-- Two sample issue records: one still out, one already returned
INSERT INTO issue_records (member_id, book_id, issue_date, due_date, return_date, fine_amount, status) VALUES
('S1', 'B2', '2026-08-05', '2026-08-19', NULL, 0.00, 'ISSUED'),
('F1', 'B4', '2026-07-01', '2026-07-15', '2026-07-16', 2.00, 'RETURNED');

-- Quick checks you can run after loading:
-- SELECT * FROM members;
-- SELECT * FROM books;
-- SELECT * FROM issue_records;
