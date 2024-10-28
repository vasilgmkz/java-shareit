insert into USERS (user_name, user_email)
values ('name_1', 'email_1@email.com'),
       ('name_2', 'email_2@email.com'),
       ('name_3', 'email_3@email.com'),
       ('name_4', 'email_4@email.com');

insert into ITEMREQUEST (request_description, request_created, user_id)
values ('description_1', '2024-10-22 11:10', 1),
       ('description_2', '2024-10-22 12:20', 2),
       ('description_3', '2024-10-22 13:30', 3),
       ('description_4', '2024-10-22 11:15', 1);

insert into ITEMS (item_name, item_description, item_available, user_id, request_id)
values ('name_1', 'description_1', false, 1, 1),
       ('name_2', 'description_2', false, 2, 2),
       ('name_3', 'description_3', true, 3, 3);

insert into bookings (booking_start, booking_end, item_id, user_id, booking_status)
values ('2024-10-22 11:10', '2024-10-22 12:10', 1, 2, 'WAITING'),
       ('2024-10-28 04:00', '2024-10-28 12:00', 2, 3, 'WAITING'),
       ('2026-10-22 11:10', '2026-10-22 12:10', 1, 2, 'WAITING');

