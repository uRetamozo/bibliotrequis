package controller;

import model.EmprestimoModel;
import model.LivroModel;
import model.UsuarioModel;
import repository.EmprestimoRepository;
import repository.LivroRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.*;

public class EmprestimoController {

    private final EmprestimoRepository repo = EmprestimoRepository.getInstance();
    private final LivroRepository livroRepo = LivroRepository.getInstance();

    private int countEmprestimosAtivos(UsuarioModel usuario) {

        if (usuario == null || usuario.getIdUsuario() <= 0)
            return 0;

        int count = 0;
        List<EmprestimoModel> lista = repo.listarTodos();

        for (EmprestimoModel e : lista) {
            if (e.getUsuario() == null) continue;

            if (e.getUsuario().getIdUsuario() == usuario.getIdUsuario()
                    && e.getDataDevolucao() == null) {
                count++;
            }
        }

        return count;
    }

    private Date calcularDataPrevista(Date dataInicio) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataInicio);
        cal.add(Calendar.DAY_OF_MONTH, 14);
        return cal.getTime();
    }

    public String registrarEmprestimo(EmprestimoModel emprestimo) {

        try {
            if (emprestimo.getUsuario() == null ||
                    emprestimo.getUsuario().getIdUsuario() <= 0)
                return " Usuário inválido ou não selecionado.";

            if (emprestimo.getLivro() == null ||
                    emprestimo.getLivro().getIdLivro() <= 0)
                return " Livro inválido ou não selecionado.";

            UsuarioModel usuario = emprestimo.getUsuario();
            LivroModel livroBanco = emprestimo.getLivro();

            int quantidadeSolicitada = emprestimo.getQuantidade();

            if (countEmprestimosAtivos(usuario) >= 5)
                return " Usuário já possui 5 empréstimos ativos!";

            if (quantidadeSolicitada > livroBanco.getQuantidade())
                return " Não há exemplares suficientes disponíveis!";

            livroBanco.setQuantidade(
                    livroBanco.getQuantidade() - quantidadeSolicitada
            );
            livroRepo.editar(livroBanco);

            Date inicio = emprestimo.getDataInicio();
            emprestimo.setDataPrevista(calcularDataPrevista(inicio));
            emprestimo.setDataDevolucao(null);

            return repo.salvar(emprestimo);

        } catch (Exception e) {
            e.printStackTrace();
            return " Erro ao registrar empréstimo: " + e.getMessage();
        }
    }

    public String devolverLivro(int idEmprestimo, Date dataInformada) {

        EmprestimoModel emprestimo = repo.buscarPorId(idEmprestimo);

        if (emprestimo == null)
            return " Empréstimo não encontrado!";

        if (emprestimo.getDataDevolucao() != null)
            return "⚠ Este empréstimo já foi devolvido em: " +
                    emprestimo.getDataDevolucao();

        if (emprestimo.getLivro() == null)
            return " Este empréstimo não possui livro vinculado!";

        LivroModel livro = emprestimo.getLivro();

        livro.setQuantidade(
                livro.getQuantidade() + emprestimo.getQuantidade()
        );
        livroRepo.editar(livro);

        emprestimo.setDataDevolucao(dataInformada);
        repo.editar(emprestimo);

        long diasAtraso = calcularDiasAtraso(
                emprestimo.getDataPrevista(),
                dataInformada
        );

        if (diasAtraso > 0) {
            double multa = diasAtraso * 1.00; // R$ 1 por dia
            String multaF = String.format("%.2f", multa);
            return "⚠️ DEVOLUÇÃO EM ATRASO!\n" +
                    "Dias de atraso: " + diasAtraso + "\n" +
                    "Multa: R$ " + multaF;
        }

        return "✅ Devolução realizada no prazo!";
    }

    public long calcularDiasAtraso(Date dataPrevista, Date dataDevolucao) {

        if (dataPrevista == null || dataDevolucao == null) return 0;

        Calendar prev = Calendar.getInstance();
        Calendar dev = Calendar.getInstance();

        prev.setTime(dataPrevista);
        dev.setTime(dataDevolucao);

        prev.set(Calendar.HOUR_OF_DAY, 0);
        prev.set(Calendar.MINUTE, 0);
        prev.set(Calendar.SECOND, 0);
        prev.set(Calendar.MILLISECOND, 0);

        dev.set(Calendar.HOUR_OF_DAY, 0);
        dev.set(Calendar.MINUTE, 0);
        dev.set(Calendar.SECOND, 0);
        dev.set(Calendar.MILLISECOND, 0);

        long millis = dev.getTimeInMillis() - prev.getTimeInMillis();
        long dias = millis / (1000 * 60 * 60 * 24);

        return Math.max(dias, 0);
    }

    public boolean estaAtrasado(EmprestimoModel e) {

        if (e == null) return false;
        if (e.getDataPrevista() == null) return false;

        Date dataFinal;

        if (e.getDataDevolucao() != null) {
            dataFinal = e.getDataDevolucao();
        }
        else {
            dataFinal = new Date();
        }

        return dataFinal.after(e.getDataPrevista());
    }

    public EmprestimoModel buscarPorId(int id) {
        return repo.buscarPorId(id);
    }

    public String editar(EmprestimoModel emprestimo) {
        return repo.editar(emprestimo);
    }

    public String remover(EmprestimoModel emprestimo) {
        return repo.remover(emprestimo);
    }

    public List<EmprestimoModel> listarTodos() {
        List<EmprestimoModel> lista = repo.listarTodos();
        System.out.println("TOTAL DE EMPRÉSTIMOS: " + lista.size());
        return lista;
    }



}
