package repository;

import model.EmprestimoModel;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmprestimoRepository {

    private static EmprestimoRepository instance;
    private static EntityManagerFactory factory;

    private EmprestimoRepository() {
        factory = Persistence.createEntityManagerFactory("crudHibernatePU");
    }

    public static EmprestimoRepository getInstance() {
        if (instance == null) {
            instance = new EmprestimoRepository();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    public String salvar(EmprestimoModel emprestimo) {
        EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(emprestimo);
            em.getTransaction().commit();
            return "✅ Empréstimo registrado com sucesso!";
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return "❌ Erro ao salvar empréstimo: " + e.getMessage();
             } finally {
            em.close();
        }
    }

    public String editar(EmprestimoModel emprestimo) {
        EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();
            em.merge(emprestimo);
            em.getTransaction().commit();
            return "✅ Empréstimo atualizado!";
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return "❌ Erro ao editar empréstimo: " + e.getMessage();
        } finally {
            em.close();
        }
    }

    public String remover(EmprestimoModel emprestimo) {
        EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();
            EmprestimoModel emp = em.find(EmprestimoModel.class, emprestimo.getIdEmprestimo());
            if (emp != null) {
                em.remove(emp);
            }
            em.getTransaction().commit();
            return "✅ Empréstimo removido!";
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return "❌ Erro ao remover empréstimo: " + e.getMessage();
        } finally {
            em.close();
        }
    }


    public List<EmprestimoModel> listarTodos() {
        EntityManager em = getEntityManager();

        try {
            List<EmprestimoModel> lista = em.createNativeQuery(
                    "SELECT * FROM emprestimo",
                    EmprestimoModel.class
            ).getResultList();

            System.out.println("TOTAL LISTAR TODOS: " + lista.size());
            return lista;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            em.close();
        }
    }

    public EmprestimoModel buscarPorId(int id) {
        EntityManager em = getEntityManager();

        try {
            return em.find(EmprestimoModel.class, id);
        } finally {
            em.close();
        }
    }

    public boolean estaAtrasado(EmprestimoModel e) {
        return e.getDataDevolucao() == null &&
                new Date().after(e.getDataPrevista());
    }
}
