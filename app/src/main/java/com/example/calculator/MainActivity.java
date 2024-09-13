package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class MainActivity extends AppCompatActivity {
    TextView calc,result;
    private boolean decimalType=true,operationType=false,openParenthesisType=true,closeParenthesisType=false;
    int openParenthesis=0,closeParenthesis=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calc = findViewById(R.id.calc);
        result=findViewById(R.id.result);
        calc.setText("");
        result.setText("");
        setButtonClickListener(R.id.button1);
        setButtonClickListener(R.id.button2);
        setButtonClickListener(R.id.button3);
        setButtonClickListener(R.id.button4);
        setButtonClickListener(R.id.button5);
        setButtonClickListener(R.id.button6);
        setButtonClickListener(R.id.button7);
        setButtonClickListener(R.id.button8);
        setButtonClickListener(R.id.button9);
        setButtonClickListener(R.id.button0);
        setButtonClickListener(R.id.button_plus);
        setButtonClickListener(R.id.button_minus);
        setButtonClickListener(R.id.button_multiply);
        setButtonClickListener(R.id.button_divide);
        setButtonClickListener(R.id.button_open);
        setButtonClickListener(R.id.button_close);
        setButtonClickListener(R.id.button_clear);
        setButtonClickListener(R.id.button_dec);
        setButtonClickListener(R.id.button_equal);
        setButtonClickListener(R.id.button_backspace);
    }
    private void setButtonClickListener(int buttonId) {
        Button button=findViewById(buttonId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button.getText().equals("C")) {
                    calc.setText("");
                    result.setText("");
                    decimalType = true;
                    operationType = false;
                    openParenthesisType=true;
                    closeParenthesisType=false;
                } else if (button.getText().equals(".")) {
                    if (decimalType) {
                        calc.append(".");
                        decimalType = false;
                        openParenthesisType=false;
                        closeParenthesisType=true;
                    }
                } else if (button.getText().equals("=")) {
                    if (!calc.getText().toString().isEmpty()) {
                        decimalType = true;
                        operationType = true;
                        openParenthesisType=false;
                        closeParenthesisType=false;
                        calc.setText(result.getText().toString());
                        result.setText("");
                    }
                } else if (button.getText().equals("undo")) {
                    if (!calc.getText().toString().equals("")) {
                        if (calc.getText().toString().endsWith(".")) {
                            decimalType = true;
                        }
                        if (calc.getText().toString().endsWith("+") || calc.getText().toString().endsWith("-") || calc.getText().toString().endsWith("*") || calc.getText().toString().endsWith("/")) {
                            operationType = true;
                        }
                        if (calc.getText().toString().endsWith("(")) {
                            openParenthesisType=true;
                        }
                        if(calc.getText().toString().endsWith(")")){
                            closeParenthesisType=true;
                        }
                        calc.setText(calc.getText().toString().substring(0, calc.getText().toString().length() - 1));
                    }
                } else if (button.getText().equals("(")) {
                    if (openParenthesisType) {
                        calc.append(button.getText().toString());
                        openParenthesis++;
                    }
                } else if (button.getText().equals(")")) {
                    if (closeParenthesisType&&openParenthesis>closeParenthesis) {
                        calc.append(button.getText().toString());
                        decimalType=false;
                        closeParenthesis++;
                    }
                } else if (button.getText().equals("+") || button.getText().equals("-") || button.getText().equals("*") || button.getText().equals("/")) {
                    if (operationType) {
                        calc.append(button.getText().toString());
                        decimalType = true;
                        operationType = false;
                        openParenthesisType=true;
                        closeParenthesisType=false;
                    }
                } else {
                    calc.append(button.getText().toString());
                    operationType = true;
                    openParenthesisType=false;
                    closeParenthesisType=true;
                }
                if (!calc.getText().toString().isEmpty()) {
                    String finalResult = calculate(calc.getText().toString());
                    if (!finalResult.equals("Error")) {
                        result.setText(finalResult);
                    }
                }else{
                    result.setText("");
                }
            }
        });
    }
    String calculate(String calculation){
        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();
            String finalResult = context.evaluateString(scriptable, calculation, "Javascript", 1, null).toString();
            double result = Double.parseDouble(finalResult);
            finalResult=String.format("%.12g",result);
            while(finalResult.endsWith("0")&&finalResult.contains(".")&&!finalResult.contains("e+")){
                finalResult=finalResult.substring(0,finalResult.length()-1);
            }
            if(finalResult.endsWith(".")){
                finalResult=finalResult.substring(0,finalResult.length()-1);
            }
            return finalResult;
        }catch(Exception e){
            return "Error";
        }
    }
}