package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.calculator.databinding.ActivityMainBinding;

import java.util.ArrayList;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ScreenModel screenModel;
    private HistoryModel historyModel;
    private ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");

    boolean dot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        historyModel = new ViewModelProvider(this).get(HistoryModel.class);
        screenModel = new ViewModelProvider(this).get(ScreenModel.class);

        screenModel.getString().observe(this, string -> {
            binding.tvScreen.setText(string);
        });
        historyModel.getHistory().observe(this, strings -> {
            String temp = "";
            int n = strings.size();
            if (n == 1) {
                temp = strings.get(0);
            } else if (n > 1) {
                temp = strings.get(n - 2) + " " + strings.get(n - 1);
            }
            binding.tvHistory.setText(temp);
        });
    }
    public void ButtonHandler(View v) {
        String btText = ((Button)v).getText().toString();
        String str = screenModel.getString().getValue();
        char last = '0';
        if (!"".equals(str)) {
            last = str.charAt(str.length() - 1);
        }
        switch (btText) {
            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
                screenModel.addString(btText);
                break;
            case "+":
            case "-":
            case "*":
            case "/":
                dot = false;
                if (last == '+' || last == '-' || last == '*' || last == '/') {
                    screenModel.removeLast();
                }
                screenModel.addString(btText);
                break;
            case "DEL":
                if (last == '.') {
                    dot = false;
                }
                screenModel.removeLast();
                break;
            case "C":
                screenModel.clear();
                historyModel.clearHistory();
                break;
            case "=":
                if (last == '+' || last == '-' || last == '*' || last == '/') {
                    calculatorWarning("Lỗi: Dư toán tử " + last + " ở cuối");
                } else if (str.length() > 0) {
                    dot = false;
                    solve();
                    screenModel.clear();
                } else {
                    historyModel.addHistory("0");
                    screenModel.clear();
                }
                break;
            case ".":
                if (!dot) {
                    dot = true;
                    screenModel.addString(btText);
                } else {
                    calculatorWarning("Đã tồn tại dấu thập phân");
                }
        }
    }
    private void calculatorWarning(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
    private void solve() {
        String str = screenModel.getString().getValue();
        try {
            historyModel.addHistory(str + "=" + engine.eval(str));
        } catch (ScriptException e) {
            calculatorWarning("Can't calculate");
        }
    }
}