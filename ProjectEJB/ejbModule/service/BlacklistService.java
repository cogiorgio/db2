package service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;


import java.util.*;
import exceptions.BlacklistException;
import model.Blacklist;
import model.User;
import javax.persistence.Entity;

/**
 * Session Bean implementation class BlacklistService
 */
@Stateless
public class BlacklistService {
	
	@PersistenceContext(unitName = "projectEJB")
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public BlacklistService() {
        // TODO Auto-generated constructor stub
    }
    
    
    
    public void checkBlacklist(String inputText, User usr) throws BlacklistException
    {
    	System.out.println("checking if there are baddies");
    	boolean found = false;
    	List<Blacklist> bList = null;
    	try 
    	{
    		//bList = em.createQuery("SELECT b FROM Blacklist b").getResultList();
    		bList = em.createNamedQuery("Blacklist.findAll", Blacklist.class).getResultList(); 
    		
    	}
    	catch (PersistenceException e)
    	{
    		throw new BlacklistException("Cannot load Blacklist");
    	}
    	
    	
    	for (Blacklist badword : bList)
    	{
    		String baddies = badword.getBadwords();
    		if(inputText.contains(baddies))
    		{
    			found = true;
    			//System.out.println("baddies here");
    			break;
    		}
    	}
    	
    	
    	if(found == true)
    	{
    		System.out.println("baddies found, I'm blocking the user");
    		usr.setBlocked(found); //I'm getting the wrong user
    		em.merge(usr);
    	}
    	
    	System.out.println("done checking if there are baddies");
    }

}
