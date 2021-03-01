package model;

import java.io.Serializable;


import javax.persistence.*;

import java.util.Base64;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the questionnaire database table.
 * 
 */
@Entity
@Table(name = "questionnaire", schema = "projectdb")
@NamedQueries({@NamedQuery(name="Questionnaire.findAll", query="SELECT q FROM Questionnaire q"), 
	@NamedQuery(name="Questionnaire.findByDate", query="SELECT q FROM Questionnaire q WHERE q.date= :qdate")})
public class Questionnaire implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String product;
	
	@Column(name = "date")
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@Lob
	private byte[] img;


	//bi-directional many-to-one association to Question
	@OneToMany(mappedBy="questionnaire", cascade= CascadeType.REMOVE)
	private List<Question> questions;
	
	//bi-directional one-to-Many association to Reviews
	@OneToMany(mappedBy="questionnaire", fetch=FetchType.EAGER, cascade= CascadeType.REMOVE)
	private List<Review> reviews;

	public Questionnaire() {
	}
	
	public Questionnaire(String product, Date date, byte[] img) {
		this.product=product;
		this.date= date;
		this.img=img;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getProduct() {
		return this.product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getImg() {
		return Base64.getMimeEncoder().encodeToString(img);
	}

	public void setImg(byte[] img) {
		this.img = img;
	}
	
	public List<Question> getQuestions() {
		return this.questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public Question addQuestion(Question question) {
		getQuestions().add(question);
		question.setQuestionnaire(this);

		return question;
	}

	
	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
	
	public Review addReview(Review review) {
		getReviews().add(review);
		review.setQuestionnaire(this);

		return review;
	}


}