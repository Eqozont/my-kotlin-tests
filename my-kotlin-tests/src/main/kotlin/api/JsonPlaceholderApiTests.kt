import java.net.URL
import java.net.HttpURLConnection
import org.json.JSONObject

fun sendRequest(url: String, method: String, requestBody: String? = null): Any {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.requestMethod = method
    if (requestBody != null) {
        connection.doOutput = true
        connection.outputStream.write(requestBody.toByteArray())
    }
    connection.connect()

    val responseCode = connection.responseCode
    if (method == "GET" && responseCode != 200) {
        throw Exception("Response code: $responseCode")
    } else if (method == "POST" && responseCode != 201) {
        throw Exception("Response code: $responseCode")
    }

    val responseBody = connection.inputStream.bufferedReader().readText()
    if (responseBody.isNotEmpty()) {
        if (responseBody.startsWith("[") || responseBody.startsWith("{")) {
            try {
                return JSONObject(responseBody)
            } catch (e: Exception) {
                return responseBody
            }
        } else {
            return responseBody
        }
    } else {
        throw Exception("Ответ сервера пуст")
    }
}

fun testGetPosts() {
    val url = "https://jsonplaceholder.typicode.com/posts"
    val response = sendRequest(url, "GET")

    if (response is JSONObject) {
        assert(response.length() > 0)
    } else {
        println("Ответ сервера не является JSON-объектом")
    }
    println("Тест 1 пройден: получен список постов")
}

fun testGetPost() {
    val url = "https://jsonplaceholder.typicode.com/posts/1"
    val response = sendRequest(url, "GET")

    if (response is JSONObject) {
        assert(response.getInt("id") == 1)
        assert(response.getString("title") != "")
        assert(response.getString("body") != "")
    } else {
        println("Ответ сервера не является JSON-объектом")
    }
    println("Тест 2 пройден: получен конкретный пост")
}

fun testCreatePost() {
    val url = "https://jsonplaceholder.typicode.com/posts"
    val requestBody = "{\"title\":\"New Post\",\"body\":\"This is a new post\"}"
    val response = sendRequest(url, "POST", requestBody)

    println("Тест 3 пройден: создан новый пост")
}

fun main() {
    try {
        testGetPosts()
        testGetPost()
        testCreatePost()
    } catch (e: Exception) {
        println("Ошибка: ${e.message}")
    }
}