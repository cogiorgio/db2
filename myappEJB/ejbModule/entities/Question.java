package entities;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="question",schema="db_myapp")
public class Question {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String line;
	
	@ManyToOne
	@JoinColumn(name = "product")
	private Product product;
	
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Question() {};
	
	
	

}
