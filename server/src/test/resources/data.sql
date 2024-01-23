delete from USERS;
alter table USERS alter column USER_ID restart with 1;
alter table ITEMS alter column ITEM_ID restart with 1;
alter table BOOKINGS alter column BOOKING_ID restart with 1;
alter table COMMENTS alter column COMMENT_ID restart with 1;
alter table REQUESTS alter column REQUEST_ID restart with 1;