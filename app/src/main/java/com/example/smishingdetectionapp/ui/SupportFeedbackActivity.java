package com.example.smishingdetectionapp.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smishingdetectionapp.R;

import java.util.ArrayList;
import java.util.List;

public class SupportFeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showFeedbackDialog();
    }

    private void showFeedbackDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_detailed_feedback);
        dialog.setCancelable(false);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        // Views
        RadioGroup rgSolved = dialog.findViewById(R.id.rgSolved);
        LinearLayout problemsLayout = dialog.findViewById(R.id.problemsLayout);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        ImageButton btnClose = dialog.findViewById(R.id.btnClose);

        // Close button
        btnClose.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        // Yes / No
        rgSolved.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbYes) {
                dialog.dismiss();
                showThankYouDialog();
            } else if (checkedId == R.id.rbNo) {
                problemsLayout.setVisibility(LinearLayout.VISIBLE);
            }
        });

        // Submit
        btnSubmit.setOnClickListener(v -> {
            CheckBox cbSolution = dialog.findViewById(R.id.cbSolution);
            CheckBox cbRobotic = dialog.findViewById(R.id.cbRobotic);
            CheckBox cbSlow = dialog.findViewById(R.id.cbSlow);
            CheckBox cbWrongWords = dialog.findViewById(R.id.cbWrongWords);
            CheckBox cbRepetitive = dialog.findViewById(R.id.cbRepetitive);
            CheckBox cbUnfriendly = dialog.findViewById(R.id.cbUnfriendly);
            EditText editFeedback = dialog.findViewById(R.id.editFeedback);

            List<String> issues = new ArrayList<>();
            if (cbSolution.isChecked()) issues.add(getString(R.string.problem_solution_not_helpful));
            if (cbRobotic.isChecked()) issues.add(getString(R.string.problem_robotic_replies));
            if (cbSlow.isChecked()) issues.add(getString(R.string.problem_slow_response));
            if (cbWrongWords.isChecked()) issues.add(getString(R.string.problem_wrong_words));
            if (cbRepetitive.isChecked()) issues.add(getString(R.string.problem_repetitive));
            if (cbUnfriendly.isChecked()) issues.add(getString(R.string.problem_unfriendly));

            String feedbackText = editFeedback.getText().toString().trim();

            // Debug logs
            System.out.println("Issues: " + issues);
            System.out.println("Feedback Text: " + feedbackText);

            dialog.dismiss();
            showThankYouDialog();
        });

        dialog.show();
    }

    private void showThankYouDialog() {
        Dialog thankDialog = new Dialog(this);
        thankDialog.setContentView(R.layout.dialog_thank_you);

        if (thankDialog.getWindow() != null) {
            thankDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        Button btnClose = thankDialog.findViewById(R.id.btnCloseThankYou);
        btnClose.setOnClickListener(v -> {
            thankDialog.dismiss();
            finish();
        });

        thankDialog.show();
    }
}
