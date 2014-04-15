package cn.edu.nju.software.obdii.ui;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.software.obdii.R;
import cn.edu.nju.software.obdii.network.HttpClient;
import cn.edu.nju.software.obdii.network.Url;
import cn.edu.nju.software.obdii.util.Utilities;
import cn.jpush.android.api.JPushInterface;

public class LoginActivity extends Activity {
    private EditText mUsernameEdit;
    private EditText mPasswordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsernameEdit = (EditText) findViewById(R.id.username);
        mPasswordEdit = (EditText) findViewById(R.id.password);
        Button signInButton = (Button) findViewById(R.id.sign_in_button);

        mPasswordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if (actionID == EditorInfo.IME_ACTION_GO) {
                    signIn();
                }
                return false;
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void signIn() {
        final String username = mUsernameEdit.getText().toString();
        final String password = mPasswordEdit.getText().toString();
        if (username.length() < 0) {
            Utilities.showMessage(this, R.string.username_empty);
        } else if (password.length() < 0) {
            Utilities.showMessage(this, R.string.password_empty);
        } else {
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... voids) {
                    String MD5edUsername = Utilities.MD5(username);
                    String MD5edPassword = Utilities.MD5(password);
                    List<NameValuePair> parameters = new ArrayList<NameValuePair>(2);
                    parameters.add(new BasicNameValuePair("username", MD5edUsername));
                    parameters.add(new BasicNameValuePair("password", MD5edPassword));
                    try {
                        return HttpClient.getInstance().httpPost(Url.LOGIN_URL, parameters);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return "1,";
                }

                @Override
                protected void onPostExecute(String signInResult) {
                    if (signInResult == null) {
                        Utilities.showMessage(LoginActivity.this, R.string.connection_fail);
                        return;
                    }
                    String[] results = signInResult.split(",");
                    if (results[0].equals("1")) {
                        // TODO Add sign in logic
                        JPushInterface.setAlias(LoginActivity.this, results[1], null);
                    } else {
                        Utilities.showMessage(LoginActivity.this, R.string.sign_in_fail);
                    }
                }
            }.execute();
        }
    }
}
