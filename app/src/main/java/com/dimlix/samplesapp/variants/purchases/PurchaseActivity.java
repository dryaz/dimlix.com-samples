package com.dimlix.samplesapp.variants.purchases;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;
import com.anjlab.android.iab.v3.SkuDetails;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.dimlix.samplesapp.R;
import com.dimlix.samplesapp.variants.BaseSampleActivity;

public class PurchaseActivity extends BaseSampleActivity implements BillingProcessor.IBillingHandler {

    BillingProcessor mBillingProcessor;

    private final static String ONE_TIME_PAYMENT = "otp";
    private final static String SUBSCRIPTION = "subs";

    private final static String GPLAY_LICENSE = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnjzOg5BORvyrs6yRBjTqDdSisuHfTJGYO5ciWRbNlG8T9b0WTGfFsJAVjc31TMYERFg/w+N2Ugyj5+dgE564HORY7N23tYp6p0Iv3ozC7+z5BaNMdRGMkz6tlQFu2xqYkRjj4/yIOhq7xnnIVNohfE66N6Tleov2szfSLaIQ/3UU20zlcophWXVMMg/Hxu0raYtl9jAoJKMG9jk2iC4aI2qTLD0ulmHuesiNOtCFlntVmI9eqkiPgFpf9PoIgZyyOgYZJvXT6EK4E4DYjErY7F7YM8OsEwUDsbOzZdBS+HkxmGlcJLZmRmhBRp4s1TfcIlMhyfWKAQcdx8gHLyLYzwIDAQAB";

    private Button mSingleTimePaymentButton;
    private Button mConsumabelButton;
    private Button mSubscriptionButton;

    private View mProgress;

    @Override
    protected int getLayoutId() {
        return R.layout.billing_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBillingProcessor = new BillingProcessor(this, GPLAY_LICENSE, this);

        mProgress = findViewById(R.id.progress);

        mSingleTimePaymentButton = findViewById(R.id.btnSingleTypePayment);
        mConsumabelButton = findViewById(R.id.btnConsume);
        mSubscriptionButton = findViewById(R.id.btnSubscription);

        boolean isAvailable = BillingProcessor.isIabServiceAvailable(this);
        if (!isAvailable) {
            mProgress.setVisibility(View.GONE);
            showMsg(getString(R.string.billing_not_available));
        } else {
            mBillingProcessor.initialize();
        }

        setupButtonClickListeners();
    }

    private void setupButtonClickListeners() {
        mSingleTimePaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBillingProcessor.purchase(PurchaseActivity.this, ONE_TIME_PAYMENT);
            }
        });

        mSubscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBillingProcessor.subscribe(PurchaseActivity.this, SUBSCRIPTION);
            }
        });

        mConsumabelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean consumptionResult = mBillingProcessor.consumePurchase(ONE_TIME_PAYMENT);
                if (consumptionResult) {
                    setupConsumableButtons(false);
                }
            }
        });
    }

    @Override
    public void onBillingInitialized() {
        /*
         * Called when BillingProcessor was initialized and it's ready to purchase
         */
        showMsg("onBillingInitialized");
        if (mBillingProcessor.loadOwnedPurchasesFromGoogle()) {
            handleLoadedItems();
        }
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        /*
         * Called when requested PRODUCT ID was successfully purchased
         */
        showMsg("onProductPurchased");
        if (checkIfPurchaseIsValid(details.purchaseInfo)) {
            showMsg("purchase: " + productId + " COMPLETED");
            switch (productId) {
                case ONE_TIME_PAYMENT:
                    setupConsumableButtons(true);
                    break;
                case SUBSCRIPTION:
                    setupSubscription(true);
                    break;
            }
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
        handleLoadedItems();
    }

    private void handleLoadedItems() {
        mProgress.setVisibility(View.GONE);

        boolean isOneTimePurchaseSupported = mBillingProcessor.isOneTimePurchaseSupported();
        if (isOneTimePurchaseSupported) {
            mSingleTimePaymentButton.setVisibility(View.VISIBLE);
            mConsumabelButton.setVisibility(View.VISIBLE);
        } else {
            showMsg(getString(R.string.one_time_payment_not_supported));
        }

        boolean isSubsUpdateSupported = mBillingProcessor.isSubscriptionUpdateSupported();
        if (isSubsUpdateSupported) {
            mSubscriptionButton.setVisibility(View.VISIBLE);
        } else {
            showMsg(getString(R.string.subscription_not_supported));
        }

        setupConsumableButtons(mBillingProcessor.listOwnedProducts().contains(ONE_TIME_PAYMENT));
        setupSubscription(mBillingProcessor.listOwnedSubscriptions().contains(SUBSCRIPTION));
    }

    private void setupConsumableButtons(boolean isPurchased) {
        mConsumabelButton.setEnabled(isPurchased);
        mSubscriptionButton.setEnabled(!isPurchased);
        if (isPurchased) {
            mSingleTimePaymentButton.setText(R.string.already_bought);
        } else {
            SkuDetails details = mBillingProcessor.getPurchaseListingDetails(ONE_TIME_PAYMENT);
            mSingleTimePaymentButton.setText(getString(R.string.one_time_payment_value, details.priceText));
            mConsumabelButton.setText(R.string.not_bought_yet);
        }
    }

    private void setupSubscription(boolean isPurchased) {
        mSubscriptionButton.setEnabled(!isPurchased);
        if (isPurchased) {
            mSubscriptionButton.setText(R.string.already_subscribed);
        } else {
            SkuDetails details = mBillingProcessor.getSubscriptionListingDetails(SUBSCRIPTION);
            mSubscriptionButton.setText(getString(R.string.subscription_value, details.priceText));
        }
    }

    /**
     * With this PurchaseInfo a developer is able verify
     * a purchase from the google play store on his own
     * server. An example implementation of how to verify
     * a purchase you can find <a href="https://github.com/mgoldsborough/google-play-in-app-billing-
     * verification/blob/master/library/GooglePlay/InAppBilling/GooglePlayResponseValidator.php#L64">here</a>
     *
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
