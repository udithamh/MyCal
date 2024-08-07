package com.example.mycal;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private TextView displayExpression, display;
    private ImageButton btnBackspace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayExpression = findViewById(R.id.displayExpression);
        display = findViewById(R.id.display);
        btnBackspace = findViewById(R.id.btnBackspace);

        btnBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentText = display.getText().toString();
                if (currentText.length() > 0) {
                    display.setText(currentText.substring(0, currentText.length() - 1));
                }
            }
        });

        // Initialize other buttons here and set their onClickListeners
        initializeButtons();
    }

    private void initializeButtons() {
        MaterialButton btnC = findViewById(R.id.btnC);
        MaterialButton btnParentheses = findViewById(R.id.btnParentheses);
        MaterialButton btnPercent = findViewById(R.id.btnPercent);
        MaterialButton btnDivide = findViewById(R.id.btnDivide);
        MaterialButton btnMultiply = findViewById(R.id.btnMultiply);
        MaterialButton btnMinus = findViewById(R.id.btnMinus);
        MaterialButton btnPlus = findViewById(R.id.btnPlus);
        MaterialButton btnEquals = findViewById(R.id.btnEquals);
        MaterialButton btnDot = findViewById(R.id.btnDot);
        MaterialButton btn0 = findViewById(R.id.btn0);
        MaterialButton btn1 = findViewById(R.id.btn1);
        MaterialButton btn2 = findViewById(R.id.btn2);
        MaterialButton btn3 = findViewById(R.id.btn3);
        MaterialButton btn4 = findViewById(R.id.btn4);
        MaterialButton btn5 = findViewById(R.id.btn5);
        MaterialButton btn6 = findViewById(R.id.btn6);
        MaterialButton btn7 = findViewById(R.id.btn7);
        MaterialButton btn8 = findViewById(R.id.btn8);
        MaterialButton btn9 = findViewById(R.id.btn9);

        // Set click listeners for the buttons
        btnC.setOnClickListener(view -> {
            display.setText("0");
            displayExpression.setText("");
        });

        // Add other button listeners here
    }
}
