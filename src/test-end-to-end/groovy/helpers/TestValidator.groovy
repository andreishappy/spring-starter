package helpers
import cucumber.api.DataTable

class TestValidator {
    static def objectContainsProperties(Map object, DataTable dataTable) {
        def map = dataTable.asMap(String, String)
        map.each { key, value ->
            def valueUnderTest = object.get(key)
            if (value.startsWith('#size:')) {
                assert valueUnderTest.size() == (value - '#size:').toInteger()
                return;
            }
            assert object.get(key).toString() == value
        }
    }
}
