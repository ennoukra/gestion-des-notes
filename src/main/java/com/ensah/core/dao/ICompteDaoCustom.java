package com.ensah.core.dao;

import com.ensah.core.bo.Compte;

public interface ICompteDaoCustom {
	public Compte searchByLogin(String username);

}
