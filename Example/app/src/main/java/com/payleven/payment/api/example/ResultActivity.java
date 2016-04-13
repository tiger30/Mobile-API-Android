package com.payleven.payment.api.example;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.payleven.payment.api.ApiPaymentCompletedStatus;
import com.payleven.payment.api.ApiSalesHistoryCompletedStatus;
import com.payleven.payment.api.ApiTransactionDetailsCompletedStatus;
import com.payleven.payment.api.PaylevenApi;
import com.payleven.payment.api.PaylevenResponseListener;
import com.payleven.payment.api.TransactionRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ResultActivity extends ListActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent();
        if (getIntent().hasExtra("result")) {
            i.putExtras(getIntent().getExtras().getBundle("result"));
        }
        int requestCode = getIntent().getIntExtra("request_code", 0);
        PaylevenApi.handleIntent(requestCode, i, new PaylevenResponseListener() {

            @Override
            public void onPaymentFinished(String orderId, TransactionRequest originalRequest, Map<String, String> result, ApiPaymentCompletedStatus status) {
                SharedPreferences prefs = getSharedPreferences("api_example", Context.MODE_PRIVATE);
                prefs.edit().putString("order_id", orderId).apply();
                showResult(orderId, status.getClass().getSimpleName() + "" + status.name(), result);
            }

            @Override
            public void onNoPaylevenResponse(Intent data) {

            }

            @Override
            public void onOpenTransactionDetailsFinished(String orderId, Map<String, String> transactionData, ApiTransactionDetailsCompletedStatus status) {
                showResult(orderId, status.getClass().getSimpleName() + "" + status.name(), transactionData);
            }

            @Override
            public void onOpenSalesHistoryFinished(ApiSalesHistoryCompletedStatus status) {
                showResult(null, status.getClass().getSimpleName() + "" + status.name(), null);
            }

        });
        
    }
    
    protected void showResult(String orderId, String statusName, Map<String, String> result) {
        List<String> resultList = new ArrayList<String>();
        resultList.add("OrderId = " + orderId);
        resultList.add("Status = " + statusName);
        if (result != null) {
            for (Entry<String, String> entry : result.entrySet()) {
                resultList.add(entry.getKey() + " = " + entry.getValue());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ResultActivity.this, android.R.layout.simple_list_item_1, resultList);
        setListAdapter(adapter);
    }
    
}
