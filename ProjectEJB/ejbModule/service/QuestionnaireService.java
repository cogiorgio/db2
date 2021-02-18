package service;

import java.sql.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import exceptions.QuestionnaireException;
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
    
	public void createQuestionnaire(String product, Date date) {
		Questionnaire q= new Questionnaire (product, date);
		em.persist(q);
		em.flush();
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
	
}
