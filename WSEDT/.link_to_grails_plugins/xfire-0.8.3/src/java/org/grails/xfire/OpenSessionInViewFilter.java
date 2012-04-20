package org.grails.xfire;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessResourceFailureException;

public class OpenSessionInViewFilter extends
		org.springframework.orm.hibernate3.support.OpenSessionInViewFilter {

	protected void closeSession(Session arg0, SessionFactory arg1) {
		arg0.flush();
		super.closeSession(arg0, arg1);
	}

	protected Session getSession(SessionFactory arg0) throws DataAccessResourceFailureException {
		Session session = super.getSession(arg0);
		session.setFlushMode(FlushMode.AUTO);
		return session;
	}

}
