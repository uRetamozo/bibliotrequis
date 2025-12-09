package repository;

import model.LivroModel;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LivroRepository {

    private static LivroRepository instance;
    protected EntityManager entityManager;

    public LivroRepository() {
        entityManager = getEntityManager();
    }

    private EntityManager getEntityManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("crudHibernatePU");
        if (entityManager == null) {
            entityManager = factory.createEntityManager();
        }
        return entityManager;
    }

    public static LivroRepository getInstance() {
        if (instance == null) {
            instance = new LivroRepository();
        }
        return instance;
    }

    public String Salvar(LivroModel livro) throws SQLException {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(livro);
            entityManager.getTransaction().commit();
            return "Livro salvo com sucesso!";
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            return e.getMessage();
        }
    }

    public String remover(LivroModel livro) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(entityManager.contains(livro) ? livro : entityManager.merge(livro));
            entityManager.getTransaction().commit();
            return "Livro removido com sucesso!";
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            return "Erro ao remover o livro: " + e.getMessage();
        }
    }

    public String editar(LivroModel livro) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(livro);
            entityManager.getTransaction().commit();
            return "Livro editado!";
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            return e.getMessage();
        }
    }

    public List<LivroModel> listarTodos() {
        try {
            return entityManager.createQuery("FROM LivroModel", LivroModel.class).getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public LivroModel buscarPorId(Long id) {
        try {
            return entityManager.find(LivroModel.class, id);
        } catch (Exception e) {
            return null;
        }
    }
}
