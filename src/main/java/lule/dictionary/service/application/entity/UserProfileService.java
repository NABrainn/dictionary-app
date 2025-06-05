package lule.dictionary.service.application.entity;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfile;
import lule.dictionary.repository.UserProfileRepository;
import lule.dictionary.exception.RepositoryException;
import lule.dictionary.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfile findByUsername(@NonNull String username) throws ServiceException {
        try {
            return userProfileRepository.findByUsername(username).orElseThrow(() -> new ServiceException("User not found"));
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to fetch user", e.getCause());
        }
    }

    public UserProfile addUserProfile(@NonNull UserProfile userProfile) throws ServiceException {
        try {
            return userProfileRepository.addUserProfile(userProfile.userProfileCredentials(), userProfile.userProfileSettings()).orElseThrow(() -> new ServiceException("Failed to add user"));
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to add user", e.getCause());
        }
    }

    public List<UserProfile> findAll() throws ServiceException {
        try {
            return userProfileRepository.findAll();
        } catch (RepositoryException e) {
            throw new ServiceException("User not found", e.getCause());
        }
    }
}
