package model;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the answer database table.
 * 
 */
@Entity
@NamedQuery(name="Answer.findAll", query="SELECT a FROM Answer a")
public class Answer implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AnswerPK id;

	private String text;

	//bi-directional many-to-one association to Question
	@ManyToOne
	@JoinColumn(name="question")
	private Question question;

	//bi-directional many-to-one association to Review
	@ManyToOne
	@JoinColumn(name="review")
	private Review review;

	public Answer() {
	}

	public AnswerPK getId() {
		return this.id;
	}

	public void setId(AnswerPK id) {
		this.id = id;
	}


	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Question getQuestion() {
		return this.question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Review getReview() {
		return this.review;
	}

	public void setReview(Review review) {
		this.review = review;
	}
	
	public String toString() {
		String s= "Question id"+ getQuestion().getId()+ "answer" + getText();
		return s;
	}

}