package com.ensah.core.dao.impl;



import org.springframework.stereotype.Repository;

import com.ensah.core.bo.Compte;
import com.ensah.core.dao.ICompteDaoCustom;

@Repository
public class CompteDaoCustomImpl extends GenericJpaImpl<Compte, Long> implements ICompteDaoCustom {

	
	public Compte searchByLogin(String login) {
	
		return getEntityByColValue(Compte.class, "login", login);
	}
}
