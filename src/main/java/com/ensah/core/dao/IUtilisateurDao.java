package com.ensah.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ensah.core.bo.Utilisateur;

public interface IUtilisateurDao extends JpaRepository<Utilisateur, Long>, GenericJpa<Utilisateur, Long> {

}
