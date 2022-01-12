package com.park.stub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Main2Activity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ((TextView)findViewById(R.id.tv)).setText("main 2");
    findViewById(R.id.btn2).setOnClickListener(v-> startActivity(new Intent(Main2Activity.this, Main3Activity.class)));
  }
}
