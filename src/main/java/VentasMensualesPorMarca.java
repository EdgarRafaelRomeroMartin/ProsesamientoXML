import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Logger;

public class VentasMensualesPorMarca extends DefaultHandler {
    private static final String CLASS_NAME = VentasMensualesPorMarca.class.getName();
    private final static Logger LOG = Logger.getLogger(CLASS_NAME);

    private SAXParser parser = null;
    private SAXParserFactory spf;

    private double totalSales;
    private boolean inSales;
    private String currentElement;
    private String id;
    private String name;
    private String lastName;
    private String insurance;
    private String car;
    private String model;
    private String contract_date;
    private String keyword;

    private HashMap<String, Double> subtotales;

    public VentasMensualesPorMarca() {
        super();
        spf = SAXParserFactory.newInstance();
        // verificar espacios de nombre
        spf.setNamespaceAware(true);
        // validar que el documento este bien formado (well formed)
        spf.setValidating(true);

        subtotales = new HashMap<>();

    }

    void process(File file) {
        try {
            // obtener un parser para verificar el documento
            parser = spf.newSAXParser();

        } catch (SAXException | ParserConfigurationException e) {
            LOG.severe(e.getMessage());
            System.exit(1);
        }
        System.out.println("\nStarting parsing of " + file + "\n");
        try {
            // iniciar analisis del documento
            keyword = car;
            parser.parse(file, this);
        } catch (IOException | SAXException e) {
            LOG.severe(e.getMessage());
        }
    }

    @Override
    public void startDocument() throws SAXException {
        // al inicio del documento inicializar
        // las ventas totales
        totalSales = 0.0;
    }

    @Override
    public void endDocument() throws SAXException {
        // Se proceso todo el documento, imprimir resultado
        Set<Map.Entry<String,Double>> entries = subtotales.entrySet();
        for (Map.Entry<String,Double> entry: entries) {
            System.out.printf("La venta de la marca %s fue: %s$\n",entry.getKey(),entry.getValue());
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {

        if (localName.equals("sale_record")) {
            inSales = true;
        }
        currentElement = localName;
    }

    @Override
    public void characters(char[] bytes, int start, int length) throws SAXException {

        switch (currentElement) {
            case "id":
                this.id = new String(bytes, start, length);
                break;
            case "first_name":
                this.name = new String(bytes, start, length);
                break;
            case "last_name":
                this.lastName = new String(bytes, start, length);
                break;
            case "insurance":
                this.insurance = new String(bytes, start, length);
                break;
            case "car":
                this.car = new String(bytes, start, length);
                break;
            case "model":
                this.model = new String(bytes, start, length);
                break;
            case "contract_date":
                this.contract_date = new String(bytes, start, length);
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {


        if ( localName.equals("insurance_record") ) {
            String Mes=null;
            double val = 0.0;
            try {
                val = Double.parseDouble(this.insurance);
            } catch (NumberFormatException e) {
                LOG.severe(e.getMessage());
            }

            try
            {
                Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(this.contract_date);
                ZoneId timeZone = ZoneId.systemDefault();
                LocalDate getLocalDate = date1.toInstant().atZone(timeZone).toLocalDate();
                Mes= String.valueOf(getLocalDate.getMonth());

            }
            catch (ParseException e)
            {
                // Error, la cadena de texto no se puede convertir en fecha.
            }

            if ( subtotales.containsKey(Mes) ) {

                double sum = subtotales.get(Mes);
                if ( subtotales.containsKey(this.car) ) {
                    subtotales.put(this.car +" del mes de "+Mes, sum + val);
                }
                subtotales.put(this.car +" del mes de "+Mes, sum + val);
            } else {
                subtotales.put(this.car +" del mes de "+Mes, val );
            }


            inSales = false;
        }
    }


    private void printRecord() {
        System.out.printf("%4.4s %-10.10s %-10.10s %9.9s %-10.10s %-15.15s\n",
                id, name, lastName, insurance, car, model);
    }



    public static void main(String args[]) {
        if (args.length == 0) {
            LOG.severe("No file to process. Usage is:" + "\njava DeptSalesReport <keyword>");
            return;
        }
        File xmlFile = new File(args[0] );
        VentasMensualesPorMarca handler = new VentasMensualesPorMarca();
        handler.process( xmlFile );
    }
}
