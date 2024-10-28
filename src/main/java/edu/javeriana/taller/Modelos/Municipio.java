package edu.javeriana.taller.Modelos;

/**
 * Representa un municipio con datos de población y superficie.
 */
public class Municipio {
    private int codigoDepartamento;
    private String nombreDepartamento;
    private int codigoMunicipio;
    private String nombreMunicipio;
    private double superficie;
    private int poblacionUrbana;
    private int poblacionRural;

    /**
     * Constructor para inicializar los datos de un municipio.
     * @param codigoDepartamento código del departamento.
     * @param nombreDepartamento nombre del departamento.
     * @param codigoMunicipio código del municipio.
     * @param nombreMunicipio nombre del municipio.
     * @param superficie área del municipio en km².
     * @param poblacionUrbana población urbana del municipio.
     * @param poblacionRural población rural del municipio.
     */
    public Municipio(int codigoDepartamento, String nombreDepartamento, int codigoMunicipio,
                     String nombreMunicipio, double superficie, int poblacionUrbana, int poblacionRural) {
        this.codigoDepartamento = codigoDepartamento;
        this.nombreDepartamento = nombreDepartamento;
        this.codigoMunicipio = codigoMunicipio;
        this.nombreMunicipio = nombreMunicipio;
        this.superficie = superficie;
        this.poblacionUrbana = poblacionUrbana;
        this.poblacionRural = poblacionRural;
    }

    // Getters para los atributos de municipio
    public int getCodigoDepartamento() { return codigoDepartamento; }
    public String getNombreDepartamento() { return nombreDepartamento; }
    public int getCodigoMunicipio() { return codigoMunicipio; }
    public String getNombreMunicipio() { return nombreMunicipio; }
    public double getSuperficie() { return superficie; }
    public int getPoblacionUrbana() { return poblacionUrbana; }
    public int getPoblacionRural() { return poblacionRural; }

    /**
     * Calcula y retorna la población total del municipio.
     * @return suma de población urbana y rural.
     */
    public int getPoblacionTotal() {
        return poblacionUrbana + poblacionRural;
    }
}
