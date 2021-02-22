package service;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import exceptions.QuestionnaireException;
import model.Answer;
import model.Question;
import model.Questionnaire;
import model.Review;

/**
 * Session Bean implementation class QuestionService
 */
@Stateless
@LocalBean
public class AnswerService {
	@PersistenceContext(unitName = "projectEJB")
	private EntityManager em;
	
    public AnswerService() {
    }
    
	public void createAnswer(String q, Review r,String text) {
		Question quest=em.find(Question.class,Integer.parseInt(q));
		Answer a=new Answer();
		a.setText(text);
		quest.addAnswer(a);
		r.addAnswer(a);
		em.persist(a);
		em.merge(r);
		em.flush();
	}
	

}