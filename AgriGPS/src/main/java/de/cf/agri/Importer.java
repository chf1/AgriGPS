package de.cf.agri;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import de.cf.agri.model.Betrieb;
import de.cf.agri.model.Flur;

/**
 * Created by cfrank on 01.01.14.
 */
public class Importer {

    private Context context;
    public Importer(Context context) {
        this.context = context;
    }

    public Betrieb importiereFlaechen(String resName) {
        Betrieb result = new Betrieb();
        try {
            DocumentBuilderFactory DOMfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = DOMfactory.newDocumentBuilder();
            try {
                Document doc = builder.parse(context.getClassLoader().getResourceAsStream(resName));
                NodeList grundstuecke = doc.getElementsByTagName("trk");
                List<LatLng> centers = new ArrayList<LatLng>();
                for (int i = 0; i < grundstuecke.getLength(); i++) {
                    if(grundstuecke.item(i) instanceof Element){
                        Flur flurOb = new Flur();

                        Element flur = (Element) grundstuecke.item(i);
                        String attribute = flur.getAttribute("width");
                        Element segment = (Element)flur.getElementsByTagName("trkseg").item(0);
                        NodeList positions = segment.getChildNodes();
                        LatLng point = null;
                        for (int i2 = 0; i2 < positions.getLength(); i2++) {
                            if(positions.item(i2) instanceof Element){
                                Element pos = (Element)positions.item(i2);
                                point = new LatLng(
                                        Double.parseDouble(pos.getAttribute("lat")),
                                        Double.parseDouble(pos.getAttribute("lon"))
                                );
                                flurOb.addKoord(point);
                            }
                        }
                        if (point != null) {
                            Node name = flur.getElementsByTagName("name").item(0);
                            Node desc = flur.getElementsByTagName("desc").item(0);

                            StringTokenizer tok = new StringTokenizer(name.getTextContent(), "_");

                            Flur.Typ typ = Flur.Typ.valueOf(tok.nextToken().toUpperCase());
                            String gemarkung = tok.nextToken();
                            String flurnummer = tok.nextToken();
                            String flurName = tok.nextToken();
                            flurOb.setFlurnummer(flurnummer);
                            flurOb.setTyp(typ);
                            flurOb.setName(flurName);
                            flurOb.setGemarkung(gemarkung);
                        }
                        result.addFlur(flurOb);
                    }
                }
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return result;
    }
}
