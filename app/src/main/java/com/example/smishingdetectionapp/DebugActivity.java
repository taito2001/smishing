package com.example.smishingdetectionapp;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smishingdetectionapp.notifications.NotificationHelper;
import com.example.smishingdetectionapp.notifications.NotificationType;
import com.example.smishingdetectionapp.navigation.BottomNavCoordinator;

public class DebugActivity extends AppCompatActivity {

    private LinearLayout buttonContainer;
    private LinearLayout currentCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        BottomNavCoordinator.setup(this, R.id.nav_settings);

        buttonContainer = findViewById(R.id.debugButtonContainer);

        // Notification Tests:
        beginCategory("Notifcation Tests");

        addButton("Test Smishing Notification", () -> {
            NotificationType type = NotificationType.createSmishDetectionAlert(getApplicationContext());
            new NotificationHelper(getApplicationContext())
                    .createNotification(type, "SMISHING MESSAGE TEST", "SMISHY MESSAGE");
        });

        addButton("Test Spam Notification", () -> {
            NotificationType type = NotificationType.createSpamDetectionAlert(getApplicationContext());
            new NotificationHelper(getApplicationContext())
                    .createNotification(type, "SPAM MESSAGE TEST", "SPAMMY MESSAGE");
        });

        addButton("Test News Notification", () -> {
            NotificationType type = NotificationType.createNewsAlert(getApplicationContext());
            new NotificationHelper(getApplicationContext())
                    .createNotification(type, "NEWS NOTIFICATION", "SOME NEWS");
        });

        addButton("Test All Notifications", () -> {
            sendAllTestNotifications();
        });

        // SMS Tests:
        beginCategory("SMS Tests");

        addButton("Open SMS Activity", () -> {
            startActivity(new Intent(this,SmsActivity.class));
        });

        //Risk Scanner Tests
        beginCategory("Risk Scanner Tests");

        addButton("Run Scan (Raw Values)", () -> {
            try {
                com.example.smishingdetectionapp.domain.riskscanner.AndroidRiskCheckProvider provider =
                        new com.example.smishingdetectionapp.domain.riskscanner.AndroidRiskCheckProvider(this);
                com.example.smishingdetectionapp.domain.riskscanner.RiskScore score =
                        com.example.smishingdetectionapp.domain.riskscanner.RiskScannerEngine.INSTANCE.scanHabits(
                                provider, false, false, false, 23
                        );

                StringBuilder sb = new StringBuilder();
                sb.append("Score: ").append(score.getTotalScore()).append("%\n");
                sb.append("Risk Level: ").append(score.getRiskLevel()).append("\n\n");

                sb.append("Check Results:\n");
                for (com.example.smishingdetectionapp.domain.riskscanner.RiskCheckResult result : score.getCheckResults()) {
                    sb.append(result.getPassed() ? "✅" : "❌");
                    sb.append(" ").append(result.getName());
                    sb.append(" (").append(result.getRiskPoints()).append(" pts)");
                    if (!result.getPassed()) {
                        sb.append("\n   → ").append(result.getFailureMessage());
                    }
                    sb.append("\n");
                }

                if (!score.getTriggeredRisks().isEmpty()) {
                    sb.append("\nTriggered Risks:\n");
                    for (String risk : score.getTriggeredRisks()) {
                        sb.append("• ").append(risk).append("\n");
                    }
                } else {
                    sb.append("\nNo risks triggered!\n");
                }

                // Show in a dialog so it's readable
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Risk Scanner Results")
                        .setMessage(sb.toString())
                        .setPositiveButton("OK", null)
                        .show();

            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });


        addButton("Risk Scanner (All Checks)", () -> {
            try {
                startActivity(new Intent(this, com.example.smishingdetectionapp.riskmeter.RiskScannerTCActivity.class));
            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        //KMM Util Tests:
        beginCategory("KMM Util Tests");

        addButton("Platform Info", () -> {
            try {
                com.example.smishingdetectionapp.util.PlatformInfo info =
                        com.example.smishingdetectionapp.util.PlatformInfo_androidKt.getPlatformInfo(this);
                String msg = info.getPlatformName() + " " + info.getOsVersion() + "\n" +
                        info.getDeviceModel() + " v" + info.getAppVersion();
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });

    }

    private void beginCategory(String title) {
        LinearLayout categoryWrapper = new LinearLayout(this);
        categoryWrapper.setOrientation(LinearLayout.VERTICAL);
        categoryWrapper.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        LinearLayout headerRow = new LinearLayout(this);
        headerRow.setOrientation(LinearLayout.HORIZONTAL);
        headerRow.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        headerRow.setPadding(0, 32, 0, 8);
        headerRow.setGravity(android.view.Gravity.CENTER_VERTICAL);

        TextView arrow = new TextView(this);
        arrow.setText("▼ ");
        arrow.setTextSize(16);
        arrow.setClickable(false);  // ← don't consume clicks
        arrow.setFocusable(false);

        TextView header = new TextView(this);
        header.setText(title);
        header.setTextSize(18);
        header.setTextColor(0xFF4B74F0);
        header.setTypeface(null, android.graphics.Typeface.BOLD);
        header.setClickable(false);  // ← don't consume clicks
        header.setFocusable(false);

        LinearLayout.LayoutParams headerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        arrow.setLayoutParams(headerParams);
        header.setLayoutParams(headerParams);

        headerRow.addView(arrow);
        headerRow.addView(header);

        LinearLayout categoryButtons = new LinearLayout(this);
        categoryButtons.setOrientation(LinearLayout.VERTICAL);
        categoryButtons.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        categoryButtons.setVisibility(View.GONE);  // ← start collapsed

        // Toggle on header click
        final boolean[] expanded = {false};
        headerRow.setOnClickListener(v -> {
            if (expanded[0]) {
                categoryButtons.setVisibility(View.GONE);
                arrow.setText("▶ ");
                expanded[0] = false;
            } else {
                categoryButtons.setVisibility(View.VISIBLE);
                arrow.setText("▼ ");
                expanded[0] = true;
            }
        });

        categoryWrapper.addView(headerRow);
        categoryWrapper.addView(categoryButtons);
        buttonContainer.addView(categoryWrapper);

        currentCategory = categoryButtons;
    }


    private void addButton(String label, Runnable action) {
        Button button = new Button(this);
        button.setText(label);
        button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        button.setOnClickListener(v -> action.run());
        currentCategory.addView(button);
    }

    private void sendAllTestNotifications() {
        NotificationHelper helper = new NotificationHelper(getApplicationContext());

        helper.createNotification(
                NotificationType.createSmishDetectionAlert(this),
                "Smishing Alert", "Test smishing notification.");

        helper.createNotification(
                NotificationType.createSpamDetectionAlert(this),
                "Spam Alert", "Test spam notification.");

        helper.createNotification(
                NotificationType.createNewsAlert(this),
                "News Update", "Test news notification.");

        helper.createNotification(
                NotificationType.createIncidentAlert(this),
                "Incident Report", "Test incident alert.");

        helper.createNotification(
                NotificationType.createUpdateNotification(this),
                "App Update", "Test update notification.");

        helper.createNotification(
                NotificationType.createBackupNotification(this),
                "Backup Complete", "Test backup notification.");

        helper.createNotification(
                NotificationType.createPasswordNotification(this),
                "Password Reminder", "Test password notification.");
    }
}



