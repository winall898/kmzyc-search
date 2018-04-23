package com.kmzyc.search.shiro.session;

import java.io.Serializable;
import java.util.Collection;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;

/**
 * custom shiro sessionDAO
 * 
 * @author river
 */
public class CustomShiroSessionDAO extends AbstractSessionDAO {

  private ShiroSessionRepository shiroSessionRepository;

  @Override
  public void update(Session session) throws UnknownSessionException {
    getShiroSessionRepository().saveSession(session);
  }

  @Override
  public void delete(Session session) {
    if (session == null) {
      return;
    }
    Serializable id = session.getId();
    if (id != null) {
      getShiroSessionRepository().deleteSession(id);
    }
    // TODO if session is too large,when session destory clear shiro cache
  }

  @Override
  public Collection<Session> getActiveSessions() {
    return getShiroSessionRepository().getAllSessions();
  }

  @Override
  protected Serializable doCreate(Session session) {
    Serializable sessionId = this.generateSessionId(session);
    this.assignSessionId(session, sessionId);
    getShiroSessionRepository().saveSession(session);
    return sessionId;
  }

  @Override
  protected Session doReadSession(Serializable sessionId) {
    return getShiroSessionRepository().getSession(sessionId);
  }

  public ShiroSessionRepository getShiroSessionRepository() {
    return shiroSessionRepository;
  }

  public void setShiroSessionRepository(ShiroSessionRepository shiroSessionRepository) {
    this.shiroSessionRepository = shiroSessionRepository;
  }

}
