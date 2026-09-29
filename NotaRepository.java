package br.com.cafeeiradoisirmaos.sistema.repository;

import br.com.cafeeiradoisirmaos.sistema.model.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NotaRepository extends JpaRepository<Nota, Long> {

    /** Busca pelo numero formatado (ex.: 00001). */
    Optional<Nota> findByNumero(String numero);

    /** Lista todos os numeros, do maior para o menor, para gerar o proximo sequencial. */
    @Query("SELECT n.numero FROM Nota n ORDER BY n.numero DESC")
    List<String> listarNumerosOrdenados();

    /** Pesquisa combinada para a tela de lista (numero, nome e data). */
    @Query("SELECT n FROM Nota n WHERE " +
           "(:numero IS NULL OR n.numero LIKE CONCAT('%', :numero, '%')) AND " +
           "(:nome IS NULL OR LOWER(n.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
           "(:data IS NULL OR n.data = :data) " +
           "ORDER BY n.numero DESC")
    List<Nota> pesquisar(@Param("numero") String numero,
                         @Param("nome") String nome,
                         @Param("data") LocalDate data);

    /** 5 ultimas notas para o dashboard. */
    List<Nota> findTop5ByOrderByNumeroDesc();
}