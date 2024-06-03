package com.livovic.ime;

import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;

public class IMEExample extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    @Override
    public View onCreateInputView() {
        KeyboardView keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        Keyboard keyboard = new Keyboard(this, R.xml.keyboard);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);

        return keyboardView;
    }

    @Override
    public View onCreateCandidatesView() {
        return super.onCreateCandidatesView();
    }

    @Override
    public void onPress(int primaryCode) {
        Log.d("IME Example", "onPress: " + primaryCode);
    }

    @Override
    public void onRelease(int primaryCode) {
        Log.d("IME Example", "onRelease: " + primaryCode);
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        Log.d("IME Example", "onKey: " + primaryCode);
        InputConnection ic = getCurrentInputConnection();
        CharSequence currentText = ic.getExtractedText(new ExtractedTextRequest(), 0).text;

        switch(primaryCode){
            case 1: // R
                ic.commitText("R",1);
                break;
            case 2: // I
                ic.commitText("I",0);
                break;
            case 3: // T
                ic.setComposingText("RITEH",1);
                break;
            case 4: // E
                if (currentText != null && currentText.length() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        ic.replaceText(0, currentText.length(), "RITEH", 1, null);
                    }
                }
            case 5: // H
                ic.commitText("H",1);
                break;

            case 6: // Brisi sve
                if (currentText != null && currentText.length() > 0) {
                    ic.deleteSurroundingText(currentText.length(), 0);
                }
                break;
            case 7: // POSTAVKE
                String imId = Settings.Secure.getString(getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
                final Intent intent = new Intent(Settings.ACTION_INPUT_METHOD_SUBTYPE_SETTINGS);
                intent.putExtra(Settings.EXTRA_INPUT_METHOD_ID, imId);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_TITLE, "Select subtypes");
                startActivity(intent);

                break;
            case 8: // ZATVORI
                switchToNextInputMethod(true);
                requestHideSelf(0);
                break;
            case 10: // KRAJ
                ic.finishComposingText();
                break;
            default:
                char code = (char) primaryCode;
                ic.commitText(String.valueOf(code), 1);
        }
    }


    @Override
    public void onText(CharSequence text) {
        Log.d("IME Example", "onText: " + text);
    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}