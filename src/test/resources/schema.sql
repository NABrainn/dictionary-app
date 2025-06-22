CREATE SCHEMA IF NOT EXISTS dictionary;
DROP TYPE IF EXISTS dictionary.lang CASCADE;
DROP TYPE IF EXISTS dictionary.familiarity CASCADE;

CREATE TYPE dictionary.lang as ENUM ('PL', 'EN', 'NO');
CREATE TYPE dictionary.familiarity as ENUM ('UNKNOWN', 'RECOGNIZED', 'FAMILIAR', 'KNOWN', 'IGNORED');

CREATE CAST (varchar AS dictionary.lang) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS dictionary.familiarity) WITH INOUT AS IMPLICIT;

CREATE TABLE IF NOT EXISTS dictionary.user_profile_settings (
	settings_id						SERIAL PRIMARY KEY,
    source_lang  					dictionary.lang,
    target_lang  					dictionary.lang
);

CREATE TABLE IF NOT EXISTS dictionary.user_profiles (
    username        				VARCHAR(50) NOT NULL UNIQUE,
    email           				VARCHAR(100) NOT NULL PRIMARY KEY,
    password        			    VARCHAR(500) NOT NULL,
	settings_id						int REFERENCES dictionary.user_profile_settings(settings_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS dictionary.translations (
    translations_id              	SERIAL PRIMARY KEY,
    source_words     				TEXT[] NOT NULL,
    target_word     				VARCHAR(200) NOT NULL,
    source_lang  					dictionary.lang NOT NULL,
    target_lang  					dictionary.lang NOT NULL,
  	translation_owner				VARCHAR(50) NOT NULL references dictionary.user_profiles(username) ON UPDATE CASCADE ON DELETE CASCADE,
  	familiarity						dictionary.familiarity NOT NULL
);
CREATE TABLE IF NOT EXISTS dictionary.imports (
  	imports_id						SERIAL PRIMARY KEY,
  	title							VARCHAR (100) NOT NULL,
  	content							TEXT NOT NULL,
  	url								VARCHAR(200),
  	source_lang						dictionary.lang NOT NULL,
  	target_lang						dictionary.lang NOT NULL,
  	import_owner					VARCHAR(50) NOT NULL references dictionary.user_profiles(username) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS dictionary.imports_translations (
	imports_id 						int REFERENCES dictionary.imports(imports_id) ON UPDATE CASCADE ON DELETE CASCADE,
    translations_id				    int REFERENCES dictionary.translations(translations_id) ON UPDATE CASCADE ON DELETE CASCADE,
    amount							int NOT NULL DEFAULT 1,

    CONSTRAINT imports_translations_pkey PRIMARY KEY (imports_id, translations_id)
);

-- INSERT INTO dictionary.user_profile_settings (settings_id, source_lang, target_lang) VALUES (1, 'EN', 'NO');
-- INSERT INTO dictionary.user_profiles (username, email, password, settings_id) VALUES ('nabrainn', 'na@brain.com', 'password', 1);