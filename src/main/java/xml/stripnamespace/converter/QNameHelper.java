package xml.stripnamespace.converter;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.XMLGregorianCalendar;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * This is the helper class which can find the qname from local name based on XML element
 *
 * @author helen ding on 26/04/2017.
 */
public class QNameHelper {
    private Map<String, String> namespace = new HashMap<>();
    static final String SPLIT = "&";

    private Set<Class<?>> getWrapperTypes() {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(String.class);
        ret.add(Date.class);
        ret.add(XMLGregorianCalendar.class);
        return ret;
    }

    private static QNameHelper instance;

    private QNameHelper(List<Class> xmlElements) {
        for (Class cl : xmlElements) {
            parse(cl, "");
        }
    }

    public synchronized static QNameHelper getInstance(List<Class> xmlElements) {
        if (instance == null) {
            instance = new QNameHelper(xmlElements);
        }
        return instance;
    }

    /**
     * Parse the XML element namespace
     *
     * @param cl          the XML element class
     * @param parentNames the parent tag name list
     */
    private void parse(Class cl, String parentNames) {
        if (cl == null) {
            return;
        }
        Annotation annotation = cl.getDeclaredAnnotation(XmlRootElement.class);
        if (annotation != null) {
            parentNames += SPLIT + ((XmlRootElement) annotation).name();
            namespace.put(parentNames, ((XmlRootElement) annotation).namespace());
        }
        if (isPrimitiveOrWrapper(cl)) {
            return;
        }
        for (Field field : cl.getDeclaredFields()) {
            try {
                Annotation fieldAnnotation = field.getDeclaredAnnotation(XmlElement.class);
                if (fieldAnnotation != null) {
                    String localNames = parentNames + SPLIT + ((XmlElement) fieldAnnotation).name();
                    namespace.put(localNames, ((XmlElement) fieldAnnotation).namespace());
                    Class fieldDeclaringClass = field.getType();
                    if (isPrimitiveOrWrapper(fieldDeclaringClass)) {
                        continue;
                    }
                    if (fieldDeclaringClass.isAssignableFrom(List.class)) {
                        ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
                        Class<?> fieldActClass = (Class<?>) stringListType.getActualTypeArguments()[0];
                        parse(fieldActClass, localNames);
                    } else {
                        parse(fieldDeclaringClass, localNames);
                    }
                }
            } catch (Exception e) {
                //log the error
            }
        }
    }


    private boolean isPrimitiveOrWrapper(Class cl) {
        if (cl.isPrimitive()) {
            return true;
        }
        if (getWrapperTypes().contains(cl)) {
            return true;
        }
        if (cl.getName().startsWith("java.lang")) {
            return true;
        }
        return false;
    }

    public String getNamespace(String localName) {
        return namespace.get(localName);
    }
}
