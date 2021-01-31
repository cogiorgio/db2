package entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name="product",schema="db_myapp")
public class Product {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="product", cascade = { CascadeType.PERSIST, CascadeType.REMOVE,
			CascadeType.REFRESH })
	private List<Review> review;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="product", cascade = { CascadeType.PERSIST, CascadeType.REMOVE,
			CascadeType.REFRESH })
	private List<Question> question;
	
	private String image_path;
	
	private String name;
	
	@Temporal(TemporalType.DATE)
	private Date date;
	
	
	public Product() {}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getImage_path() {
		return image_path;
	}


	public void setImage_path(String image_path) {
		this.image_path = image_path;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}
	
	public List<Review> getReview() {
		return review;
	}
	
	public void AddReview(Review r) {
		review.add(r);
		r.setProduct(this);
	}
	
	public List<Question> getQuestion() {
		return question;
	}
	
	public void AddQuestion(Question q) {
		question.add(q);
		q.setProduct(this);
	}
	
	
	
	
	
	
}
