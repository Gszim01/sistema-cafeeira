package br.com.cafeeiradoisirmaos.sistema.controller;

import br.com.cafeeiradoisirmaos.sistema.model.Nota;
import br.com.cafeeiradoisirmaos.sistema.service.NotaService;
import br.com.cafeeiradoisirmaos.sistema.service.PdfGeradorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Controller
public class NotaController {

    private final NotaService notaService;
    private final PdfGeradorService pdfGeradorService;

    public NotaController(NotaService notaService, PdfGeradorService pdfGeradorService) {
        this.notaService = notaService;
        this.pdfGeradorService = pdfGeradorService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("totalNotas", notaService.contarNotas());
        model.addAttribute("totalSacas", notaService.totalSacas());
        model.addAttribute("ultimasNotas", notaService.ultimasNotas());
        return "dashboard";
    }

    @GetMapping("/notas/nova")
    public String nova(Model model) {
        Nota nota = new Nota();
        nota.setData(LocalDate.now());
        model.addAttribute("nota", nota);
        return "formulario";
    }

    @PostMapping("/notas")
    public String salvar(@Valid @ModelAttribute("nota") Nota nota,
                         BindingResult resultado,
                         Model model) {
        if (resultado.hasErrors()) {
            return "formulario";
        }

        Nota salvo = notaService.salvar(nota);
        model.addAttribute("mensagem", "Nota " + salvo.getNumero() + " salva com sucesso!");
        model.addAttribute("nota", salvo);
        return "visualizar";
    }

    @GetMapping("/notas")
    public String listar(@RequestParam(required = false) String numero,
                         @RequestParam(required = false) String nome,
                         @RequestParam(required = false) String data,
                         Model model) {
        LocalDate dataFiltro = converterData(data);
        model.addAttribute("notas", notaService.pesquisar(numero, nome, dataFiltro));
        model.addAttribute("filtroNumero", numero);
        model.addAttribute("filtroNome", nome);
        model.addAttribute("filtroData", data);
        return "lista";
    }

    @GetMapping("/notas/{id}")
    public String visualizar(@PathVariable Long id, Model model) {
        model.addAttribute("nota", buscarNota(id));
        return "visualizar";
    }

    @GetMapping("/notas/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("nota", buscarNota(id));
        return "formulario";
    }

    @PostMapping("/notas/{id}")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute("nota") Nota nota,
                            BindingResult resultado,
                            Model model) {
        if (resultado.hasErrors()) {
            return "formulario";
        }

        Nota original = buscarNota(id);
        nota.setId(id);
        nota.setNumero(original.getNumero());
        Nota atualizada = notaService.salvar(nota);
        model.addAttribute("mensagem",
                "Nota " + atualizada.getNumero() + " atualizada com sucesso!");
        model.addAttribute("nota", atualizada);
        return "visualizar";
    }

    @PostMapping("/notas/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        notaService.excluir(id);
        return "redirect:/notas";
    }

    @GetMapping("/notas/{id}/pdf")
    public ResponseEntity<byte[]> gerarPdf(@PathVariable Long id) {
        Nota nota = buscarNota(id);
        byte[] pdf = pdfGeradorService.gerarPdf(nota);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=nota-" + nota.getNumero() + ".pdf")
                .body(pdf);
    }

    private Nota buscarNota(Long id) {
        return notaService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Nota nao encontrada"));
    }

    private LocalDate converterData(String data) {
        if (data == null || data.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(data);
        } catch (DateTimeParseException exception) {
            return null;
        }
    }
}
