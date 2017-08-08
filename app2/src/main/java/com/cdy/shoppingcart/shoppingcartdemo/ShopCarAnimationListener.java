package com.cdy.shoppingcart.shoppingcartdemo;

import android.view.animation.Animation;

/**
 * Created by 蓝兵 on 2017/8/8.
 */

public interface ShopCarAnimationListener {

    void onAnimationStart(Animation animation, int animNumber);

    void onAnimationEnd(Animation animation, int animNumber);
}
