package service;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import exceptions.QuestionnaireException;
import model.Question;
import model.Questionnaire;

/**
 * Session Bean implementation class QuestionService
 */
@Stateless
@LocalBean
public class QuestionService {
	@PersistenceContext(unitName = "projectEJB")
	private EntityManager em;
	private int max=10;
	
    public QuestionService() {
        // TODO Auto-generated constructor stub
    }
    
	public void addQuestion(Questionnaire questionnaire, String text) throws QuestionnaireException {
		Question question = new Question(text, questionnaire);
		int size= questionnaire.getQuestions().size();
		
		if(size<=10) {
			questionnaire.addQuestion(question);
			em.persist(question);
		}
		else throw new QuestionnaireException("Max number of questions");
	}
	

}
