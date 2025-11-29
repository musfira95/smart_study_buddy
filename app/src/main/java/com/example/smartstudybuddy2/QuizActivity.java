// QuizActivity.java
package com.example.smartstudybuddy2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    TextView tvQuestion;
    RadioGroup rgOptions;
    RadioButton optionA, optionB, optionC, optionD;
    Button btnNext, btnSubmit;

    ArrayList<QuizQuestion> questions = new ArrayList<>();
    int currentIndex = 0;
    int correct = 0;
    int wrong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvQuestion = findViewById(R.id.tvQuestion);
        rgOptions = findViewById(R.id.rgOptions);
        optionA = findViewById(R.id.optionA);
        optionB = findViewById(R.id.optionB);
        optionC = findViewById(R.id.optionC);
        optionD = findViewById(R.id.optionD);
        btnNext = findViewById(R.id.btnNext);
        btnSubmit = findViewById(R.id.btnSubmit);

        loadDummyQuestions();
        showQuestion();

        btnNext.setOnClickListener(v -> {
            if (rgOptions.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Please select an option!", Toast.LENGTH_SHORT).show();
                return;
            }

            checkAnswer();
            currentIndex++;
            rgOptions.clearCheck();

            if (currentIndex < questions.size()) {
                showQuestion();
            } else {
                btnNext.setVisibility(Button.GONE);
                btnSubmit.setVisibility(Button.VISIBLE);
            }
        });

        btnSubmit.setOnClickListener(v -> {
            Intent intent = new Intent(QuizActivity.this, QuizResultActivity.class);
            intent.putExtra("correctCount", correct);
            intent.putExtra("wrongCount", wrong);
            startActivity(intent);
            finish();
        });
    }

    private void showQuestion() {
        QuizQuestion q = questions.get(currentIndex);
        tvQuestion.setText(q.getQuestion());
        optionA.setText(q.getOptionA());
        optionB.setText(q.getOptionB());
        optionC.setText(q.getOptionC());
        optionD.setText(q.getOptionD());
    }

    private void checkAnswer() {
        QuizQuestion q = questions.get(currentIndex);
        RadioButton selected = findViewById(rgOptions.getCheckedRadioButtonId());
        String selectedText = selected.getText().toString();

        if (selectedText.equals(q.getAnswer())) correct++;
        else wrong++;
    }

    private void loadDummyQuestions() {
        questions.add(new QuizQuestion(
                "What is AI?",
                "Artificial Input",
                "Automatic Intelligence",
                "Artificial Intelligence",
                "Auto Internet",
                "Artificial Intelligence"
        ));

        questions.add(new QuizQuestion(
                "Which one is a programming language?",
                "HTML",
                "CSS",
                "Java",
                "Photoshop",
                "Java"
        ));
    }
}
