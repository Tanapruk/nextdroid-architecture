package com.nextzy.library.base.utils.android;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.graphics.Palette;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Method;

import codetail.graphics.drawables.RippleDrawable;

/**
 * Utility methods for working with Views.
 */
public class ViewUtility {

    public static RippleDrawable createRipple( @ColorInt int color,
                                               @FloatRange(from = 0f, to = 1f) float alpha,
                                               boolean bounded) {
        color = ColorUtility.modifyAlpha(color, alpha);

        // TODO: Use RippleDrawable support library if it available.
        return new RippleDrawable(ColorStateList.valueOf(color), null,
                bounded ? new ColorDrawable(Color.WHITE) : null);
    }

    public static RippleDrawable createRipple( Palette palette,
                                               @FloatRange(from = 0f, to = 1f) float darkAlpha,
                                               @FloatRange(from = 0f, to = 1f) float lightAlpha,
                                               @ColorInt int fallbackColor,
                                               boolean bounded) {
        int rippleColor = fallbackColor;
        if (palette != null) {
            // try the named swatches in preference order
            if (palette.getVibrantSwatch() != null) {
                rippleColor =
                        ColorUtility.modifyAlpha(palette.getVibrantSwatch().getRgb(), darkAlpha);

            } else if (palette.getLightVibrantSwatch() != null) {
                rippleColor = ColorUtility.modifyAlpha(palette.getLightVibrantSwatch().getRgb(),
                        lightAlpha);
            } else if (palette.getDarkVibrantSwatch() != null) {
                rippleColor = ColorUtility.modifyAlpha(palette.getDarkVibrantSwatch().getRgb(),
                        darkAlpha);
            } else if (palette.getMutedSwatch() != null) {
                rippleColor = ColorUtility.modifyAlpha(palette.getMutedSwatch().getRgb(), darkAlpha);
            } else if (palette.getLightMutedSwatch() != null) {
                rippleColor = ColorUtility.modifyAlpha(palette.getLightMutedSwatch().getRgb(),
                        lightAlpha);
            } else if (palette.getDarkMutedSwatch() != null) {
                rippleColor =
                        ColorUtility.modifyAlpha(palette.getDarkMutedSwatch().getRgb(), darkAlpha);
            }
        }

        // TODO: Use RippleDrawable support library if it available.
        return new RippleDrawable(ColorStateList.valueOf(rippleColor), null,
                bounded ? new ColorDrawable(Color.WHITE) : null);
    }

    public static void setLightStatusBar(@NonNull View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
        }
    }

    public static void clearLightStatusBar(@NonNull View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
        }
    }


    /**
     * Determines if two views intersect in the window.
     */
    public static boolean isViewIntersect(View view1, View view2) {
        if (view1 == null || view2 == null) return false;

        final int[] view1Loc = new int[2];
        view1.getLocationOnScreen(view1Loc);
        final Rect view1Rect = new Rect(view1Loc[0],
                view1Loc[1],
                view1Loc[0] + view1.getWidth(),
                view1Loc[1] + view1.getHeight());
        int[] view2Loc = new int[2];
        view2.getLocationOnScreen(view2Loc);
        final Rect view2Rect = new Rect(view2Loc[0],
                view2Loc[1],
                view2Loc[0] + view2.getWidth(),
                view2Loc[1] + view2.getHeight());
        return view1Rect.intersect(view2Rect);
    }

    public static Point getActivityScreenSize( FragmentActivity activity ){
        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize( point );
        return point;
    }

    public static Point getDeviceScreenSize( Context context ){
        int resolutionX = 0, resolutionY = 0;
        WindowManager wm = (WindowManager) context.getSystemService( Context.WINDOW_SERVICE );
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics( dm );

        if( Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 ){
            try{
                Method mGetRawH = Display.class.getMethod( "getRawHeight" );
                Method mGetRawW = Display.class.getMethod( "getRawWidth" );
                resolutionX = (Integer) mGetRawW.invoke( display );
                resolutionY = (Integer) mGetRawH.invoke( display );
            }catch( Exception e ){
                resolutionX = display.getWidth();
                resolutionY = display.getHeight();
            }
        }else if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 ){
            DisplayMetrics outMetrics = new DisplayMetrics();
            display.getRealMetrics( outMetrics );

            resolutionX = outMetrics.widthPixels;
            resolutionY = outMetrics.heightPixels;
        }

        Point point = new Point();
        point.x = resolutionX;
        point.y = resolutionY;
        return point;
    }


}
