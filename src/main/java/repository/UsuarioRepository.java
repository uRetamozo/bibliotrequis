package repository;

import model.UsuarioModel;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {

    private static UsuarioRepository instance;
    protected EntityManager entityManager;

    public UsuarioRepository() {
        entityManager = getEntityManager();
    }

    private EntityManager getEntityManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("crudHibernatePU");
        if (entityManager == null) {
            entityManager = factory.createEntityManager();
        }
        return entityManager;
    }

    public static UsuarioRepository getInstance() {
        if (instance == null) {
            instance = new UsuarioRepository();
        }
        return instance;
    }

    public String Salvar(UsuarioModel usuario) throws SQLException {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(usuario);
            entityManager.getTransaction().commit();
            return "Usuário salvo com sucesso!";
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            return e.getMessage();
        }
    }

    public String remover(UsuarioModel usuario) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(entityManager.contains(usuario) ? usuario : entityManager.merge(usuario));
            entityManager.getTransaction().commit();
            return "Usuário removido com sucesso!";
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            return "Erro ao remover usuário: " + e.getMessage();
        }
    }

    public String editar(UsuarioModel usuario) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(usuario);
            entityManager.getTransaction().commit();
            return "Usuário editado!";
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            return e.getMessage();
        }
    }

    public List<UsuarioModel> listarTodos() {
        try {
            return entityManager.createQuery("FROM UsuarioModel", UsuarioModel.class).getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public UsuarioModel buscarPorId(Long id) {
        try {
            return entityManager.find(UsuarioModel.class, id);
        } catch (Exception e) {
            return null;
        }
    }
}
