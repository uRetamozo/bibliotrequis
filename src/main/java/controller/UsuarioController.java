package controller;

import model.UsuarioModel;
import repository.UsuarioRepository;

import java.sql.SQLException;
import java.util.List;

public class UsuarioController {

    private final UsuarioRepository repo = UsuarioRepository.getInstance();

    public String salvar(UsuarioModel usuario) {
        try {
            return repo.Salvar(usuario);
        } catch (SQLException e) {
            return "Erro ao salvar usuário: " + e.getMessage();
        }
    }

    public String editar(UsuarioModel usuario) {
        try {
            return repo.editar(usuario);
        } catch (Exception e) {
            return "Erro ao editar usuário: " + e.getMessage();
        }
    }

    public String remover(UsuarioModel usuario) {
        try {
            return repo.remover(usuario);
        } catch (Exception e) {
            return "Erro ao remover usuário: " + e.getMessage();
        }
    }

    public UsuarioModel buscarPorId(Long id) {
        return repo.buscarPorId(id);
    }

    public List<UsuarioModel> listarTodos() {
        return repo.listarTodos();
    }



}
