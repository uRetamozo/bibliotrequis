package view;

import javax.swing.*;

public class MainView extends JFrame {

    private JPanel rootPanel;
    private JButton btnLivros;
    private JButton btnUsuarios;
    private JButton btnEmprestimo;
    private JButton btnDevolucao;
    private JButton btnListar;   // Listagem de empréstimos (Relatório)
    private JButton btnListarr;  // Listagem de usuários (se quiser usar futuramente)
    private JButton btnSair;

    public MainView() {
        setTitle("Sistema de Gestão de Biblioteca");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(rootPanel);
        setSize(500, 450);
        setLocationRelativeTo(null);

        configurarEventos();
    }

    private void configurarEventos() {


        btnLivros.addActionListener(e -> new LivroView());
        btnUsuarios.addActionListener(e -> new UsuarioView());
        btnEmprestimo.addActionListener(e -> new EmprestimoView());
        btnDevolucao.addActionListener(e -> new DevolucaoView());
        btnListar.addActionListener(e -> new RelatorioView());
        btnListarr.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Tela de listagem de usuários ainda não implementada.")
        );

        btnSair.addActionListener(e -> {
            int r = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja sair?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION
            );

            if (r == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainView().setVisible(true));
    }
}
