package view;

import controller.EmprestimoController;
import model.EmprestimoModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class RelatorioView extends JFrame {

    private JPanel rootPanel;
    private JTable tblRelatorio;
    private JButton btnVoltar;

    private EmprestimoController emprestimoController = new EmprestimoController();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public RelatorioView() {

        // ✅ GARANTE QUE OS COMPONENTES EXISTEM
        if (rootPanel == null) {
            rootPanel = new JPanel(new BorderLayout());
            tblRelatorio = new JTable();
            btnVoltar = new JButton("Voltar");

            rootPanel.add(new JScrollPane(tblRelatorio), BorderLayout.CENTER);
            rootPanel.add(btnVoltar, BorderLayout.SOUTH);
        }

        setTitle("Relatório de Empréstimos");
        setContentPane(rootPanel);
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        carregarRelatorio();
        configurarEventos();

        setVisible(true);
    }

    private void configurarEventos() {
        btnVoltar.addActionListener(e -> this.dispose());
    }

    private void carregarRelatorio() {

        List<EmprestimoModel> lista = emprestimoController.listarTodos();

        String[] colunas = {
                "ID",
                "Usuário",
                "Livro",
                "Data Início",
                "Prevista",
                "Data Devolução",
                "Atraso"
        };

        DefaultTableModel model = new DefaultTableModel(colunas, 0);

        for (EmprestimoModel e : lista) {

            boolean atrasado = emprestimoController.estaAtrasado(e);

            String usuario = (e.getUsuario() != null)
                    ? e.getUsuario().getNome()
                    : "Não informado";

            String livro = (e.getLivro() != null)
                    ? e.getLivro().getTitulo()
                    : "Não informado";

            String dataInicio = (e.getDataInicio() != null)
                    ? sdf.format(e.getDataInicio())
                    : "-";

            String dataPrevista = (e.getDataPrevista() != null)
                    ? sdf.format(e.getDataPrevista())
                    : "-";

            String dataDevolucao = (e.getDataDevolucao() == null)
                    ? "Não devolvido"
                    : sdf.format(e.getDataDevolucao());

            model.addRow(new Object[]{
                    e.getIdEmprestimo(),
                    usuario,
                    livro,
                    dataInicio,
                    dataPrevista,
                    dataDevolucao,
                    atrasado ? "SIM" : "NÃO"
            });
        }

        tblRelatorio.setModel(model);
        destacarAtrasados();
    }

    private void destacarAtrasados() {

        tblRelatorio.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                String atraso = table.getValueAt(row, 6).toString();

                if ("SIM".equals(atraso)) {
                    c.setBackground(Color.RED);
                    c.setForeground(Color.WHITE);
                } else if (isSelected) {
                    c.setBackground(new Color(51, 102, 255));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }

                return c;
            }
        });
    }

    public static void main(String[] args) {
        System.out.println("MAIN DA TELA EXECUTADO");
        SwingUtilities.invokeLater(RelatorioView::new);
    }
}
