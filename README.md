# SiksaGo

식단 관리 플래너 앱



## 프로젝트 개요
요즘 시대에 건강을 중요시 하는 사람이 많아지면서 식단을 관리하려고 하는 사람이 많아졌다. 

(사진1.1)에서 건강관리 중요성 인식에 대하여 중요하다고 생각하는 사람이 89.2%정도이다. 또한 (사진1.2)를 보았을때 건강관리를 하는 사람들 중 '정기적인 운동'과 '정기적인 건강검진' 다음으로 식습관 개선을 통하여 건강관리를 하는 사람이 많다.

이는 식단 관리하는 앱이 필요한 이유이다. 

스마트폰이 일상이 된 현재 식단관리를 통하여 건강관리를 하려는 사람들이 많아지자 식단을 관리하는 앱의 수요가 늘어났다.

건강관리를 중요하게 생각하는 나머지 이러한 오해를 하는 사람도 있다. 

건강을 챙기기 위하여 잡곡밥을 섭취하는 사람들이 많은데 실제로는 잡곡밥이 흰쌀밥보다 칼로리가 더 높다. 

100g 기준으로 쌀밥은 121kcal인데 잡곡밥 100g은 131kcal이다. 즉 단순하게 목적이 다이어트인 사람에게는 잘못된 식단일 수도 있다. 

그렇다고 흰쌀밥이 잡곡밥보다 더 좋은 식단이라고 할 수 없다. 

잡곡밥의 영양소가 흰쌀밥의 영양소에 비해 훨씬 풍부하기 때문이다. 

따라서 건강을 목적으로 하는 사람들 대부분에게는 잡곡밥이 더 좋은 식단이다. 

우리는 정확한 영양분과 칼로리 표기로 이러한 오해를 풀어주면서 사용자가 직접하는 식단관리를 통하여 건강관리를 하려는 사람들에게 도움을 주고자 이 앱을 만들기로 하였다. 

사람들이 먹은 음식을 입력하면 kcal와 영양소를 계산하여 본인에게 더 필요한 영양소와 과하게 섭취한 영양소를 알려줌으로써 더 효율적인 관리를 유도하고자 한다.


## 기능 구현 방식
기능1. 로그인 및 회원가입 기능

![image](https://github.com/babychoomp/SiksaGo/assets/126652551/9ee9b1f7-1fde-475f-8a2b-040aae31059a)

기능2. 식단 검색, 식단 추가, 식단 삭제, 식단 출력 구현 

![image](https://github.com/babychoomp/SiksaGo/assets/126652551/4b8d5027-e269-4b9b-a55b-7c5cd44a053b)
![image](https://github.com/babychoomp/SiksaGo/assets/126652551/6f160a91-4b92-4937-b1cd-d8c175fdf182)


기능3. 사진으로 음식 예측 구현 

![image](https://github.com/babychoomp/SiksaGo/assets/126652551/17f52fa6-8bd7-451f-9281-c61390b44ad1)
![image](https://github.com/babychoomp/SiksaGo/assets/126652551/ef844948-7675-440d-9fe9-3fe3e8926bed)


기능4. 마이페이지 관리 기능

![image](https://github.com/babychoomp/SiksaGo/assets/126652551/4d4982dd-c7b7-45ba-b630-f360fdc52b52)


기능 5. 메모 기능

![image](https://github.com/babychoomp/SiksaGo/assets/126652551/364c8348-ce2b-44aa-9777-1c501d03b7c2)


기능 6. 식사고 데이터베이스

![image](https://github.com/babychoomp/SiksaGo/assets/126652551/a0c64537-766a-4da5-911c-590f1b241853)



## 유지보수 및 개선사항

유지 및 보수 하는 이유

소프트웨어가 운영될수록 일부 시스템이 변경을 요구하는 상황이 생기고 이는 앱의 수명에 영향을 
미친다. 식단 관리 앱 같은 경우 유지 및 보수가 부실하여 일부 사용자들이 다른 식단 관리 앱으로 
옮기는 경우가 있는데 이를 방지하고 사용자의 요구사항을 지속적으로 만족시켜야한다. 그러므로 
유지 및 보수가 필요하다.

유지 및 보수 유형

- 결함

앱을 출시하고 나서 발생할 수 있는 결함 또는 버그를 수정해야한다.
결함 또는 버그가 지속될 경우 사용자의 만족도가 떨어질 수 있으며 앱의 수명을 저하시킬 수 있다.

- 기능 추가

새로운 기능을 추가하여 사용자에게 더 좋은 환경을 제공한다.

- 기능 변경

기능의 일부 또는 기능의 전체를 변경하고자 할때 기존의 앱과의 차이를 비교해야한다.

- 제약환경의 변경

외부환경에 의해 제약되어 있던 사항이 변경되어 나은 사항이 되거나 그 반대의 
경우 일부기능을 제한, 변경, 추가해야한다.


- 개선사항

h5 모델을 안드로이드에서 구현하기 위해 Tflite로 형식을 바꿔서 집어넣었는데 거기서 정확도가 감소한걸로 예측되는 상황이라 추후 서버에 예측 모델을 연동해서 앱이랑 서버간에 통신으로 바꿔서 개선할 예정
