package helper

class TestUtils {

    static setField(Object target, String fieldName, Object value) {
        def field = target.getClass().getDeclaredField(fieldName)
        field.setAccessible(true)
        field.set(target, value)
    }
}
