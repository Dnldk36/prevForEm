let questionCount = 0;

function addQuestion() {
    questionCount++;

    const questionsContainer = document.getElementById('questions-container');
    const questionDiv = document.createElement('div');
    questionDiv.className = 'question';

    const questionLabel = document.createElement('label');
    questionLabel.innerText = `Вопрос ${questionCount}: `;
    const questionInput = document.createElement('input');
    questionInput.type = 'text';
    questionInput.name = `question${questionCount}`;
    questionInput.placeholder = 'Введите вопрос';
    questionInput.required = true;

    const answersContainer = document.createElement('div');
    answersContainer.id = `answers-container-${questionCount}`;

    const addAnswerButton = document.createElement('button');
    addAnswerButton.innerText = 'Добавить вариант ответа';
    addAnswerButton.onclick = function () {
        addAnswer(questionCount);
    };

    questionDiv.appendChild(questionLabel);
    questionDiv.appendChild(questionInput);
    questionDiv.appendChild(answersContainer);
    questionDiv.appendChild(addAnswerButton);

    questionsContainer.appendChild(questionDiv);

    // Добавляем первый вопрос
    addAnswer(questionCount);
}

function addAnswer(questionNumber) {
    const answersContainer = document.getElementById(`answers-container-${questionNumber}`);
    const answerDiv = document.createElement('div');
    answerDiv.className = 'answer';

    const answerLabel = document.createElement('label');
    answerLabel.innerText = `Вариант ответа: `;
    const answerInput = document.createElement('input');
    answerInput.type = 'text';
    answerInput.name = `answer${questionNumber}[]`;
    answerInput.placeholder = 'Введите вариант ответа';
    answerInput.required = true;

    const correctAnswerLabel = document.createElement('label');
    correctAnswerLabel.innerText = 'Правильный ответ: ';
    const correctAnswerInput = document.createElement('input');
    correctAnswerInput.type = 'checkbox';
    correctAnswerInput.name = `correctAnswer${questionNumber}`;

    answerDiv.appendChild(answerLabel);
    answerDiv.appendChild(answerInput);
    answerDiv.appendChild(correctAnswerLabel);
    answerDiv.appendChild(correctAnswerInput);

    answersContainer.appendChild(answerDiv);
}

function submitTest() {
    const testName = document.getElementById('testName').value;

    const questions = document.querySelectorAll('.question');
    const testObject = {
        testTitle: testName,
        questions: []
    };

    questions.forEach((question, index) => {
        const questionInput = question.querySelector('input[name^="question"]');
        const answerInputs = question.querySelectorAll('input[name^="answer"]');
        const correctAnswerInputs = question.querySelectorAll('input[name^="correctAnswer"]');

        const answers = [];
        answerInputs.forEach(answerInput => {
            answers.push(answerInput.value);
        });

        const correctAnswerInput = Array.from(correctAnswerInputs).find(input => input.checked);
        const correctAnswerText = correctAnswerInput ? correctAnswerInput.previousSibling.previousSibling.value : '';

        const questionObject = {
            questionText: questionInput.value,
            answers: answers,
            correctAnswerText: correctAnswerText
        };

        testObject.questions.push(questionObject);
    });

    // Отправляем данные на сервер
    fetch('/test/submit-test', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(testObject)
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            window.location.href='http://localhost:8081/test'
        })
        .catch(error => {
            console.error('Ошибка отправки данных:', error);
        });
    window.location.href='http://localhost:8081/test'
}