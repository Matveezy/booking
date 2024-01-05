package booking.common.mapper;

public interface Mapper<F, T> {

    T map(F object);

    default T map(F fromObject, T toObject) {
        return toObject;
    } // метод для копирования полей сущности без создания новой
}
