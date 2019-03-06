package com.rhinhospital.rnd.rhiot.RetrofitAPI;


import com.rhinhospital.rnd.rhiot.Model.BloodPressure;
import com.rhinhospital.rnd.rhiot.Model.Nurse;
import com.rhinhospital.rnd.rhiot.Result.Department;
import com.rhinhospital.rnd.rhiot.Result.Login;
import com.rhinhospital.rnd.rhiot.Result.MeasurementHistory;
import com.rhinhospital.rnd.rhiot.Result.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface IRetrofitService {
    // 아래부터는 예제 코드로 실제로 동작하는 코드는 아닙니다.

    /**
     * GET, POST, PUT, PATCH, DELETE에 대한 설명
     * 참고 사이트
     * 1. https://altongmon.tistory.com/745
     * 2. https://meetup.toast.com/posts/92
     * 3. https://futurestud.io/tutorials/retrofit-2-add-multiple-query-parameter-with-querymap
     */

    /**
     * GET: 해당 리소스를 조회할 때 사용함. 리소스를 조회하고 해당 도큐먼트에 대한 자세한 정보를 가져온다  ex) 게시물 조회, 단, 로그인은 제외
     * POST: 해당 URI를 요청하면 리소스를 생성한다. ex) 회원가입, 게시판 조회
     * PUT: 해당 리소스를 수정하거나 추가할 때 사용한다.
     * PATCH: 해당 리소스를 오로지 수정할때만 사용한다.
     * DELETE: 해당 리소스를 삭제한다.
     */

    /**
     * Path vs Query 어노테이션 선택 어떤걸?
     * https://stackoverflow.com/questions/37698501/retrofit-2-path-vs-query
     */

    /**
     * GET 방식, 주소는 URL/board/boardName 호출.
     * Data Type의 JSON을 통신을 통해 받음
     * @Path("boardName") String boardName: boardName 로 들어간 String 값을, 첫 줄에 말한 {boardName}에 넘겨줌.
     * boardName에 "qna"가 들어가면 "http://jsonplaceholder,typicode.com/board/pna"가 최종 호출 주소.
     * @param  boardName 요청에 필요한 boardName
     * @return Data 객체를 JSON 형태로 반환.
     * 예제 코드에는 게시판 예제가 아니라 사용자 userId를 검색하는 코드인데 @Path 이노테이션의 용도와는 어울리지 않는 것 같아 적절한 게시판 예제로 변경함.
     */
    @GET("/board/{boardName}")
    Call<ExampleData> getData(@Path("boardName") String boardName);

    /**
     * 다수의 ExampleData 객체를 JSON 형태로 반환받고 싶은 경우
     * GET 방식, 위의 예제와 같이 URL/board/{boardName} 호출
     */
    @GET("board/{boardName}")
    Call<List<ExampleData>> getData2(@Path("boardName") String boardName);

    /**
     * 파라미터를 전송하고 싶은 경우
     * GET 방식
     * @Query("userId") String id
     * @Query("password") String password
     * getData1()과 getData2랑 다르게 뒤에 붙는 도메인 없음.
     * 단, 주소값이 "http://jsonplaceholder.typicode.com/posts?userId=1"이 됨.
     * Data Type의 여러 개의 JSON 통신을 통해 받음.
     * @param id 요청에 필요한 userId
     * @return 다수의 Data 객체를 JSON 형태로 반환
     * getData 에서 boardName을 호출할 때는 Call<List<Example>>로 반환받는게 맞음
     */
    @GET("/checkId")
    Call<ExampleData> checkId(@Query("userId") String id);

    /**
     * 파라미터를 여러개 전송하고 싶은 경우
     * @param id
     * @param pw
     * @return ExampleData 객체를 JSON으로 받음
     */
    @GET("/login1")
    Call<ExampleData> login1(@Query("userId") String id, @Query("password") String pw);

    /**
     * GET방식에서 메소드 파라미터가 많은 경우 POST 방식에서 FieldMap을 사용했던 것과 마찬가지로
     * GET방식에서는 QueryMap을 사용하여 HashMap 객체를 통해 데이터를 전달할 수 있다.
     */
    @GET("/login2")
    Call<ExampleData> login2(@QueryMap HashMap<String, Object> login_info);

    /**
     * POST 방식
     * @FieldMap HashMap <String, Object> param : HashMap 구조를 알아야 이해가 편함
     * Field 형식을 통해 넘겨주는 값들이 여러 개일 때 FieldMap을 사용함
     * Retrofit에서는 Map보다는 HashMap을 권장함
     * @FormUrlEncoded Feild 형식 사용 시 Form 이 Encording 되어야 하기 때문에 사용하는 Field 어노테이션은 POST 방식에서만 사용할 수 있다.
     * @param param 요청에 필요한 값들
     * @return ExampleData 객체를 JSON 형식으로 받음
     */
    @FormUrlEncoded
    @POST("/post_login")
    Call<ExampleData> post_login1(@FieldMap HashMap<String, Object> param);


    /**
     * PUT 방식
     * PUT 방식 안드로이드에서 JSON파일을 만들어서 서버로 전송하는 방법을 제공한다.
     * @Body 어노테이션을 사용하여 JSON파일을 String 타입으로 받아서 서버에 전송.
     * 메소드 호출 시 JSONObject 클래스를 사용하여 메소드 파라미터로 넘긴다.
     * 참고 사이트: https://code.i-harness.com/ko-kr/q/1468446
     */
    @PUT("/join")
    Call<ExampleData> putData(@Body ExampleData param);

    /**
     * PATCH 방식
     * @Field("title") String title: patch 방식을 통해서 title에 해당하는 값을 넘기기 위해 사용
     * @FormUrlEncoded Field 형식 사용 시 Form 이 Encoding 되어야 하기 때문에 사용하는 어노테이션
     * @param title
     * @return
     */
    @FormUrlEncoded
    @PATCH("/nurse-info-change/1")
    Call<ExampleData> patchData(@Field("title") String title);

    /**
     * DELETE 방식
     * ResponseBody는 통신을 통해 되돌려 받는 값이 없을 경우 사용
     * @return
     */
    @DELETE("/board/{id}")
    Call<ResponseBody> deleteData(@Path("id") String id);

    /**
     * DELETE 방식에서 Body 어노테이션을 사용하고 싶은 경우
     */
    @HTTP(method = "DELETE", path="/board", hasBody = true)
    Call<ResponseBody> delete(@Body ExampleData parameters);

    // -----------------여기까지 예제 코드--------------------------

    /**
     * 기타 오류
     * 1. POST로 데이터 전송 시 안되는 문제점
     *    GET으로 데이터를 전송하면 정상적으로 서버에서 받는데 POST로 전송하면 서버에서 데이터가 비어있는 상태로 받게된다.
     *    인코딩 문제로 예상됨
     *    참고사이트 :http://www.todayhumor.co.kr/board/view.php?table=programmer&no=20460&s_no=20460&page=9
     */

    /**
     * 간호사 등록(nurse regist)
     * 현재 POST 방식으로 넘기면 데이터가 정상적으로 서버로 전송이 안된다......
     *
     */
    @FormUrlEncoded
    @POST("join-nurse")
    Call<ResponseBody> join_nurse(@FieldMap HashMap<String, Object> nurse_info);

    /**
     * 로그인(login)
     * url -> http://106.10.33.63/login?emp_no='$emp_no'&password='$password'
     *
     */
    @GET("login")
    Call<Login> login(@Query("emp_no") String emp_no, @Query("password") String password);

    @GET("getDepartmentList")
    Call<Department> getDepartmentList();

    @GET("getPositionList")
    Call<Position> getPositionList();

    @GET("getNurseInfo")
    Call<Nurse> getNurseInfo(@Query("emp_no") String emp_no);

    @GET("")
    Call<BloodPressure> setBloodPressureMeasure(@Query("emp_no") String emp_no, @Query("patient_no") String patient_no,
                                                @Query("maximal_blood_pressure") int maximal_blood_pressure, @Query("minimal_blood_pressure") int minimal_blood_pressure,
                                                @Query("heart_per_rate") int heart_per_rate);

    @GET("getPatientMeasurementHistory")
    Call<ArrayList<MeasurementHistory>> getMeasurementHistory(@Query("patient_no") String patient_no, @Query("page") int page);
}
