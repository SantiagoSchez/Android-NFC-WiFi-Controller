package sanchezsobrino.multimedia.anwc;

import net.sourceforge.zbar.Symbol;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;

public class MainActivity extends Activity {
	private static final int ZBAR_SCANNER_REQUEST = 0;

	private Button connectButton;
	private Button addButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		connectButton = (Button) findViewById(R.id.connectButton);
		addButton = (Button) findViewById(R.id.addButton);

		connectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launchQRScanner();
			}
		});

		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), ControlActivity.class));
			}
		});

	}

	public void launchQRScanner() {
		if (isCameraAvailable()) {
			Intent intent = new Intent(this, ZBarScannerActivity.class);
			intent.putExtra(ZBarConstants.SCAN_MODES, new int[] { Symbol.QRCODE });
			startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
		} else {
			Toast.makeText(this, "Cámara no disponible", Toast.LENGTH_SHORT).show();
		}
	}

	public boolean isCameraAvailable() {
		PackageManager pm = getPackageManager();

		if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) || pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
			return true;
		}

		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Intent i = new Intent(getApplicationContext(), ConnectedActivity.class);
			i.putExtra("socket", data.getStringExtra(ZBarConstants.SCAN_RESULT));
			startActivity(i);
		} else if (resultCode == RESULT_CANCELED) {
			Toast.makeText(this, "Camera unavailable", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
