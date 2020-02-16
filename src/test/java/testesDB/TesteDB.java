package testesDB;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.jsf.model.IdadeSuino;
import com.jsf.model.Perfil;

public class TesteDB {

	public static void main(String[] args) {

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("suino");

		EntityManager em = factory.createEntityManager();

		EntityTransaction trx = em.getTransaction();

		trx.begin();
		
		/*//Usuario
		INSERT INTO `Usuario`( `ativo`, `cpf`, `dataRegistro`, `login`, `nome`, `senha`, `idPerfil`) 
		VALUES (true,'12343567898','2019-01-18 13:46:30','admin','SUPER ADMINISTRADOR','7c4a8d09ca3762af61e59520943dc26494f8941b',1)   */
		
		//Perfil perfil = new Perfil();
		
		//perfil.setNome("ADMINISTRADOR");
		//perfil.setNome("COMUM");
		
//	IdadeSuino idade = new IdadeSuino();
//		
//		//idade.setNome("FILHOTE");
//		//idade.setDescricao("De 0 a 12 meses");
//		
//		//idade.setNome("LEITÃO");
//		//idade.setDescricao("De 13 a 23 meses");
//		
//		idade.setNome("ADULTO");
//		idade.setDescricao("Mais que 24 meses");
//
//		em.persist(idade);
	
		trx.commit();

	}

}
