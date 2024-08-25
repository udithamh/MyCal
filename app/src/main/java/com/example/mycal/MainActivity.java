package com.example.mycal;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import android.util.TypedValue;
import com.google.android.material.button.MaterialButton;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {
    private TextView displayExpression, display;
    private MaterialButton btnC, btnParentheses, btnPercent, btnDivide, btnMultiply, btnMinus, btnPlus, btnPlusMinus, btnDot, btnEquals;
    private StringBuilder expression = new StringBuilder();
    private ImageButton btnBackspace;
    private MaterialButton[] numericButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find views
        displayExpression = findViewById(R.id.displayExpression);
        display = findViewById(R.id.display);
        btnBackspace = findViewById(R.id.btnBackspace);
        btnC = findViewById(R.id.btnC);
        btnParentheses = findViewById(R.id.btnParentheses);
        btnPercent = findViewById(R.id.btnPercent);
        btnDivide = findViewById(R.id.btnDivide);
        btnMultiply = findViewById(R.id.btnMultiply);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        btnPlusMinus = findViewById(R.id.btnPlusMinus);
        btnDot = findViewById(R.id.btnDot);
        btnEquals = findViewById(R.id.btnEquals);

        // Find numeric buttons
        numericButtons = new MaterialButton[]{
                findViewById(R.id.btn0),
                findViewById(R.id.btn1),
                findViewById(R.id.btn2),
                findViewById(R.id.btn3),
                findViewById(R.id.btn4),
                findViewById(R.id.btn5),
                findViewById(R.id.btn6),
                findViewById(R.id.btn7),
                findViewById(R.id.btn8),
                findViewById(R.id.btn9)
        };

        // Set initial values
        displayExpression.setText(expression);
        display.setText("");

        // Add click listeners
        btnBackspace.setOnClickListener(view -> handleBackspace());
        btnC.setOnClickListener(view -> handleClear());
        btnParentheses.setOnClickListener(view -> handleParentheses());
        btnPercent.setOnClickListener(view -> handlePercent());
        btnDivide.setOnClickListener(view -> handleOperator("/"));
        btnMultiply.setOnClickListener(view -> handleOperator("*"));
        btnMinus.setOnClickListener(view -> handleOperator("-"));
        btnPlus.setOnClickListener(view -> handleOperator("+"));
        btnPlusMinus.setOnClickListener(view -> handlePlusMinus());
        btnDot.setOnClickListener(view -> handleDot());
        btnEquals.setOnClickListener(view -> handleEquals());

        // Add click listeners for numeric buttons
        for (MaterialButton btn : numericButtons) {
            btn.setOnClickListener(view -> handleNumericInput(btn.getText().toString()));
        }

        // Add text change listener to the expression TextView
        displayExpression.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Display the result only if the expression is complete
                if (!isExpressionIncomplete(s.toString())) {
                    String result = evaluateExpression(s.toString());
                    adjustTextSize(display, result);
                    display.setText(result);
                } else {
                    display.setText("");
                }
            }
        });
    }

    private void handleBackspace() {
        if (expression.length() > 0) {
            expression.deleteCharAt(expression.length() - 1);
        }
        displayExpression.setText(expression);
    }

    private void handleClear() {
        expression.setLength(0);
        displayExpression.setText(expression);
        display.setText("");
    }

    private void handleParentheses() {
        int openParentheses = 0, closedParentheses = 0;

        // Count open and closed parentheses in the current expression
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                openParentheses++;
            } else if (expression.charAt(i) == ')') {
                closedParentheses++;
            }
        }

        char lastChar;

        if (expression.length() > 0) {
            lastChar = expression.charAt(expression.length() - 1);
        } else {
            lastChar = ' ';
        }

        // If last char is a digit or a closing parenthesis, and there are unclosed parentheses
        if (Character.isDigit(lastChar) || lastChar == ')') {
            if (openParentheses > closedParentheses) {
                expression.append(")");
            } else {
                expression.append("*(");
            }
        }
        // If last char is an operator or there's nothing yet, add an opening parenthesis
        else if (isOperator(lastChar) || lastChar == ' ' || lastChar == '(') {
            expression.append("(");
        }

        displayExpression.setText(expression);
    }

    private void handlePercent() {
        if (expression.length() > 0 && Character.isDigit(expression.charAt(expression.length() - 1))) {
            expression.append("%");  // Append % to expression for display purposes
            displayExpression.setText(expression);  // Update displayExpression with %
        }
    }

    private void handleOperator(String operator) {
        if (expression.length() > 0) {
            char lastChar = expression.charAt(expression.length() - 1);

            if (isOperator(lastChar)) {
                expression.setCharAt(expression.length() - 1, operator.charAt(0));
            } else {
                expression.append(operator);
            }
        }
        displayExpression.setText(expression);
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private void handlePlusMinus() {
        if (expression.length() > 0 && expression.charAt(0) == '-') {
            expression.deleteCharAt(0);
        } else {
            expression.insert(0, "-");
        }
        displayExpression.setText(expression);
    }

    private void handleDot() {
        if (expression.length() == 0 || (expression.length() > 0 && expression.charAt(expression.length() - 1) != '.')) {
            expression.append(".");
            displayExpression.setText(expression);
        }
    }

    private void handleEquals() {
        if (!isExpressionIncomplete(expression.toString())) {
            String result = evaluateExpression(expression.toString());
            expression.setLength(0);  // Clear the current expression
            expression.append(result);  // Set the result as the new expression
            displayExpression.setText(expression);  // Update displayExpression with the result
            adjustTextSize(display, result);  // Adjust text size before setting the text
            display.setText(result);  // Update display with the result
        } else {
            Toast.makeText(this, "Incomplete expression", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleNumericInput(String digit) {
        char lastChar = expression.length() > 0 ? expression.charAt(expression.length() - 1) : ' ';

        if (lastChar == ')') {
            expression.append("*").append(digit);
        } else {
            expression.append(digit);
        }
        displayExpression.setText(expression);
    }

    private String evaluateExpression(String expr) {
        try {

            // Create a copy of the expression to manipulate for evaluation
            String evalExpr = expr.replaceAll("%", "*0.01");

            double result = new ExpressionBuilder(evalExpr).build().evaluate();
            String resultString;
            if (result == (int) result) {
                resultString = String.valueOf((int) result);
            } else {
                resultString = String.format("%.10f", result).replaceAll("\\.?0+$", "");
            }
            adjustTextSize(display, resultString);
            return resultString;
        } catch (Exception e) {
            return "Error";
        }
    }

    private boolean isExpressionIncomplete(String expr) {
        if (expr.isEmpty()) return true;

        char lastChar = expr.charAt(expr.length() - 1);
        int openParentheses = expr.length() - expr.replace("(", "").length();
        int closedParentheses = expr.length() - expr.replace(")", "").length();

        return isOperator(lastChar) || openParentheses > closedParentheses || lastChar == '(';
    }

    private void adjustTextSize(TextView textView, String text) {
        int maxLength = 12; // Maximum number of characters before reducing text size
        float defaultTextSize = 48f; // Default text size in sp
        float minTextSize = 24f; // Minimum text size in sp

        if (text.length() > maxLength) {
            float ratio = (float) maxLength / text.length();
            float newSize = Math.max(defaultTextSize * ratio, minTextSize);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, newSize);
        } else {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, defaultTextSize);
        }
    }

}
