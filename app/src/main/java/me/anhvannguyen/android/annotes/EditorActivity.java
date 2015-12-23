package me.anhvannguyen.android.annotes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class EditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        if (savedInstanceState == null) {
            EditorFragment fragment = new EditorFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.note_editor_container, fragment)
                    .commit();
        }
    }
}
