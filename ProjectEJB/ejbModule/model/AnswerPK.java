package model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the answer database table.
 * 
 */
@Embeddable
public class AnswerPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(insertable=false, updatable=false)
	private int review;

	@Column(insertable=false, updatable=false)
	private int question;

	public AnswerPK() {
	}
	public int getReview() {
		return this.review;
	}
	public void setReview(int review) {
		this.review = review;
	}
	public int getQuestion() {
		return this.question;
	}
	public void setQuestion(int question) {
		this.question = question;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof AnswerPK)) {
			return false;
		}
		AnswerPK castOther = (AnswerPK)other;
		return 
			(this.review == castOther.review)
			&& (this.question == castOther.question);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.review;
		hash = hash * prime + this.question;
		
		return hash;
	}
}