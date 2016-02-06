package com.headout.hack.grouphead;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.headout.hack.grouphead.Model.KeyValue;
import com.headout.hack.grouphead.Model.UserModel;
import com.headout.hack.grouphead.Model.Vault;
import com.headout.hack.grouphead.db.DbHandler;

import java.util.ArrayList;
import java.util.List;

public class SecureKey extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {

    private KeyboardView kv;
    private Keyboard keyboard;
    LayoutInflater li;
    static Boolean pavan = false;

    private boolean caps = false;

    Keyboard qwertyKeyboard;
    Keyboard symbolsKeyboard;
    Keyboard symbolsShiftKeyboard;

    ProgressDialog dialog;
    WebView webView;

    @Override
    public View onCreateInputView() {
        qwertyKeyboard = new Keyboard(this, R.xml.qwerty);
        symbolsKeyboard = new Keyboard(this, R.xml.symbols);
        symbolsShiftKeyboard = new Keyboard(this, R.xml.symbols_shift);

        li = LayoutInflater.from(this);
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = qwertyKeyboard;
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        switch(primaryCode){
            case Keyboard.KEYCODE_DELETE :
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                Keyboard currentKeyboard = kv.getKeyboard();
                if(currentKeyboard == symbolsKeyboard || currentKeyboard == symbolsShiftKeyboard){
                    HandleShift(true);
                }else {
                    caps = !caps;
                    keyboard.setShifted(caps);
                    kv.invalidateAllKeys();
                }
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            case -22:
                Authenticate();
                break;
            case -33:
                VaultAccess();
                break;
            case -456:
                NewTest();
                break;
            case -200:
                Browser();
                break;
            case -50:
                NewTest();
                break;
            case Keyboard.KEYCODE_MODE_CHANGE:
                HandleShift(false);
                break;
            default:
                char code = (char)primaryCode;
                if(Character.isLetter(code) && caps){
                    code = Character.toUpperCase(code);
                }
                ic.commitText(String.valueOf(code),1);
        }
    }

    private void Browser() {
        pavan = true;
        LinearLayout v = (LinearLayout) getLayoutInflater().inflate(R.layout.browser_view, null);
        webView = (WebView) v.findViewById(R.id.webview);

        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        String url = "";
        if(clipboard.hasPrimaryClip()){
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            url = item.getText().toString();
            new LoadSocialNetworkUrlTask().execute(url);
            setInputView(v);
        }

/*

        view.getSettings().setJavaScriptEnabled(true);*//*
        view.getSettings().setLoadWithOverviewMode(true);
        view.getSettings().setUseWideViewPort(true);*//*
        view.setWebViewClient(new WebViewClient()*//*{

           *//**//* @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progDailog.show();
                view.loadUrl(url);

                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
                progDailog.dismiss();
            }*//**//*
        }*//*);


        view.loadUrl("http://www.facebook.com");
        setInputView(v);*/
    }

    public class LoadSocialNetworkUrlTask extends
            AsyncTask<String, String, Void> {

        protected void onPreExecute() {
            /*dialog = new ProgressDialog(getApplicationContext());
            dialog.setMessage("Loading,Please wait...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            dialog.show();*/
        }

        protected void onProgressUpdate(final String... url) {
            try {

                ((WebView) webView).getSettings().setJavaScriptEnabled(true);
                ((WebView) webView).setBackgroundColor(Color.TRANSPARENT);

                ((WebView) webView).setWebViewClient(new WebViewClient() {

                    @Override
                    public void onReceivedSslError(WebView view,
                                                   SslErrorHandler handler, SslError error) {
                        handler.proceed(); // Ignore SSL certificate errors
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        // TODO hide your progress image
                        super.onPageFinished(view, url);

                        //dialog.dismiss();
                    }
                });
                ((WebView) webView).loadUrl(url[0]);

            } catch (Exception e) {
                e.printStackTrace();
                //dialog.dismiss();
            }

        }

        @Override
        protected Void doInBackground(String... url) {
            try {
                publishProgress(url);

            } catch (Exception e) {
                e.printStackTrace();
                dialog.dismiss();
            }

            return null;
        }
    }

