package com.example.smishingdetectionapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.smishingdetectionapp.Community.CommunityReportActivity;
import com.example.smishingdetectionapp.ui.FaqActivity; // <-- add this import
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

public class HelpActivity extends SharedActivity {

    // key used by FaqActivity to auto-expand the matching item
    public static final String EXTRA_FAQ_KEY = "faq_key";

    private void openFaq(String key) {
        Intent i = new Intent(this, FaqActivity.class);
        if (key != null) i.putExtra(EXTRA_FAQ_KEY, key);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_updated);

        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        if (nav != null) {
            nav.setSelectedItemId(R.id.nav_settings);
            nav.setOnItemSelectedListener(menuItem -> {
                int id = menuItem.getItemId();
                if (id == R.id.nav_home) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;

                } else if (id == R.id.nav_report) {
                    Intent i = new Intent(this, CommunityReportActivity.class);
                    i.putExtra("source", "home");
                    startActivity(i);
                    overridePendingTransition(0, 0);
                    finish();
                    return true;

                } else if (id == R.id.nav_news) {
                    startActivity(new Intent(getApplicationContext(), NewsActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;

                } else if (id == R.id.nav_settings) {
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    intent.putExtra("from_navigation", true);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                }
                return false;
            });
        }

        // System insets padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Back button
        ImageButton helpBack = findViewById(R.id.help_back);
        if (helpBack != null) {
            helpBack.setOnClickListener(v -> {
                startActivity(new Intent(this, SettingsActivity.class));
                finish();
            });
        }

        // Menu (optional)
        ImageButton helpMenu = findViewById(R.id.help_menu);
        if (helpMenu != null) helpMenu.setOnClickListener(v -> { /* add actions if needed */ });

        // ---------- Common Topics -> open FAQ and auto-expand ----------
        MaterialCardView cardTopic1 = findViewById(R.id.cardTopic1);
        if (cardTopic1 != null) cardTopic1.setOnClickListener(v -> openFaq("detect_smishing"));

        MaterialCardView cardTopic2 = findViewById(R.id.cardTopic2);
        if (cardTopic2 != null) cardTopic2.setOnClickListener(v -> openFaq("report_sms"));

        MaterialCardView cardTopic3 = findViewById(R.id.cardTopic3);
        if (cardTopic3 != null) cardTopic3.setOnClickListener(v -> openFaq("smishing_vs_phishing"));

        // ---------- FAQ entry (single id across layouts) ----------
        MaterialCardView cardFAQ = findViewById(R.id.cardFAQ);   // <-- ensure your layout uses this id
        if (cardFAQ != null) cardFAQ.setOnClickListener(v -> openFaq(null));

        // ---------- Contact ----------
        MaterialCardView cardCallUs = findViewById(R.id.cardCallUs);
        if (cardCallUs != null) {
            cardCallUs.setOnClickListener(v -> {
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+1234567890"));
                startActivity(phoneIntent);
            });
        }

        MaterialCardView cardMailUs = findViewById(R.id.cardMailUs);
        if (cardMailUs != null) {
            cardMailUs.setOnClickListener(v ->
                    startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:support@example.com")))
            );
        }

        MaterialCardView cardFeedback = findViewById(R.id.cardFeedback);
        if (cardFeedback != null) {
            cardFeedback.setOnClickListener(v ->
                    startActivity(new Intent(HelpActivity.this, FeedbackActivity.class))
            );
        }
    }
}
