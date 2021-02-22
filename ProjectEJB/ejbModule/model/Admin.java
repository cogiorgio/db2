package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the admin database table.
 * 
 */
@Entity
@NamedQueries({@NamedQuery(name = "Admin.checkCredentials", query = "SELECT a FROM Admin a  WHERE a.username = ?1 and a.password = ?2"),
	@NamedQuery(name = "Admin.checkUsername", query = "SELECT a FROM Admin a  WHERE a.username = ?1") ,@NamedQuery(name="Admin.findAll", query="SELECT a FROM Admin a")})
public class Admin implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String mail;

	private String password;

	private String username;

	public Admin() {
	}
	
	public Admin(String username, String password, String mail) {
		this.username=username;
		this.password=password;
		this.mail=mail;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMail() {
		return this.mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}