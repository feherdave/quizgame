// Functions for the play page

const submitButton = document.getElementById('quiz-play-submit');
const nextButton = document.getElementById('quiz-play-next');
const resultDiv = document.getElementById('quiz-play-result');
const quizTable = document.getElementById('quiz-play-table');

function checkSelection() {
    let oneChecked = false;

    for (let i = 0; i < 4; i++) {
        let checkBox = document.getElementById(`option-checkbox[${i}]`);

        if (checkBox.checked) {
            oneChecked = true;
            break;
        }
    }

    return oneChecked;
}

const evaluateResponse = function() {
    let responseObject = JSON.parse(this.responseText);

    if (responseObject.success) {
        quizTable.setAttribute('class', `${quizTable.getAttribute('class')} success`);
        resultDiv.innerHTML = "<p>Good answer! Move on to the next quiz!</p>"
    } else {
        quizTable.setAttribute('class', `${quizTable.getAttribute('class')} fail`);
        resultDiv.innerHTML = "<p>Oops, that was not right... :( Don't give up and do the next quiz!</p>"
    }

    nextButton.removeAttribute('disabled');
    nextButton.removeAttribute('class');
}

const submitQuizSolution = function() {
    // Check if at least one option is selected
    if (checkSelection()) {
        // Disable submit button
        submitButton.setAttribute('disabled', '');
        submitButton.setAttribute('class', 'disabled');

        const xhr = new XMLHttpRequest();

        xhr.onload = evaluateResponse;

        let quizid = document.getElementById('quizid').value;
        let answerObject = { answer: [] };

        // Collect selected options
        for (let i = 0; i < 4; i++) {
            let checkBox = document.getElementById(`option-checkbox[${i}]`);

            if (checkBox.checked) {
                answerObject.answer.push(i);
            }
        }

        xhr.open('post', `/api/quizzes/${quizid}/solve`);
        xhr.setRequestHeader("Content-type", "application/json");
        xhr.withCredentials = true;

        xhr.send(JSON.stringify(answerObject));

        // Disable submit button
        submitButton.setAttribute('disabled', '');
        submitButton.setAttribute('class', 'disabled');
    } else {
        console.log("Look, that's a hacker!");
    }
};

// Add event listeners

submitButton.addEventListener('click', submitQuizSolution);
nextButton.addEventListener('click', () => location.reload());

for (let i = 0; i < 4; i++) {
    let checkBox = document.getElementById(`option-checkbox[${i}]`);

    checkBox.addEventListener('click', () => {
        if (checkSelection()) {
            submitButton.removeAttribute('disabled');
            submitButton.removeAttribute('class');
        } else {
            submitButton.setAttribute('disabled', '');
            submitButton.setAttribute('class', 'disabled');
        }
    });
}