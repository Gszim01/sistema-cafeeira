package br.com.cafeeiradoisirmaos.sistema.service;

import br.com.cafeeiradoisirmaos.sistema.model.Nota;
import br.com.cafeeiradoisirmaos.sistema.repository.NotaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class NotaService {

    private final NotaRepository notaRepository;

    public NotaService(NotaRepository notaRepository) {
        this.notaRepository = notaRepository;
    }

    @Transactional
    public Nota salvar(Nota nota) {
        if (nota.getNumero() == null || nota.getNumero().isBlank()) {
            nota.setNumero(gerarProximoNumero());
        }
        return notaRepository.save(nota);
    }

    @Transactional(readOnly = true)
    public List<Nota> listarTodas() {
        return notaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Nota> buscarPorId(Long id) {
        return notaRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Nota> buscarPorNumero(String numero) {
        return notaRepository.findByNumero(numero);
    }

    @Transactional(readOnly = true)
    public List<Nota> pesquisar(String numero, String nome, LocalDate data) {
        String numeroFiltro = normalizarFiltro(numero);
        String nomeFiltro = normalizarFiltro(nome);
        return notaRepository.pesquisar(numeroFiltro, nomeFiltro, data);
    }

    @Transactional(readOnly = true)
    public List<Nota> ultimasNotas() {
        return notaRepository.findTop5ByOrderByNumeroDesc();
    }

    @Transactional(readOnly = true)
    public List<Nota> listarUltimas() {
        return ultimasNotas();
    }

    @Transactional(readOnly = true)
    public long contarNotas() {
        return notaRepository.count();
    }

    @Transactional(readOnly = true)
    public BigDecimal totalSacas() {
        return notaRepository.findAll().stream()
                .map(Nota::getSacas)
                .filter(sacas -> sacas != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public void excluir(Long id) {
        notaRepository.deleteById(id);
    }

    private String gerarProximoNumero() {
        int maiorNumero = notaRepository.listarNumerosOrdenados().stream()
                .mapToInt(this::converterNumero)
                .max()
                .orElse(0);
        return "%05d".formatted(maiorNumero + 1);
    }

    private int converterNumero(String numero) {
        try {
            return Integer.parseInt(numero);
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    private String normalizarFiltro(String filtro) {
        return filtro == null || filtro.isBlank() ? null : filtro.trim();
    }
}
