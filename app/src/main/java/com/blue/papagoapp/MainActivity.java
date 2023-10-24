package com.blue.papagoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.blue.papagoapp.adapter.HistoryAdapter;
import com.blue.papagoapp.model.History;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RadioGroup radioGroup; // 그룹만 써도 된대
    EditText editText;
    Button button;
    TextView txtResult;
    ArrayList<History> historyArrayList = new ArrayList<History>();
    HistoryAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroup = findViewById(R.id.radioGroup);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        txtResult = findViewById(R.id.txtResult);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. 어떤 언어로 번역할지에 대한 정보를 가져오기 => 선택한 라디오 버튼 정보를 가져오기!
                // 라디오 그룹만 멤버변수로 쓴 이유가 여기서 정보를 가져오기 때문

                String target; // 파파고 언어코드 저장할 변수(파리미터)

                int radioButtonId = radioGroup.getCheckedRadioButtonId();

                if(radioButtonId == R.id.radioButton1){
                    target = "en";
                } else if(radioButtonId == R.id.radioButton2){
                    target = "zh-CN";
                } else if(radioButtonId == R.id.radioButton3){
                    target = "zh-TW";
                } else if(radioButtonId == R.id.radioButton4){
                    target = "th";
                } else {
                    Snackbar.make(button,
                            "번역할 언어를 선택하세요.",
                            Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // 2. 에디트텍스트에 유저가 쓴 글을 가져오기
                String text = editText.getText().toString().trim();
                if(text.isEmpty()){
                    Snackbar.make(button,
                            "번역할 문장을 입력하세요.",
                            Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // 3. 파파고 API 호출! 그 결과를 화면에 보여주기
                String source = "ko"; // 원본 언어는 ko

                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

                // 요청 url
                String url = "https://openapi.naver.com/v1/papago/n2mt";

                // 파라미터 넣어주기
                // 포스트맨에서 했던걸 떠올리면 쉽다
                JSONObject body = new JSONObject();
                try {
                    body.put("source", source);
                    body.put("target", target);
                    body.put("text", text);

                } catch (JSONException e) {
                    return;
                }

                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        body, //null을 사용하지 않는다!
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // 네이버로부터 받은 데이터를 처리하고 화면에 표시
                                Log.i("aaa", response.toString());

//                                // message > result > translatedText (번역결과)

                                try {
                                    JSONObject message = response.getJSONObject("message");
                                    JSONObject result = message.getJSONObject("result");
                                    String translatedText = result.getString("translatedText");

                                    txtResult.setText(translatedText);

                                    History history = new History(text, target, translatedText);
                                    // 입력한 텍스트와 번역 언어는 1,2번 순서에서 만든 로컬 변수를 넣음

                                    historyArrayList.add(0, history);
                                    // 맨 앞에서부터 추가하기위해 인덱스 0 넣어주기
                                    // (아니 정렬 이거였냐고;)

                                } catch (JSONException e) {
                                    return;
                                }
                                adapter = new HistoryAdapter(MainActivity.this, historyArrayList);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }

                ){ // Volley 에서 헤더에 데이터를 세팅하고 싶으면 이렇게
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> header = new HashMap<>();

                        // 클라이언트 아이디 값과 시크릿 값
                        header.put("X-Naver-Client-Id", "_q6x2QR6s12w3oNQaAmB");
                        header.put("X-Naver-Client-Secret", "t_u0L7VxD0");

                        return header;
                    }
                };
                queue.add(request);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 액션바의 메뉴가 나오도록 설정하기
        getMenuInflater().inflate(R.menu.main, menu); // 화면과 menu 를 연결
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // 유저가 아이콘을 눌렀을 경우, 히스토리 액티비티를 실행시키기
        int itemId = item.getItemId();

        if (itemId == R.id.menuAdd) {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            intent.putExtra("history", historyArrayList); // 번역결과 보내주기
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}