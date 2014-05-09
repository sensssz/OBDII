package cn.edu.nju.software.obdii.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.edu.nju.software.obdii.R;
import cn.edu.nju.software.obdii.data.DataDispatcher;
import cn.edu.nju.software.obdii.network.HttpClient;
import cn.edu.nju.software.obdii.network.Url;
import cn.edu.nju.software.obdii.util.Utilities;
import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class LoginActivity extends InstrumentedActivity {
    private static final int ID_LENGTH = 20;

    private EditText mUsernameEdit;
    private EditText mPasswordEdit;
    private Button mSignInButton;
    private Button mForgetButton;
//    private String mForgetLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.login_main);
        frameLayout.getForeground().setAlpha(0);//设置前景色为透明
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.sign_in);
        }
        mUsernameEdit = (EditText) findViewById(R.id.username);
        mPasswordEdit = (EditText) findViewById(R.id.password);
        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mForgetButton = (Button) findViewById(R.id.forget_button);
//        mForgetLink = "http://www.baidu.com";
        mPasswordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if (actionID == EditorInfo.IME_ACTION_GO) {
                    signIn();
                }
                return false;
            }
        });

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        mForgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Uri uri = Uri.parse(mForgetLink);
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
                LayoutInflater inflater = (LayoutInflater) LoginActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                final View popView = inflater.inflate(R.layout.popup_forgot, null, false);
                final PopupWindow forgetWindow = new PopupWindow(popView,
                        LinearLayout.LayoutParams.MATCH_PARENT, 300, true);

                final FrameLayout frameLayout = (FrameLayout)findViewById(R.id.login_main);
                frameLayout.getForeground().setAlpha(200);//设置前景色不透明，达到暗化的目的

                forgetWindow.setAnimationStyle(R.style.popup_anim_style);
                forgetWindow.showAtLocation(findViewById(R.id.login_main), Gravity.CENTER, 0, 0);



                Button okButton = (Button)popView.findViewById(R.id.forget_ok);
                Button cancelButton = (Button)popView.findViewById(R.id.forget_cancel);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText editText = (EditText)popView.findViewById(R.id.forget_email);
                        final String email = editText.getText().toString();
                        final String url = "http://112.124.47.134/OBDController/mail/forget_password_sendmail.html";
                        if (email.equals("")) {
                            Utilities.showMessage(LoginActivity.this, R.string.username_empty);
                        }
                        else {

                            new AsyncTask<Void, Void, String>() {
                                @Override
                                protected void onPreExecute() {

                                }

                                @Override
                                protected String doInBackground(Void... voids) {
                                    String result = "";
                                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                                    params.add(new BasicNameValuePair("email", email));
                                    try {
                                        result = HttpClient.getInstance().httpPost(url,params);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    return result;
                                }

                                @Override
                                protected void onPostExecute(String signInResult) {
                                    forgetWindow.dismiss();
                                    frameLayout.getForeground().setAlpha(0);
                                    Utilities.showMessage(LoginActivity.this, R.string.email_sent);

                                }
                            }.execute();
                        }

                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(forgetWindow != null){
                           forgetWindow.dismiss();
                           frameLayout.getForeground().setAlpha(0);
                        }

                    }
                });
            }
        });
    }

    private void signIn() {
        final String username = mUsernameEdit.getText().toString();
        final String password = mPasswordEdit.getText().toString();
        if (username.length() < 1) {
            Utilities.showMessage(this, R.string.username_empty);
        } else if (password.length() < 1) {
            Utilities.showMessage(this, R.string.password_empty);
        } else {
            new AsyncTask<Void, Void, String>() {
                @Override
                protected void onPreExecute() {
                    signInInProgress();
                }

                @Override
                protected String doInBackground(Void... voids) {
                    String result = null;
                    String encodedUsername = Utilities.urlEncode(username);
                    String MD5edPassword = Utilities.md5(password);
                    String url = Url.LOGIN_URL + "?email=" + encodedUsername + "&password=" + MD5edPassword;
                    try {
                        result = HttpClient.getInstance().httpGet(url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return result;
                }

                @Override
                protected void onPostExecute(String signInResult) {
                    if (signInResult == null) {
                        Utilities.showMessage(LoginActivity.this, R.string.connection_fail);
                        signInFinished();
                        return;
                    }
                    String[] results = signInResult.split(",");
                    if (results[0].equals("1")) {
                        if (results[1].equals("未绑定设备")) {
                            Toast.makeText(LoginActivity.this, results[1], Toast.LENGTH_LONG).show();
                            signInFinished();
                        } else {
                            String ID = results[1].substring(0, ID_LENGTH);
                            JPushInterface.setAlias(LoginActivity.this, ID, new TagAliasCallback() {
                                @Override
                                public void gotResult(int code, String alias, Set<String> strings) {
                                    DataDispatcher.getInstance().setUsername(LoginActivity.this, username);
                                    Intent intent = new Intent(LoginActivity.this, MainViewActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    } else {
                        Utilities.showMessage(LoginActivity.this, R.string.sign_in_fail);
                        signInFinished();
                    }
                }
            }.execute();
        }
    }

    private void signInInProgress() {
        mUsernameEdit.setEnabled(false);
        mPasswordEdit.setEnabled(false);
        mSignInButton.setEnabled(false);
        mSignInButton.setText(R.string.sign_in_executing);
    }

    private void signInFinished() {
        mUsernameEdit.setEnabled(true);
        mPasswordEdit.setEnabled(true);
        mSignInButton.setEnabled(true);
        mSignInButton.setText(R.string.sign_in);
    }
}
