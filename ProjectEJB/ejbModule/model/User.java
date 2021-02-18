package model;

import java.io.Serializable;


import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
//@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
@NamedQuery(name = "User.checkCredentials", query = "SELECT r FROM User r  WHERE r.username = ?1 and r.password = ?2")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String username;
	
	private String password;

	private String mail;
	
	private boolean blocked;
	
	//bi-directional many-to-one association to Question
	@OneToMany(mappedBy="user", cascade= CascadeType.REMOVE)
	private List<Review> reviews;

	public User() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getMail() {
		return this.mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public boolean getBlocked() {
		return this.blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	
	public Review addReview(Review review) {
		getReviews().add(review);
		review.setUser(this);
		return review;
	}

	public Review removeReview(Review review) {
		getReviews().remove(review);
		review.setUser(null);
		return review;
	}

}