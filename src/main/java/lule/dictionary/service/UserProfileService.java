package lule.dictionary.service;

import lombok.RequiredArgsConstructor;
import lule.dictionary.entity.UserProfile;
import lule.dictionary.repository.UserProfileRepository;
import lule.dictionary.exception.RepositoryException;
import lule.dictionary.exception.ServiceException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfile add(UserProfile userProfile) throws ServiceException {
        try {
            return userProfileRepository.addUserProfile(userProfile).orElseThrow(() -> new ServiceException("failed to add new user profile"));
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public UserProfile findByUsername(String username) {
        try {
            return userProfileRepository.findByUsername(username).orElseThrow(() -> new ServiceException("an unknown issue occurred"));
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }


}
