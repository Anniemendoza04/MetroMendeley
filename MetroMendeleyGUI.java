import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * La clase MetroMendeleyGUI proporciona la interfaz gráfica de usuario para el sistema MetroMendeley.
 * Permite a los usuarios interactuar con el sistema a través de una serie de paneles y botones,
 * facilitando la gestión de resúmenes de investigaciones.
 */
public class MetroMendeleyGUI extends JFrame {
    private MyMap<String, Resumen> tablaResumenes = new MyMap<String, Resumen>();
    private JTextArea resultArea = new JTextArea();
    private ResumenManager resumenManager = new ResumenManager();
    private JList<String> listaResultados = new JList<>();
    private DefaultListModel<String> modeloLista = new DefaultListModel<>();
    private BusquedaResumen busquedaResumen = new BusquedaResumen(); 


    /**
     * Constructor de la clase MetroMendeleyGUI.
     * Inicializa la ventana principal y carga la interfaz de usuario.
     */
    public MetroMendeleyGUI() {
        setTitle("MetroMendeley");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        resumenManager.cargarResumenesDesdeJson();
        initUI();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                resumenManager.guardarResumenesComoJson();
                System.exit(0);
            }
        });
    }

    /**
     * Inicializa los componentes de la interfaz de usuario y los agrega al JFrame.
     * Configura el layout principal, los paneles de lista de investigaciones y acciones,
     * y maneja los eventos de los botones.
     */
    private void initUI() {
        // Layout principal
        setLayout(new BorderLayout());

        // Panel de lista de investigaciones
        JPanel panelLista = new JPanel();
        JList<String> listaInvestigaciones = new JList<>();
        panelLista.add(new JScrollPane(listaInvestigaciones));
        add(panelLista, BorderLayout.WEST);

        // Panel de acciones
        JPanel panelAcciones = new JPanel();
        panelAcciones.setLayout(new GridLayout(5, 1));
        JButton btnAgregar = new JButton("Agregar Resumen");
        JButton btnAnalizar = new JButton("Analizar Resumen");
        btnAnalizar.addActionListener(e -> {
            int indiceSeleccionado = listaInvestigaciones.getSelectedIndex();
            if (indiceSeleccionado != -1) { // Ensure a selection has been made
                String infoResumen = resumenManager.analizarResumen(indiceSeleccionado);
                mostrarInfoResumen(infoResumen); 
            } else {

                JOptionPane.showMessageDialog(this, "Por favor, seleccione un resumen para analizar.");

                
            }
        });
        JButton btnBuscarPalabra = new JButton("Buscar por Palabra Clave");
        JButton btnBuscarAutor = new JButton("Buscar por Autor");
        JButton btnSalir = new JButton("Salir");
        panelAcciones.add(btnAgregar);
        panelAcciones.add(btnAnalizar);
        panelAcciones.add(btnBuscarPalabra);
        panelAcciones.add(btnBuscarAutor);

        // Panel de resultados
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        listaResultados.setModel(modeloLista);
        add(new JScrollPane(listaResultados), BorderLayout.WEST);

        // Botón de salida
        panelAcciones.add(btnSalir);
        add(panelAcciones, BorderLayout.EAST);

        // Eventos de botones
        btnAgregar.addActionListener(e -> agregarResumen());
        btnAnalizar.addActionListener(e -> {
            int indiceSeleccionado = listaInvestigaciones.getSelectedIndex();
            String infoResumen = resumenManager.analizarResumen(indiceSeleccionado);
            mostrarInfoResumen(infoResumen);
        });
        btnBuscarPalabra.addActionListener(e -> buscarPorPalabraClave());
        btnBuscarAutor.addActionListener(e -> buscarPorAutor());
        btnSalir.addActionListener(e -> System.exit(0));
    }

    // Métodos privados para manejar eventos de botones

    public void mostrarInfoResumen(String info) {
        resultArea.setText(info);
    }
    
    private void agregarResumen() {
        JFrame frame = new JFrame("Agregar Resumen");
        frame.setSize(300, 400);
        frame.setLayout(new BorderLayout());
    
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));
    
        JTextField tituloField = new JTextField();
        JTextField autoresField = new JTextField();
        JTextArea cuerpoField = new JTextArea();
        JTextField palabrasClaveField = new JTextField();
        JButton rutaArchivoButton = new JButton("Seleccionar Archivo");
        JTextField rutaArchivoField = new JTextField();
        rutaArchivoField.setEditable(false); // Make the field non-editable
    
        panel.add(new JLabel("Título:"));
        panel.add(tituloField);
        panel.add(new JLabel("Autores:"));
        panel.add(autoresField);
        panel.add(new JLabel("Cuerpo:"));
        panel.add(cuerpoField);
        panel.add(new JLabel("Palabras Clave:"));
        panel.add(palabrasClaveField);
        panel.add(new JLabel("Ruta del Archivo:"));
        panel.add(rutaArchivoField);
    
        JButton submitButton = new JButton("Agregar");
        submitButton.addActionListener(e -> {
            String titulo = tituloField.getText();
            String[] autores = autoresField.getText().split(",");
            String cuerpo = cuerpoField.getText();
            String rutaArchivo = rutaArchivoField.getText();
            String[] palabrasClave = palabrasClaveField.getText().split(",");
    
            // Llamada al método agregarResumen de ResumenManager y captura del Resumen devuelto
            Resumen nuevoResumen = resumenManager.agregarResumen(titulo, autores, cuerpo, rutaArchivo, palabrasClave);
            tablaResumenes.put(nuevoResumen.getTitulo(), nuevoResumen);
    
            frame.dispose(); // Cierra la ventana después de agregar
        });
    
        rutaArchivoButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                rutaArchivoField.setText(fileChooser.getSelectedFile().getPath());
            }
        });

        listaResultados.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                analizarResumenSeleccionado();
            }
        });
    
        panel.add(rutaArchivoButton);
    
        frame.add(panel, BorderLayout.CENTER);
        frame.add(submitButton, BorderLayout.SOUTH);
    
        frame.setVisible(true);
    }

    private void analizarResumen() {
        JFrame frame = new JFrame("Analizar Resumen");
        frame.setSize(300, 400);
        frame.setLayout(new BorderLayout());
    
        // Use listarTitulosResumenes() from resumenManager
        String[] titulosResumenes = resumenManager.listarTitulosResumenes();
        JList<String> listaResumenes = new JList<>(titulosResumenes);
    
        JButton analizarButton = new JButton("Analizar");
        analizarButton.addActionListener(e -> {
            int indiceResumenSeleccionado = listaResumenes.getSelectedIndex();
            if (indiceResumenSeleccionado >= 0) { // Asegura que se ha seleccionado un resumen
                Resumen resumenSeleccionado = tablaResumenes.get(titulosResumenes[indiceResumenSeleccionado]);
                StringBuilder infoResumen = new StringBuilder();
                infoResumen.append("Título: ").append(resumenSeleccionado.getTitulo()).append("\n");
                infoResumen.append("Autores: ").append(String.join(", ", resumenSeleccionado.getAutores())).append("\n");
                infoResumen.append("Cuerpo: ").append(resumenSeleccionado.getCuerpo()).append("\n");
                infoResumen.append("Palabras Clave: ").append(String.join(", ", resumenSeleccionado.getPalabrasClave())).append("\n");

        
                resultArea.setText(infoResumen.toString()); // Mostrar la información en resultArea
            } else {
                JOptionPane.showMessageDialog(frame, "Por favor, seleccione un resumen para analizar.");
            }
        });
        frame.add(new JScrollPane(listaResumenes), BorderLayout.CENTER);
        frame.add(analizarButton, BorderLayout.SOUTH);
    
        frame.setVisible(true);
    }
    
    // Asumiendo que el nuevo método analizarResumen(int indiceResumenSeleccionado) está correctamente implementado y accesible
    private void buscarPorPalabraClave() {
        JFrame frame = new JFrame("Buscar por Palabra Clave");
        frame.setSize(300, 200);
        frame.setLayout(new BorderLayout());
    
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));
    
        JTextField palabraClaveField = new JTextField();
        panel.add(new JLabel("Palabra Clave:"));
        panel.add(palabraClaveField);
    
        JButton buscarButton = new JButton("Buscar");
        buscarButton.addActionListener(e -> {
            String palabraClave = palabraClaveField.getText().trim(); // Eliminar espacios adicionales
            MyLinkedList<Investigacion> resultados = busquedaResumen.buscarPorPalabraClave(palabraClave); // Llamada al método
            if (resultados.size() == 0) {
                JOptionPane.showMessageDialog(frame, "No hay resultados para mostrar.");
            } else {
                displayResults(resultados); // Muestra los resultados utilizando un método para actualizar la interfaz gráfica
            }
            frame.dispose(); // Cierra la ventana después de buscar
        });
    
        frame.add(panel, BorderLayout.CENTER);
        frame.add(buscarButton, BorderLayout.SOUTH);
    
        frame.setVisible(true);
    }
    
    public void displayResults(MyLinkedList<Investigacion> results) {
        // Paso 1: Verificar si results es nulo o está vacío
        if (results == null || results.size() == 0) {
            // Manejar la situación, por ejemplo, mostrar un mensaje de que no hay resultados
            System.out.println("No hay resultados para mostrar.");
            return;
        }
    
        // Paso 2: Inicializar el índice
        int i = 0;
    
        // Paso 3: Iterar sobre results con un bucle while
        while (i < results.size()) {
            // Obtener el elemento en la posición i
            Investigacion investigacion = results.get(i);
    
            // Realizar operaciones con el elemento, por ejemplo, mostrarlo en la GUI
            // (Este paso depende de cómo se manejen los elementos en la GUI)
            System.out.println(investigacion); // Ejemplo de operación: imprimir el elemento
    
            // Incrementar el índice
            i++;
        }
    }

    private void buscarPorAutor() {
        JFrame frame = new JFrame("Buscar por Autor");
        frame.setSize(300, 200);
        frame.setLayout(new BorderLayout());
    
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));
    
        JTextField autorField = new JTextField();
        panel.add(new JLabel("Autor:"));
        panel.add(autorField);
    
        JButton buscarButton = new JButton("Buscar");
        buscarButton.addActionListener(e -> {
            String autor = autorField.getText(); // Get the author from the text field
            MyLinkedList<Investigacion> resultados = buscarPorAutor(autor); // Correctly pass the author to the method
            // Display results
            if (resultados.size() > 0) {
                // Mostrar el mensaje con el número correcto de resultados
                JOptionPane.showMessageDialog(frame, "Resultados para: " + autor + " - " + resultados.size() + " encontrados");
            } else {
                // Manejar el caso donde no se encuentran resultados
                JOptionPane.showMessageDialog(frame, "No se encontraron resultados para: " + autor);
            }
            frame.dispose(); // Close the window after searching
        });
    
        frame.add(panel, BorderLayout.CENTER);
        frame.add(buscarButton, BorderLayout.SOUTH);
    
        frame.setVisible(true);
    }

    private MyLinkedList<Investigacion> buscarPorAutor(String autor) {
        // Create a custom list to store matching investigations
        MyLinkedList<Investigacion> resultados = new MyLinkedList<>();
        Iterator<Investigacion> iterador = resultados.iterator();
        modeloLista.clear();
        // Iterate over the entries in the tablaResumenes (or your data structure)
        while(iterador.hasNext()) {
            // Assuming Resumen has a method getAutores that returns a list of authors
            Investigacion investigacion = iterador.next();
            modeloLista.addElement(investigacion.getTitulo()); 
        }
        return resultados;
    }

    private void analizarResumenSeleccionado() {
        String tituloSeleccionado = listaResultados.getSelectedValue();
        if (tituloSeleccionado != null) {
            tablaResumenes.get(tituloSeleccionado);
            // Mostrar detalles del resumen en resultArea
        }
    }
    
    public static void main(String[] args) {
        MetroMendeleyGUI gui = new MetroMendeleyGUI();
        gui.setVisible(true);
    }
}