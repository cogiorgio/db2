package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the questionnaire database table.
 * 
 */
@Entity
@NamedQuery(name="Questionnaire.findAll", query="SELECT q FROM Questionnaire q")
public class Questionnaire implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String product;
	
	@Temporal(TemporalType.DATE)
	private Date date;


	//bi-directional many-to-one association to Question
	@OneToMany(mappedBy="questionnaire", cascade= CascadeType.REMOVE)
	private List<Question> questions;

	public Questionnaire() {
	}
	
	public Questionnaire(String product, Date date) {
		this.product=product;
		this.date= date;
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

	public Question removeQuestion(Question question) {
		getQuestions().remove(question);
		question.setQuestionnaire(null);

		return question;
	}

}