package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "emprestimo")
public class EmprestimoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEmprestimo")
    private int idEmprestimo;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private UsuarioModel usuario;

    @ManyToOne
    @JoinColumn(name = "idLivro", nullable = false)
    private LivroModel livro;

    // ✅ DATETIME(6)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dataInicio")
    private Date dataInicio;

    // ✅ DATE
    @Temporal(TemporalType.DATE)
    @Column(name = "dataPrevista")
    private Date dataPrevista;

    // ✅ DATE
    @Temporal(TemporalType.DATE)
    @Column(name = "dataDevolucao")
    private Date dataDevolucao;

    @Column(name = "quantidade", nullable = false)
    private int quantidade;


    public int getIdEmprestimo() {
        return idEmprestimo;
    }

    public UsuarioModel getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioModel usuario) {
        this.usuario = usuario;
    }

    public LivroModel getLivro() {
        return livro;
    }

    public void setLivro(LivroModel livro) {
        this.livro = livro;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataPrevista() {
        return dataPrevista;
    }

    public void setDataPrevista(Date dataPrevista) {
        this.dataPrevista = dataPrevista;
    }

    public Date getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(Date dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
