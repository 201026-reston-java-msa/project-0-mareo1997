drop table bankuser cascade;
drop table roles cascade;
drop table account cascade;
drop table accountstatus cascade;
drop table accounttype cascade;
/*select * from bankuser;
select * from roles;
select * from account;
select * from accountstatus;
select * from accounttype;*/
create table bankuser(
	userId serial primary key,
	username VARCHAR(100) NOT NULL unique,
	bankpassword VARCHAR(100) NOT null,
	firstname VARCHAR(100) NOT null,
	lastname VARCHAR(100) NOT null,
	email VARCHAR(100) NOT NULL unique
	--roleid integer not null,
	--FOREIGN KEY (roleid) REFERENCES roles (roleid) on delete cascade
);
--alter table bankuser add unique (username,email);

create table roles(
	roleid serial primary key,
	bankrole VARCHAR(100) NOT null,
	userID INTEGER,
	FOREIGN KEY (userID) REFERENCES bankuser (userId) on delete cascade
);

--Many to one foreign key
create table account(
	accountid serial primary key,
	balance NUMERIC (12,2) DEFAULT 0,
	--statusid integer not null, --typeid integer not null,
	ownerid integer not null,
	FOREIGN KEY (ownerid) REFERENCES bankuser (userId) on delete CASCADE
	--FOREIGN KEY (typeid) REFERENCES accounttype (typeid) on delete CASCADE --FOREIGN KEY (acctid) REFERENCES account (accountid) on delete cascade,
);

create table accountstatus(
	statusid serial primary key,
	status VARCHAR(100) NOT NULL,
	acctid integer,
	FOREIGN KEY (acctid) REFERENCES account (accountid) on delete cascade
);

create table accounttype(
	typeid serial primary key,	
	accttype VARCHAR(100) NOT NULL,
	acctid integer,
	FOREIGN KEY (acctid) REFERENCES account (accountid) on delete cascade
);

INSERT INTO bankuser(username, bankpassword, firstname, lastname, email) values ('mareo1997', 'password', 'Mareo', 'Yapp', 'mareo1997@gmail.com');
INSERT INTO bankuser(username, bankpassword, firstname, lastname, email) values ('marwil', 'william', 'Marcia', 'Williamson', 'mareo199@gmail.com');
INSERT INTO bankuser(username, bankpassword, firstname, lastname, email) values ('king', 'george', 'Kingsley', 'Yapp', 'mareo19@gmail.com');

insert into roles(bankrole, userid) values ('Customer', 1);
insert into roles(bankrole, userid) values ('Employee', 2);
insert into roles(bankrole, userid) values ('Admin', 3);

insert into account(balance,ownerid) values (5000, 1);
insert into accountstatus(status, acctid) values ('Open',1);
insert into accounttype(accttype, acctid) values ('Checkings',1);

CREATE OR REPLACE PROCEDURE insert_role(userid integer)
AS $$
	BEGIN 
		INSERT INTO roles(bankrole,userid) VALUES ('Customer',userid);
	END;
$$ language plpgsql; --STORED PROCEDURE FOR ADDING NEWLY MADE USERID IN BANKUSER TABLE TO BE REFERENCED BY THE ROLES TABLE USERID FK

select * --REGISTRATION AND LOGIN
from bankuser b
full join roles r on b.userid =r.userid;

--STORED PROCEDURE FOR ADDING NEWLY MADE ACCTID TO BE REFERENCED BY THE STATUS AND TYPE TABLES ACCTID FK
CREATE OR REPLACE PROCEDURE insert_accts(acctid integer, accttype text)
AS $$
	BEGIN 
		INSERT INTO accountstatus(status,acctid) VALUES ('Pending',acctid);
		INSERT INTO accounttype(accttype,acctid) VALUES (accttype,acctid);
	END;	
$$ language plpgsql;

select * from account a --ACCOUNTS AND TRANSACTIONS
full join accountstatus a2 on a.accountid = a2.acctid 
full join accounttype a3 on a2.acctid =a3.acctid;

select *
from bankuser b
inner join account a on a.ownerid=b.userid
INNER join accountstatus a2 on a.accountid = a2.acctid 
INNER join accounttype a3 on a2.acctid =a3.acctid;

select *
from bankuser b
inner JOIN roles r ON b.userid =r.userid 
inner join account a on a.ownerid=b.userid
inner join accountstatus a2 on a.accountid = a2.acctid 
inner join accounttype a3 on a2.acctid =a3.acctid;

update bankuser set userid=1 where roleid=1;
update account set accountid =1 where ownerid = 1;
update accountstatus set status ='open' where statusid =3;
update accounttype set accttype ='checkings' where typeid = 1;

delete from roles where roleid>=5;
delete from account where accountid=4;
delete from accountstatus where statusid=3;
delete from accounttype where typeid=3;

DELETE FROM bankuser WHERE userid =1;

rollback;
commit;
