package webtech2.rest.auth;

import java.util.HashMap;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

import webtech2.rest.exceptions.UserNotResolvedException;
import webtech2.rest.temporary.SerializableUser;

public class AuthRealm extends JdbcRealm {
	private static HashMap<String,SerializableUser> userlist = new HashMap<>(); //TODO Shiro magic
	
	private static Factory<SecurityManager> factory;
	
	public AuthRealm() {
		// init shiro - place this e.g. in the constructor
		Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory();
		org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
	}
	
	public void registrate(String email, String plainTextPassword) {
		User user = new User();
		user.setUsername(email);
		user.setEmail(email);

		generatePassword(user, plainTextPassword);

		Session session = HibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();
		session.save(user);
		tx.commit();

		System.err.println("User with email:" + user.getEmail() + " hashedPassword:"+ user.getPassword() + " salt:" + user.getSalt());
	}

	private void generatePassword(User user, String plainTextPassword) {
		RandomNumberGenerator rng = new SecureRandomNumberGenerator();
		Object salt = rng.nextBytes();

		// Now hash the plain-text password with the random salt and multiple
		// iterations and then Base64-encode the value (requires less space than Hex):
		String hashedPasswordBase64 = new Sha256Hash(plainTextPassword, salt,1024).toBase64();

		user.setPassword(hashedPasswordBase64);
		user.setSalt(salt.toString());
	}
	
	public static void init() {
		 factory = new IniSecurityManagerFactory("shiro.ini");
		 SecurityManager securityManager = factory.getInstance();
		 SecurityUtils.setSecurityManager(securityManager);
	}
	
	public static SerializableUser resolveUser(String sessionID) throws UserNotResolvedException{
		if(factory==null) {
			init();
		}
		
		SerializableUser tempUser = userlist.get(sessionID);
		if(tempUser==null) throw new UserNotResolvedException(sessionID);
		return tempUser;
	}
	
	public static Subject getCurrentSubject() {
		if(factory==null) {
			init();
		}
		return SecurityUtils.getSubject();
	}
	
	public static void registerUser(String username, String password) {
		//Write into shiro.ini
	}
	
	public static void loginUser(String username, String password) throws Exception{
		Subject currentUser = getCurrentSubject();
		
		if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            token.setRememberMe(true);
            try {
                currentUser.login(token);
            } catch (UnknownAccountException uae) {
                System.out.println("There is no user with username of " + token.getPrincipal());
            } catch (IncorrectCredentialsException ice) {
                System.out.println("Password for account " + token.getPrincipal() + " was incorrect!");
            }
            // ... catch more exceptions here 
            catch (AuthenticationException ae) {
                System.out.println("ERROR: " + ae.getMessage());
            }
        }
	}
	
	@Override
	  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
	    // identify account to log to
	    UsernamePasswordToken userPassToken = (UsernamePasswordToken) token;
	    final String username = userPassToken.getUsername();

	    if (username == null) {
	      System.out.println("Username is null.");
	      return null;
	    }

	    // read password hash and salt from db
	    final User user = UserDAO.getUserByEmail(username);

	    if (user == null) {
	      System.out.println("No account found for user [" + username + "]");
	      return null;
	    }

	    // return salted credentials
	    SaltedAuthenticationInfo info = new MySaltedAuthentificationInfo(username, user.getPassword(), user.getSalt());

	    return info;
	  }
}
