package service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.joda.time.DateTime;

import exceptions.QuestionnaireException;
import exceptions.ReviewException;
import model.Questionnaire;
import model.Review;
import model.User;

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
    
    public Review createReview(Questionnaire q,User u) {

			Review r= new Review (Age==0?0:Age,sex.charAt(0)=='n'?'\0':sex.charAt(0), level.strip().equals("none")?"\0":level, "submitted");
			q.addReview(r);
			u.addReview(r);
			em.persist(r);	
			em.flush();
			return r;
    }
    
    public Review cancelReview(Questionnaire q,User u) {
	    	
		Review r= new Review (0,'\0',"", "cancelled");
		q.addReview(r);
		u.addReview(r);
		em.persist(r);
		em.flush();
		return r;
    }
    
    public void setQuestionnaire(int r,int q) {
    	
		Review rev=em.find( Review.class,q);
		Questionnaire qst=em.find( Questionnaire.class,r);
		rev.setQuestionnaire(qst);
		em.flush();

}
    
    public Review findByUserQuestionnaire(Integer userId, Integer qId) throws ReviewException {
    	
    	List<Review> r= em.createNamedQuery("Review.findByUserQ").setParameter("questionnaire", qId).setParameter("user", userId).getResultList();   	
	
		if(r.isEmpty()) {
			return null;
		}
		else if(r.size()==1) {
			return r.get(0);
		}
		throw new ReviewException("Not unique result");   	
    }


}
