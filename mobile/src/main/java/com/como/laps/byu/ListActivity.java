package com.como.laps.byu;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends Activity {

    private PendingIntent nfcPendingIntent;
    private IntentFilter[] intentFiltersArray;
    private NfcAdapter nfcAdpt;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        final View layout = findViewById(R.id.list_layout);
        Bitmap b = BitmapUtils.getBitmap("background.png", this, size.x, size.y);
        BitmapDrawable background = new BitmapDrawable(getResources(), b);
        layout.setBackground(background);
        final ListView productListView = (ListView) findViewById(R.id.product_list);
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("iohoihsoic", "Iphone 6", "Photo", 600f, true));
        productList.add(new Product("iohoihsoic", "Iphone 6", "Photo", 600f, true));
        productList.add(new Product("iohoihsoic", "Iphone 6", "Photo", 600f, true));
        productList.add(new Product("iohoihsoic", "Iphone 6", "Photo", 600f, true));
        productList.add(new Product("iohoihsoic", "Iphone 6", "Photo", 600f, true));
        productList.add(new Product("iohoihsoic", "Iphone 6", "Photo", 600f, true));
        productList.add(new Product("iohoihsoic", "Iphone 6", "Photo", 600f, true));
        productList.add(new Product("iohoihsoic", "Iphone 6", "Photo", 600f, true));
        productList.add(new Product("iohoihsoic", "Iphone 6", "Photo", 600f, true));
        productList.add(new Product("iohoihsoic", "Iphone 6", "Photo", 600f, true));
        productList.add(new Product("iohoihsoic", "Iphone 6", "Photo", 600f, true));

        ProductAdapter productAdapter = new ProductAdapter(this, 0, productList);
        productListView.setAdapter(productAdapter);

        Intent nfcIntent = new Intent(this, getClass());
        nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        nfcPendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);

        IntentFilter tagIntentFilter =
                new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            tagIntentFilter.addDataType("text/plain");
            intentFiltersArray = new IntentFilter[]{tagIntentFilter};
        } catch (Throwable t) {
            t.printStackTrace();
        }
        nfcAdpt = NfcAdapter.getDefaultAdapter(this);
        // Check if the smartphone has NFC
        if (nfcAdpt == null) {
            Toast.makeText(this, "NFC not supported", Toast.LENGTH_LONG).show();
            finish();
        }
        // Check if NFC is enabled
        if (!nfcAdpt.isEnabled()) {
            Toast.makeText(this, "Enable NFC before using the app", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.d("Nfc", "New intent");
        try {
            getTag(intent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void getTag(Intent i) throws UnsupportedEncodingException {

        if (i == null)
            return;

        String type = i.getType();
        String action = i.getAction();

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Log.d("Nfc", "Action NDEF Found");
            Parcelable[] parcs = i.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            // List record

            for (Parcelable p : parcs) {
                NdefMessage msg = (NdefMessage) p;
                final int numRec = msg.getRecords().length;

                NdefRecord[] records = msg.getRecords();
                for (NdefRecord record : records) {
                    byte[] payload = record.getPayload();
                    String payloadText = new String(payload, "UTF-8");
                }
            }

        }

    }


    private void handleIntent(Intent i) {
        Log.d("NFC", "Intent [" + i + "]");
        try {
            getTag(i);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcAdpt.enableForegroundDispatch(
                this,
                nfcPendingIntent,
                intentFiltersArray,
                null);
        handleIntent(getIntent());
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcAdpt.disableForegroundDispatch(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
