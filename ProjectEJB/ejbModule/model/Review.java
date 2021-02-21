package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Review
 *
 */
@Entity
@NamedQuery(name="Review.findByUserQ", query="SELECT r FROM Review r WHERE r.questionnaire.id = :questionnaire AND r.user.id = :user")
public class Review implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private int age;
	
	private char sex;
	
	private String level;
		
	private String status;
	
	//bi-directional many-to-one association to Questionnaire
	@ManyToOne
	@JoinColumn(name="questionnaire")
	private Questionnaire questionnaire;
	
	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user")
	private User user;
	
	//bi-directional one-to-many association to Answer
	@OneToMany(mappedBy="review", cascade= {CascadeType.REMOVE, CascadeType.PERSIST})
	private List<Answer> answers;

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


	public String getLevel() {
		return level;
	}


	public void setLevel(String expLevel) {
		this.level = expLevel;
	}


	public Review(int age, char sex, String level, String status, Date logData) {
		super();
		this.age=age;
		this.sex=sex;
		this.level=level;
		this.status = status;	
	}


	public String getStatus() {
		return status;
	}
	
	


	public void setStatus(String status) {
		this.status = status;
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
	
	public List<Answer> getAnswers() {
		return answers;
	}


	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	
	public void addAnswer(Answer answer) {
		getAnswers().add(answer);
		answer.setReview(this);
	}

	public void removeAnswer(Answer answer) {
		getAnswers().remove(answer);
		answer.setReview(null);
	}
	
   
}
