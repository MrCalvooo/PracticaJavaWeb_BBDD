package es.damut12.model;

public class Tarea {
    private int idCategoria;
    private String categoria;
    private String titulo;
    private String descripcion;
    private boolean completada;
    private String fechaCreacion;

    // Constructor vacío
    public Tarea() {
    }

    // Constructor completo
    public Tarea(int idCategoria, String categoria, String titulo, String descripcion, boolean completada, String fechaCreacion) {
        this.idCategoria= idCategoria;
        this.categoria = categoria;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.completada = completada;
        this.fechaCreacion = fechaCreacion;
    }

    public int getIdCategoria()
    {
        return idCategoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isCompletada() {
        return completada;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

   @Override
    public String toString() {
        return "Tarea{" +
                "categoria=" + categoria +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", completada=" + completada + 
                ", fecha Creacion= " + fechaCreacion;
    }
}
