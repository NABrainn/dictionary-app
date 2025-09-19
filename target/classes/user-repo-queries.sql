DELETE
FROM dictionary.user_profiles
WHERE username='useruseruser';

--findByUsername
SELECT p.username, p.password, p.email, s.source_lang, s.target_lang, str.day_count, str.is_daily_goal_met, str.timezone, str.updated_at
FROM dictionary.user_profiles p
LEFT JOIN dictionary.user_profile_settings s ON p.settings_id=s.settings_id
LEFT JOIN dictionary.streaks str ON p.username=str.streak_owner
WHERE p.username='useruseruser';

--findByUsernameOrEmail
SELECT p.username, p.password, p.email, s.source_lang, s.target_lang, str.day_count, str.is_daily_goal_met, str.timezone, str.updated_at
FROM dictionary.user_profiles p
LEFT JOIN dictionary.user_profile_settings s ON p.settings_id=s.settings_id
LEFT JOIN dictionary.streaks str ON p.username=str.streak_owner
WHERE p.username='useruseruser' OR p.email='useruser@useruser.com';

--addUserProfile
WITH settings AS (
	INSERT INTO dictionary.user_profile_settings (source_lang, target_lang)
	VALUES ('EN', 'NO')
	RETURNING settings_id, source_lang, target_lang
),
streak AS (
	INSERT INTO dictionary.streaks (day_count, is_daily_goal_met, streak_owner, timezone, updated_at)
	VALUES (0, false, 'useruseruser', 'CET', '2025-07-02 15:00:00+02')
	RETURNING day_count, is_daily_goal_met, timezone, streak_owner, updated_at
),
user_insert AS (
	INSERT INTO dictionary.user_profiles (username, email, password, settings_id)
	SELECT 'useruseruser', 'useruser@useruser.com', 'passwird', s.settings_id
	FROM settings s
	RETURNING username, email, password, settings_id
)
SELECT u.username, u.email, u.password, s.source_lang, s.target_lang, str.day_count, str.is_daily_goal_met, str.timezone, str.updated_at
FROM user_insert u
LEFT JOIN settings s ON u.settings_id = s.settings_id
LEFT JOIN streak str ON u.username = str.streak_owner;

--findAll
SELECT p.username, p.email, p.password, s.source_lang, s.target_lang, str.day_count, str.is_daily_goal_met, str.timezone, str.updated_at
FROM dictionary.user_profiles as p
LEFT JOIN dictionary.user_profile_settings s ON p.settings_id=s.settings_id
LEFT JOIN dictionary.streaks str ON p.username=str.streak_owner



