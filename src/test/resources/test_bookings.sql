insert into bookings (start_date, end_date, item_id, booker_id, status)
values ((current_timestamp + (300 ||' minutes')::interval), (current_timestamp + (700 ||' minutes')::interval),
        1, 3, 'APPROVED');
insert into bookings (start_date, end_date, item_id, booker_id, status)
values ((current_timestamp - (500 ||' minutes')::interval), (current_timestamp - (50 ||' minutes')::interval),
        2, 1, 'APPROVED');
insert into bookings (start_date, end_date, item_id, booker_id, status)
values ((current_timestamp + (40 ||' minutes')::interval), (current_timestamp + (500 ||' minutes')::interval),
        3, 1, 'APPROVED');
insert into bookings (start_date, end_date, item_id, booker_id, status)
values ((current_timestamp - (900 ||' minutes')::interval), (current_timestamp - (550 ||' minutes')::interval),
        1, 2, 'APPROVED');