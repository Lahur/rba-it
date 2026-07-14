DELETE FROM card_request;

INSERT INTO card_request (id, first_name, last_name, oib, status)
VALUES ('11111111-1111-1111-1111-111111111111', 'Ana', 'Anic', '00111111113', 'PENDING'),
       ('22222222-2222-2222-2222-222222222222', 'Bruno', 'Brunic', '00222222224', 'APPROVED');