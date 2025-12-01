package com.example.loanapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // Inputs
    private EditText price, downpayment, loan, rate;
    // Outputs
    private TextView resultLoan, resultInterest, resultTotal, resultMonthly;
    private Button btnCalculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorOnPrimary));
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }


        if (findViewById(R.id.main) != null) {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        price = findViewById(R.id.price);
        downpayment = findViewById(R.id.downpayment);
        loan = findViewById(R.id.loan);
        rate = findViewById(R.id.rate);

        resultLoan = findViewById(R.id.resultLoan);
        resultInterest = findViewById(R.id.resultInterest);
        resultTotal = findViewById(R.id.resultTotal);
        resultMonthly = findViewById(R.id.resultMonthly);

        btnCalculate = findViewById(R.id.btnCalculate);

        if (price == null || downpayment == null || loan == null || rate == null ||
                resultLoan == null || resultInterest == null || resultTotal == null || resultMonthly == null ||
                btnCalculate == null) {
            Toast.makeText(this, "One or more views not found. Check your activity_main.xml IDs.", Toast.LENGTH_LONG).show();
            return;
        }

        btnCalculate.setOnClickListener(v -> calculateLoanSafe());
    }

    private void calculateLoanSafe() {

        String sPrice = price.getText().toString().trim();
        String sDown = downpayment.getText().toString().trim();
        String sLoan = loan.getText().toString().trim();
        String sRate = rate.getText().toString().trim();

        if (sPrice.isEmpty()) { price.setError("Enter vehicle price"); price.requestFocus(); return; }
        if (sDown.isEmpty()) { downpayment.setError("Enter down payment"); downpayment.requestFocus(); return; }
        if (sLoan.isEmpty()) { loan.setError("Enter loan period (years)"); loan.requestFocus(); return; }
        if (sRate.isEmpty()) { rate.setError("Enter interest rate (%)"); rate.requestFocus(); return; }

        double vehiclePrice, downPayment, loanPeriod, interestRate;
        try {
            vehiclePrice = Double.parseDouble(sPrice);
            downPayment = Double.parseDouble(sDown);
            loanPeriod = Double.parseDouble(sLoan);
            interestRate = Double.parseDouble(sRate);
        } catch (NumberFormatException ex) {
            Toast.makeText(this, "Please enter valid numeric values.", Toast.LENGTH_LONG).show();
            return;
        }

        if (loanPeriod <= 0) { loan.setError("Loan period must be > 0"); loan.requestFocus(); return; }
        if (vehiclePrice < 0 || downPayment < 0 || interestRate < 0) {
            Toast.makeText(this, "Values cannot be negative.", Toast.LENGTH_LONG).show();
            return;
        }
        if (downPayment > vehiclePrice) {
            downpayment.setError("Down payment cannot exceed vehicle price");
            downpayment.requestFocus();
            return;
        }

        double loanAmount = vehiclePrice - downPayment;
        double totalInterest = loanAmount * (interestRate / 100.0) * loanPeriod;
        double totalPayment = loanAmount + totalInterest;
        double monthlyPayment = totalPayment / (loanPeriod * 12.0);


        resultLoan.setText(String.format("Loan Amount: RM %.2f", loanAmount));
        resultInterest.setText(String.format("Total Interest: RM %.2f", totalInterest));
        resultTotal.setText(String.format("Total Payment: RM %.2f", totalPayment));
        resultMonthly.setText(String.format("Monthly Payment: RM %.2f", monthlyPayment));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            // open AboutActivity (make sure AboutActivity exists and is declared in Manifest)
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        } else if (id == R.id.action_home) {
            Toast.makeText(this, "Already on Home Page", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
