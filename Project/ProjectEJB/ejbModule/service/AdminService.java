package service;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import exceptions.CredentialsException;
import exceptions.UpdateProfileException;
import model.Admin;
import model.User;

/**
 * Session Bean implementation class AdminService
 */
@Stateless
public class AdminService {
	@PersistenceContext(unitName = "projectEJB")
	private EntityManager em;
	
    public AdminService() {}
        // TODO Auto-generated constructor stub
  	
    	public Admin checkCredentials(String usrn, String pwd) throws CredentialsException, NonUniqueResultException {
    		List<Admin> aList = null;
    		try {
    			aList = em.createNamedQuery("Admin.checkCredentials", Admin.class).setParameter(1, usrn).setParameter(2, pwd)
    					.getResultList();
    		} catch (PersistenceException e) {
    			throw new CredentialsException("Could not verify credentals");
    		}
    		if (aList.isEmpty())
    			
    			return null;
    		else if (aList.size() == 1)
    			
    			return aList.get(0);
    		throw new CredentialsException("More than one admin registered with same credentials");
    	}

    	public void updateProfile(Admin a) throws UpdateProfileException {
    		try {
    			em.merge(a);
    		} catch (PersistenceException e) {
    			throw new UpdateProfileException("Could not change profile");
    		}
    	}
    	
    	public Admin signIn(String username,String password, String mail) throws CredentialsException {
    		List<Admin> aList = null;
 
    		try {
    			aList = em.createNamedQuery("Admin.checkUsername", Admin.class).setParameter(1, username).getResultList();
    		} catch (PersistenceException e) {
    			throw new CredentialsException("Could not verify credentals");
    		}
    		if (aList.isEmpty()) {
    			Admin a=new Admin(username, password, mail);
    			em.persist(a);
    			return a;
    		}	
    		else if (aList.size() >= 1) 
    			throw new CredentialsException("Username already used.");
    		return null;
    	}

}
