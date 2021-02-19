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
    		bList = em.createQuery("SELECT b FROM Blacklist b").getResultList();
    		//bList = em.createNamedQuery("Blacklist.findALL", Blacklist.class).getResultList(); 
    		//NamedQueryNotFound wtf!?
    		//TypedQuery<Blacklist> query = em.createNamedQuery("Blacklist.findALL", Blacklist.class);
    		//bList = query.getResultList();
    	}
    	catch (PersistenceException e)
    	{
    		throw new BlacklistException("Cannot load Blacklist");
    	}
    	
    	//String [] baddies = new String[bList.size()]; 
    	
    	//baddies = ((Blacklist) bList).getBadwords().toArray();
    	
    	String baddies = "$";
    	for (Blacklist badword : bList)
    	{
    		baddies = badword.getBadwords();
    		if(inputText.contains(baddies))
    		{
    			found = true;
    			System.out.println("baddies here");
    			break;
    		}
    	}
    	
    	
    	if(found == true)
    	{
    		System.out.println("baddies found");
    		usr.setBlocked(found); //I'm getting the wrong user
    		em.persist(usr);
    	}
    	
    	System.out.println("done checking if there are baddies");
    }

}
