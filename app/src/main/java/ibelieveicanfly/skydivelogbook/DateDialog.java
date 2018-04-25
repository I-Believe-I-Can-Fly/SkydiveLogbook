package ibelieveicanfly.skydivelogbook;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Creates interface when user can select a date
 * @param View should have current view
 * @param Date should contain the starting date, leave all at -1 for current day
 */

public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    String dateArray[];
    EditText txtDate;
    public DateDialog (View view, String d) {
        txtDate = (EditText)view;

        dateArray = d.split("/");


    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year;
        int month;
        int day;
        if (dateArray[0].equals("-1") && dateArray[1].equals("-1") && dateArray[2].equals("-1")) {
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        } else {
            year = Integer.parseInt(dateArray[2]);
            month = Integer.parseInt(dateArray[1]) - 1;
            day = Integer.parseInt(dateArray[0]);
        }

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        String date = day + "/" + (month + 1) + "/" + year;
        txtDate.setText(date);
    }
}
