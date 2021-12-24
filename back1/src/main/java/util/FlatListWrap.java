package util;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;
import model.Flat;

import java.util.List;

@Data
@XStreamAlias("flatList")
public class FlatListWrap {

    public FlatListWrap(List<Flat> flats, int totalFlats) {
        this.flats = flats;
        this.totalFlats = totalFlats;
    }

    @XStreamImplicit
    private List<Flat> flats;

    private int totalFlats;
}
