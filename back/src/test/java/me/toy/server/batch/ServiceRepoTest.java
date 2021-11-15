package me.toy.server.batch;


import java.util.ArrayList;
import java.util.List;
import me.toy.server.entity.Location;
import me.toy.server.repository.LocationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ServiceRepoTest {

  @Autowired
  LocationRepository locationRepository;

  @Test
  @DisplayName("단일 트랜잭션 단일 Insert Test")
  public void serviceInsertTest() throws Exception {

    List<Location> list = new ArrayList<>();
    for (int i = 0; i < 10000; i++) {
      Location location = Location.builder()
          .name("test" + i)
          .photoUrl(null)
          .posx(123L)
          .posy(12L)
          .text("teetetetet")
          .build();

      locationRepository.save(location);
    }

  }

  @Test
  @DisplayName("단일 트랜잭션 Batch Insert Test")
  public void serviceBulkInsertTest() throws Exception {
    List<Location> list = new ArrayList<>();
    for (int i = 0; i < 10000; i++) {
      Location location = Location.builder()
          .name("test" + i)
          .photoUrl(null)
          .posx(123L)
          .posy(12L)
          .text("teetetetet")
          .build();

      list.add(location);
    }

    locationRepository.saveAll(list);
  }
}
