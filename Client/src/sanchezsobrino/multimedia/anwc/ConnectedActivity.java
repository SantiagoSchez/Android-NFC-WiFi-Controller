package sanchezsobrino.multimedia.anwc;

import java.io.DataOutputStream;
import android.os.PowerManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class ConnectedActivity extends Activity {
	private Socket socket;
	private Thread clientThread;
	private NfcAdapter mNfcAdapter;
	private IntentFilter[] intentFiltersArray;
	private String[][] techListsArray;
	private PendingIntent pendingIntent;

	private Button startButton;
	private Button stopButton;
	private Button btnAr, btnIz;
	private Button btnAb, btnDe;
	
	protected PowerManager.WakeLock wakelock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connected);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		if (getIntent().getExtras().getString("socket") != null) {
			String[] socket_str = getIntent().getExtras().getString("socket").split(":");
			Toast.makeText(this, socket_str[0] + ":" + socket_str[1], Toast.LENGTH_SHORT).show();
			clientThread = new ClientThread(socket_str[0], Integer.parseInt(socket_str[1]));
			clientThread.start();
		}

		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mNfcAdapter == null) {
			Toast.makeText(this, "NFC no está disponible en este dispositivo", Toast.LENGTH_SHORT).show();
		}

		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		
		if (mNfcAdapter != null){
			IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		
			try {
				ndef.addDataType("application/sanchezsobrino.multimedia.anwc.tag");
			} catch (MalformedMimeTypeException e) {
				throw new RuntimeException("fail", e);
			}
			intentFiltersArray = new IntentFilter[] { ndef, };
			techListsArray = new String[][] { new String[] { NfcF.class.getName() } };
		}

		startButton = (Button) findViewById(R.id.startButton);
		stopButton = (Button) findViewById(R.id.stopButton);
		btnAr = (Button) findViewById(R.id.btnAr);
		btnIz = (Button) findViewById(R.id.btnIz);
		btnAb = (Button) findViewById(R.id.btnAb);
		btnDe = (Button) findViewById(R.id.btnDe);

		startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					PrintWriter out = new PrintWriter(new DataOutputStream(socket.getOutputStream()), true);
					out.println(CodeEvent.fromCodeEvent(CodeEvent.START_PRESENTATION));
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		stopButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					PrintWriter out = new PrintWriter(new DataOutputStream(socket.getOutputStream()), true);
					out.println(CodeEvent.fromCodeEvent(CodeEvent.STOP_PRESENTATION));
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		btnAr.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					PrintWriter out = new PrintWriter(new DataOutputStream(socket.getOutputStream()), true);
					out.println(CodeEvent.fromCodeEvent(CodeEvent.ARRIBA));
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		btnAb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					PrintWriter out = new PrintWriter(new DataOutputStream(socket.getOutputStream()), true);
					out.println(CodeEvent.fromCodeEvent(CodeEvent.ABAJO));
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		btnIz.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					PrintWriter out = new PrintWriter(new DataOutputStream(socket.getOutputStream()), true);
					out.println(CodeEvent.fromCodeEvent(CodeEvent.IZQUIERDA));
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		btnDe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					PrintWriter out = new PrintWriter(new DataOutputStream(socket.getOutputStream()), true);
					out.println(CodeEvent.fromCodeEvent(CodeEvent.DERECHA));
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected void onDestroy(){
		super.onDestroy();
		
    }
	
	public void onPause() {
		super.onPause();
		if (mNfcAdapter != null) mNfcAdapter.disableForegroundDispatch(this);
	}

	public void onResume() {
		super.onResume();
		if (mNfcAdapter != null)  mNfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finish();
	}

	public void onNewIntent(Intent intent) {
		List<NdefMessage> intentMessages = NfcUtils.getMessagesFromIntent(intent);
		List<String> payloadStrings = new ArrayList<String>(intentMessages.size());

		for (NdefMessage message : intentMessages) {
			for (NdefRecord record : message.getRecords()) {
				byte[] payload = record.getPayload();
				String payloadString = new String(payload);

				if (!TextUtils.isEmpty(payloadString))
					payloadStrings.add(payloadString);
			}
		}

		for (String ps : payloadStrings) {
			try {
				PrintWriter out = new PrintWriter(new DataOutputStream(socket.getOutputStream()), true);
				out.println(Integer.parseInt(ps));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class ClientThread extends Thread {
		private String ip;
		private int port;

		public ClientThread(String ip, int port) {
			this.ip = ip;
			this.port = port;
		}

		@Override
		public void run() {
			try {
				socket = new Socket(ip, port);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
