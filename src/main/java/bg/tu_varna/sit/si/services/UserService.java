package bg.tu_varna.sit.si.services;
import bg.tu_varna.sit.si.models.User;
import bg.tu_varna.sit.si.repositories.UserRepository;
import bg.tu_varna.sit.si.requests.ResponseCodes;
import bg.tu_varna.sit.si.security.PasswordEncoderWrapper;

import java.util.List;

public class UserService {

    final static String SYSTEM_USER_EMAIL = "system";

    public static ResponseCodes.ResponseCodeTypes editProfile(User user) {

        UserRepository userRepository = new UserRepository();
        User userFromDb = userRepository.findById(user.getId());
        if (userFromDb == null) {
            return ResponseCodes.ResponseCodeTypes.INTERNAL_ERROR;
        }

        userFromDb.setFirstName(user.getFirstName());
        userFromDb.setLastName(user.getLastName());
        userFromDb.setPhoneNumber(user.getPhoneNumber());
        userFromDb.setDescription(user.getDescription());

        User updatedUser = userRepository.saveOrUpdate(userFromDb);
        if( updatedUser == null) {
            return ResponseCodes.ResponseCodeTypes.INTERNAL_ERROR;
        }

        return ResponseCodes.ResponseCodeTypes.SUCCESS;
    }

    public static User getSystemUser() {

        return new UserRepository().getUserByEmail(SYSTEM_USER_EMAIL);
    }

    public static List<User> getUsers(int currentUserId) {

        return new UserRepository().getUsers(currentUserId);
    }

    public User login(User user) {

        UserRepository userRepository = new UserRepository();
        User databaseUser = userRepository.getUserByEmail(user.getEmail());

        PasswordEncoderWrapper passwordEncoderWrapper = new PasswordEncoderWrapper();

        if (databaseUser == null ){
            return null;
        }

        if (!passwordEncoderWrapper.matches(user.getPassword(), databaseUser.getPassword())) {
            return null;
        }

        return databaseUser;
    }

    public ResponseCodes.ResponseCodeTypes registerUser(User user) {

        UserRepository userRepository = new UserRepository();

        if (userRepository.getUserByEmail(user.getEmail()) != null) {
            return ResponseCodes.ResponseCodeTypes.USER_WITH_THIS_EMAIL_ALREADY_EXISTS;
        }

        if (userRepository.getUserByPhoneNumber(user.getPhoneNumber()) != null) {
            return ResponseCodes.ResponseCodeTypes.USER_WITH_THIS_PHONE_NUMBER_ALREADY_EXISTS;
        }

        PasswordEncoderWrapper passwordEncoderWrapper = new PasswordEncoderWrapper();
        String encodedPassword = passwordEncoderWrapper.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.saveOrUpdate(user);

        return ResponseCodes.ResponseCodeTypes.SUCCESS;
    }
}
