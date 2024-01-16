package booking.security.mapper;

public interface Mapper<D, E> {

    D mapToDto(E entity);

    default E mapToEntity(D dto) {
        return null;
    }
}