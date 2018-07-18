package webtech2.rest.auth;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

import webtech2.jpa.App;
import webtech2.jpa.entities.User;
import webtech2.jpa.exceptions.NoDBEntryException;
@SuppressWarnings("deprecation")
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
			System.out.println("Currently serving: "+user.getLoginName());
			return user;
		} else {
			return null;
		}
	}

	/**
	 * Tries to log in a user.
	 * @return true if logged in NOW, false if was already logged in.
	 */
	public boolean loginUser(String loginName, String password) throws AuthenticationException, InvalidSessionException, NoDBEntryException {
		Subject currentUser = getCurrentSubject();

		if (!currentUser.isAuthenticated()) {
			UsernamePasswordToken token = new UsernamePasswordToken(loginName, password);
			token.setRememberMe(true);
			currentUser.login(token);
			System.out.println("Login of new user successful!");
			currentUser.getSession().setAttribute("actualUser", App.instance.getUserByLoginName(loginName));
			System.out.println("Attached JPA user to new user!");
			return true;
		}else {
			return false;
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
	
	private Subject getCurrentSubject() {
		if (factory == null) {
			System.out.println("AuthRealm not created");
		}
		return SecurityUtils.getSubject();
	}
}
