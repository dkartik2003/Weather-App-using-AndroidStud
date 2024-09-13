package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText cityInput;
    private TextView resultText;
    private Button getWeatherButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityInput = findViewById(R.id.cityInput);
        resultText = findViewById(R.id.resultText);
        getWeatherButton = findViewById(R.id.getWeatherButton);

        getWeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = cityInput.getText().toString().trim();
                if (!city.isEmpty()) {
                    getWeather(city);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a city", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getWeather(String city) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<WeatherResponse> call = apiService.getWeather(city, "your_api_key_here", "metric");
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weatherResponse = response.body();
                    if (weatherResponse != null) {
                        String weatherData = "City: " + weatherResponse.getName() + "\n"
                                + "Temperature: " + weatherResponse.getMain().getTemp() + "°C\n"
                                + "Description: " + weatherResponse.getWeather().get(0).getDescription();
                        resultText.setText(weatherData);
                    }
                } else {
                    resultText.setText("Error fetching data");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                resultText.setText("Error: " + t.getMessage());
            }
        });
    }
}
