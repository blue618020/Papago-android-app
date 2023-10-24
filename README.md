# 파파고 번역기 앱

📝 <b> tistory : </b> https://blue618020.tistory.com/109 , https://blue618020.tistory.com/110

### 🔍 학습 내용
-  네이버 파파고 번역 API 레퍼런스를 확인하여 사용하기
-  Volley POST 통신하기
-  번역 히스토리 확인하기

### 💻 실습
-  원본 언어는 'ko'(한국어)로 설정
-  선택한 라디오 버튼 정보를 가져와서, 어떤 언어로 번역할지에 대한 정보를 가져오기(target)

    -  버튼1 : 'en'(영어)
    -  버튼2 : 'zh-CN'(중국어간체)
    -  버튼3 : 'zh-TW'(중국어 번체)
    -  버튼4 : 'th'(태국어)
 
-  Snackbar를 띄워서 예외처리도 진행하기
-  파파고 API를 <b> Volley </b>를 사용해서 호출하기

                String source = "ko"; // 원본 언어는 ko
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

                // 요청 url
                String url = "https://openapi.naver.com/v1/papago/n2mt";

                // 파라미터 넣어주기
                JSONObject body = new JSONObject();
                try {
                    body.put("source", source);
                    body.put("target", target);
                    body.put("text", text);

                } catch (JSONException e) {
                    return;
                } 
    -  source : 원본 언어인 'ko'
    -  target : 유저가 선택한 라디오 버튼 정보
    -  text : 유저가 입력한 텍스트
 
-  번역결과는 message > result > translatedText 순으로 들어있기 때문에, 이중 Json 파싱하기

              // message > result > translatedText (번역결과)

              try {
                    JSONObject message = response.getJSONObject("message");
                    JSONObject result = message.getJSONObject("result");
                    String translatedText = result.getString("translatedText");

                    txtResult.setText(translatedText);
   
-  HTTP 요청 헤더에 클라이언트 아이디와 클라이언트 시크릿을 추가 필수!
      -  X-Naver-Client-Id : 아이디 값
      -  X-Naver-Client-Secret : 시크릿 값
 
-  MainActivity에서 intent를 통해 번역결과를 HistoryActivity로 보내줌
-  받아올 때 historyArrayList 로 전체를 묶어서 addAll 로 리스트 안에 다 추가

         // 번역결과 히스토리 화면에 띄우기
            historyArrayList.addAll((ArrayList<History>) getIntent().getSerializableExtra("history"));
   
-  adapter를 사용해서 리스트 전체를 화면에 띄우기
  
