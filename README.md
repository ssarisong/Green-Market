# GreenMarket
### 참고사항
1. **Overall Structure**
```
GreenMarket/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── kr.ac.hansung/
│   │   │   │   ├── greenmarket/
│   │   │   │   │   ├── ui/  # UI 관련 코드 - Front
│   │   │   │   │   │   ├── MainActivity.kt
│   │   │   │   │   │   ├── LoginActivity.kt
│   │   │   │   │   │   ├── ... (다른 액티비티나 프래그먼트)
│   │   │   │   │   │
│   │   │   │   │   ├── utils/  # 유틸리티나 헬퍼 클래스 - Back
│   │   │   │   │   │   ├── FirebaseAuthHelper.kt
│   │   │   │   │   │   ├── ... (다른 헬퍼 클래스)
│   │   │   │   │   │
│   │   │   │   │   ├── models/  # 데이터 모델 - Back
│   │   │   │   │   │   ├── User.kt
│   │   │   │   │   │   ├── ... (다른 데이터 클래스)
│   │   │   │   │   │
│   │   │   │   │   ├── ... (다른 패키지나 클래스 추가시 수정)
│   │   │   │   │   
│   │   ├── res/
│   │   │   ├── layout/  # 레이아웃 - Front
│   │   │   │   ├── activity_main.xml
│   │   │   │   ├── activity_login.xml
│   │   │   │   ├── ... (다른 레이아웃 파일)
│   │   │   │
│   │   │   ├── values/
│   │   │   ├── drawable/
│   │   │   ├── mipmap/
│   │   │   ├── ...
│   │   │   
│   ├── androidTest/
│   ├── test/
│
├── .gitignore
├── build.gradle
├── ...
```