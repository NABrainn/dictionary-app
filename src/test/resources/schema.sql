CREATE SCHEMA IF NOT EXISTS dictionary;
DROP TYPE IF EXISTS dictionary.lang CASCADE;
DROP TYPE IF EXISTS dictionary.familiarity CASCADE;

CREATE TYPE dictionary.lang as ENUM ('pl', 'en', 'no');
CREATE TYPE dictionary.familiarity as ENUM ('unknown', 'recognized', 'familiar', 'known', 'ignored');

CREATE CAST (varchar AS dictionary.lang) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS dictionary.familiarity) WITH INOUT AS IMPLICIT;

CREATE TABLE IF NOT EXISTS dictionary.user_profiles (
    username        				VARCHAR(50) NOT NULL UNIQUE,
    email           				VARCHAR(50) NOT NULL PRIMARY KEY,
    user_password        			VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS dictionary.translations (
    translations_id              	SERIAL PRIMARY KEY,
    source_word     				VARCHAR(50) NOT NULL,
    target_word     				VARCHAR(50) NOT NULL UNIQUE,
    source_lang  					dictionary.lang NOT NULL,
    target_lang  					dictionary.lang NOT NULL,
  	translation_owner				VARCHAR(20) NOT NULL references dictionary.user_profiles(username) ON UPDATE CASCADE ON DELETE CASCADE,
  	familiarity						dictionary.familiarity NOT NULL
);
CREATE TABLE IF NOT EXISTS dictionary.imports (
  	imports_id						SERIAL PRIMARY KEY,
  	title							VARCHAR (100) NOT NULL,
  	content							TEXT NOT NULL,
  	url								VARCHAR(200),
  	source_lang						dictionary.lang NOT NULL,
  	target_lang						dictionary.lang NOT NULL,
  	import_owner					VARCHAR(20) NOT NULL references dictionary.user_profiles(username) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS dictionary.imports_translations (
	imports_id 						int REFERENCES dictionary.imports(imports_id) ON UPDATE CASCADE ON DELETE CASCADE,
    translations_id				    int REFERENCES dictionary.translations(translations_id) ON UPDATE CASCADE ON DELETE CASCADE,
    amount							int NOT NULL DEFAULT 1,

    CONSTRAINT imports_translations_pkey PRIMARY KEY (imports_id, translations_id)
);