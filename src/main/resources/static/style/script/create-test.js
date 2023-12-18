let questionCount = 1;

function addQuestion(){
    questionCount++;



    // блок create-question
    let createQuestion=document.createElement('div');
    createQuestion.className='create-question';

    let questionNumber=document.createElement('div');
    questionNumber.className='number';
    questionNumber.innerText=`${questionCount}.`;

    let questionInput=document.createElement('div');
    questionInput.className='question-input';

    let questionTextarea = document.createElement('textarea');
    questionTextarea.className='main-input';
    questionTextarea.placeholder='Question';
    questionTextarea.name=`question${questionCount}`;
    questionTextarea.id=`question-${questionCount}`;
    questionTextarea.rows='3';

    createQuestion.appendChild(questionNumber);
    createQuestion.appendChild(questionInput);
    questionInput.appendChild(questionTextarea);

    //блок create-test-answer
    let createAnswers=document.createElement('div');
    createAnswers.className='create-test-answer';

    let chooseAnswer=document.createElement('div');
    chooseAnswer.className='choose-answer';
    chooseAnswer.id=`answers-${questionCount}`;

    let answer1=document.createElement('div');
    answer1.className='answer-item';

    let radio1=document.createElement('input');
    radio1.type='radio';
    radio1.name=`correct-answer-${questionCount}`;
    radio1.id=`radio-${questionCount}-1`;

    let answerLabel1=document.createElement('label');
    answerLabel1.setAttribute('for', `radio-${questionCount}-1`);

    let inputContainer1=document.createElement('div');
    inputContainer1.className='inputs validate-input';

    let inputContainer2=document.createElement('div');
    inputContainer2.className='inputs validate-input';

    let input1=document.createElement('input');
    input1.type='text';
    input1.name=`question${questionCount}`
    input1.className='main-input create-test';
    input1.placeholder='Answer';

    let inputSpan1 = document.createElement('span');
    inputSpan1.className='focus-main-input create-test';

    let input2=document.createElement('input');
    input2.type='text';
    input2.name=`question${questionCount}`
    input2.className='main-input create-test';
    input2.placeholder='Answer';

    let inputSpan2 = document.createElement('span');
    inputSpan2.className='focus-main-input create-test';

    let answer2=document.createElement('div');
    answer2.className='answer-item';

    let radio2=document.createElement('input');
    radio2.type='radio';
    radio2.name=`correct-answer-${questionCount}`;
    radio2.id=`radio-${questionCount}-2`;

    let answerLabel2=document.createElement('label');
    answerLabel2.setAttribute('for', `radio-${questionCount}-2`);

    let addAnswerButton=document.createElement('div');
    addAnswerButton.className='add add-answer';
    addAnswerButton.id='add-answer';
    addAnswerButton.innerText='+ Add answer';
    addAnswerButton.onclick = function () {
        addAnswer(questionCount);
    };

    let deleteQuestion=document.createElement('div');
    deleteQuestion.innerText='Delete';
    deleteQuestion.className='delete-answer';
    deleteQuestion.id=`${questionCount}`;

    createAnswers.appendChild(chooseAnswer);

    answer1.appendChild(radio1);
    answer1.appendChild(answerLabel1);
    inputContainer1.appendChild(input1);
    inputContainer1.appendChild(inputSpan1);
    answer1.appendChild(inputContainer1);

    answer2.appendChild(radio2);
    answer2.appendChild(answerLabel2);
    inputContainer2.appendChild(input2);
    inputContainer2.appendChild(inputSpan2);
    answer2.appendChild(inputContainer2)

    chooseAnswer.appendChild(answer1);
    chooseAnswer.appendChild(answer2);

    // блок create-tets-container
    let questionContainer = document.createElement('div');
    questionContainer.className='create-test-container pannel center';
    questionContainer.id=`question-container-${questionCount}`;
    questionContainer.appendChild(createQuestion);
    questionContainer.appendChild(createAnswers);
    questionContainer.appendChild(addAnswerButton)
    questionContainer.appendChild(deleteQuestion);

    let mainContainer=document.getElementById('main-container');
    mainContainer.appendChild(questionContainer);

    // alert(questionCount);
}

function answerCounter(id){
    let answersContainer=document.getElementById(id);
    let answers = answersContainer.getElementsByClassName('answer-item');
    let count = answers.length;
    return count;
}

function addAnswer(questionNumber){
    let answerContainer=document.getElementById(`answers-${questionNumber}`);
    answerContainer.id=(`answers-${questionNumber}`);

    let answersCount = answerCounter(answerContainer.id);

    let answer = document.createElement('div');
    answer.className='answer-item';

    let radio=document.createElement('input');
    radio.type='radio';
    radio.name=`correct-answer-${questionNumber}`;
    radio.id=`answer-${questionNumber}-${answersCount+1}`;

    let inputContainer=document.createElement('div');
    inputContainer.className='inputs validate-input';

    let input=document.createElement('input');
    input.type='text';
    input.className='main-input create-test';
    input.name=`answer${questionNumber}`
    input.placeholder='Answer';

    let inputSpan = document.createElement('span');
    inputSpan.className='focus-main-input create-test';

    let answerLabel=document.createElement('label');
    answerLabel.setAttribute('for', `radio-${questionCount}-${answersCount+1}`);

    inputContainer.appendChild(input);
    inputContainer.appendChild(inputSpan);
    answerLabel.appendChild(inputContainer);
    answer.appendChild(radio);
    answer.appendChild(answerLabel);

    answerContainer.appendChild(answer);
}

function submitTest(){
    let testName = document.getElementById('test-name');

    let questions = document.querySelectorAll('.create-question');
    let testObject = {
        testTitle: testName,
        questions: []
    };

    questions.forEach((question, index) => {
        let  questionInput=question.querySelector('textarea[name^="question-"]');
        let answerInputs = question.querySelectorAll('input[name^="answer-"');
        let correctAnswerInputs=question.querySelectorAll('input[name^="correct-answer-"]');

        let answers=[];
        answerInputs.forEach(answerInput =>{
            answers.push(answerInput.value);
        });

        let correctAnswerInput = Array.from(correctAnswerInputs);
        let correctAnswerText = correctAnswerInput ? correctAnswerInput.previousSibling.previousSibling.value : '';

        let questionObject = {
            questiontext: questionInput.value,
            answers: answers,
            correctAnswertext: correctAnswerText
        };

        testObject.questions.push(questionObject);
        console.log(testObject);
    });




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
}
