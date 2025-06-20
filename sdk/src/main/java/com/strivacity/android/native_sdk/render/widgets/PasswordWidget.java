package com.strivacity.android.native_sdk.render.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.strivacity.android.native_sdk.R;
import com.strivacity.android.native_sdk.render.constants.Colors;
import com.strivacity.android.native_sdk.render.models.WidgetModel;

public class PasswordWidget extends EditableWidget {

    protected final EditText inputView;

    protected final TextView labelView;

    protected final RelativeLayout inputLayoutView;

    protected final TextView errorLabelView;
    protected final ImageView passwordToggleView;

    public PasswordWidget(Context context, WidgetModel.PasswordWidgetModel widgetModel) {
        super(context);
        // Creating the parent layout

        final LinearLayout parentLayoutView = new LinearLayout(context);
        parentLayoutView.setLayoutParams(
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        parentLayoutView.setOrientation(LinearLayout.VERTICAL);

        // Creating the layout for the input

        inputLayoutView = new RelativeLayout(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.ALIGN_BASELINE);
        inputLayoutView.setLayoutParams(layoutParams);

        labelView = new TextView(context);
        labelView.setText(widgetModel.getLabel());

        errorLabelView = new TextView(context);
        errorLabelView.setTextColor(Color.parseColor(Colors.danger));

        // Modifying the input

        inputView = new EditText(context);
        inputView.setLayoutParams(
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        inputView.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        inputView.setTransformationMethod(PasswordTransformationMethod.getInstance());

        // Creating the password toggle image

        passwordToggleView = new ImageView(context);
        RelativeLayout.LayoutParams passwordToggleParams = new RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        );
        passwordToggleParams.addRule(RelativeLayout.CENTER_VERTICAL);
        passwordToggleParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        passwordToggleView.setLayoutParams(passwordToggleParams);
        passwordToggleView.setImageResource(R.drawable.icon_eye_on);

        // Adding views

        parentLayoutView.addView(labelView);
        inputLayoutView.addView(inputView);
        inputLayoutView.addView(passwordToggleView);
        parentLayoutView.addView(inputLayoutView);
        parentLayoutView.addView(errorLabelView);
        setView(parentLayoutView);

        // Adding password toggle functionality

        addPasswordToggleBehavior(passwordToggleView, inputView);
    }

    public final boolean isReadonly() {
        return false;
    }

    protected void addPasswordToggleBehavior(ImageView passwordToggle, EditText inputView) {
        final Drawable eyeOffIcon = ResourcesCompat.getDrawable(
            getContext().getResources(),
            R.drawable.icon_eye_off,
            getContext().getTheme()
        );
        final Drawable eyeOnIcon = ResourcesCompat.getDrawable(
            getContext().getResources(),
            R.drawable.icon_eye_on,
            getContext().getTheme()
        );

        passwordToggle.setOnClickListener(v -> {
            if (inputView.getTransformationMethod() instanceof PasswordTransformationMethod) {
                inputView.setTransformationMethod(new SingleLineTransformationMethod());
                passwordToggle.setImageDrawable(eyeOffIcon);
            } else {
                inputView.setTransformationMethod(PasswordTransformationMethod.getInstance());
                passwordToggle.setImageDrawable(eyeOnIcon);
            }

            inputView.setSelection(inputView.getText().length());
        });
    }

    @Override
    public Object getValue() {
        return inputView.getText() == null ? null : inputView.getText().toString();
    }

    @Override
    public void clearError() {
        super.clearError();

        getContext()
            .getMainExecutor()
            .execute(() -> {
                errorLabelView.setText(null);
            });
    }

    @Override
    public void showError(String message) {
        super.showError(message);

        getContext()
            .getMainExecutor()
            .execute(() -> {
                errorLabelView.setText(message);
            });
    }
}