    private void HandleShift(boolean shiftToSymbols) {
        Keyboard currentKeyboard = kv.getKeyboard();

        if(shiftToSymbols){
            kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
            if(currentKeyboard == symbolsShiftKeyboard){
                keyboard = symbolsKeyboard;
            }else {
                keyboard = symbolsShiftKeyboard;
            }
            kv.setKeyboard(keyboard);
            kv.setOnKeyboardActionListener(this);
            setInputView(kv);
        }
        else if(currentKeyboard == qwertyKeyboard) {
            kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
            keyboard = symbolsKeyboard;
            kv.setKeyboard(keyboard);
            kv.setOnKeyboardActionListener(this);
            setInputView(kv);
        }else if(currentKeyboard == symbolsKeyboard){
            kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
            keyboard = qwertyKeyboard;
            kv.setKeyboard(keyboard);
            kv.setOnKeyboardActionListener(this);
            setInputView(kv);
        }
    }

    private void NewTest() {
        pavan = true;
        RelativeLayout v = (RelativeLayout) getLayoutInflater().inflate(R.layout.image_layout, null);
        setInputView(v);
    }

    private void AddKey() {
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.newquery);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        setInputView(kv);
    }

    private void Authenticate() {
        View promptsView = li.inflate(R.layout.authenticate, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final EditText input = (EditText) promptsView.findViewById(R.id.editTextPassword);
        Button button1 = (Button) promptsView.findViewById(R.id.click_1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.append("1");
            }
        });
        Button button2 = (Button) promptsView.findViewById(R.id.click_2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.append("2");
            }
        });
        Button button3 = (Button) promptsView.findViewById(R.id.click_3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.append("3");
            }
        });
        Button button4 = (Button) promptsView.findViewById(R.id.click_4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.append("4");
            }
        });
        Button button5 = (Button) promptsView.findViewById(R.id.click_5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.append("5");
            }
        });
        Button button6 = (Button) promptsView.findViewById(R.id.click_6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.append("6");
            }
        });
        Button button7 = (Button) promptsView.findViewById(R.id.click_7);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.append("7");
            }
        });
        Button button8 = (Button) promptsView.findViewById(R.id.click_8);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.append("8");
            }
        });
        Button button9 = (Button) promptsView.findViewById(R.id.click_9);
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.append("9");
            }
        });
        Button button0 = (Button) promptsView.findViewById(R.id.click_0);
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.append("0");
            }
        });

        Button buttonDel = (Button) promptsView.findViewById(R.id.click_del);
        buttonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = input.getText().toString();
                if (text.length() > 0){
                    text = text.substring(0, text.length()-1);
                    input.setText("");
                    input.append(text);
                }
            }
        });

        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setMessage("Authenticate to SecureKey!");
        alertDialogBuilder.setPositiveButton("Login",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                                String enteredPassword = input.getText().toString();
                                UserModel user = new UserModel();
                                user.UserName = "";
                                user.Password = Long.parseLong(input.getText().toString());
                                if (!enteredPassword.equals("") && user.Password == 1234) {
                                    Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                                    AddKey();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Login Failed, Try Again!", Toast.LENGTH_SHORT).show();
                                }
                        }
                    }
                );

        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.token = kv.getWindowToken();
        lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertDialog.show();
    }

    private void VaultAccess() {
        View promptsView = li.inflate(R.layout.alert_list, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setMessage("Choose your vault");
        final AlertDialog alertDialog = alertDialogBuilder.create();

        final ListView listView = (ListView) promptsView.findViewById(R.id.listViewList);

        final List<Vault> vaults = DbHandler.readfromvault(getApplicationContext());
        String[] VaultNames = new String[vaults.size()];
        for(int i = 0; i < vaults.size(); i++){
            VaultNames[i] = vaults.get(i).getName();
        }

        /*String[] vaults = new String[]{"Hello", "How", "are", "you"};*/
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.plain_text_list_item, VaultNames );
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                final Vault selectedVault = vaults.get(position);

                if(selectedVault.getIsSecure() == 1){
                    View promptsView = li.inflate(R.layout.authenticate, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());
                    final EditText input = (EditText) promptsView.findViewById(R.id.editTextPassword);
                    Button button1 = (Button) promptsView.findViewById(R.id.click_1);
                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            input.append("1");
                        }
                    });
                    Button button2 = (Button) promptsView.findViewById(R.id.click_2);
                    button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            input.append("2");
                        }
                    });
                    Button button3 = (Button) promptsView.findViewById(R.id.click_3);
                    button3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            input.append("3");
                        }
                    });
                    Button button4 = (Button) promptsView.findViewById(R.id.click_4);
                    button4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            input.append("4");
                        }
                    });
                    Button button5 = (Button) promptsView.findViewById(R.id.click_5);
                    button5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            input.append("5");
                        }
                    });
                    Button button6 = (Button) promptsView.findViewById(R.id.click_6);
                    button6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            input.append("6");
                        }
                    });
                    Button button7 = (Button) promptsView.findViewById(R.id.click_7);
                    button7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            input.append("7");
                        }
                    });
                    Button button8 = (Button) promptsView.findViewById(R.id.click_8);
                    button8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            input.append("8");
                        }
                    });
                    Button button9 = (Button) promptsView.findViewById(R.id.click_9);
                    button9.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            input.append("9");
                        }
                    });
                    Button button0 = (Button) promptsView.findViewById(R.id.click_0);
                    button0.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            input.append("0");
                        }
                    });

                    Button buttonDel = (Button) promptsView.findViewById(R.id.click_del);
                    buttonDel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String text = input.getText().toString();
                            if (text.length() > 0){
                                text = text.substring(0, text.length()-1);
                                input.setText("");
                                input.append(text);
                            }
                        }
                    });

                    alertDialogBuilder.setView(promptsView);
                    alertDialogBuilder.setMessage("Authenticate to SecureKey!");
                    alertDialogBuilder.setPositiveButton("Login",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                String enteredPassword = input.getText().toString();
                                if (!enteredPassword.equals("") && enteredPassword.equals(Long.toString(selectedVault.getPasscode()))) {
                                    Toast.makeText(getApplicationContext(), "Vault Login Successful!", Toast.LENGTH_SHORT).show();
                                    AddKey();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Vault Login Failed, Try Again!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    );

                    alertDialogBuilder.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }
                    );

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    Window window = alertDialog.getWindow();
                    WindowManager.LayoutParams lp = window.getAttributes();
                    lp.token = kv.getWindowToken();
                    lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
                    window.setAttributes(lp);
                    window.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    alertDialog.show();
                }

                Toast.makeText(getApplicationContext(), "You have clicked :" + selectedVault.getName(), Toast.LENGTH_LONG).show();
                //String[] vaults = new String[]{"New", "Set", "Of", "Strings"};

                final List<KeyValue> kv = DbHandler.getAllPairs(getApplicationContext());
                final List<KeyValue> mkv = new ArrayList<KeyValue>();
                for(int i = 0; i < kv.size(); i++){
                    if(kv.get(i).getVaultId() == selectedVault.getId()){
                        mkv.add(kv.get(i));
                    }
                }

                final String[] keyNames = new String[mkv.size()];
                for(int i = 0; i < mkv.size(); i++){
                    keyNames[i] = mkv.get(i).getName();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.plain_text_list_item, keyNames );
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view,
                                            int position, long id) {
                        final String item = (String) parent.getItemAtPosition(position);
                        KeyValue toBeInserted = mkv.get(position);
                        Toast.makeText(getApplicationContext(), "You have clicked :" + toBeInserted.getName(), Toast.LENGTH_LONG).show();
                        InputConnection ic = getCurrentInputConnection();
                        ic.commitText(toBeInserted.getValue(), toBeInserted.getValue().length());
                        alertDialog.dismiss();
                    }
                });
            }
        });
        alertDialogBuilder.setView(promptsView);
        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.token = kv.getWindowToken();
        lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertDialog.show();
    }

    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
        if(pavan){
            RelativeLayout v = (RelativeLayout) getLayoutInflater().inflate(R.layout.alert_generate_service_request, null);
            setInputView(v);
        }
    }

    @Override
    public void swipeUp() {
    }

    @Override
    public void onStartInputView(EditorInfo attribute, boolean restarting) {
        super.onStartInputView(attribute, restarting);

        setInputView(onCreateInputView());
    }
}
