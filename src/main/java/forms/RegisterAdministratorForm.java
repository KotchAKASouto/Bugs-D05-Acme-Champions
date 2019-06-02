
package forms;


public class RegisterAdministratorForm {

	private String	name;
	private String	surnames;
	private String	photo;
	private String	email;
	private String	phone;
	private String	address;

	private String	username;
	private String	password;
	private String	confirmPassword;

	private Boolean	checkbox;


	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getSurnames() {
		return this.surnames;
	}

	public void setSurnames(final String surnames) {
		this.surnames = surnames;
	}

	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return this.confirmPassword;
	}

	public void setConfirmPassword(final String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public Boolean getCheckbox() {
		return this.checkbox;
	}

	public void setCheckbox(final Boolean checkbox) {
		this.checkbox = checkbox;
	}

	//Business metohds--------------------------------------------
	public Boolean checkPassword() {
		Boolean res;

		if (this.password.equals(this.confirmPassword))
			res = true;
		else
			res = false;

		return res;
	}

}
