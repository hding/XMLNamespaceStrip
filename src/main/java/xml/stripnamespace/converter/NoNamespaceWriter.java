package xml.stripnamespace.converter;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.Writer;
import java.util.Iterator;

/**
 * Writer without namespace XML
 *
 * @author helen ding on 26/04/2017.
 */
public class NoNamespaceWriter {
    public static final String EMPTY_STRING = "";

    /**
     * Empty NamespaceContext
     *
     * @author helen ding on 18/04/2017.
     */
    static class EmptyNamespaceContext implements NamespaceContext {
        @Override
        public String getNamespaceURI(String prefix) {
            return EMPTY_STRING;
        }

        @Override
        public String getPrefix(String namespaceURI) {
            return EMPTY_STRING;
        }

        @Override
        public Iterator getPrefixes(String namespaceURI) {
            return null;
        }
    }

    /**
     * Apply the filter for Writer
     *
     * @param writer Writer Object
     * @return XMLStreamWriter
     * @throws XMLStreamException if something goes wrong
     */
    public static XMLStreamWriter filter(Writer writer) throws XMLStreamException {
        XMLStreamWriter result = XMLOutputFactory.newInstance().createXMLStreamWriter(writer);
        result.setNamespaceContext(new NoNamespaceWriter.EmptyNamespaceContext());
        return result;
    }
}
