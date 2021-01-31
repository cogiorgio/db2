package entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="answer",schema="db_myapp")
public class Answer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String line;
	
	@ManyToOne
	@JoinColumn(name = "question")
	private Question question;
	
	@ManyToOne
	@JoinColumn(name = "review")
	private Review review;
	
	@Temporal(TemporalType.DATE)
	private Date date;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}


	public Answer() {}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}

	public Date getD() {
		return date;
	}

	public void setD(Date d) {
		this.date = d;
	};
	
	
	
	
	

}