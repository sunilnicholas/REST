package org.rest.common.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.rest.common.util.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;

/**
 * @author eugenp
 */
@Transactional( propagation = Propagation.SUPPORTS )
public class CustomHibernateDaoSupport< T extends Serializable > extends HibernateDaoSupport{
	
	protected Class< T > clazz;
	
	public CustomHibernateDaoSupport( final Class< T > theClassToSet ){
		super();
		
		this.clazz = theClassToSet;
	}
	
	// config
	
	@Autowired
	public final void init( final SessionFactory factory ){
		this.setSessionFactory( factory );
	}
	
	// API
	
	@Transactional( readOnly = true )
	public T getById( final Long id ){
		Preconditions.checkNotNull( id );
		
		final DetachedCriteria criteria = DetachedCriteria.forClass( this.clazz );
		criteria.add( Restrictions.eq( QueryUtil.ID, id ) );
		final List< T > entitiesInList = this.getHibernateTemplate().findByCriteria( criteria );
		
		if( entitiesInList.size() == 0 ){
			return null;
		}
		
		final T entity = entitiesInList.get( 0 );
		
		return entity;
	}
	
	@Transactional( readOnly = true )
	public T getByIdExperimental( final Long id ){
		Preconditions.checkNotNull( id );
		
		return this.getHibernateTemplate().get( this.clazz, id );
	}
	
}