package de.cf.agri;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import de.cf.agri.model.Betrieb;
import de.cf.agri.model.MapObject;

import static de.cf.agri.model.MapObject.Type;


/**
 * Created by cfrank on 22.12.13.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ImportBetriebDialog extends DialogFragment {

    public static interface Callback {
        public void uebernehmen(Betrieb betrieb);
    }

    public ImportBetriebDialog() {
        super();
    }

    public interface MapObjectListener {
        void onObjektAnlage(MapObject object);
    }

    private Button btnImport;
    private Button btnCancel;
    private EditText edtBetriebsname;
    private EditText edtBetriebsnummer;
    private EditText edtInhaber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.import_betrieb, container, false);

        Activity activity = getActivity();
        final Betrieb betrieb = ((BetriebHolder) activity).getBetrieb();
        final Callback callback = (Callback) activity;

        edtBetriebsname = (EditText)view.findViewById(R.id.edtBetriebsname);
        edtBetriebsnummer = (EditText)view.findViewById(R.id.edtBetriebsnummer);
        edtInhaber = (EditText)view.findViewById(R.id.editInhaber);

        btnImport = (Button)view.findViewById(R.id.btnimport);
        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                betrieb.setName(edtBetriebsname.getText().toString());
                betrieb.setBetriebsnummer(edtBetriebsnummer.getText().toString());
                betrieb.setInhaber(edtInhaber.getText().toString());
                dismiss();
                callback.uebernehmen(betrieb);
            }
        });
        btnCancel = (Button)view.findViewById(R.id.btnabbruch);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImportBetriebDialog.this.dismiss();
            }
        });
        getDialog().setTitle("Betrieb importieren");
        return view;
    }
}
/*
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.import_betrieb, null);
        builder.setTitle("Betrieb importieren:")
            .setMessage("Es wurde kein Betrieb in der Datenbank gefunden,\nes ist aber ein Betrieb in der Importdatei init.GPX vorhanden")
            .setView(inflater.inflate(R.layout.import_betrieb, null));
        edtBetriebsname = (EditText)view.findViewById(R.id.edtBetriebsname);
        edtBetriebsnummer = (EditText)view.findViewById(R.id.edtBetriebsnummer);
        edtInhaber = (EditText)view.findViewById(R.id.editInhaber);

        btnImport = (Button)view.findViewById(R.id.btnimport);
        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                betrieb.setBeschreibung(edtBetriebsname.getText().toString());
                betrieb.setBetriebsnummer(edtBetriebsnummer.getText().toString());
                betrieb.setInhaber(edtInhaber.getText().toString());
                dismiss();
                callback.uebernehmen(betrieb);
            }
        });
        btnCancel = (Button)view.findViewById(R.id.btnabbruch);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImportBetriebDialog.this.dismiss();
            }
        });
        return builder.create();
    }

}
*/