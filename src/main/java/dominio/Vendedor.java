package dominio;

import dominio.repositorio.RepositorioProducto;
import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioGarantiaExtendida;

public class Vendedor {

	public static final String EL_PRODUCTO_TIENE_GARANTIA = "El producto ya cuenta con una garantia extendida";
	public static final String CODIGO_TIENE_TRES_VOCALES = "Este producto no cuenta con garantía extendida";

	private RepositorioProducto repositorioProducto;
	private RepositorioGarantiaExtendida repositorioGarantia;

	public Vendedor(RepositorioProducto repositorioProducto, RepositorioGarantiaExtendida repositorioGarantia) {
		this.repositorioProducto = repositorioProducto;
		this.repositorioGarantia = repositorioGarantia;

	}

	public void generarGarantia(String codigo, String nombreCliente) {

		if (tieneGarantia(codigo)) {
			throw new GarantiaExtendidaException(EL_PRODUCTO_TIENE_GARANTIA);
		}
		
		// se agrega Codigo para validar las 3 vocales en el codigo
		String upperCodigo = codigo.toUpperCase();
		String vocales = "AEIOU";
		int contadorVocales = 0;

		for (int i = 0; i < upperCodigo.length(); i++) {

			for (int j = 0; j < vocales.length(); j++) {

				if (upperCodigo.charAt(i) == vocales.charAt(j))
					contadorVocales++;
			}
		}
		
		if (contadorVocales == 3) {
			throw new GarantiaExtendidaException(CODIGO_TIENE_TRES_VOCALES);
		}

		Producto producto = repositorioProducto.obtenerPorCodigo(codigo);

		GarantiaExtendida garantiaExt = new GarantiaExtendida(producto);
		
		garantiaExt.setNombreCliente(nombreCliente);
		
		repositorioGarantia.agregar(garantiaExt);

	}

	public boolean tieneGarantia(String codigo) {

		Producto producto = repositorioGarantia.obtenerProductoConGarantiaPorCodigo(codigo);
		if (producto != null) {
			return true;
		}
		return false;
	}
}
