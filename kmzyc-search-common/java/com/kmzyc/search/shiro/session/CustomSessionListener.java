package com.kmzyc.search.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

public class CustomSessionListener implements SessionListener {

  private ShiroSessionRepository shiroSessionRepository;

  @Override
  public void onStart(Session session) {
    // TODO
    // System.out.println("on start");
  }

  @Override
  public void onStop(Session session) {
    // TODO
    // System.out.println("on stop");
  }

  @Override
  public void onExpiration(Session session) {
    shiroSessionRepository.deleteSession(session.getId());
  }

  public ShiroSessionRepository getShiroSessionRepository() {
    return shiroSessionRepository;
  }

  public void setShiroSessionRepository(ShiroSessionRepository shiroSessionRepository) {
    this.shiroSessionRepository = shiroSessionRepository;
  }

}
