package webtech2.rest.auth;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

import webtech2.jpa.App;
import webtech2.jpa.entities.User;
import webtech2.jpa.exceptions.NoDBEntryException;

public class AuthRealm extends JdbcRealm {
	public static AuthRealm instance;
	private static Factory<SecurityManager> factory;

	public AuthRealm() {
		factory = new IniSecurityManagerFactory();
		org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
		new App();
		instance = this;
	}

	public PasswordSaltMixture generatePassword(String plainTextPassword) {
		RandomNumberGenerator rng = new SecureRandomNumberGenerator();
		Object salt = rng.nextBytes();

		// Now hash the plain-text password with the random salt
		String hashedPasswordBase64 = new Sha256Hash(plainTextPassword, salt, 1024).toBase64();

		return new PasswordSaltMixture(hashedPasswordBase64, salt.toString());
	}

	public User getCurrentUser() throws UnauthenticatedException{
		Subject currentUser = SecurityUtils.getSubject();

		if (currentUser.isAuthenticated()) {
			User user = (User) currentUser.getSession().getAttribute("actualUser");
			return user;
		} else {
			return null;
		}
	}

	public static Subject getCurrentSubject() {
		if (factory == null) {
			System.out.println("AuthRealm not created");
		}
		return SecurityUtils.getSubject();
	}

	public static void registerUser(String username, String password) {
		// Write into shiro.ini
	}

	public static void loginUser(String loginName, String password) throws Exception {
		Subject currentUser = getCurrentSubject();

		if (!currentUser.isAuthenticated()) {
			UsernamePasswordToken token = new UsernamePasswordToken(loginName, password);
			token.setRememberMe(true);
			try {
				currentUser.login(token);
				currentUser.getSession().setAttribute("actualUser", App.instance.getUserByLoginName(loginName));
			} catch (AuthenticationException ae) {
				System.out.println("ERROR: " + ae.getMessage());
			}
		}
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// identify account to log to
		UsernamePasswordToken userPassToken = (UsernamePasswordToken) token;
		final String loginName = userPassToken.getUsername();

		if (loginName == null) {
			System.out.println("Username is null.");
			return null;
		}

		// read password hash and salt from db
		User user;
		try {
			user = App.instance.getUserByLoginName(loginName);
		} catch (NoDBEntryException e) {
			throw new AuthenticationException();
		}

		// return salted credentials
		SaltedAuthenticationInfo info = new MySaltedAuthentificationInfo(loginName, user.getPassword(), user.getSalt());

		return info;
	}
}
