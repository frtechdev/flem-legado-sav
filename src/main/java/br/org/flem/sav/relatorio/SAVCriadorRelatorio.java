package br.org.flem.sav.relatorio;

import br.org.flem.fwe.exception.RelatorioSemDadosException;
import br.org.flem.fwe.web.relatorio.CriadorRelatorio;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author ilfernandes
 */
public class SAVCriadorRelatorio extends CriadorRelatorio {

    @Override
    protected void montaRelatorio(HttpServletRequest request, Map parametros) {
        parametros.put("logo", request.getSession().getServletContext().getRealPath("/img/logo.gif"));
        parametros.put("logoFLEM", request.getSession().getServletContext().getRealPath("/img/logoflemrelatorio.jpg"));
    }

    @Override
    public void exportaRelatorioPDF(HttpServletRequest request, HttpServletResponse response, String localArquivo,
            Map parametros, Collection resultado) throws JRException, RelatorioSemDadosException {

        if (resultado == null || resultado.size() <= 0) {
            throw new RelatorioSemDadosException("O relatório selecionado não possui dados a serem exibidos.");
        }

        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "attachment; filename=" + "Relatorio_SAV.pdf");
            ServletOutputStream servletOutputStream = response.getOutputStream();
            InputStream input = request.getSession().getServletContext().getResourceAsStream(localArquivo);
            JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(resultado);
            this.montaRelatorio(request, parametros);
            JasperRunManager.runReportToPdfStream(input, servletOutputStream, parametros, beanDataSource);
            servletOutputStream.flush();
            servletOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new JRException(e);
        }
    }

    public void exportaRelatorioXLS(HttpServletRequest request, HttpServletResponse response, String localArquivo,
            Map parametros, Collection resultado) throws JRException, RelatorioSemDadosException, IOException {

        // if (resultado == null || resultado.size() <= 0) {
        // throw new RelatorioSemDadosException("O relatório selecionado não possui
        // dados a serem exibidos.");
        // }
        ServletOutputStream servletOutputStream = null;
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=" + "FlemWeb_Relatorio_SAV.xls");
            servletOutputStream = response.getOutputStream();

            // InputStream input =
            // request.getSession().getServletContext().getResourceAsStream(localArquivo);
            JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(resultado);
            this.montaRelatorio(request, parametros);
            // JasperReport jasperReport = (JasperReport)
            // JRLoader.loadObject(request.getSession().getServletContext().getRealPath(localArquivo))
            // ;
            // LINHA ABAIXO ADICIONADA PARA COMPATIBILIDADE COM A NOVA VERSÃO DO JASPER
            // REPORTS
            JasperReport jasperReport = (JasperReport) JRLoader
                    .loadObject(new File(request.getSession().getServletContext().getRealPath(localArquivo)));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, beanDataSource);
            JRXlsExporter exporter = new JRXlsExporter();

            exporter.setParameter(JRXlsExporterParameter.SHEET_NAMES, new String[] { "Relatorio" });
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
            exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
            exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, true);
            exporter.exportReport();
        } catch (Exception e) {
            throw new JRException(e);
        } finally {
            if (servletOutputStream != null) {
                servletOutputStream.flush();
                servletOutputStream.close();
            }
        }

    }
}
