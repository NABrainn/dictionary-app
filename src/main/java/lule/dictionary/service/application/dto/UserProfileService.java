package lule.dictionary.service.application.dto;

import lombok.RequiredArgsConstructor;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfile;
import lule.dictionary.repository.UserProfileRepository;
import lule.dictionary.exception.RepositoryException;
import lule.dictionary.exception.ServiceException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfile findByUsername(String username) throws ServiceException {
        try {
            return userProfileRepository.findByUsername(username).orElseThrow(() -> new ServiceException("User not found"));
        } catch (RepositoryException e) {
            throw new ServiceException("User not found");
        }
    }

    public UserProfile addUserProfile(UserProfile userProfile) throws ServiceException {
        try {
            return userProfileRepository.addUserProfile(userProfile.userProfileCredentials(), userProfile.userProfileSettings()).orElseThrow(() -> new ServiceException("User not found"));
        } catch (RepositoryException e) {
            throw new ServiceException("User not found");
        }
    }
}
