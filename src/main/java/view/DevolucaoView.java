package view;

import controller.EmprestimoController;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DevolucaoView extends JFrame {

    private JPanel rootPanel;
    private JTextField txtIdEmprestimo;
    private JFormattedTextField txtDataDevolucao;
    private JButton btnDevolver;
    private JButton btnVoltar;

    private final EmprestimoController emprestimoController = new EmprestimoController();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public DevolucaoView() {
        setTitle("Devolução de Livros");

        if (rootPanel == null) {
            rootPanel = new JPanel();
            rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));

            rootPanel.add(new JLabel("ID do Empréstimo:"));
            txtIdEmprestimo = new JTextField();
            rootPanel.add(txtIdEmprestimo);

            rootPanel.add(new JLabel("Data da Devolução:"));
            txtDataDevolucao = new JFormattedTextField();
            rootPanel.add(txtDataDevolucao);

            btnDevolver = new JButton("DEVOLVER");
            btnVoltar = new JButton("VOLTAR");

            rootPanel.add(btnDevolver);
            rootPanel.add(btnVoltar);
        }

        setContentPane(rootPanel);
        setSize(450, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        configurarMascara();
        configurarEventos();

        setVisible(true);
    }

    private void configurarMascara() {
        try {
            MaskFormatter mf = new MaskFormatter("##/##/####");
            mf.install(txtDataDevolucao);
            txtDataDevolucao.setText(sdf.format(new Date()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao aplicar máscara de data.");
        }
    }

    private void configurarEventos() {

        btnDevolver.addActionListener(e -> registrarDevolucao());

        btnVoltar.addActionListener(e -> dispose());
    }

    private void registrarDevolucao() {

        try {
            if (txtIdEmprestimo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Informe o ID do empréstimo.");
                return;
            }

            int id = Integer.parseInt(txtIdEmprestimo.getText().trim());

            Date data = sdf.parse(txtDataDevolucao.getText().trim());

            String msg = emprestimoController.devolverLivro(id, data);

            JOptionPane.showMessageDialog(this, msg);

            txtIdEmprestimo.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Digite um ID válido (somente números).");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao registrar devolução: " + ex.getMessage());
        }
    }
}
