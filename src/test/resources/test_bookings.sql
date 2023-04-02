insert into bookings (start_date, end_date, item_id, booker_id, status)
values (timestampadd('hour', -10, now()), timestampadd('hour', -2, now()), 1, 3, 'APPROVED');
insert into bookings (start_date, end_date, item_id, booker_id, status)
values (timestampadd('hour', -6, now()), timestampadd('hour', -1, now()), 2, 1, 'APPROVED');
insert into bookings (start_date, end_date, item_id, booker_id, status)
values (timestampadd('hour', -4, now()), timestampadd('hour', 6, now()), 3, 1, 'APPROVED');
insert into bookings (start_date, end_date, item_id, booker_id, status)
values (timestampadd('hour', 3, now()), timestampadd('hour', 8, now()), 1, 2, 'APPROVED');