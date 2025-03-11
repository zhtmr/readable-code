package cleancode.studycafe.tobe.model;

import java.util.List;
import java.util.Optional;

public class StudyCafeLockerPasses {

  private final List<StudyCafeLockerPass> lockerPasses;

  public StudyCafeLockerPasses(List<StudyCafeLockerPass> lockerPasses) {
    this.lockerPasses = lockerPasses;
  }

  public static StudyCafeLockerPasses of(List<StudyCafeLockerPass> passes) {
    return new StudyCafeLockerPasses(passes);
  }

  public Optional<StudyCafeLockerPass> findLockerPassBy(StudyCafePass pass) {
    return lockerPasses
      .stream()
      .filter(pass::isSameDurationType)
      .findFirst();
  }
}
