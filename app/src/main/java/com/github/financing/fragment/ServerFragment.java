package com.github.financing.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.financing.R;
import com.github.financing.base.BaseFragment;
import com.github.financing.ui.WebTitleViewActivity;
import com.github.financing.ui.WebViewActivity;
import com.github.financing.utils.CommonUtil;
import com.github.financing.utils.Constants;

/********************************************
 * 作者：Administrator
 * 时间：2016/10/9
 * 描述：
 *******************************************/
public class ServerFragment extends BaseFragment {
    private View view;
    private Button button;
    private TextView tvComputer,tvHelp,tvKefu,tvAbout;
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.server_help:
                    gotoWebView("file:///android_asset/sample.html","帮助中心");
                    break;
                case R.id.server_kefu:
                   showDialog();
                    break;
                case R.id.server_about:
                    gotoWebView("file:///android_asset/sample.html","关于我们");
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_activity, null);
//            tvComputer = (TextView) view.findViewById(R.id.server_computer);
//            tvComputer.setOnClickListener(clickListener);
            tvHelp = (TextView) view.findViewById(R.id.server_help);
            tvHelp.setOnClickListener(clickListener);
            tvKefu = (TextView) view.findViewById(R.id.server_kefu);
            tvKefu.setOnClickListener(clickListener);
            tvAbout = (TextView) view.findViewById(R.id.server_about);
            tvAbout.setOnClickListener(clickListener);
        }else{
            ViewGroup parent = (ViewGroup)view.getParent();
            if(parent != null){
                parent.removeView(view);
            }
        }

        return view;
    }

    private void showDialog(){
        final Dialog dialog = new Dialog(getActivity(), R.style.AlertActivity_AlertStyle);
        dialog.setContentView(R.layout.dialog_service);
        TextView callCancel = (TextView) dialog.findViewById(R.id.call_cancel);
        TextView callService = (TextView) dialog.findViewById(R.id.call_service);
        callCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        callService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + "400-654-0000");
                intent.setData(data);
                startActivity(intent);
            }
        });

        dialog.show();
    }

    private void gotoWebView(String url,String title){
        Intent intent = new Intent(getActivity(),WebTitleViewActivity.class);
        intent.putExtra(Constants.INTENT_API_DATA_KEY_URL, url);
        intent.putExtra(Constants.INTENT_API_DATA_KEY_DATA, title);
        startActivity(intent);
    }
}
