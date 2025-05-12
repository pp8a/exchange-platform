-- changeset user:insert-users
INSERT INTO users (id, name, date_of_birth, password)
VALUES 
  ('00000000-0000-0000-0000-000000000001', 'Alice Smith', '1990-05-12', '$2a$10$B4t5aEj117KZO.wcjiJqE.yfaYECuYBJQvFRt8Uyyn8X9LsWNOeFK'), -- "password1"
  ('00000000-0000-0000-0000-000000000002', 'Bob Johnson', '1985-08-20', '$2a$10$PON9lyk1gf27QbKBBT.ja.ho4JNznLG8dRNdSFkh.9/OnDhMY2Rtq'), -- "password2"
  ('00000000-0000-0000-0000-000000000003', 'Charlie Davis', '1995-01-10', '$2a$10$MoxmLQOspYdNbBC5FyurT.QyGTAZCevs3hun7R9UNBSmgoGyHpAVe'); -- "password3"

-- changeset user:insert-email-data
INSERT INTO email_data (user_id, email, is_verified)
VALUES
  ('00000000-0000-0000-0000-000000000001', 'alice@example.com', TRUE),
  ('00000000-0000-0000-0000-000000000001', 'alice.work@example.com', FALSE),
  ('00000000-0000-0000-0000-000000000002', 'bob@example.com', TRUE),
  ('00000000-0000-0000-0000-000000000002', 'bob.alt@example.com', FALSE),
  ('00000000-0000-0000-0000-000000000003', 'charlie@example.com', TRUE);

-- changeset user:insert-phone-data
INSERT INTO phone_data (user_id, phone_number, is_verified)
VALUES
  ('00000000-0000-0000-0000-000000000001', '+1234567890', TRUE),
  ('00000000-0000-0000-0000-000000000001', '+1234567891', FALSE),
  ('00000000-0000-0000-0000-000000000002', '+1234567892', TRUE),
  ('00000000-0000-0000-0000-000000000002', '+1234567893', FALSE),
  ('00000000-0000-0000-0000-000000000003', '+1234567894', TRUE);

-- changeset user:insert-account
INSERT INTO account (user_id, balance, initial_balance)
VALUES 
  ('00000000-0000-0000-0000-000000000001', 10.00, 1000.00),
  ('00000000-0000-0000-0000-000000000002', 5.00, 500.00),
  ('00000000-0000-0000-0000-000000000003', 32.00, 4000.00);

