package com.ensah.core.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import com.ensah.core.bo.Compte;

public interface ICompteDao extends JpaRepository<Compte, Long>,
ICompteDaoCustom , GenericJpa<Compte, Long> {

}
