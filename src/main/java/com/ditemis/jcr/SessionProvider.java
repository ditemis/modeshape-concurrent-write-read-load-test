package com.ditemis.jcr;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.jcr.Credentials;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

@RequestScoped
public class SessionProvider {
    
    @Resource(mappedName = "java:/jcr/test")
    private Repository repository;

    @RequestScoped
    @Produces
    public Session getSession() throws RepositoryException {
        Credentials credentials = new SimpleCredentials("jcradmin", "jcradmin".toCharArray());
        return repository.login(credentials, "core");
    }
    
    public void logoutSession( @Disposes final Session session ) {
        session.logout();
    }
}
