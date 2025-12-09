package view;

import controller.EmprestimoController;
import controller.LivroController;
import controller.UsuarioController;
import model.EmprestimoModel;
import model.LivroModel;
import model.UsuarioModel;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EmprestimoView extends JFrame {

    private JComboBox<UsuarioModel> cbUsuario;
    private JComboBox<LivroModel> cbLivro;
    private JTextField txtDisponivel;
    private JSpinner spQuantidade;
    private JFormattedTextField txtDataInicio;
    private JTextField txtDataPrevista;
    private JButton btnRegistrar;
    private JButton btnVoltar;

    private final UsuarioController usuarioController = new UsuarioController();
    private final LivroController livroController = new LivroController();
    private final EmprestimoController emprestimoController = new EmprestimoController();

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public EmprestimoView() {
        setTitle("Empréstimo de Livros");
        setSize(520, 430);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        montarTela();
        carregarUsuarios();
        carregarLivros();
        configurarEventos();

        setVisible(true);
    }

    private void montarTela() {
        JPanel painel = new JPanel(new GridLayout(8, 2, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        cbUsuario = new JComboBox<>();
        cbLivro = new JComboBox<>();
        txtDisponivel = new JTextField();
        spQuantidade = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        txtDataInicio = new JFormattedTextField();
        txtDataPrevista = new JTextField();
        btnRegistrar = new JButton("CONFIRMAR EMPRÉSTIMO");
        btnVoltar = new JButton("VOLTAR");

        txtDisponivel.setEditable(false);
        txtDataPrevista.setEditable(false);

        aplicarMascaraData(txtDataInicio);

        txtDataInicio.setText(sdf.format(new Date()));
        atualizarDataPrevista();

        painel.add(new JLabel("Usuário:"));
        painel.add(cbUsuario);

        painel.add(new JLabel("Livro:"));
        painel.add(cbLivro);

        painel.add(new JLabel("Disponíveis:"));
        painel.add(txtDisponivel);

        painel.add(new JLabel("Quantidade:"));
        painel.add(spQuantidade);

        painel.add(new JLabel("Data do Empréstimo:"));
        painel.add(txtDataInicio);

        painel.add(new JLabel("Data Devolução Prevista:"));
        painel.add(txtDataPrevista);

        painel.add(btnVoltar);
        painel.add(btnRegistrar);

        setContentPane(painel);
    }

    private void aplicarMascaraData(JFormattedTextField campo) {
        try {
            MaskFormatter mf = new MaskFormatter("##/##/####");
            mf.setPlaceholderCharacter('_');
            mf.install(campo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro na máscara de data.");
        }
    }

    private void atualizarDataPrevista() {
        try {
            Date inicio = sdf.parse(txtDataInicio.getText());

            Calendar cal = Calendar.getInstance();
            cal.setTime(inicio);
            cal.add(Calendar.DAY_OF_MONTH, 14);

            txtDataPrevista.setText(sdf.format(cal.getTime()));
        } catch (Exception e) {
            txtDataPrevista.setText("");
        }
    }

    private void carregarUsuarios() {
        cbUsuario.removeAllItems();

        var lista = usuarioController.listarTodos();

        if (lista == null || lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum usuário cadastrado.");
            return;
        }

        for (UsuarioModel u : lista) {
            cbUsuario.addItem(u);
        }
    }

    private void carregarLivros() {
        cbLivro.removeAllItems();

        var lista = livroController.listarTodos();

        if (lista == null || lista.isEmpty()) {
            txtDisponivel.setText("");
            JOptionPane.showMessageDialog(this, "Nenhum livro cadastrado.");
            return;
        }

        for (LivroModel l : lista) {
            cbLivro.addItem(l);
        }

        cbLivro.setSelectedIndex(0);

        LivroModel l = (LivroModel) cbLivro.getSelectedItem();
        txtDisponivel.setText(String.valueOf(l.getQuantidade()));

        spQuantidade.setValue(1);
    }

    private void configurarEventos() {

        // ✅ Atualiza disponibilidade ao trocar livro
        cbLivro.addActionListener(e -> {
            Object obj = cbLivro.getSelectedItem();

            if (obj == null) {
                txtDisponivel.setText("");
                return;
            }

            LivroModel livro = (LivroModel) obj;
            txtDisponivel.setText(String.valueOf(livro.getQuantidade()));
            spQuantidade.setValue(1);
        });

        txtDataInicio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent e) {
                atualizarDataPrevista();
            }
        });

        btnRegistrar.addActionListener(e -> registrarEmprestimo());
        btnVoltar.addActionListener(e -> dispose());
    }

    private void registrarEmprestimo() {

        try {
            UsuarioModel usuario = (UsuarioModel) cbUsuario.getSelectedItem();
            LivroModel livro = (LivroModel) cbLivro.getSelectedItem();
            int quantidadeSolicitada = (int) spQuantidade.getValue();

            if (usuario == null) {
                JOptionPane.showMessageDialog(this, "Selecione um usuário.");
                return;
            }

            if (livro == null) {
                JOptionPane.showMessageDialog(this, "Selecione um livro.");
                return;
            }

            if (quantidadeSolicitada > livro.getQuantidade()) {
                JOptionPane.showMessageDialog(this,
                        "Quantidade solicitada maior que o estoque disponível!");
                return;
            }

            EmprestimoModel emp = new EmprestimoModel();
            emp.setUsuario(usuario);
            emp.setLivro(livro);
            emp.setQuantidade(quantidadeSolicitada);

            emp.setDataInicio(sdf.parse(txtDataInicio.getText()));
            emp.setDataPrevista(sdf.parse(txtDataPrevista.getText()));
            emp.setDataDevolucao(null);

            String msg = emprestimoController.registrarEmprestimo(emp);
            JOptionPane.showMessageDialog(this, msg);

            carregarLivros();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erro ao registrar empréstimo:\n" + e.getMessage());
        }
    }
}
