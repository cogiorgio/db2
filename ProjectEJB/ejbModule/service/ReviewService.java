package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import exceptions.ReviewException;
import model.Review;

/**
 * Session Bean implementation class ReviewService
 */
@Stateful
@LocalBean
public class ReviewService {
	@PersistenceContext(unitName = "projectEJB",type = PersistenceContextType.EXTENDED)
	private EntityManager em;
	
	private Map<String,String> answers=new HashMap<String,String>();
	
	private int Age;
	private String sex="";
	private String level="";

    public ReviewService() {
        // TODO Auto-generated constructor stub
    }
    
    public void addAnswer(String id,String answer) {
    	answers.put(id, answer);
    }
    
    public Map<String,String> getAnswers(){
    	return this.answers;
    };
    
    public void setAge(int a) {
    	this.Age=a;
    }
    public void setSex(String s) {
    	this.sex=s;
    	
    }
    
    public int getAge() {
    	return this.Age;
    }
    public String getSex() {
    	return this.sex;
    }
    
    public void setExpertise(String l) {
    	this.level=l;
    	
    }
    public String getExpertise() {
    	return this.level;
    	
    }
    
    //questo er stateless dovremo un attimo ripensare la cosa no?
    public Review findByUserQuestionnaire(Integer userId, Integer qId) throws ReviewException {
    	
    	List<Review> r= em.createNamedQuery("Review.findByUserQ").setParameter("questionnaire", qId).setParameter("user", userId).getResultList();   	
	
		if(r.isEmpty()) {
			return null;
		}
		else if(r.size()==1) {
			return r.get(0);
		}
		throw new ReviewException("not unique result");   	
    }


}
