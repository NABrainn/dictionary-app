package lule.dictionary.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.entity.UserProfile;
import lule.dictionary.exception.RepositoryException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserProfileRepository {

    private final JdbcTemplate template;

    private static final RowMapper<UserProfile> ROW_MAPPER = ((rs, rowNum) ->
            new UserProfile(
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("user_password")
            )
    );

    public Optional<UserProfile> addUserProfile(UserProfile userProfile) throws RepositoryException {
        String sql = "INSERT INTO dictionary.user_profiles (username, email, user_password) VALUES (?, ?, ?) RETURNING *";
        try {
            UserProfile result = template.queryForObject(sql, ROW_MAPPER,
                    userProfile.username(),
                    userProfile.email(),
                    userProfile.password());
            return Optional.ofNullable(result);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new RepositoryException(e.getMessage(), e.getCause());
        }
    }

    public Optional<UserProfile> findByUsername(String username) {
        String sql = "SELECT * FROM dictionary.user_profiles WHERE username=?";
        try {
            UserProfile result = template.queryForObject(sql, ROW_MAPPER, username);
            return Optional.ofNullable(result);
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            throw new RepositoryException(e.getMessage(), e.getCause());
        }
    }
}
