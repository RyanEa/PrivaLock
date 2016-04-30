import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by David on 4/29/2016.
 */
public class PasswordAuthentication extends Authentication {

    public PasswordAuthentication() {
        setAuthenticationType(AuthenticationType.PASSWORD);
        setHashedForm(new byte[0]);
    }

    public PasswordAuthentication(AuthenticationObject authObj) {
        setAuthenticationType(AuthenticationType.PASSWORD);
        setHashedForm(authObj.getHashedForm());
        setSetUp(true);
    }

    @Override
    public boolean setUpAuthentication(AuthenticationObject authObj) throws AlreadySetUpException {
        if (!isSetUp()) {
            setHashedForm(authObj.getHashedForm());
            return true;
        }
        else {
            throw new AlreadySetUpException("Password already set up!");
        }
    }

    @Override
    public boolean checkAuthentication(AuthenticationObject authObj) {
        if (!isSetUp()) {
            return false;
        }
        else {
            try {
                // The password in this object.
                MessageDigest systemHashedContent = MessageDigest.getInstance("MD5");
                systemHashedContent.update(this.getHashedForm());
                // The user-entered password.
                MessageDigest userHashedContent = MessageDigest.getInstance("MD5");
                userHashedContent.update(authObj.getHashedForm());
                if (MessageDigest.isEqual(systemHashedContent.digest(), userHashedContent.digest())) {
                    return true; // Looks like it's the same password.
                }
            }
            catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return false; // Wrong password.
        }
    }

}
