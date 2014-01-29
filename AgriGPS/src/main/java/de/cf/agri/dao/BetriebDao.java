package de.cf.agri.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import de.cf.agri.model.Betrieb;
import de.cf.agri.model.Flur;

/**
 * Created by cfrank on 28.12.13.
 */
public class BetriebDao extends SQLiteOpenHelper {

    public BetriebDao(Context context, String betrieb) {
        super(context, betrieb + ".db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table betrieb (" +
                "_id integer primary key autoincrement, " +
                "name text," +
                "betriebsnummer text," +
                "inhaber text," +
                "beschreibung text," +
                "erfdat integer," +
                "aendat integer)");
        db.execSQL("create table flur (" +
                "_id integer primary key autoincrement, " +
                "id_betrieb integer default 1," +
                "typ varchar(1), " +
                "gemarkung integer, " +
                "flurnummer varchar(12)," +
                "name text," +
                "flaeche real," +
                "erfdat integer," +
                "aendat integer)");
        db.execSQL("create table koord (" +
                "_id integer primary key autoincrement, " +
                "id_flur integer," +
                "lat real, " +
                "lon real)");
        db.execSQL("create table objekt (" +
                "_id integer primary key autoincrement, " +
                "id_flur integer," +
                "lat real, " +
                "lon real," +
                "erfdat integer," +
                "aendat integer)");
        db.execSQL("create table partner (" +
                "_id integer primary key autoincrement, " +
                "name text," +
                "contact_id integer," + /* 0 paechter, 1 verpaechter */
                "comment text," +
                "erfdat integer," +
                "aendat integer)");
        db.execSQL("create table vertrag (" +
                "_id integer primary key autoincrement, " +
                "id_flur integer," +
                "typ integer," +   /* 0 pacht */
                "rolle integer," + /* 0 paechter, 1 verpaechter */
                "partner_id integer, " +
                "preis_ha integer, " +
                "preis_abs integer, " +
                "comment text," +
                "erfdat integer," +
                "aendat integer)");
    }

    public Betrieb importiereBetrieb(Betrieb betrieb) {
        betrieb = insertBetrieb(betrieb);
        for (Flur flur : betrieb.getFlure()) {
            betrieb.addFlur(insertFlur(flur));
        }
        return betrieb;
    }

    public Betrieb insertBetrieb(Betrieb betrieb) {
        ContentValues values = new ContentValues();
        values.put("name", betrieb.getName());
        values.put("betriebsnummer", betrieb.getBetriebsnummer());
        values.put("inhaber", betrieb.getInhaber());
        values.put("beschreibung", betrieb.getComment());
        values.put("erfdat", new Date().getTime());
        betrieb.setId(getWritableDatabase().insert("betrieb", null, values));
        return betrieb;
    }

    public Flur insertFlur(Flur flur) {
        ContentValues values = new ContentValues();
        values.put("id_betrieb", flur.getBetrieb().getId());
        values.put("typ", flur.getTyp().name());
        values.put("gemarkung", flur.getGemarkung());
        values.put("flurnummer", flur.getFlurnummer());
        values.put("name", flur.getName());
        values.put("erfdat", new Date().getTime());
        flur.setId(getWritableDatabase().insert("flur", null, values));
        for (LatLng koord : flur.getKoordinaten()) {
            insertKoord(koord, flur.getId());
        }
        return flur;
    }

    public void insertKoord(LatLng koord, Long idFlur) {
        ContentValues values = new ContentValues();
        values.put("id_flur", idFlur);
        values.put("lat", koord.latitude);
        values.put("lon", koord.longitude);
        getWritableDatabase().insert("koord", null, values);
    }

    public void insertPartner(LatLng koord, Long idFlur) {
        ContentValues values = new ContentValues();
        values.put("id_flur", idFlur);
        values.put("lat", koord.latitude);
        values.put("lon", koord.longitude);
        getWritableDatabase().insert("koord", null, values);
    }

    public Collection<Betrieb> getBetriebe() {
        List<Betrieb> result = new ArrayList<Betrieb>();
        Cursor cs = getWritableDatabase().rawQuery(
                "select _id,name,betriebsnummer,inhaber,beschreibung,erfdat,aendat from betrieb",
                new String[]{});
        cs.moveToFirst();
        while (!cs.isAfterLast()){
            Betrieb b = new Betrieb();
            b.setId(cs.getLong(0));
            b.setName(cs.getString(1));
            b.setBetriebsnummer(cs.getString(2));
            b.setInhaber(cs.getString(3));
            b.setComment(cs.getString(4));
            b.setErfdat(new Date(cs.getInt(5)));
            if (!cs.isNull(6)) {
                b.setAendat(new Date(cs.getInt(6)));
            }
            result.add(b);
            cs.moveToNext();
        }
        cs.close();
        return result;
    }

    public  Betrieb fillBetrieb(Betrieb betrieb) {
        Cursor cs = getWritableDatabase().rawQuery("select _id, typ, gemarkung, flurnummer, name, erfdat, aendat from flur where id_betrieb = ?",
                new String[]{betrieb.getId() + ""});

        cs.moveToFirst();
        while (!cs.isAfterLast()){
            Flur flur = new Flur();
            flur.setId(cs.getLong(0));
            flur.setTyp(Flur.Typ.valueOf(cs.getString(1)));
            flur.setGemarkung(cs.getString(2));
            flur.setFlurnummer(cs.getString(3));
            flur.setName(cs.getString(4));
            flur.setErfdat(new Date(cs.getInt(5)));
            if (!cs.isNull(5)) {
                flur.setAendat(new Date(cs.getInt(6)));
            }
            Cursor cs2 = getReadableDatabase().rawQuery("select lat, lon from koord where id_flur = ? order by _id asc",
                    new String[]{flur.getId() + ""});
            cs2.moveToFirst();
            while(!cs2.isAfterLast()) {
                flur.addKoord(new LatLng(cs2.getDouble(0), cs2.getDouble(1)));
                cs2.moveToNext();
            }
            cs2.close();
            betrieb.addFlur(flur);
            cs.moveToNext();
        }
        cs.close();
        return betrieb;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
