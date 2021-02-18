package model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Review
 *
 */
@Entity

public class Review implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private int age;
	
	private char sex;
	
	private int level;
	
	private int points;
	
	private String status;
	
	@Temporal(TemporalType.DATE)
	private Date logData;
	
	//bi-directional many-to-one association to Questionnaire
	@ManyToOne
	@JoinColumn(name="review")
	private Questionnaire questionnaire;
	
	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user")
	private User user;
	

	public Review() {
		super();
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}


	public char getSex() {
		return sex;
	}


	public void setSex(char sex) {
		this.sex = sex;
	}


	public int getLevel() {
		return level;
	}


	public void setLevel(int expLevel) {
		this.level = expLevel;
	}


	public int getPoints() {
		return points;
	}


	public void setPoints(int points) {
		this.points = points;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Date getLogData() {
		return logData;
	}


	public void setLogData(Date logData) {
		this.logData = logData;
	}


	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}


	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}
	
   
}
