package view;

import controller.UsuarioController;
import model.UsuarioModel;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;

public class UsuarioView extends JFrame {

    private JPanel rootPanel;
    private JTextField txtNome;
    private JComboBox<String> cbSexo;
    private JFormattedTextField txtCelular;
    private JTextField txtEmail;
    private JButton btnSalvar;
    private JButton btnVoltar;

    private UsuarioController usuarioController = new UsuarioController();

    public UsuarioView() {
        setTitle("Cadastro de Usuário");
        setContentPane(rootPanel);
        setSize(500, 450);
        setLocationRelativeTo(null);

        configurarMascara();
        configurarCombo();
        configurarEventos();

        setVisible(true);
    }

    private void configurarMascara() {
        try {
            MaskFormatter mf = new MaskFormatter("(##) #####-####");
            mf.install(txtCelular);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void configurarCombo() {
        cbSexo.addItem("Masculino");
        cbSexo.addItem("Feminino");
        cbSexo.addItem("Outro");
    }

    private void configurarEventos() {

        btnSalvar.addActionListener(e -> salvarUsuario());

        btnVoltar.addActionListener(e -> this.dispose());
    }

    private void salvarUsuario() {

        try {
            UsuarioModel u = new UsuarioModel();

            u.setNome(txtNome.getText());
            u.setSexo(cbSexo.getSelectedItem().toString());
            u.setCelular(txtCelular.getText());
            u.setEmail(txtEmail.getText());

            String msg = usuarioController.salvar(u);
            JOptionPane.showMessageDialog(this, msg);

            limparCampos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage());
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtCelular.setText("");
        txtEmail.setText("");
        cbSexo.setSelectedIndex(0);
    }
}
