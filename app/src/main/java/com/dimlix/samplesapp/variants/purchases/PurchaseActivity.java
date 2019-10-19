package com.dimlix.samplesapp.variants.purchases;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.dimlix.samplesapp.R;
import com.dimlix.samplesapp.variants.BaseSampleActivity;

public class PurchaseActivity extends BaseSampleActivity implements BillingProcessor.IBillingHandler {

    BillingProcessor mBillingProcessor;

    private final static String ONE_TIME_PAYMENT = "otp";
    private final static String MULTI_TIME_PAYMENT = "mtp";
    private final static String SUBSCRIPTION = "sups";

    @Override
    protected int getLayoutId() {
        return R.layout.billing_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBillingProcessor = new BillingProcessor(this, "YOUR LICENSE KEY FROM GOOGLE PLAY CONSOLE HERE", this);
        mBillingProcessor.initialize();
    }

    @Override
    public void onBillingInitialized() {
        /*
         * Called when BillingProcessor was initialized and it's ready to purchase
         */
        showMsg("onBillingInitialized");
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        /*
         * Called when requested PRODUCT ID was successfully purchased
         */
        showMsg("onProductPurchased");
        if (checkIfPurchaseIsValid(details.purchaseInfo)) {
            showMsg("purchase: " + productId + " COMPLETED");
        } else {
            showMsg("fakePayment");
        }
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        /*
         * Called when some error occurred. See Constants class for more details
         *
         * Note - this includes handling the case where the user canceled the buy dialog:
         * errorCode = Constants.BILLING_RESPONSE_RESULT_USER_CANCELED
         */
        showMsg("onBillingError");
    }

    @Override
    public void onPurchaseHistoryRestored() {
        /*
         * Called when purchase history was restored and the list of all owned PRODUCT ID's
         * was loaded from Google Play
         */
        showMsg("onPurchaseHistoryRestored");
    }

    /**
     * With this PurchaseInfo a developer is able verify
     * a purchase from the google play store on his own
     * server. An example implementation of how to verify
     * a purchase you can find <a href="https://github.com/mgoldsborough/google-play-in-app-billing-
     * verification/blob/master/library/GooglePlay/InAppBilling/GooglePlayResponseValidator.php#L64">here</a>
     * @return if purchase is valid
     */
    private boolean checkIfPurchaseIsValid(PurchaseInfo purchaseInfo) {
        // TODO as of now we assume that all purchases are valid
        return true;
    }

    private void showMsg(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mBillingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        if (mBillingProcessor != null) {
            mBillingProcessor.release();
        }
        super.onDestroy();
    }

}
