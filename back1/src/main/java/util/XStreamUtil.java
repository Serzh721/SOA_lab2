package util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConversionException;
import model.Coordinates;
import model.Flat;
import model.House;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

public class XStreamUtil {

    private static final XStream xStream = createXStream();

    private static XStream createXStream() {
        XStream result = new XStream();
        result.processAnnotations(Flat.class);
        result.processAnnotations(House.class);
        result.processAnnotations(FlatListWrap.class);
        result.addDefaultImplementation(Timestamp.class, Date.class);
        Class<?>[] classes = new Class[]{Flat.class, House.class, Coordinates.class};
        result.allowTypes(classes);
        result.setMode(XStream.NO_REFERENCES);
        return result;
    }

    public static String toXML(Flat flat) {
        return xStream.toXML(flat);
    }

    public static String toXML(FlatListWrap wrap) {
        return xStream.toXML(wrap);
    }

    public static Flat fromXML(RequestStructure rs) {
        try {
            return (Flat) xStream.fromXML(rs.getRequestBody());
        } catch (ConversionException e) {
            rs.setMessage(Objects.isNull(e.get("message")) ? e.get("cause-message") : e.get("message"));
            return null;
        }
    }
}
