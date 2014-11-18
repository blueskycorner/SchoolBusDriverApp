/*
Created: 16/11/2014
Modified: 18/11/2014
Project: SchoolBusService
Model: SQLite 3.7
Database: SQLite 3.7
*/

-- Create tables section -------------------------------------------------

-- Table school

CREATE TABLE school
(
  id INTEGER NOT NULL,
  name TEXT NOT NULL,
  CONSTRAINT Key1 PRIMARY KEY (id)
);

-- Table trip

CREATE TABLE trip
(
  id INTEGER NOT NULL,
  school_id INTEGER NOT NULL,
  destination_id INTEGER NOT NULL,
  day_id INTEGER NOT NULL,
  hour INTEGER NOT NULL,
  minute INTEGER NOT NULL,
  return boolean NOT NULL,
  CONSTRAINT Key2 PRIMARY KEY (id),
  CONSTRAINT fk_trip_school_id FOREIGN KEY (school_id) REFERENCES school (id),
  CONSTRAINT Relationship1 FOREIGN KEY (id) REFERENCES trip_destination (id),
  CONSTRAINT Relationship4 FOREIGN KEY (destination_id) REFERENCES trip_destination (id)
);

-- Table child

CREATE TABLE child
(
  id INTEGER NOT NULL,
  first_name TEXT NOT NULL,
  last_name TEXT NOT NULL,
  address TEXT NOT NULL,
  language_id INTEGER NOT NULL,
  creation_date date NOT NULL,
  modification_date date NOT NULL,
  CONSTRAINT Key3 PRIMARY KEY (id)
);

-- Table version

CREATE TABLE version
(
  id INTEGER NOT NULL,
  name TEXT NOT NULL,
  version INTEGER NOT NULL,
  CONSTRAINT Key6 PRIMARY KEY (id)
);

-- Table trip_destination

CREATE TABLE trip_destination
(
  id INTEGER NOT NULL,
  destination TEXT NOT NULL,
  CONSTRAINT Key8 PRIMARY KEY (id)
);

-- Table trip_child_association

CREATE TABLE trip_child_association
(
  trip_id INTEGER NOT NULL,
  child_id INTEGER NOT NULL,
  pickup_time_hour INTEGER NOT NULL,
  pickup_time_minute INTEGER NOT NULL,
  CONSTRAINT Key9 PRIMARY KEY (trip_id,child_id),
  CONSTRAINT Relationship2 FOREIGN KEY (trip_id) REFERENCES trip (id),
  CONSTRAINT Relationship3 FOREIGN KEY (child_id) REFERENCES child (id)
);


