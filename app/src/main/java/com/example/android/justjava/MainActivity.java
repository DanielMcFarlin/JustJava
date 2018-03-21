/**
 * IMPORTANT: Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 * <p>
 * package com.example.android.justjava;
 */

package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

import static android.R.attr.name;
import static android.os.Build.VERSION_CODES.N;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {

        //Looks for the Name of the order
        EditText EnterNameTextBox = (EditText) findViewById(R.id.User_Name);
        String UserName = EnterNameTextBox.getText().toString();

        //Looks for if Whipped cream is checked
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        //Looks for if chocolate is checked
        CheckBox ChocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = ChocolateCheckBox.isChecked();

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummary(price, hasWhippedCream, hasChocolate, UserName);

        //This makes an intent which opens up the Email app and composes an email automatically :)
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email_subject)  + " " + UserName);
            intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
    }
    /**
     * Calculates the price of the order.
     *
     * @param addWhippedCream is whether or not the user wants whipped cream topping
     * @param addChocolate    is whether or not the user wants chocolate topping
     * @return total price
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        //Price of 1 cup of coffee
        int basePrice = 5;

        //Add $1 to price if user wants whipped cream
        if (addWhippedCream) {
            basePrice += 1;
        }

        //Add $2 to price if user wants chocolate
        if (addChocolate) {
            basePrice += 2;
        }

        //Calculates the total order price by multiplying by quantity
        return quantity * basePrice;
    }

    private String createOrderSummary(int price, boolean hasWhippedCream, boolean hasChocolate, String UserName) {
        String priceMessage = getString(R.string.order_summary_name, UserName);
        priceMessage += "\n" + getString(R.string.order_summary_whipped_cream, hasWhippedCream);
        priceMessage += "\n" + getString(R.string.order_summary_chocolate, hasChocolate);
        priceMessage += "\n" + getString(R.string.order_summary_quantity, + quantity);
        priceMessage += "\n" + getString(R.string.order_summary_price,
                NumberFormat.getCurrencyInstance().format(price));
        priceMessage += "\n" + getString(R.string.thank_you);
        return priceMessage;
    }

    //    This causes the "+" button to increase the number
    public void increment(View view) {
        if (quantity == 100) {
            //Show Error Message
            Toast.makeText(this, getString(R.string.way_too_much_coffee), Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    //    This causes the "-" button to decrease the number
    public void decrement(View view) {
        if (quantity < 1) {
            //Show Error Message
            Toast.makeText(this, getString(R.string.negative_coffees_error) , Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);
    }


    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

}