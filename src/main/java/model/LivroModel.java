package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "livro")
public class LivroModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idLivro;

    private String titulo;
    private String tema;
    private String autor;
    private String isbn;
    private int quantidade;

    @Temporal(TemporalType.DATE)
    private Date dataPublicacao;

    // ✅ GETTERS E SETTERS CORRETOS
    public int getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(int idLivro) {
        this.idLivro = idLivro;
    }

    public String getTitulo(){
        return titulo;
    }

    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

    public String getTema(){
        return tema;
    }

    public void setTema(String tema){
        this.tema = tema;
    }

    public String getAutor(){
        return autor;
    }

    public void setAutor(String autor){
        this.autor = autor;
    }

    public String getIsbn(){
        return isbn;
    }

    public void setIsbn(String isbn){
        this.isbn = isbn;
    }

    public Date getDataPublicacao(){
        return dataPublicacao;
    }

    public void setDataPublicacao(Date dataPublicacao){
        this.dataPublicacao = dataPublicacao;
    }

    public int getQuantidade(){
        return quantidade;
    }

    public void setQuantidade(int quantidade){
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return titulo;
    }
}
