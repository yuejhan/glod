package com.github.financing.listener;

import android.view.View;

import com.github.financing.fragment.ProductsFragment;

import com.github.financing.utils.DropEnum;
/**
 * Created by user on 2016/10/18.
 */
public class TabChangeListener implements View.OnClickListener {
    private ProductsFragment context;
    private View containt;

    public TabChangeListener(ProductsFragment context,View containt) {
        this.context = context;
        this.containt = containt;

    }

    @Override
    public void onClick(View v) {
        if(containt.getVisibility() == View.VISIBLE){
            containt.setVisibility(View.GONE);
        }else{
            containt.setVisibility(View.VISIBLE);
            if("order".equals(v.getTag())){
//                context.checkMenu(DropEnum.order);
            }else if("type".equals(v.getTag())){
//                context.checkMenu(DropEnum.type);
            }
        }
    }
}
