package com.example.demo.testcase;

import static com.example.demo.testdata.TestData.getPetJsonString;
import static io.restassured.RestAssured.given;

import com.example.demo.MockServerTestBase;
import com.example.demo.common.annotations.Duration;
import com.example.demo.common.enums.AuthHeader;
import com.example.demo.common.utils.LifecycleLogger;
import com.example.demo.expecations.AddPet;
import com.example.demo.model.ModelApiResponse;
import com.example.demo.model.Pet;
import com.example.demo.model.Pet.StatusEnum;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("MockServerAddPetTest")
@Duration
@ExtendWith(AddPet.class)
class MockServerAddPetTest extends MockServerTestBase implements LifecycleLogger {

  private static final String API_PATH = MOCK_SERVER_URL + "/pet";

  @Test
  void testAddPetStatusCode200() throws Exception {
    Pet pet =
        given()
            .log()
            .all()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(AuthHeader.OK_200.header())
            .body(getPetJsonString(StatusEnum.AVAILABLE))
            .then()
            .log()
            .all()
            .expect()
            .statusCode(200)
            .when()
            .post(API_PATH)
            .as(Pet.class);
    Assertions.assertThat(pet.getId()).isNotNull();
  }

  @Test
  void testAddPetStatusCode405() throws Exception {
    ModelApiResponse rsp =
        given()
            .log()
            .all()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .header(AuthHeader.METHOD_NOT_ALLOWED_405.header())
            .body(getPetJsonString(StatusEnum.AVAILABLE))
            .then()
            .log()
            .all()
            .expect()
            .statusCode(405)
            .when()
            .post(API_PATH)
            .as(ModelApiResponse.class);
    Assertions.assertThat(rsp.getMessage()).isEqualTo("Invalid input");
  }
}