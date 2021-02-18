package service;

import java.util.Date;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import exceptions.QuestionnaireException;
import model.Question;
import model.Questionnaire;

/**
 * Session Bean implementation class QuestionnaireService
 */
@Stateless
@LocalBean
public class QuestionnaireService {
	@PersistenceContext(unitName = "projectEJB")
	private EntityManager em;
    public QuestionnaireService() {
        // TODO Auto-generated constructor stub
    }
    
	public Questionnaire createQuestionnaire(String product, Date date) {
		Questionnaire q= new Questionnaire (product, date);
		em.persist(q);
		//em.flush();
		return q;
	}

	public void deleteQuestionnaire(int qId) {
		Questionnaire q = em.find(Questionnaire.class, qId);
		if (q == null)
			return;
		em.remove(q);
	}
	
	public List<Questionnaire> findAllQuestionnaire() throws QuestionnaireException {
		List<Questionnaire> questionnaires = null;
		try {
			
			questionnaires = em.createNamedQuery("Questionnaire.findAll", Questionnaire.class).getResultList();

		} catch (PersistenceException e) {
			throw new QuestionnaireException("Cannot load questionnares");

		}
		return questionnaires;
	}
	
	public void addQuestion(Integer id, String text) {
		Question question = new Question(text);
		Questionnaire questionnaire= em.find(Questionnaire.class, id);		
		questionnaire.addQuestion(question);
		em.persist(question);
		
	}
	
}
