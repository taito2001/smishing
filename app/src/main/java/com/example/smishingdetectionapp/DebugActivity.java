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


//KMM module
import com.example.smishingdetectionapp.util.StringUtils;
import com.example.smishingdetectionapp.util.PhoneUtils;
import com.example.smishingdetectionapp.util.ValidationUtils;
import com.example.smishingdetectionapp.util.PlatformInfo;
import com.example.smishingdetectionapp.util.PlatformInfo_androidKt;
import com.example.smishingdetectionapp.util.Logger_androidKt;
import com.example.smishingdetectionapp.util.UuidUtils_androidKt;
import com.example.smishingdetectionapp.domain.riskscanner.AndroidRiskCheckProvider;
import com.example.smishingdetectionapp.domain.riskscanner.RiskCheckResult;
import com.example.smishingdetectionapp.domain.riskscanner.RiskScannerEngine;
import com.example.smishingdetectionapp.domain.riskscanner.RiskScore;


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
                AndroidRiskCheckProvider provider =
                        new AndroidRiskCheckProvider(this);
                RiskScore score =
                        RiskScannerEngine.INSTANCE.scanHabits(
                                provider, false, false, false, 23
                        );

                StringBuilder sb = new StringBuilder();
                sb.append("Score: ").append(score.getTotalScore()).append("%\n");
                sb.append("Risk Level: ").append(score.getRiskLevel()).append("\n\n");

                sb.append("Check Results:\n");
                for (RiskCheckResult result : score.getCheckResults()) {
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


        //ValidationUtils Tests:
        beginCategory("ValidationUtils Tests");

        addButton("Email Validation", () -> {
            try {
                android.widget.EditText input = new android.widget.EditText(this);
                input.setHint("Enter email address");

                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Test Email Validation")
                        .setView(input)
                        .setPositiveButton("Test", (dialog, which) -> {
                            String email = input.getText().toString();
                            String error = ValidationUtils.INSTANCE.getEmailError(email);
                            boolean valid = ValidationUtils.INSTANCE.isValidEmail(email);

                            StringBuilder sb = new StringBuilder();
                            sb.append("Input: ").append(email).append("\n\n");
                            sb.append("Valid: ").append(valid ? "✅ Yes" : "❌ No").append("\n");
                            if (error != null) {
                                sb.append("Error: ").append(error);
                            }

                            new androidx.appcompat.app.AlertDialog.Builder(this)
                                    .setTitle("Result")
                                    .setMessage(sb.toString())
                                    .setPositiveButton("OK", null)
                                    .show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        addButton("PIN Validation", () -> {
            try {
                android.widget.EditText input = new android.widget.EditText(this);
                input.setHint("Enter PIN");
                input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Test PIN Validation")
                        .setView(input)
                        .setPositiveButton("Test", (dialog, which) -> {
                            String pin = input.getText().toString();
                            String error = ValidationUtils.INSTANCE.getPinError(pin);
                            boolean valid = ValidationUtils.INSTANCE.isValidPin(pin);

                            StringBuilder sb = new StringBuilder();
                            sb.append("Input: ").append(pin).append("\n\n");
                            sb.append("Valid: ").append(valid ? "✅ Yes" : "❌ No").append("\n");
                            if (error != null) {
                                sb.append("Error: ").append(error);
                            }

                            new androidx.appcompat.app.AlertDialog.Builder(this)
                                    .setTitle("Result")
                                    .setMessage(sb.toString())
                                    .setPositiveButton("OK", null)
                                    .show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        addButton("Password Validation", () -> {
            try {
                android.widget.EditText input = new android.widget.EditText(this);
                input.setHint("Enter password");
                input.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);

                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Test Password Validation")
                        .setView(input)
                        .setPositiveButton("Test", (dialog, which) -> {
                            String password = input.getText().toString();
                            ValidationUtils.PasswordStrength strength =
                                    ValidationUtils.INSTANCE.getPasswordStrength(password);

                            StringBuilder sb = new StringBuilder();
                            sb.append("Strength: ").append(strength.getLabel()).append(" (").append(strength.getScore()).append("/5)\n\n");

                            for (ValidationUtils.PasswordCheck check : strength.getChecks()) {
                                sb.append(check.getPassed() ? "✅" : "❌");
                                sb.append(" ").append(check.getDescription()).append("\n");
                            }

                            new androidx.appcompat.app.AlertDialog.Builder(this)
                                    .setTitle("Result")
                                    .setMessage(sb.toString())
                                    .setPositiveButton("OK", null)
                                    .show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



        //StringUtils Tests:
        beginCategory("StringUtil Tests");

        addButton("String Analysis", () -> {
            try {
                android.widget.EditText input = new android.widget.EditText(this);
                input.setHint("Enter any text");

                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Test String Analysis")
                        .setView(input)
                        .setPositiveButton("Test", (dialog, which) -> {
                            String text = input.getText().toString();

                            StringBuilder sb = new StringBuilder();
                            sb.append("Length: ").append(text.length()).append("\n\n");
                            sb.append("Contains URL: ").append(StringUtils.INSTANCE.containsUrl(text) ? "Yes" : "No").append("\n");

                            if (StringUtils.INSTANCE.containsUrl(text)) {
                                sb.append("URLs: ").append(StringUtils.INSTANCE.extractUrls(text)).append("\n");
                            }

                            sb.append("Suspicious Keywords: ").append(StringUtils.INSTANCE.containsSuspiciousKeywords(text) ? "⚠️ Yes" : "✅ No").append("\n");
                            sb.append("Valid Email: ").append(ValidationUtils.INSTANCE.isValidEmail(text) ? "Yes" : "No").append("\n");
                            sb.append("Valid Phone: ").append(PhoneUtils.INSTANCE.getCountryFromNumber(text) != null ? "Yes" : "No").append("\n");

                            new androidx.appcompat.app.AlertDialog.Builder(this)
                                    .setTitle("Result")
                                    .setMessage(sb.toString())
                                    .setPositiveButton("OK", null)
                                    .show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        //PhoneUtils Tests:
        beginCategory("PhoneUtil Tests");

        addButton("Phone Validation", () -> {
            try {
                android.widget.EditText input = new android.widget.EditText(this);
                input.setHint("Enter phone number");
                input.setInputType(android.text.InputType.TYPE_CLASS_PHONE);

                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Test Phone Validation")
                        .setView(input)
                        .setPositiveButton("Test", (dialog, which) -> {
                            String phone = input.getText().toString();

                            StringBuilder sb = new StringBuilder();
                            sb.append("Input: ").append(phone).append("\n\n");
                            String country = PhoneUtils.INSTANCE.getCountryFromNumber(phone);
                            sb.append("Country: ").append(country != null ? country : "Unknown").append("\n");
                            sb.append("Normalized: ").append(PhoneUtils.INSTANCE.normalizeNumber(phone)).append("\n");
                            sb.append("Short Code: ").append(PhoneUtils.INSTANCE.isShortCode(phone) ? "Yes" : "No").append("\n");
                            sb.append("Suspicious Sender: ").append(PhoneUtils.INSTANCE.isSuspiciousSender(phone) ? "Yes" : "No").append("\n");

                            new androidx.appcompat.app.AlertDialog.Builder(this)
                                    .setTitle("Result")
                                    .setMessage(sb.toString())
                                    .setPositiveButton("OK", null)
                                    .show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        //KMM Misc Tests:
        beginCategory("KMM Misc Tests");

        addButton("Platform Info", () -> {
            try {
                PlatformInfo info =
                        PlatformInfo_androidKt.getPlatformInfo(this);
                String msg = info.getPlatformName() + " " + info.getOsVersion() + "\n" +
                        info.getDeviceModel() + " v" + info.getAppVersion();
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });

        addButton("Logger Test", () -> {
            try {
                Logger_androidKt.logDebug("DebugTest", "This is a debug message");
                Logger_androidKt.logWarning("DebugTest", "This is a warning");
                Logger_androidKt.logError("DebugTest", "This is an error");
                Toast.makeText(this, "Check logcat for output", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        addButton("UUID Generator Test", () -> {
            try {
                String uuid = UuidUtils_androidKt.generateUUID();
                Toast.makeText(this, "UUID: " + uuid, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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



