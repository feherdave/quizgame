// Functions for the play page

const usernameField = document.getElementById('username');
const emailField = document.getElementById('email');
const passwordField = document.getElementById('password');
const password2Field = document.getElementById('password2');
const submitButton = document.getElementById('register');
const resultDiv = document.getElementById('registration-result');

const evaluateResponse = function() {
    let responseObject = JSON.parse(this.responseText);
    let success = true;
    let message = "";

    if (this.status == 400) {
        success = false;

        if (responseObject.errors != undefined) {
            responseObject.errors.forEach((e) => { message += `The validation of '${e.field}' field was unsuccessful: ${e.defaultMessage}<br />`; });
        } else {
            message = responseObject.message;
        }
    }

    if (success) {
        resultDiv.setAttribute('class', `success-notification-box`);
        resultDiv.innerHTML = '<p>Registration was successful! Please go to the <a href="/login">login</a> page to start!</p>';
    } else {
        resultDiv.setAttribute('class', `error-notification-box`);
        resultDiv.innerHTML = `<p>The following errors were reported by the server during the registration process:<br /><br />${message}</p>`;
    }
}

const submitRegistration = function() {

    if (passwordField.value != password2Field.value) {
        alert("Passwords must be the same!");
    } else {

        const xhr = new XMLHttpRequest();

        xhr.onload = evaluateResponse;

        let requestObject = {
            username: usernameField.value,
            password: passwordField.value,
            email: emailField.value
        };

        xhr.open('post', '/api/register');
        xhr.setRequestHeader("Content-type", "application/json");

        xhr.send(JSON.stringify(requestObject));
    }
};

// Add event listeners

submitButton.addEventListener('click', submitRegistration);