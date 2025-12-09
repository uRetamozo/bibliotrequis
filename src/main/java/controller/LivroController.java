package controller;

import model.LivroModel;
import repository.LivroRepository;

import java.sql.SQLException;
import java.util.List;

public class LivroController {

    private final LivroRepository repo = LivroRepository.getInstance();

    public String salvar(LivroModel livro) {
        try {
            return repo.Salvar(livro);
        } catch (SQLException e) {
            return "Erro ao salvar livro: " + e.getMessage();
        }
    }

    public String editar(LivroModel livro) {
        try {
            return repo.editar(livro);
        } catch (Exception e) {
            return "Erro ao editar livro: " + e.getMessage();
        }
    }

    public String remover(LivroModel livro) {
        try {
            return repo.remover(livro);
        } catch (Exception e) {
            return "Erro ao remover livro: " + e.getMessage();
        }
    }

    public LivroModel buscarPorId(Long id) {
        return repo.buscarPorId(id);
    }

    public List<LivroModel> listarTodos() {
        return repo.listarTodos();
    }
}
