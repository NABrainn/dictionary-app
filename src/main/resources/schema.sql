CREATE SCHEMA IF NOT EXISTS dictionary;
DROP TYPE IF EXISTS dictionary.lang CASCADE;
DROP TYPE IF EXISTS dictionary.familiarity CASCADE;

CREATE TYPE dictionary.lang as ENUM ('PL', 'EN', 'NO', 'IT');
CREATE TYPE dictionary.familiarity as ENUM ('UNKNOWN', 'RECOGNIZED', 'FAMILIAR', 'KNOWN', 'IGNORED');

CREATE CAST (varchar AS dictionary.lang) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS dictionary.familiarity) WITH INOUT AS IMPLICIT;

CREATE TABLE IF NOT EXISTS dictionary.user_profile_settings (
	settings_id						SERIAL PRIMARY KEY,
    source_lang  					dictionary.lang,
    target_lang  					dictionary.lang,
);

CREATE TABLE IF NOT EXISTS dictionary.user_profiles (
    username        				VARCHAR(50) NOT NULL UNIQUE,
    email           				VARCHAR(100) NOT NULL PRIMARY KEY,
    password        			    VARCHAR(500) NOT NULL,
	settings_id						int REFERENCES dictionary.user_profile_settings(settings_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS dictionary.streaks (
	day_count						int NOT NULL DEFAULT 0,
	words_added_today				int	NOT NULL DEFAULT 0,
	streak_owner					VARCHAR(50) REFERENCES dictionary.user_profiles(username) ON UPDATE CASCADE ON DELETE CASCADE,
	tz_offset                       VARCHAR(100) NOT NULL DEFAULT '+00:00',
	last_day_counted                DATE,
	updated_at						TIMESTAMPTZ
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
  	title							VARCHAR (200) NOT NULL,
  	content							TEXT NOT NULL,
  	url								VARCHAR(200),
  	source_lang						dictionary.lang NOT NULL,
  	target_lang						dictionary.lang NOT NULL,
  	import_owner					VARCHAR(50) NOT NULL references dictionary.user_profiles(username) ON UPDATE CASCADE ON DELETE CASCADE,
	total_length					int NOT NULL
);

CREATE TABLE IF NOT EXISTS dictionary.imports_translations (
	imports_id 						int REFERENCES dictionary.imports(imports_id) ON UPDATE CASCADE ON DELETE CASCADE,
    translations_id				    int REFERENCES dictionary.translations(translations_id) ON UPDATE CASCADE ON DELETE CASCADE,
    amount							int NOT NULL DEFAULT 1,

    CONSTRAINT imports_translations_pkey PRIMARY KEY (imports_id, translations_id)
);

