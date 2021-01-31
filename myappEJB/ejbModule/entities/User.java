package entities;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


import java.util.List;


@Entity
@Table(name = "user", schema = "db_myapp")
public class User {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String username;

	private String password;
    
	@Temporal(TemporalType.DATE)
	private Date lastLog;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="user", cascade = { CascadeType.PERSIST, CascadeType.REMOVE,
			CascadeType.REFRESH })
	private List<Review> review;
	
	public User() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getLastLog() {
		return lastLog;
	}

	public void setLastLog(Date lastLog) {
		this.lastLog = lastLog;
	}

	public List<Review> getReview() {
		return review;
	}
	
	public void AddReview(Review r) {
		review.add(r);
		r.setUser(this);
	}
	

	
	
}
