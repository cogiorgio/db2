package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the blacklist database table.
 * 
 */
@Entity
@NamedQuery(name="Blacklist.findAll", query="SELECT b FROM Blacklist b")
//@NamedQuery(name="Blacklist.findAllBaddies", query="SELECT b.badwords FROM Blacklist b")
public class Blacklist implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String badwords;


	public Blacklist() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBadwords() {
		return this.badwords;
	}

	public void setBadwords(String badwords) {
		this.badwords = badwords;
	}

}