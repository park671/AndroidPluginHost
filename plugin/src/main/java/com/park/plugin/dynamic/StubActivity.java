package com.park.plugin.dynamic;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class StubActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    TextView textView = new TextView(StubActivity.this);
    textView.setText("Something Error Happend.");
    setContentView(textView);
  }
}
