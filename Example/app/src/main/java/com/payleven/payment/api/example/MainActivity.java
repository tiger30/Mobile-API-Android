package com.payleven.payment.api.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.payleven.payment.api.PaylevenApi;
import com.payleven.payment.api.TransactionRequest;
import com.payleven.payment.api.TransactionRequestBuilder;

import java.util.Currency;
import java.util.UUID;

public class MainActivity extends Activity {

    private final static String LOG_TAG               = MainActivity.class.getSimpleName();
    private static final String PAYLEVEN_PACKAGE_NAME = "de.payleven.androidphone";

    private String mApiKey;

    private EditText mEtOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mApiKey = getString(R.string.api_key);

        PaylevenApi.configure(mApiKey);

        final Button startPaymentButton = (Button) findViewById(R.id.button_start_payment);
        final EditText etDescription = (EditText) findViewById(R.id.editText_description);
        final EditText etAmount = (EditText) findViewById(R.id.editText_amount);
        final EditText etEmail = (EditText) findViewById(R.id.editText_email);
        mEtOrderId = (EditText) findViewById(R.id.editText_order_id);

        startPaymentButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // check if the payleven application is available
                // if not, display a dialog that opens the playstore.
                if (!PaylevenApi.isPaylevenAvailable(MainActivity.this)) {
                    displayPlaystoreDialog();
                    return;
                }

                try {

                    int amount = Integer.parseInt(etAmount.getText().toString());
                    String description = etDescription.getText().toString();
                    Bitmap image = BitmapFactory.decodeResource(getResources(),
                            R.drawable.payleven);

                    TransactionRequestBuilder builder = new TransactionRequestBuilder(amount,
                            getSelectedCurrency());
                    builder.setDescription(description)
                            .setBitmap(image);

                    String email = etEmail.getText().toString();
                    if (!TextUtils.isEmpty(email)) {
                        builder.setEmail(email);
                    }

                    TransactionRequest request = builder.createTransactionRequest();

                    //create a unique id for the payment.
                    //For reasons of simplicity the UUID class is used here.
                    // In a production environment it would be more feasible to use
                    // an ascending numbering scheme
                    String orderId = UUID.randomUUID().toString();
                    PaylevenApi.initiatePayment(MainActivity.this, orderId, request);
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), R.string.invalid_amount,
                            Toast.LENGTH_LONG).show();
                } catch (IllegalArgumentException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        final Button startSalesHistoryButton = (Button) findViewById(R.id.button_start_history);
        startSalesHistoryButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!PaylevenApi.isPaylevenAvailable(MainActivity.this)) {
                    displayPlaystoreDialog();
                    return;
                }
                String email = etEmail.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    PaylevenApi.openSalesHistory(MainActivity.this);
                }
            }
        });

        final Button startRefundButton = (Button) findViewById(R.id.button_start_refund);
        startRefundButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!PaylevenApi.isPaylevenAvailable(MainActivity.this)) {
                    displayPlaystoreDialog();
                    return;
                }

                String orderId = mEtOrderId.getText().toString();
                PaylevenApi.openTransactionDetailsForRefund(MainActivity.this, orderId);

            }
        });

        final Button startDetailsButton = (Button) findViewById(R.id.button_start_details);
        startDetailsButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!PaylevenApi.isPaylevenAvailable(MainActivity.this)) {
                    displayPlaystoreDialog();
                    return;
                }

                String orderId = mEtOrderId.getText().toString();
                String email = etEmail.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    PaylevenApi.openTransactionDetails(MainActivity.this, orderId);
                } else {
                    PaylevenApi.openTransactionDetails(MainActivity.this, orderId, email);

                }
            }
        }

        );
    }

    /**
     * Returns the currency selected by the user in the radio buttons
     * @return {@link Currency}
     */
    private Currency getSelectedCurrency() {
        RadioGroup radioButtonGroupView = (RadioGroup) findViewById(R.id.currency_radiogroup);
        String currencyCode = ((RadioButton) this.findViewById(radioButtonGroupView
                .getCheckedRadioButtonId())).getText().toString();
        return Currency.getInstance(currencyCode);
    }

    /**
     * Displays a dialog notifying the user that the payleven app must be installed
     * Pressing ok will open the playstore to the payleven app.
     */

    private void displayPlaystoreDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.install_payleven)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id="
                                        + PAYLEVEN_PACKAGE_NAME)));
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("api_example", Context.MODE_PRIVATE);
        String orderId = prefs.getString("order_id", "");
        mEtOrderId.setText(orderId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        Log.i(LOG_TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);

        //handle response in ResultActivity
        Intent i = new Intent(MainActivity.this, ResultActivity.class);
        if (data != null) {
            i.putExtra("result", data.getExtras());
        }
        i.putExtra("request_code", requestCode);
        startActivity(i);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.default_orientation:
                if (checked)
                    PaylevenApi.configure(mApiKey, PaylevenApi.Orientation.DEFAULT);
                break;
            case R.id.portrait_orientation:
                if (checked)
                    PaylevenApi.configure(mApiKey, PaylevenApi.Orientation.PORTRAIT);
                break;
            case R.id.landscape_orientation:
                if (checked)
                    PaylevenApi.configure(mApiKey, PaylevenApi.Orientation.LANDSCAPE);
                break;
        }
    }
}
