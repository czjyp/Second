package com.tt.czj.ui.activity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tt.czj.R;
import com.tt.czj.app.MyApp;
import com.tt.czj.di.component.AppComponent;
import com.tt.czj.di.component.DaggerForgetActivityComponent;
import com.tt.czj.di.modules.ForgetActivityModule;
import com.tt.czj.mvp.presenter.ForgetPassPresenter;
import com.tt.czj.mvp.views.ForgetPassView;
import com.tt.czj.ui.activity.base.BaseActivity;
import com.tt.czj.utils.GlobalDefineValues;
import com.tt.czj.utils.ToastUtil;
import com.tt.czj.utils.ToolsUtils;
import com.tt.czj.utils.UIUtils;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * The type Forget pass activity.
 */
public class ForgetPassActivity extends BaseActivity implements ForgetPassView,View.OnClickListener {

    /**
     * The M back linear layout.
     */
    @BindView(R.id.activity_forget_password_back_ll)
    LinearLayout mBackLinearLayout;
    /**
     * The M input phone number text view.
     */
    @BindView(R.id.activity_forget_password_input_phone_number_textview)
    TextView mInputPhoneNumberTextView;
    /**
     * The M input check number text view.
     */
    @BindView(R.id.activity_forget_password_input_check_number_textview)
    TextView mInputCheckNumberTextView;
    /**
     * The M input new pass word text view.
     */
    @BindView(R.id.activity_forget_password_input_password_textview)
    TextView mInputNewPassWordTextView;
    /**
     * The M input check number linear layout.
     */
    @BindView(R.id.activity_forget_password_checknumber_input_ll)
    LinearLayout mInputCheckNumberLinearLayout;
    /**
     * The M input phone number linear layout.
     */
    @BindView(R.id.activity_forget_password_input_phonenumber_ll)
    LinearLayout mInputPhoneNumberLinearLayout;
    /**
     * The M input pass word linear layout.
     */
    @BindView(R.id.activity_reinput_password_input_ll)
    LinearLayout mInputPassWordLinearLayout;
    /**
     * The M confirm input pass word linear layout.
     */
    @BindView(R.id.activity_reinput_confirm_password_input_ll)
    LinearLayout mConfirmInputPassWordLinearLayout;
    /**
     * The M sure text view.
     */
    @BindView(R.id.activity_forget_password_sure_textview)
    TextView mSureTextView;
    /**
     * The M confirm new pass word edit text.
     */
    @BindView(R.id.activity_forget_password_confirm_input_new_password_edittext)
    EditText mConfirmNewPassWordEditText;
    /**
     * The M input phone number edit text.
     */
    @BindView(R.id.activity_forget_password_check_number_edittext)
    EditText mInputPhoneNumberEditText;
    /**
     * The M input check number edit text.
     */
    @BindView(R.id.activity_forget_password_check_number_input_edittext)
    EditText mInputCheckNumberEditText;
    /**
     * The M input new pass word edit text.
     */
    @BindView(R.id.activity_forget_password_input_new_password_edittext)
    EditText mInputNewPassWordEditText;
    /**
     * The Forget pass presenter.
     */
    @Inject
    ForgetPassPresenter forgetPassPresenter;
    private String phoneNum ;
    private String mWorkMode = GlobalDefineValues.ForgetPassWordInputPhoneNumber;

    @Override
    public int getContentViewId() {
        return R.layout.activity_forget_pass;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setupComponent(((MyApp) getApplicationContext()).getAppComponent());
    }

