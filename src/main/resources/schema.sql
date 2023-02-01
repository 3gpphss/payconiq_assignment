CREATE TABLE IF NOT EXISTS stocks (
  id bigint NOT NULL AUTO_INCREMENT, 
  name varchar(255), 
  current_price double,
  last_update timestamp not null,
  primary key (id) 
);


CREATE TABLE IF NOT EXISTS users (
  id bigint NOT NULL AUTO_INCREMENT, 
  user_name varchar(255), 
  password varchar(255),
  primary key (id) 
);