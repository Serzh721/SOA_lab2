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
    public static XStream createXStream() {
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
}
