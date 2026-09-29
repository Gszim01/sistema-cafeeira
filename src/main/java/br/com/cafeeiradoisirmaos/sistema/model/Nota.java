package br.com.cafeeiradoisirmaos.sistema.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Numero sequencial exibido com 5 digitos (00001, 00002...). */
    @Column(nullable = false, unique = true)
    private String numero;

    @NotBlank(message = "Informe o nome do cliente")
    private String nome;

    private String telefone;
    private String linha;
    private String observacoes;

    @NotNull(message = "Informe a quantidade de sacas")
    @Positive(message = "Sacas deve ser maior que zero")
    private BigDecimal sacas;

    @NotNull(message = "Informe a quantidade de quilos")
    @Positive(message = "Quilos deve ser maior que zero")
    private BigDecimal quilos;

    @NotNull(message = "Informe a umidade")
    @DecimalMin(value = "0", message = "Umidade nao pode ser negativa")
    private BigDecimal umidade;

    @NotNull(message = "Informe a data")
    private LocalDate data;

    private String assinatura;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    private LocalDateTime atualizadoEm;

    @PrePersist
    void aoSalvar() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = this.criadoEm;
    }

    @PreUpdate
    void aoAtualizar() {
        this.atualizadoEm = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getLinha() { return linha; }
    public void setLinha(String linha) { this.linha = linha; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public BigDecimal getSacas() { return sacas; }
    public void setSacas(BigDecimal sacas) { this.sacas = sacas; }
    public BigDecimal getQuilos() { return quilos; }
    public void setQuilos(BigDecimal quilos) { this.quilos = quilos; }
    public BigDecimal getUmidade() { return umidade; }
    public void setUmidade(BigDecimal umidade) { this.umidade = umidade; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public String getAssinatura() { return assinatura; }
    public void setAssinatura(String assinatura) { this.assinatura = assinatura; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
}
