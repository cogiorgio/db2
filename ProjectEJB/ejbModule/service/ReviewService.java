package service;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import exceptions.QuestionnaireException;
import exceptions.ReviewException;
import model.Questionnaire;
import model.Review;
import model.User;

/**
 * Session Bean implementation class ReviewService
 */
@Stateless
@LocalBean
public class ReviewService {
	@PersistenceContext(unitName = "projectEJB")
	private EntityManager em;
    /**
     * Default constructor. 
     */
    public ReviewService() {
        // TODO Auto-generated constructor stub
    }
    
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
