package webtech2.rest.auth;

public class PasswordSaltMixture {
	private String password, salt;

	public String getPassword() {
		return password;
	}

	public String getSalt() {
		return salt;
	}

	public PasswordSaltMixture(String password, String salt) {
		this.password = password;
		this.salt = salt;
	}
	
}
