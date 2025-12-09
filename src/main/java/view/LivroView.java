package view;

import controller.LivroController;
import model.LivroModel;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LivroView extends JFrame {

    private JPanel rootPanel;
    private JTextField txtTitulo;
    private JTextField txtTema;
    private JTextField txtAutor;
    private JTextField txtISBN;
    private JFormattedTextField txtData;
    private JTextField txtQtd;

    private JButton btnSalvar;
    private JButton btnVoltar;

    private LivroController livroController = new LivroController();

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public LivroView() {

        setTitle("Cadastro de Livro");
        setContentPane(rootPanel);
        setSize(550, 420);
        setLocationRelativeTo(null);

        sdf.setLenient(false);

        configurarMascara();
        configurarEventos();

        setVisible(true);
    }


    private void configurarMascara() {
        configurarMascaraData(txtData);
    }

    private void configurarMascaraData(JFormattedTextField campo) {
        try {
            MaskFormatter mf = new MaskFormatter("##/##/####");
            mf.setPlaceholderCharacter('_'); // __/__/____
            mf.install(campo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configurarEventos() {
        btnSalvar.addActionListener(e -> salvarLivro());
        btnVoltar.addActionListener(e -> this.dispose());
    }

    private void salvarLivro() {

        try {
            if (txtTitulo.getText().isEmpty() ||
                    txtTema.getText().isEmpty() ||
                    txtAutor.getText().isEmpty() ||
                    txtISBN.getText().isEmpty() ||
                    txtQtd.getText().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Preencha todos os campos obrigatórios.",
                        "Campos inválidos",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }


            if (!dataEstaValida(txtData.getText())) {
                JOptionPane.showMessageDialog(this,
                        "Preencha a data corretamente no formato dd/MM/yyyy.",
                        "Data inválida",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int quantidade;
            try {
                quantidade = Integer.parseInt(txtQtd.getText());
                if (quantidade < 0) throw new NumberFormatException();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Quantidade inválida. Informe um número válido (zero ou maior).",
                        "Erro na quantidade",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            LivroModel l = new LivroModel();
            l.setTitulo(txtTitulo.getText());
            l.setTema(txtTema.getText());
            l.setAutor(txtAutor.getText());
            l.setIsbn(txtISBN.getText());

            Date dataPub = sdf.parse(txtData.getText());
            l.setDataPublicacao(dataPub);

            l.setQuantidade(quantidade);

            String msg = livroController.salvar(l);
            JOptionPane.showMessageDialog(this, msg);

            limparCampos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar livro: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        txtTitulo.setText("");
        txtTema.setText("");
        txtAutor.setText("");
        txtISBN.setText("");
        txtQtd.setText("");
        txtData.setText("");
    }

    private boolean dataEstaValida(String textoData) {


        if (textoData.contains("_")) {
            return false;
        }

        try {
            sdf.parse(textoData);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
