package xml.stripnamespace.converter;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
    
/** * Marshall XML to byte content without namespace * @param jc JAXBContext * @param element Jaxb Object * @return the byte content */static byte[] marshallToByteNoNames(JAXBContext jc, Object element) {    try {        Marshaller marshaller = jc.createMarshaller();        ByteArrayOutputStream baos = new ByteArrayOutputStream();        OutputStreamWriter writer = new OutputStreamWriter(baos);        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);        marshaller.marshal(element, NoNamesWriterHelper.filter(writer));        return baos.toByteArray();    } catch (Exception ex) {        throw new SystemException(ex.getMessage(), ex);    }}
    
    
}
