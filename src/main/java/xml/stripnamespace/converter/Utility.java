package xml.stripnamespace.converter;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

/**
 * Utility class
 */
public class Utility {

    /**
     * Marshall XML to byte content without namespace
     * @param jc JAXBContext
     * @param element Jaxb Object
     * @return the byte content
     */
    static byte[] marshallToByteNoNames(JAXBContext jc, Object element) {
        try {
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(baos);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(element, NoNamespaceWriter.filter(writer));
            return baos.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }


    /**
     * Unmarshal XML from byte without namespace
     * @param jc JAXBContext
     * @param content the byte content
     * @return Jaxb Object
     */
    static Object unMarshallFromByteNoNamesResponse(JAXBContext jc, byte[] content) {
        try {
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            Reader reader = new InputStreamReader(new ByteArrayInputStream(content));
            return unmarshaller.unmarshal(NoNamespaceReader.filter(reader));
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

}
