drop table account if exists;
create text table account (id bigint IDENTITY, account_holder varchar(100) not null, account_type varchar(10) not null, inception_date date);
set table account source "accounts.csv";
insert into account (account_holder, account_type, inception_date) values ('Matthew Bellamy', 'checking', '2009-01-01');
insert into account (account_holder, account_type, inception_date) values ('Matthew Bellamy', 'savings', '2009-01-05');
insert into account (account_holder, account_type, inception_date) values ('Christopher Wolstenholme', 'checking', '2009-04-22');
insert into account (account_holder, account_type, inception_date) values ('Dominic Howard', 'savings', '2009-10-15');
commit;
shutdown;
