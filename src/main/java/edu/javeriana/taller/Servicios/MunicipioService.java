package edu.javeriana.taller.Servicios;

import edu.javeriana.taller.Modelos.Municipio;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para manejar operaciones relacionadas con municipios.
 */
@Service
public class MunicipioService {

    /**
     * Lee el archivo CSV y convierte cada fila en un objeto Municipio.
     * @param rutaArchivo ruta del archivo CSV.
     * @return lista de municipios.
     */
    public List<Municipio> leerArchivoCSV(String rutaArchivo) {
        List<Municipio> municipios = new ArrayList<>();
        try {
            Files.lines(Paths.get(rutaArchivo), StandardCharsets.ISO_8859_1)
                    .skip(1) // Omitir la cabecera
                    .map(linea -> linea.split(";"))
                    .forEach(datos -> {
                        int codigoDepartamento = Integer.parseInt(datos[0].trim());
                        String nombreDepartamento = limpiarTexto(datos[1]);
                        int codigoMunicipio = Integer.parseInt(datos[2].trim());
                        String nombreMunicipio = limpiarTexto(datos[3]);
                        double superficie = Double.parseDouble(datos[4].trim());
                        int poblacionUrbana = Integer.parseInt(datos[5].trim());
                        int poblacionRural = Integer.parseInt(datos[6].trim());
                        municipios.add(new Municipio(codigoDepartamento, nombreDepartamento, codigoMunicipio, nombreMunicipio, superficie, poblacionUrbana, poblacionRural));
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return municipios;
    }

    /**
     * Genera un reporte de municipios agrupado por departamento en formato JSON.
     * @param municipios lista de municipios.
     * @return reporte agrupado por departamento.
     */
    public Map<String, Map<String, Object>> generarReporteJSON(List<Municipio> municipios) {
        Map<Integer, List<Municipio>> municipiosPorDepto = agruparPorDepartamento(municipios);
        Map<String, Map<String, Object>> reporteCompleto = new HashMap<>();

        municipiosPorDepto.forEach((codigoDepto, listaMunicipios) -> {
            String nombreDepto = listaMunicipios.get(0).getNombreDepartamento();

            int poblacionTotal = listaMunicipios.stream().mapToInt(Municipio::getPoblacionTotal).sum();
            int poblacionUrbana = listaMunicipios.stream().mapToInt(Municipio::getPoblacionUrbana).sum();
            int poblacionRural = listaMunicipios.stream().mapToInt(Municipio::getPoblacionRural).sum();
            double superficieTotal = listaMunicipios.stream().mapToDouble(Municipio::getSuperficie).sum();

            double densidadTotal = poblacionTotal / superficieTotal;
            double densidadUrbana = poblacionUrbana / superficieTotal;
            double densidadRural = poblacionRural / superficieTotal;

            double porcentajeUrbana = (poblacionUrbana / (double) poblacionTotal) * 100;
            double porcentajeRural = (poblacionRural / (double) poblacionTotal) * 100;

            double areaPromedio = superficieTotal / listaMunicipios.size();

            Municipio municipioMasGrande = listaMunicipios.stream()
                    .max(Comparator.comparingDouble(Municipio::getSuperficie))
                    .orElse(null);
            Municipio municipioMasPequeño = listaMunicipios.stream()
                    .min(Comparator.comparingDouble(Municipio::getSuperficie))
                    .orElse(null);

            Municipio mayorDensidad = listaMunicipios.stream()
                    .max(Comparator.comparingDouble(m -> m.getPoblacionTotal() / m.getSuperficie()))
                    .orElse(null);
            Municipio menorDensidad = listaMunicipios.stream()
                    .min(Comparator.comparingDouble(m -> m.getPoblacionTotal() / m.getSuperficie()))
                    .orElse(null);

            Map<String, Object> reporteDepto = new HashMap<>();
            reporteDepto.put("nombreDepartamento", nombreDepto);
            reporteDepto.put("densidadTotal", densidadTotal);
            reporteDepto.put("densidadUrbana", densidadUrbana);
            reporteDepto.put("densidadRural", densidadRural);
            reporteDepto.put("porcentajeUrbana", porcentajeUrbana);
            reporteDepto.put("porcentajeRural", porcentajeRural);
            reporteDepto.put("areaPromedio", areaPromedio);
            reporteDepto.put("municipioMasGrande", municipioMasGrande != null ? municipioMasGrande.getNombreMunicipio() : "N/A");
            reporteDepto.put("municipioMasPequeño", municipioMasPequeño != null ? municipioMasPequeño.getNombreMunicipio() : "N/A");
            reporteDepto.put("mayorDensidad", mayorDensidad != null ? mayorDensidad.getNombreMunicipio() : "N/A");
            reporteDepto.put("menorDensidad", menorDensidad != null ? menorDensidad.getNombreMunicipio() : "N/A");

            // Conversión de Integer a String para la clave
            reporteCompleto.put(String.valueOf(codigoDepto), reporteDepto);
        });

        return reporteCompleto;
    }


    /**
     * Genera un reporte JSON para un departamento específico.
     * @param municipios lista de municipios.
     * @param departamento nombre del departamento.
     * @return reporte del departamento.
     */
    public Map<String, Object> generarReportePorDepartamento(List<Municipio> municipios, String departamento) {
        List<Municipio> listaMunicipios = municipios.stream()
                .filter(m -> m.getNombreDepartamento().equalsIgnoreCase(departamento))
                .collect(Collectors.toList());
        if (listaMunicipios.isEmpty()) {
            return Collections.singletonMap("error", "Departamento no encontrado");
        }
        // Retorna el reporte del departamento específico usando su código como clave en el mapa
        return generarReporteJSON(listaMunicipios).get(String.valueOf(listaMunicipios.get(0).getCodigoDepartamento()));
    }


    /**
     * Limpia el texto eliminando caracteres especiales.
     * @param texto texto a limpiar.
     * @return texto limpio.
     */
    private String limpiarTexto(String texto) {
        return texto.trim()
                .replace("\t", "")
                .replace("\"", "")
                .replace("¡", "í")
                .replace("¢", "ó")
                .replace("¤", "ñ")
                .replace("", "é")
                .replace("£", "ú")
                .replace("Â", "") // Elimina caracteres extraños
                .replace("¿", "á") // Ajustes adicionales
                .replace("³", "ó")
                .replace("Ã", "i")
                .replace("©", "e")
                .replace("µ", "o"); // Otros posibles caracteres problemáticos
    }


    /**
     * Agrupa los municipios por código de departamento.
     * @param municipios lista de municipios.
     * @return municipios agrupados por departamento.
     */
    public Map<Integer, List<Municipio>> agruparPorDepartamento(List<Municipio> municipios) {
        return municipios.stream().collect(Collectors.groupingBy(Municipio::getCodigoDepartamento));
    }

    /**
     * Genera y guarda un reporte en archivo de texto.
     * @param municipios lista de municipios.
     */
    public void generarReporte(List<Municipio> municipios) {
        Map<Integer, List<Municipio>> municipiosPorDepto = agruparPorDepartamento(municipios);
        try (PrintWriter writer = new PrintWriter(new FileWriter("reporte_departamentos.txt"))) {
            municipiosPorDepto.forEach((codigoDepto, listaMunicipios) -> {
                String nombreDepto = listaMunicipios.get(0).getNombreDepartamento();
                int poblacionTotal = listaMunicipios.stream().mapToInt(Municipio::getPoblacionTotal).sum();
                int poblacionUrbana = listaMunicipios.stream().mapToInt(Municipio::getPoblacionUrbana).sum();
                double superficieTotal = listaMunicipios.stream().mapToDouble(Municipio::getSuperficie).sum();
                writer.printf("Departamento: %s, Densidad Total: %.2f, Urbana: %.2f%n", nombreDepto, poblacionTotal / superficieTotal, poblacionUrbana / superficieTotal);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
