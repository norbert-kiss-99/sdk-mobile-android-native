package com.strivacity.android.demo.render.modifiers;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import com.strivacity.android.demo.render.constants.Colors;

public class OptionalWidget {

    public static SpannableStringBuilder optionalWidgetSpannable(String label, boolean required) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(label);
        if (!required) {
            spannable.append(" (Optional)");
            spannable.setSpan(
                new ForegroundColorSpan(Color.parseColor(Colors.secondary)),
                label.length(),
                spannable.length(),
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            );
            spannable.setSpan(
                new RelativeSizeSpan(0.8f),
                label.length(),
                spannable.length(),
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            );
        }

        return spannable;
    }
}
