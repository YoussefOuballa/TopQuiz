package ma.snrt.topquiz.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
//import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ma.snrt.topquiz.R;
import ma.snrt.topquiz.model.User;

public class MainActivity extends AppCompatActivity {

    private TextView mGreetingText;
    private EditText mNameInput;
    private Button mPlayButton;
    private User mUser;
    private static final int GAME_ACTIVITY_REQUEST_CODE = 42;
    private SharedPreferences mPreferences;

    //private static final String TAG = "MainActivity";
    public static final String PREF_KEY_SCORE= "PREF_KEY_SCORE";
    public static final String PREF_KEY_FIRSTNAME = "PREF_KEY_FIRSTNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUser = new User();
        mPreferences = getPreferences(MODE_PRIVATE);

        mUser.setFirstName(mPreferences.getString(PREF_KEY_FIRSTNAME, null));
        mUser.setScore(mPreferences.getInt(PREF_KEY_SCORE, -1));

        mGreetingText = findViewById(R.id.activity_main_greeting_txt);
        mNameInput = findViewById(R.id.activity_main_name_input);
        mPlayButton = findViewById(R.id.activity_main_play_btn);

        mPlayButton.setEnabled(false);

        this.displayWelcomeMessage();

        mNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
                mPlayButton.setEnabled(s.toString().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s){

            }
        });

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mUser.setFirstName(mNameInput.getText().toString());

                mPreferences.edit().putString(PREF_KEY_FIRSTNAME, mUser.getFirstname()).apply();

                Intent gameActivityIntent = new Intent(MainActivity.this, GameActivity.class);
                startActivityForResult(gameActivityIntent, GAME_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GAME_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            int score = data.getIntExtra(GameActivity.BUNDLE_EXTRA_SCORE, 0);

            mUser.setScore(score);

            mPreferences.edit().putInt(PREF_KEY_SCORE, mUser.getScore()).apply();

            this.displayWelcomeMessage();
        }
    }

    public void displayWelcomeMessage() {
        if(mUser.getFirstname() != null){
            mGreetingText.setText("Welcome back, "+mUser.getFirstname()+"!\n Your last score was "+ mUser.getScore()+", will you do better this time ?");
            mGreetingText.setPadding(0,0,0,0);


            mNameInput.setText(mUser.getFirstname());
            mPlayButton.setEnabled(mNameInput.getText().toString().length() != 0);
            mNameInput.setSelection(mUser.getFirstname().length());
        }
    }
}
