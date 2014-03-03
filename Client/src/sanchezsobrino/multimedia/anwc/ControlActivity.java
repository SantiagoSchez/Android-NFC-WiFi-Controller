package sanchezsobrino.multimedia.anwc;

import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ControlActivity extends Activity {
	private ListView controlListView;
	private String[] controlArray;
	private boolean isWriteReady = false;
	private NfcAdapter mNfcAdapter;
	private CodeEvent currentEvent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);

		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mNfcAdapter == null) {
			Toast.makeText(this, "NFC no está disponible en este dispositivo", Toast.LENGTH_SHORT).show();
			finish();
		} else if (!mNfcAdapter.isEnabled()) {
			Toast.makeText(this, "NFC desactivado. Por favor, actívelo", Toast.LENGTH_SHORT).show();
			finish();
		}

		controlArray = new String[6];
		controlArray[0] = "Arriba";
		controlArray[1] = "Abajo";
		controlArray[2] = "Izquierda";
		controlArray[3] = "Derecha";
		controlArray[4] = "F5";
		controlArray[5] = "Esc";

		controlListView = (ListView) findViewById(R.id.controlListView);
		controlListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, controlArray));

		controlListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					currentEvent = CodeEvent.ARRIBA;
					setTagWriteReady(true);
					break;
				case 1:
					currentEvent = CodeEvent.ABAJO;
					setTagWriteReady(true);
					break;
				case 2:
					currentEvent = CodeEvent.IZQUIERDA;
					setTagWriteReady(true);
					break;
				case 3:
					currentEvent = CodeEvent.DERECHA;
					setTagWriteReady(true);
					break;
					
				case 4:
					currentEvent = CodeEvent.START_PRESENTATION;
					setTagWriteReady(true);
					break;
				case 5:
					currentEvent = CodeEvent.STOP_PRESENTATION;
					setTagWriteReady(true);
					break;
				}
			}
		});
	}
	
	public void onResume() {
		super.onResume();
		if (isWriteReady && NfcAdapter.ACTION_TAG_DISCOVERED.equals(getIntent().getAction())) {
			try {
				writeTag(getIntent());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	public void setTagWriteReady(boolean flag) {
		isWriteReady = flag;

		if (isWriteReady) {
			IntentFilter[] filters = new IntentFilter[] { new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED) };
			mNfcAdapter.enableForegroundDispatch(this, NfcUtils.getPendingIntent(this), filters, null);
		} else {
			mNfcAdapter.disableForegroundDispatch(this);
		}
	}

	public void onNewIntent(Intent intent) {
		setIntent(intent);
	}

	private static final String MIME_TYPE = "application/sanchezsobrino.multimedia.anwc.tag";
	
	private void writeTag(Intent intent) throws UnsupportedEncodingException {
		if (isWriteReady && NfcAdapter.ACTION_TAG_DISCOVERED.equals(getIntent().getAction())) {
			Tag detectedTag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
			byte[] payload = new String(String.valueOf(CodeEvent.fromCodeEvent(currentEvent))).getBytes();
			
			if (detectedTag != null && NfcUtils.writeTag(NfcUtils.createMessage(MIME_TYPE, payload), detectedTag)) {
				Toast.makeText(this, "Escrito: '" + CodeEvent.fromInt(Integer.parseInt(new String(payload, "UTF-8"))).toString() + "'", Toast.LENGTH_LONG).show();
				setTagWriteReady(false);
			} else {
				Toast.makeText(this, "La escritura fallÃ³, intentalo de nuevo", Toast.LENGTH_LONG).show();
			}
		}
	}
}
