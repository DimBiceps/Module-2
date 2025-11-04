package module26.userservice.service;

public class NotFoundException extends RuntimeException {
  public NotFoundException(String m) {
    super(m);
  }
}
