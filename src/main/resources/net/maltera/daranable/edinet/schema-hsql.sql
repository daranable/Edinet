CREATE TABLE properties (
	name VARCHAR (128) NOT NULL IDENTITY PRIMARY KEY,
	value VARCHAR (256)
);

INSERT INTO properties (key, value) VALUES ('schema.version', '0');

CREATE TABLE terms (
	year SMALLINT NOT NULL,
	serial SMALLINT NOT NULL,
	name VARCHAR (128) NOT NULL,
	start DATE,
	'end' DATE,
	
	PRIMARY KEY (year, serial)
)

CREATE TABLE courses (
	id INTEGER NOT NULL IDENTITY PRIMARY KEY,
	year SMALLINT NOT NULL,
	term SMALLINT NOT NULL,
	teacher VARCHAR (256),
	name VARCHAR (128),
	color INTEGER (32),
	notes CLOB,
	
	CONSTRAINT ref_term
		FOREIGN KEY (year, term)
		REFERENCES terms (year, serial)
		ON UPDATE CASCADE
)

CREATE TABLE assignments (
	id INTEGER NOT NULL IDENTITY PRIMARY KEY,
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
)