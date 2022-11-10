package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private boolean clearScreen = false;
    private boolean hasDot = false;
    private int action = 0;
    private float operandOne = 0;
    private float operandTwo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void appendDigit(View view) {
        Button b = (Button)view;
        String buttonText = b.getText().toString();
        TextView textWindow = (TextView) findViewById(R.id.textWindow);
        String currentText = (String) textWindow.getText();

        if (buttonText.equals(".")) {  // dot edge cases
            if (currentText.equals("") || clearScreen || hasDot) {
                // can't start with dot, or write more that one dot
                return;
            }
            hasDot = true;
        }

        if (buttonText.equals("0")) {  // zero edge cases
            if (currentText.equals("0")) {  // last char is zero
                if (!hasDot) {  // if we don't have dot, we can't write consecutive zeros
                    return;
                }
            }
        }

        if (clearScreen) {  // After clicking action or equal
            clearScreen = false;
            textWindow.setText(buttonText);
            return;
        }

        textWindow.setText(currentText + buttonText);
    }

    public void clearText(View view) {
        TextView textWindow = (TextView)findViewById(R.id.textWindow);
        textWindow.setText("");

        clearScreen = true;
        hasDot = false;
        action = 0;
        operandOne = 0;
        operandTwo = 0;
    }

    public void actionClicked(View view) {
        TextView textWindow = (TextView)findViewById(R.id.textWindow);
        String currentText = (String) textWindow.getText();

        if (currentText.equals("") || clearScreen) {  // Can't perform action with no output
            return;
        }

        clearScreen = true;
        hasDot = false;
        operandOne = Float.parseFloat(currentText);

        switch (view.getId()) {
            case R.id.sum:
                action = 1;
                break;
            case R.id.sub:
                action = 2;
                break;
            case R.id.mul:
                action = 3;
                break;
            case R.id.div:
                action = 4;
                break;
        }
    }

    public void calculate(View view) {
        if (action == 0) {  // No action chosen, can't calculate
            return;
        }

        TextView textWindow = (TextView)findViewById(R.id.textWindow);
        operandTwo = Float.parseFloat((String) textWindow.getText());
        boolean divisionByZero = false;
        double result = 0;

        switch (action) {
            case 1:
                result = (double) (operandOne + operandTwo);
                break;
            case 2:
                result = operandOne - operandTwo;
                break;
            case 3:
                result = operandOne * operandTwo;
                break;
            case 4:
                if (operandTwo == 0) {
                    divisionByZero = true;
                    break;
                }
                result = operandOne / operandTwo;
                break;
        }

        if (divisionByZero) {
            textWindow.setText("Math error");
        }
        else if (((int) result) == result) {
            textWindow.setText(String.valueOf((int) result));
        }
        else {
            DecimalFormat df = new DecimalFormat("#.#####");
            df.setRoundingMode(RoundingMode.CEILING);
            textWindow.setText(df.format(result));
        }

        clearScreen = true;
        hasDot = false;
        action = 0;
        operandOne = 0;
        operandTwo = 0;
    }
}