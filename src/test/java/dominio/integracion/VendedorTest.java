package dominio.integracion;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dominio.Vendedor;
import dominio.Producto;
import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioProducto;
import dominio.repositorio.RepositorioGarantiaExtendida;
import persistencia.sistema.SistemaDePersistencia;
import testdatabuilder.ProductoTestDataBuilder;

public class VendedorTest {

	private static final String COMPUTADOR_LENOVO = "Computador Lenovo";
	
	private static final String TRES_VOCALES_CODIGO = "ACER-A001";
	
	private static final String NOMBRE_CLIENTE = "Naren";
	
	private SistemaDePersistencia sistemaPersistencia;
	
	private RepositorioProducto repositorioProducto;
	private RepositorioGarantiaExtendida repositorioGarantia;

	@Before
	public void setUp() {
		
		sistemaPersistencia = new SistemaDePersistencia();
		
		repositorioProducto = sistemaPersistencia.obtenerRepositorioProductos();
		repositorioGarantia = sistemaPersistencia.obtenerRepositorioGarantia();
		
		sistemaPersistencia.iniciar();
	}
	

	@After
	public void tearDown() {
		sistemaPersistencia.terminar();
	}

	@Test
	public void generarGarantiaTest() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().conNombre(COMPUTADOR_LENOVO).build();
		repositorioProducto.agregar(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);

		
		
		// act
		vendedor.generarGarantia(producto.getCodigo(),NOMBRE_CLIENTE);

		// assert
		Assert.assertTrue(vendedor.tieneGarantia(producto.getCodigo()));
		Assert.assertNotNull(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo()));

	}

	@Test
	public void productoYaTieneGarantiaTest() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().conNombre(COMPUTADOR_LENOVO).build();
		
		repositorioProducto.agregar(producto);
		
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);		
	
		vendedor.generarGarantia(producto.getCodigo(),NOMBRE_CLIENTE);
		
		try {
			
			vendedor.generarGarantia(producto.getCodigo(),NOMBRE_CLIENTE);
			fail();
			
		} catch (GarantiaExtendidaException e) {
			// assert
			Assert.assertEquals(Vendedor.EL_PRODUCTO_TIENE_GARANTIA, e.getMessage());
		}
	}
	
	// test para validar las 3 vocales en el codigo	
	@Test
		public void codigoProductoTresVocalesTest() {
			// arrange
		
			Producto producto = new ProductoTestDataBuilder().conCodigo(TRES_VOCALES_CODIGO).build();
			Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
			
			try {
				
			vendedor.generarGarantia(producto.getCodigo(),NOMBRE_CLIENTE);
			fail();
			
			} catch (GarantiaExtendidaException e) {
				// assert
				Assert.assertEquals(vendedor.CODIGO_TIENE_TRES_VOCALES, e.getMessage());
			}
		}
	
	
	
	
}
