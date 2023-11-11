CREATE TABLE IF NOT EXISTS song (id SERIAL PRIMARY KEY, title VARCHAR(100) NOT NULL, artist VARCHAR(100) NOT NULL,label VARCHAR(100), released int);

insert into song values ('1', 'someTitle', 'bsmith', 'secret', '2000');
insert into song values ('2', 'someOtherTitle', 'mjack', 'noSecret', '2020');

CREATE TABLE IF NOT EXISTS usertable (userid VARCHAR(20) PRIMARY KEY, password VARCHAR(20) NOT NULL, firstName VARCHAR(50) NOT NULL,lastName VARCHAR(50));

insert into usertable values ('bsmith', 'secret', 'Burkhart', 'Schmitt');