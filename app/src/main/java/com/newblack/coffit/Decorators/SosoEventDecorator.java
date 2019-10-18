package com.newblack.coffit.Decorators;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.newblack.coffit.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

public class SosoEventDecorator implements DayViewDecorator {
    private Drawable drawable;
    private final HashSet<CalendarDay> dates;
    private Context context;

    public SosoEventDecorator(Activity context, Collection<CalendarDay> dates) {
        this.dates = new HashSet<>(dates);
        this.context = context;

        drawable = ContextCompat.getDrawable(context, R.drawable.circle_button);
        Drawable wrapped = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrapped, context.getResources().getColor(R.color.colorSoso));
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
        view.addSpan(new ForegroundColorSpan(Color.WHITE));
    }
}
