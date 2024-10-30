package edu.javeriana.taller.Controladores;

import edu.javeriana.taller.Modelos.Municipio;
import edu.javeriana.taller.Servicios.MunicipioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Controlador para los reportes de municipios.
 */
@Controller
@RequestMapping("/reporte")
public class MunicipioController {

    // Servicio para manejo de datos de municipios
    private final MunicipioService municipioService;

    /**
     * Constructor para inyectar MunicipioService.
     * @param municipioService servicio de lógica de municipios.
     */
    @Autowired
    public MunicipioController(MunicipioService municipioService) {
        this.municipioService = municipioService;
    }

    /**
     * Genera un reporte en HTML y lo agrega al modelo.
     * @param model modelo para la vista.
     * @return nombre de la plantilla HTML.
     */
    @GetMapping("/reporteHTML")
    public String obtenerReporteHTML(Model model) {
        List<Municipio> municipios = municipioService.leerArchivoCSV("src/main/resources/datosDivipola.csv");
        // Actualizamos a Map<String, Map<String, Object>> para que coincida con el servicio
        Map<String, Map<String, Object>> reporte = municipioService.generarReporteJSON(municipios);
        model.addAttribute("reporte", reporte);
        return "reporte"; // Muestra reporte.html en templates
    }

    /**
     * Endpoint para obtener el reporte completo en JSON.
     * @return reporte completo agrupado por departamento.
     */
    @GetMapping
    public Map<String, Map<String, Object>> obtenerReporteCompleto() {
        List<Municipio> municipios = municipioService.leerArchivoCSV("src/main/resources/datosDivipola.csv");
        return municipioService.generarReporteJSON(municipios);
    }

    /**
     * Obtiene el reporte de un departamento en JSON.
     * @param departamento nombre del departamento.
     * @return reporte del departamento solicitado.
     */
    @GetMapping("/departamento")
    public String obtenerReporteDepartamento(@RequestParam String departamento, Model model) {
        List<Municipio> municipios = municipioService.leerArchivoCSV("src/main/resources/datosDivipola.csv");
        Map<String, Object> reporte = municipioService.generarReportePorDepartamento(municipios, departamento);
        model.addAttribute("reporte", reporte);
        return "reporteDepartamento"; // Nombre del archivo Thymeleaf en `src/main/resources/templates`
    }


}
