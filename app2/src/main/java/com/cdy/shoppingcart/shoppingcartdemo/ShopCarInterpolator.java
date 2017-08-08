package com.cdy.shoppingcart.shoppingcartdemo;

import android.util.Log;
import android.view.animation.Interpolator;

/**
 * An interpolator where the rate of change starts out slowly and
 * and then accelerates.
 */
public class ShopCarInterpolator implements Interpolator {

    private final float mFactor;
    private final double mDoubleFactor;

    public ShopCarInterpolator() {
        mFactor = 1.0f;
        mDoubleFactor = 2.0;
    }

    /**
     * Constructor
     *
     * @param factor Degree to which the animation should be eased. Seting
     *               factor to 1.0f produces a y=x^2 parabola. Increasing factor above
     *               1.0f  exaggerates the ease-in effect (i.e., it starts even
     *               slower and ends evens faster)
     */
    public ShopCarInterpolator(float factor) {
        mFactor = factor;
        mDoubleFactor = 2 * mFactor;
    }

    public float getInterpolation(float input) {
        Log.e("getInterpolation()", "----input----" + input + "----");

        //return input;
        if (mFactor == 1.0f) {
            float v = input * input;
            Log.e("getInterpolation()", "----v----" + v + "----");

            return v;
        } else {
            return (float) Math.pow(input, mDoubleFactor);
        }
    }

    //http://blog.csdn.net/gzejia/article/details/51063564
    //https://my.oschina.net/banxi/blog/135633?fromerr=uv67kzf9#OSC_h2_7
    //http://www.wolframalpha.com/input/?i=x%5E(2*3)(0%3Cx%3C%3D1)
    //http://blog.csdn.net/u011835956/article/details/51783025


}
