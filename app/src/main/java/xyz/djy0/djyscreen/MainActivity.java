package xyz.djy0.djyscreen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

  Thread thread;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  protected void onStop() {
    super.onStop();
    if (thread != null && thread.isAlive()) {
      thread.destroy();
    }
  }
}
