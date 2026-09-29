package br.com.cafeeiradoisirmaos.sistema.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import br.com.cafeeiradoisirmaos.sistema.model.Nota;

@Service
public class PdfGeradorService {

    private static final Color AZUL = new Color(26, 79, 139);
    private static final Color AZUL_CLARO = new Color(232, 240, 249);
    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] gerar(Nota nota) {
        return gerarPdf(nota);
    }

    public byte[] gerarPdf(Nota nota) {
        Objects.requireNonNull(nota, "A nota nao pode ser nula");

        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        try {
            Document documento = new Document(PageSize.A4, 30, 30, 30, 30);
            PdfWriter.getInstance(documento, saida);
            documento.open();

            adicionarCabecalho(documento, nota);
            adicionarIdentificacao(documento, nota);
            adicionarDados(documento, nota);
            adicionarMedidas(documento, nota);
            adicionarAssinatura(documento, nota);

            documento.close();
            return saida.toByteArray();
        } catch (DocumentException exception) {
            throw new IllegalStateException("Nao foi possivel gerar o PDF da nota", exception);
        }
    }

    private void adicionarCabecalho(Document documento, Nota nota) throws DocumentException {
        Font tituloFonte = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.WHITE);
        Font detalhesFonte = FontFactory.getFont(FontFactory.HELVETICA, 9, Color.WHITE);

        PdfPTable cabecalho = new PdfPTable(1);
        cabecalho.setWidthPercentage(100);

        PdfPCell celula = new PdfPCell();
        celula.setBackgroundColor(AZUL);
        celula.setHorizontalAlignment(Element.ALIGN_CENTER);
        celula.setPadding(12);

        Paragraph nome = new Paragraph("CAFEEIRA 2 IRMAOS", tituloFonte);
        nome.setAlignment(Element.ALIGN_CENTER);
        celula.addElement(nome);

        Paragraph detalhes = new Paragraph(
                "CNPJ 38.079.416/0001-10 - (69) 99290-7581 - "
                        + "LH 102 Km Lado Sul - Sao Miguel do Guapore - RO",
                detalhesFonte);
        detalhes.setAlignment(Element.ALIGN_CENTER);
        celula.addElement(detalhes);

        cabecalho.addCell(celula);
        documento.add(cabecalho);
        documento.add(new Paragraph(" "));
    }

    private void adicionarIdentificacao(Document documento, Nota nota) throws DocumentException {
        Font textoFonte = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
        Font numeroFonte = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);

        PdfPTable tabela = new PdfPTable(2);
        tabela.setWidthPercentage(100);

        PdfPCell titulo = new PdfPCell(new Phrase(
                "NOTA DE CONTROLE DE SECAGEM E BENEFICIAMENTO", textoFonte));
        titulo.setBorder(PdfPCell.NO_BORDER);
        tabela.addCell(titulo);

        PdfPCell numero = new PdfPCell(new Phrase("N " + texto(nota.getNumero()), numeroFonte));
        numero.setBackgroundColor(AZUL);
        numero.setHorizontalAlignment(Element.ALIGN_RIGHT);
        numero.setBorder(PdfPCell.NO_BORDER);
        tabela.addCell(numero);

        documento.add(tabela);
        documento.add(new Paragraph(" "));
    }

    private void adicionarDados(Document documento, Nota nota) throws DocumentException {
        Font valorFonte = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);

        PdfPTable cliente = new PdfPTable(2);
        cliente.setWidthPercentage(100);
        cliente.setWidths(new float[]{70, 30});
        cliente.addCell(campo("Nome: ", valorOuTraco(nota.getNome()), valorFonte));
        cliente.addCell(campo("Telefone: ", valorOuTraco(nota.getTelefone()), valorFonte));
        documento.add(cliente);

        PdfPTable linha = new PdfPTable(1);
        linha.setWidthPercentage(100);
        linha.addCell(campo("Linha: ", valorOuTraco(nota.getLinha()), valorFonte));
        linha.addCell(campo("Obs: ", valorOuTraco(nota.getObservacoes()), valorFonte));
        documento.add(linha);
        documento.add(new Paragraph(" "));
    }

    private void adicionarMedidas(Document documento, Nota nota) throws DocumentException {
        PdfPTable medidas = new PdfPTable(4);
        medidas.setWidthPercentage(100);
        medidas.setWidths(new float[]{25, 25, 25, 25});
        medidas.addCell(caixaMedida("SACAS", decimal(nota.getSacas())));
        medidas.addCell(caixaMedida("QUILOS", decimal(nota.getQuilos())));
        medidas.addCell(caixaMedida("UMIDADE", decimal(nota.getUmidade())));
        medidas.addCell(caixaMedida("DATA", nota.getData() == null
                ? ""
                : nota.getData().format(FORMATO_DATA)));
        documento.add(medidas);
        documento.add(new Paragraph(" "));
    }

    private void adicionarAssinatura(Document documento, Nota nota) throws DocumentException {
        Font fonte = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
        PdfPTable tabela = new PdfPTable(1);
        tabela.setWidthPercentage(100);

        PdfPCell assinatura = new PdfPCell(new Paragraph(
                "Ass. do Maquinista: " + valorOuTraco(nota.getAssinatura()), fonte));
        assinatura.setBorder(Rectangle.TOP);
        assinatura.setPaddingTop(10);
        tabela.addCell(assinatura);
        documento.add(tabela);
    }

    private PdfPCell campo(String rotulo, String valor, Font fonte) {
        Paragraph paragrafo = new Paragraph();
        paragrafo.add(new Phrase(rotulo,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, AZUL)));
        paragrafo.add(new Phrase(valor, fonte));

        PdfPCell celula = new PdfPCell(paragrafo);
        celula.setBorder(Rectangle.BOTTOM);
        celula.setPadding(8);
        return celula;
    }

    private PdfPCell caixaMedida(String rotulo, String valor) {
        PdfPTable tabela = new PdfPTable(1);

        PdfPCell cabecalho = new PdfPCell(new Paragraph(rotulo,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE)));
        cabecalho.setBackgroundColor(AZUL);
        cabecalho.setHorizontalAlignment(Element.ALIGN_CENTER);
        cabecalho.setPadding(6);
        tabela.addCell(cabecalho);

        PdfPCell corpo = new PdfPCell(new Paragraph(valor,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, AZUL)));
        corpo.setBackgroundColor(AZUL_CLARO);
        corpo.setHorizontalAlignment(Element.ALIGN_CENTER);
        corpo.setPadding(12);
        corpo.setPaddingBottom(20);
        tabela.addCell(corpo);

        PdfPCell wrapper = new PdfPCell(tabela);
        wrapper.setBorder(PdfPCell.NO_BORDER);
        wrapper.setPadding(2);
        return wrapper;
    }

    private String texto(String valor) {
        return valor == null ? "" : valor;
    }

    private String valorOuTraco(String valor) {
        return valor == null || valor.isBlank() ? "-" : valor;
    }

    private String decimal(BigDecimal valor) {
        return valor == null ? "" : valor.toPlainString();
    }
}
