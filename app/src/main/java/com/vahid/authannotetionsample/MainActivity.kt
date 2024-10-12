package com.vahid.authannotetionsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vahid.authannotetionsample.ui.theme.AuthAnnotetionSampleTheme
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class MainActivity : ComponentActivity() {
    private val api by lazy {
        Retrofit.Builder()
            .baseUrl("")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor())
                    .addInterceptor(
                        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                    )
                    .build()
            )
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(MyApi::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = User(
            "vahid","2002-04-02"
        )
        enableEdgeToEdge()
        setContent {
            AuthAnnotetionSampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

data class User(
    val name: String,
    @AllowedRegex("\\d{4}-\\d{2}-\\d{2}") val birthday: String
) {
    init {
        val fields = this::class.java.declaredFields
        fields.forEach { field ->
            field.annotations.forEach { annotation ->
                if (field.isAnnotationPresent(AllowedRegex::class.java)) {
                    val regex = field.getAnnotation(AllowedRegex::class.java)?.regex
                    if (regex?.toRegex()?.matches(birthday) == false) {
                        throw IllegalArgumentException("Invalid birthday format")
                    }
                }
            }
        }
    }
}

@Target(AnnotationTarget.FILE, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
annotation class AllowedRegex(val regex: String)