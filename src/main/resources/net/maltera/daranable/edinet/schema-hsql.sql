CREATE TABLE properties (
	name VARCHAR (128) NOT NULL PRIMARY KEY,
	value VARCHAR (256)
);

INSERT INTO properties (name, value) VALUES ('schema.version', '0');

CREATE TABLE terms (
	year SMALLINT NOT NULL,
	serial SMALLINT NOT NULL,
	name VARCHAR (128) NOT NULL,
	start DATE,
	finish DATE,
	
	PRIMARY KEY (year, serial)
);

CREATE TABLE courses (
	id INTEGER NOT NULL PRIMARY KEY IDENTITY,
	year SMALLINT NOT NULL,
	term SMALLINT NOT NULL,
	teacher VARCHAR (256),
	name VARCHAR (128),
	abbreviation VARCHAR (16) NOT NULL,
	color INTEGER,
	notes CLOB,
	
	CONSTRAINT ref_term
		FOREIGN KEY (year, term)
		REFERENCES terms (year, serial)
		ON UPDATE CASCADE
);

CREATE TABLE assignments (
	id INTEGER NOT NULL PRIMARY KEY IDENTITY,
	course_id INTEGER NOT NULL,
	title VARCHAR (256),
	description CLOB,
	time_estimate INTEGER,
	time_worked INTEGER,
	time_remaining INTEGER,
	assigned DATE,
	due DATE,
	completed DATE,
	
	CONSTRAINT ref_course
		FOREIGN KEY (course_id)
		REFERENCES courses (id)
		ON UPDATE CASCADE
);