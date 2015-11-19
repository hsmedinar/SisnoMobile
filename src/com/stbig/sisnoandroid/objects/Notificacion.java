package com.stbig.sisnoandroid.objects;

public class Notificacion {
	
	int codigo;
	String titulo;
	String descripcion;
	String imagen;
	String id_tienda;
	int megusta;
	
	public Notificacion() {
		this.codigo = 0;
		this.titulo = "";
		this.descripcion = "";
		this.imagen = "";
		this.id_tienda="";
		this.megusta=0;
	}
	
	
	public Notificacion(int codigo, String titulo, String descripcion,String imagen,String id_tienda,int megusta) {
		this.codigo = codigo;
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.imagen = imagen;
		this.id_tienda=id_tienda;
		this.megusta=megusta;
	}	
	
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getId_tienda() {
		return id_tienda;
	}

	public void setId_tienda(String id_tienda) {
		this.id_tienda = id_tienda;
	}
	public int getMegusta() {
		return megusta;
	}

	public void setMegusta(int megusta) {
		this.megusta = megusta;
	}
	
}
