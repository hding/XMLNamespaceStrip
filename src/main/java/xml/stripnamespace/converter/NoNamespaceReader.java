package xml.stripnamespace.converter;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.StreamReaderDelegate;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Reader with no namespace XML
 *
 * @author helen ding on 26/04/2017.
 */
public class NoNamespaceReader extends StreamReaderDelegate {
    private List<Class> xmlElements = new ArrayList<>();
    private Stack<String> parent = new Stack<>();

    public void setXmlElements(Class... classes) {
        Collections.addAll(xmlElements, classes);
    }

    /**
     * Constructor
     *
     * @param reader XML Stream Reader
     */
    private NoNamespaceReader(XMLStreamReader reader) {
        super(reader);
    }

    @Override
    public int next() throws XMLStreamException {
        int event = super.next();
        if (event == XMLEvent.START_ELEMENT) {
            parent.push(getLocalName());
        }
        if (event == XMLEvent.END_ELEMENT) {
            parent.pop();
        }
        return event;
    }


    @Override
    public String getNamespaceURI() {
        return QNameHelper.getInstance(xmlElements).getNamespace(getLocalName());
    }

    /**
     * Apply the filter for XML Stream Reader
     *
     * @param reader the reader
     * @return XMLStreamReader
     * @throws XMLStreamException if something goes wrong
     */
    public static XMLStreamReader filter(Reader reader) throws XMLStreamException {
        return new NoNamespaceReader(XMLInputFactory.newInstance().createXMLStreamReader(reader));
    }
}
