# GreenMarket

### 0. Dependencies
- lifecycle-runtime v2.4.0
- firebase-bom v32.4.0
- firebase-analytics ktx
- firebase-auth ktx
- firebase-firestore ktx
- firebase-storage ktx
- glide v4.12.0
- glide-compiler v4.12.0
- dokka v1.9.10

### 1. Overall Structure
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

### 2. Documentation
프로젝트 내의 [**docs/html/index.html**]을 확인하시면 dokka 라이브러리를 이용하여 만든 문서를 확인하실 수 있습니다.   
오른쪽 위 버튼을 통해 프론트팀에서 구현한 클래스, 백엔드팀에서 구현한 클래스를 구분하여 확인하실 수 있고,   
클래스명이나 함수명을 클릭 시 해당 클래스나 함수의 설명이 나오고, 각 인자값이 어떤 값을 나타내는 지 한국어로 확인할 수 있습니다.   
해당 문서 기능을 이용하여 프론트팀과 백엔드팀의 커뮤니케이션을 더 원활이 할 수 있었습니다.   

### 3. Install
*Release의 apk파일을 다운로드하여 안드로이드 운영체제 스마트폰에 설치하시면 사용 가능합니다.*

### 4. Usage
> -**회원 기능**-
> > **로그인**: 어플을 실행하면 맨 처음 로그인 페이지가 나옵니다.   
> > 가입한 이메일과 비밀번호를 넣고 [로그인] 버튼을 누르면 홈 화면으로 이동합니다.
> 
> > **회원가입**: 로그인 화면에서 [회원가입] 버튼을 누르면 회원가입 창이 나옵니다.   
> > 이메일, 비밀번호, 이름, 생일을 입력 후 회원가입을 완료하면 가입이 완료됩니다.   
> > 하나라도 입력되지 않은 값이 있을 시 진행 불가능합니다.   
> 
> > **로그아웃**: 마이페이지 화면에서 [로그아웃]버튼을 클릭하면 로그아웃이 되며 로그인 페이지로 이동합니다.

> -**상품 기능**-
> >  **상품 추가**: 상품 목록이 나오는 메인 화면에서 [상품 추가] 버튼을 누르면 상품 추가가 가능합니다.
> > 상품 이름, 이미지, 가격, 상세내용을 입력하면 추가를 할 수 있습니다.   
> > 하나라도 입력되지 않은 값이 있을 시 진행 불가능합니다.
> 
> > **상품 조회**: 홈 페이지에서 상품 목록들을 조회할 수 있습니다.   
> > 위의 검색창에서 원하는 키워드를 입력하고 [검색]버튼을 눌러 검색도 가능합니다.   
> > [필터 버튼]을 눌러서 판매중, 예약중, 판매 완료 상품들 중 원하는 상태의 상품만 필터링하여 조회도 가능합니다.   
> > 목록에서 자세히 보고 싶은 상품을 클릭하면 해당 상품의 이름과 가격, 이미지, 상세 정보, 작성자, 상품 상태를 볼 수 있는 상품 상세정보 페이지로 이동합니다.   
> 
> > **상품 수정**: 마이페이지 화면에서 [내가 쓴 글] 페이지로 들어갑니다.   
> > 수정하고 싶은 상품을 선택하고 작성한 내용이나 이미지를 수정합니다.   
> > 상품이 예약 또는 판매 등 판매중 상태에서 변경되었다면 해당 페이지에서 수정 가능합니다.   
> > 하나라도 입력되지 않은 값이 있을 시 진행 불가능합니다.
> 
> > **상품 삭제**: 마이페이지 화면에서 [내가 쓴 글] 페이지로 들어갑니다.
> > 삭제하고 싶은 상품을 선택하고 제일 아래 삭제 버튼을 클릭합니다.

> -**채팅 기능**-
> > **채팅 시작**: 홈 페이지에서 구입을 원하는 상품을 클릭합니다.   
> > [채팅하기] 버튼을 클릭하여 해당 상품을 작성한 사용자와의 채팅을 시작할 수 있습니다.   
> > 아래 네비게이션에서 채팅탭으로 가면 추가된 채팅방을 확인하고 채팅을 이어서 하실 수 있습니다.   
> > 채팅하기 버튼을 눌렀을 때 이미 채팅방이 있는 사용자였다면 해당 상대방과 채팅했던 이전 채팅내역에서 이어서 채팅할 수 있습니다.   
> > 위의 경우에선 새로운 채팅방이 생기진 않습니다.
> 
> > **채팅 진행**: 채팅방을 생성하게 되면 바로 상대방과 실시간 채팅이 가능합니다.   
> > 채팅방을 나간 후 이어서 채팅을 진행하려면 채팅 탭에서 해당 상대방의 이름으로 되어있는 채팅방을 들어가면 이어서 채팅이 가능합니다.   
> > 알림기능, 읽지않은 메시지 카운팅, 채팅방 나가기 기능은 미구현하였습니다.   
