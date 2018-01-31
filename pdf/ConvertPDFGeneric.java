package pdf;

import connectors.ConnectorMysql; 
import entities.Temperature;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import java.io.*; 
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.ResultSetMetaData;
/**
 * Clase que sirve para el procesamiento de datos, para luego ser volcados, en un archivo pdf.
 * <p>
 * <u1>
 * <li>Crea reportes en formato PDF, de las temperaturas u  otro tipo de datos.
 * <li>Utiliza libreria iText 7 7.0.5 para el tratamiento de objetos pdf a java y su conversion. Para
 * <li>mas informacion ver: <@link http://itextsupport.com/apidocs/itext7/latest/>
 * </ul>
 * @author      Lazzaroni Gonzalo
 * @author      Fernandez Sergio
 * @version     %I%, %G%
 * @since       1.0
 */
public class ConvertPDFGeneric {
    private static final Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 26, Font.BOLDITALIC);
    private static final Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);
    
    private static final Font categoryFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static final Font subcategoryFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static final Font blueFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.RED);    
    private static final Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);   
    
    private static final String img = "C:/practica_profesional/images/jetty-logo-80x22.png";   
    private Document document;
    
    //construct
    public ConvertPDFGeneric() {}
    /**
     * @param pdfNewFile Nombre del archivo a ser creado,en formato PDF.
     * @param rs Objeto ResultSet, con registros.
     * @return Boolean False si no se ha podido completar la operacion, o si se lanza alguna excepcion.
     * @throws FileNotFoundException Lanza FileNotFoundException, Si el archivo no se ha podido encontrar.
     * @throws DocumentException Lanza DocumentException, si no se ha podido abrir el archivo deseado.
     * @throws MalformedURLException Lanza MalformedURLException, si se encuentra erronea la URL.
     * @throws Exception Exception.
     */    
    public boolean convert(File pdfNewFile, ResultSet rs) {
        boolean r = false;
        try {
            //cm.connect();
            // We create the document and set the file name.        
            // Creamos el documento e indicamos el nombre del fichero.        
            document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfNewFile));
             
            //abrimos el documento creado, para laburar
            document.open();
            // We add metadata to PDF
            // Añadimos los metadatos del PDF  
            document.addTitle("Reporte Mediciones Arduino");
            document.addSubject("Using iText (usando iText)");
            document.addKeywords("Java, PDF, iText");
            document.addAuthor("Lazzaroni Gonzalo & Fernandez Sergio");
            document.addCreator("Lazzaroni Gonzalo & Fernandez Sergio");  
            
            // First page
            // Primera página 
            /*com.itextpdf.text.Chunk: es la parte más pequeña que se puede añadir a un documento, 
            la mayor parte de los elementos se pueden dividir en varios Chunks y 
            básicamente es un String con una fuente determinada.*/
            Chunk chunk = new Chunk("Reporte Mediciones Arduino", chapterFont);
            chunk.setBackground(BaseColor.GRAY); 
            // Let's create de first Chapter (Creemos el primer capítulo)            
            Chapter chapter = new Chapter(new Paragraph(chunk), 1);//com.itextpdf.text.Paragraph: es una serie de chunks.
            chapter.setNumberDepth(0);
            chapter.add(new Paragraph("Práctica profesional ISFT 189", paragraphFont));
            
            // We add an image (Añadimos una imagen)
            Image image = Image.getInstance(img);//com.itextpdf.text.Image: representación gráfica de imágenes podemos utilizar: JPEG, PNG o GIF.
            image.setAbsolutePosition(2, 0);//posicion de imagen en x/y en la hoja.
            chapter.add(image);
            document.add(chapter);
            
            // We use various elements to add title and subtitle
            // Usamos varios elementos para añadir título y subtítulo
            Anchor anchor = new Anchor("Test de Libreria iText 7 7.0.5 API.", categoryFont);
            anchor.setName("tabla_mysql");            
            Chapter chapTitle = new Chapter(new Paragraph(anchor), 1);
            Paragraph paragraph = new Paragraph("Temperaturas en celsius y fahrenheit.", subcategoryFont);
            Section paragraphMore = chapTitle.addSection(paragraph);
            paragraphMore.add(new Paragraph("Nuestros datos en MYSQL 5.7.20"));
            
            int nr = 0;
            while(rs.next()) {
                nr++;
            }
            
            ResultSetMetaData rsm = rs.getMetaData();
            // We create the table (Creamos la tabla).
            PdfPTable table = new PdfPTable(rsm.getColumnCount());//hay que especificar el numero de columnas en su constructor.        
            PdfPCell columnHeader;         
            //aqui se crean las columnas- "es inteligente", puede saber el nombre y cantidad de cada una.
            for (int column = 1; column <= rsm.getColumnCount(); column++) {
                columnHeader = new PdfPCell(new Phrase(rsm.getColumnName(column).toString()));
                columnHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(columnHeader);
            }
            table.setHeaderRows(1);
            rs.beforeFirst();//volvemos a posicionar al puntero antes del comienzo.
            rs.next();//iniciamos y lo seguimos en el for debajo.
            
            //aqui rellenamos cada fila con los datos.tambien es inteligente(convierte cada valor a String).
            for (int row = 0; row < nr; row++,rs.next()) {
                for (int column = 1; column <= rsm.getColumnCount(); column++) {
                    table.addCell((rs.getObject(column)).toString());
                }
            }            
            // We add the table (Añadimos la tabla)
            paragraphMore.add(table);            
            // We add the paragraph with the table (Añadimos el elemento con la tabla).
            document.add(chapTitle);
            
            System.out.println("Your PDF file has been generated!(¡Se ha generado tu hoja PDF!)");
            
            r = true;//todo ok!
        }catch(FileNotFoundException e) {
            System.out.println(e.getMessage());
        }catch(DocumentException e) {
            System.out.println(e.getMessage());
        }catch(MalformedURLException e) {//BadElementException
            System.out.println(e.getMessage());
        }catch(IOException e) {
            System.out.println(e.getMessage());
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }finally {
            document.close();
            pdfNewFile = null;
            return r;
        }
    }

}

