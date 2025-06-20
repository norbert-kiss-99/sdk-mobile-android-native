package com.strivacity.android.native_sdk.render.widgets;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.strivacity.android.native_sdk.R;
import com.strivacity.android.native_sdk.render.constants.Colors;
import com.strivacity.android.native_sdk.render.models.WidgetModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class DateWidget extends EditableWidget {

    protected final WidgetModel.DateWidgetModel widgetModel;
    protected Button datePickerButton;
    protected EditText dayInput;
    protected EditText monthInput;
    protected EditText yearInput;
    protected ImageView clearButton;

    protected String dateAPIFormat = "";
    protected TextView labelView;
    protected TextView errorLabelView;

    public DateWidget(Context context, WidgetModel.DateWidgetModel widgetModel) {
        super(context);
        this.widgetModel = widgetModel;

        // Creating the parent layout
        LinearLayout parentLayoutView = new LinearLayout(context);
        parentLayoutView.setLayoutParams(
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        parentLayoutView.setOrientation(LinearLayout.VERTICAL);

        labelView = new TextView(context);
        labelView.setText(widgetModel.getLabel());

        errorLabelView = new TextView(context);
        errorLabelView.setTextColor(Color.parseColor(Colors.danger));

        switch (widgetModel.getRender().getType()) {
            case "native":
                // Creating date picker layout
                LinearLayout datePickerLayout = new LinearLayout(context);
                datePickerLayout.setOrientation(LinearLayout.HORIZONTAL);
                datePickerLayout.setGravity(Gravity.CENTER_VERTICAL);

                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f // Weight 1 gives priority to TextView to stretch it
                );

                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                );

                labelView.setLayoutParams(textParams);

                datePickerButton = new Button(context);
                datePickerButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calendar, 0);

                datePickerButton.setOnClickListener(v -> showDatePickerDialog());
                datePickerButton.setLayoutParams(buttonParams);

                if (widgetModel.getValue() != null) {
                    dateAPIFormat = widgetModel.getValue();
                    String[] dateParts = widgetModel.getValue().split("-");
                    String formattedDate = formatDateBasedOnPattern(
                        Integer.parseInt(dateParts[0]),
                        Integer.parseInt(dateParts[1]),
                        Integer.parseInt(dateParts[2])
                    );
                    datePickerButton.setText(formattedDate);
                } else {
                    datePickerButton.setText(widgetModel.getLabel());
                }

                clearButton = new ImageView(context);
                clearButton.setImageResource(R.drawable.close);
                clearButton.setOnClickListener(view -> {
                    clearButton.setVisibility(ViewGroup.GONE);
                    datePickerButton.setText(widgetModel.getLabel());
                    this.dateAPIFormat = "";
                });
                clearButton.setLayoutParams(buttonParams);

                if (this.dateAPIFormat.isEmpty()) {
                    clearButton.setVisibility(ViewGroup.GONE);
                } else {
                    clearButton.setVisibility(ViewGroup.VISIBLE);
                }

                datePickerLayout.addView(labelView);
                datePickerLayout.addView(datePickerButton);
                datePickerLayout.addView(clearButton);

                parentLayoutView.addView(datePickerLayout);
                break;
            case "fieldSet":
                LinearLayout dateInputLayout = new LinearLayout(context);
                dateInputLayout.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout.LayoutParams inputParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
                );

                Calendar calendar = Calendar.getInstance();
                dayInput = new EditText(context);
                SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
                dayInput.setHint(dayFormat.format(calendar.getTime())); // Set current day as hint
                dayInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                dayInput.setLayoutParams(inputParams);

                monthInput = new EditText(context);
                SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
                monthInput.setHint(monthFormat.format(calendar.getTime())); // Set current month as hint
                monthInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                monthInput.setLayoutParams(inputParams);

                yearInput = new EditText(context);
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
                yearInput.setHint(yearFormat.format(calendar.getTime())); // Set current year as hint
                yearInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                yearInput.setLayoutParams(inputParams);

                // Arrange the input fields
                Locale currentLocale = Locale.getDefault();
                String datePattern =
                    ((SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT, currentLocale)).toPattern();
                if (datePattern.startsWith("d")) {
                    // Format like DD/MM/YYYY
                    dateInputLayout.addView(dayInput);
                    dateInputLayout.addView(monthInput);
                    dateInputLayout.addView(yearInput);
                } else if (datePattern.startsWith("M")) {
                    // Format like MM/DD/YYYY
                    dateInputLayout.addView(monthInput);
                    dateInputLayout.addView(dayInput);
                    dateInputLayout.addView(yearInput);
                } else {
                    // Default fallback (YYYY/MM/DD)
                    dateInputLayout.addView(yearInput);
                    dateInputLayout.addView(monthInput);
                    dateInputLayout.addView(dayInput);
                }

                parentLayoutView.addView(labelView);
                parentLayoutView.addView(dateInputLayout);
                break;
            default:
                throw new RuntimeException("Unknown render type " + widgetModel.getRender().getType());
        }

        parentLayoutView.addView(errorLabelView);
        setView(parentLayoutView);
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
            context,
            (view, selectedYear, selectedMonth, selectedDay) -> {
                dateAPIFormat =
                    dateAPIFormatter(
                        String.valueOf(selectedYear),
                        String.valueOf(selectedMonth + 1),
                        String.valueOf(selectedDay)
                    );
                String formattedDate = formatDateBasedOnPattern(selectedYear, selectedMonth + 1, selectedDay);
                datePickerButton.setText(formattedDate);

                if (dateAPIFormat.isEmpty()) {
                    clearButton.setVisibility(ViewGroup.GONE);
                } else {
                    clearButton.setVisibility(ViewGroup.VISIBLE);
                }
            },
            year,
            month,
            day
        );

        datePickerDialog.show();
    }

    protected static String getDateFormatPattern() {
        Locale locale = Locale.getDefault();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);

        if (dateFormat instanceof SimpleDateFormat) {
            SimpleDateFormat simpleDateFormat = (SimpleDateFormat) dateFormat;
            return simpleDateFormat.toPattern();
        } else {
            return "dd/MM/yyyy";
        }
    }

    //This will handle the yyyy mm dd in the right order with the right separator (/, .)
    protected static String formatDateBasedOnPattern(int year, int month, int day) {
        String pattern = getDateFormatPattern();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day); // month is 0-based in Calendar, so subtract 1

        return simpleDateFormat.format(calendar.getTime());
    }

    protected String dateAPIFormatter(String year, String month, String day) {
        if (Objects.equals(year, "") && Objects.equals(month, "") && Objects.equals(day, "")) {
            return null;
        }

        // if we want to format an empty string with Integer.parseInt, it will throw an error
        String formattedMonth, formattedDay;
        if (Objects.equals(month, "") || Objects.equals(day, "")) {
            formattedMonth = "";
            formattedDay = "";
        } else {
            formattedMonth = String.format("%02d", Integer.parseInt(month));
            formattedDay = String.format("%02d", Integer.parseInt(day));
        }

        return year + "-" + formattedMonth + "-" + formattedDay;
    }

    public final boolean isReadonly() {
        return widgetModel.isReadonly();
    }

    @Override
    public String getValue() {
        if (this.widgetModel.getRender().getType().equals("native")) {
            return this.dateAPIFormat;
        } else if (this.widgetModel.getRender().getType().equals("fieldSet")) {
            return dateAPIFormatter(
                yearInput.getText().toString().trim(),
                monthInput.getText().toString().trim(),
                dayInput.getText().toString().trim()
            );
        } else {
            throw new RuntimeException("Unknown render type " + widgetModel.getRender().getType());
        }
    }

    @Override
    public void clearError() {
        super.clearError();
        errorLabelView.setText(null);
    }

    @Override
    public void showError(String message) {
        super.showError(message);
        errorLabelView.setText(message);
    }
}
