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
    
	public Questionnaire createQuestionnaire(String product, Date date) throws QuestionnaireException {
		Questionnaire q= em.createNamedQuery("findByDate", Questionnaire.class).setParameter("date", date).getSingleResult();
		
		if(q==null) {
			q= new Questionnaire (product, date);
			em.persist(q);
			em.flush();
			return q;
		}
		else throw new QuestionnaireException("A questionnaire of the day already exists");
	}

	public void deleteQuestionnaire(Integer qId, Date today) throws QuestionnaireException {
		Questionnaire q = em.find(Questionnaire.class, qId);
		if (q == null)
			return;
		
		if(q.getDate().before(today)) {
		
			em.remove(q);
		}
		
		else throw new QuestionnaireException("Not allowed to delete this questionnaire");
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