    /**
     * Sets component.
     *
     * @param appComponent the app component
     */
    protected void setupComponent(AppComponent appComponent) {
        DaggerForgetActivityComponent
                .builder()
                .appComponent(appComponent)
                .forgetActivityModule(new ForgetActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void initData() {
        mBackLinearLayout.setOnClickListener(this);
        mSureTextView.setOnClickListener(this);
        if (mWorkMode.equals(GlobalDefineValues.ForgetPassWordInputPhoneNumber)){
            processInputPhoneNumber();
        }else if (mWorkMode.equals(GlobalDefineValues.ForgetPassWordInputCheckNUmber)){
            processInputCheckNumber();
        }else if (mWorkMode.equals(GlobalDefineValues.ForgetPassWordInputPassWord)){
            processInputNewPassWord();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_forget_password_back_ll:{
                finish();
                break;
            }
            case R.id.activity_forget_password_sure_textview:{
                if (mWorkMode.equals(GlobalDefineValues.ForgetPassWordInputPhoneNumber)){
                    phoneNum = mInputPhoneNumberEditText.getText().toString();
                    if (ToolsUtils.checkPhone(phoneNum)){
                        forgetPassPresenter.requestSendSMSCode(ForgetPassActivity.this,phoneNum);
                    }else{
                        ToastUtil.showToast(ForgetPassActivity.this,"手机号格式错误");
                    }
                }else if (mWorkMode.equals(GlobalDefineValues.ForgetPassWordInputCheckNUmber)){
                    if (ToolsUtils.isNullOrEmpty(mInputCheckNumberEditText.getText().toString())){
                        forgetPassPresenter.checkSMSCode(ForgetPassActivity.this,phoneNum,mInputCheckNumberEditText.getText().toString());
                    }else {
                        ToastUtil.showToast(this,"验证码错误");
                    }
                }else if (mWorkMode.equals(GlobalDefineValues.ForgetPassWordInputPassWord)){
                    if (mInputNewPassWordEditText.getText().toString().equals(mConfirmNewPassWordEditText.getText().toString())){
                        forgetPassPresenter.modifyPass(this,phoneNum,mInputNewPassWordEditText.getText().toString());
                    }else{
                        ToastUtil.showToast(ForgetPassActivity.this,"两次密码不一致！");
                    }
                }
                break;
            }
        }
    }


    @Override
    public void foundSuccess() {
        ToastUtil.showToast(this,"找回成功，请登录");
        UIUtils.nextPage(this,LoginActivity.class);
        finish();
    }

    @Override
    public void foundFailed(String str) {
        ToastUtil.showToast(this,str);
        processInputPhoneNumber();
    }

    @Override
    public void checkRight() {
        mWorkMode = GlobalDefineValues.ForgetPassWordInputPassWord;
        processInputNewPassWord();
    }

    @Override
    public void checkError(String str) {
        ToastUtil.showToast(this,str);
    }

    @Override
    public void getSMSCodeSuccess() {
        ToastUtil.showToast(this,"验证码发送成功");
        mWorkMode = GlobalDefineValues.ForgetPassWordInputCheckNUmber;
        processInputCheckNumber();
    }

    @Override
    public void getSMSCodeFailed(String str) {
        ToastUtil.showToast(this,str);
    }

    /**
     * Process input phone number.
     */
    public void processInputPhoneNumber(){
        mInputPhoneNumberTextView.setSelected(true);
        mInputNewPassWordTextView.setSelected(false);
        mInputCheckNumberTextView.setSelected(false);
        mInputPhoneNumberLinearLayout.setVisibility(View.VISIBLE);
        mInputCheckNumberLinearLayout.setVisibility(View.GONE);
        mInputPassWordLinearLayout.setVisibility(View.GONE);
        mConfirmInputPassWordLinearLayout.setVisibility(View.GONE);
        mSureTextView.setText(R.string.activity_register_get_check_sms);
    }

    /**
     * Process input check number.
     */
    public void processInputCheckNumber(){
        mInputPhoneNumberTextView.setSelected(false);
        mInputNewPassWordTextView.setSelected(false);
        mInputCheckNumberTextView.setSelected(true);
        mInputPhoneNumberLinearLayout.setVisibility(View.GONE);
        mInputCheckNumberLinearLayout.setVisibility(View.VISIBLE);
        mInputPassWordLinearLayout.setVisibility(View.GONE);
        mConfirmInputPassWordLinearLayout.setVisibility(View.GONE);
        mSureTextView.setText(R.string.activity_register_check_string);
    }

    /**
     * Process input new pass word.
     */
    public void processInputNewPassWord(){
        mInputPhoneNumberTextView.setSelected(false);
        mInputNewPassWordTextView.setSelected(true);
        mInputCheckNumberTextView.setSelected(false);
        mInputNewPassWordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mConfirmNewPassWordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mInputPhoneNumberLinearLayout.setVisibility(View.GONE);
        mInputCheckNumberLinearLayout.setVisibility(View.GONE);
        mInputPassWordLinearLayout.setVisibility(View.VISIBLE);
        mConfirmInputPassWordLinearLayout.setVisibility(View.VISIBLE);
        mSureTextView.setText(R.string.activity_forget_password_confirm_change);
    }
}
