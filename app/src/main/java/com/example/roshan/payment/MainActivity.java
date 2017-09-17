package com.example.roshan.payment;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    TextView m_response;
    PayPalConfiguration m_configuration;
    String screte_key="ASprTXEtiT3J6amc8ilvLCDbrIE--sRJBa4QDvG3N-8WSsMmJ_Imn0jXn3b2HnJlWor1wPwHLNF9TExS";
    Intent m_service;
    int m_paypalRequestCode=999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_response=(TextView)findViewById(R.id.txt_response);

        m_configuration=new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(screte_key);
        m_service=new Intent(this, PaymentActivity.class);
        m_service.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,m_configuration);
        startActivity(m_service);

    }

    public void pay(View view){

        PayPalPayment payment=new PayPalPayment(new BigDecimal(10),"USD","Test Payment",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent=new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,m_configuration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);
        startActivityForResult(intent,m_paypalRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==m_paypalRequestCode){
            if (resultCode== Activity.RESULT_OK){
                PaymentConfirmation confirmation=data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation !=null){
                    String state=confirmation.getProofOfPayment().getState();

                    if (state.equals("approved")){
                        m_response.setText("Payment Approved");
                    }else {
                        m_response.setText("error in the payment");
                    }
                }else {
                    m_response.setText("Confirmation is null");
                }
            }
        }
    }
}
