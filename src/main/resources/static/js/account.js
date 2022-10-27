// Functions for the play page

const usernameField = document.getElementById('username');
const emailField = document.getElementById('email');
const passwordField = document.getElementById('password');
const newPasswordField = document.getElementById('new_password');
const newPassword2Field = document.getElementById('new_password2');
const updateButton = document.getElementById('update');
const removeAccountButton = document.getElementById('delete');
const resultDiv = document.getElementById('update-result');

const evaluateUpdateResponse = function() {
    let responseObject = JSON.parse(this.responseText);
    let success = true;
    let message = "";

    if (this.status != 200) {
        success = false;

        if (responseObject.errors != undefined) {
            responseObject.errors.forEach((e) => { message += `The validation of '${e.field}' field was unsuccessful: ${e.defaultMessage}<br />`; });
        } else {
            message = responseObject.message;
        }
    }

    if (success) {
        resultDiv.setAttribute('class', `success-notification-box`);
        resultDiv.innerHTML = '<p>Update was successful!</p>';
    } else {
        resultDiv.setAttribute('class', `error-notification-box`);
        resultDiv.innerHTML = `<p>The following errors were reported by the server during the update process:<br /><br />${message}</p>`;
    }
}

// Update account
const updateAccount = function() {

    if (newPasswordField.value != newPassword2Field.value) {
        alert("New passwords must be the same!");
    } else {

        const xhr = new XMLHttpRequest();

        xhr.onload = evaluateUpdateResponse;

        let data = new FormData();
        data.append("email", emailField.value);
        if (newPasswordField.value.length != 0) { data.append("newpassword", newPasswordField.value); }
        data.append("password", passwordField.value);

        xhr.open('put', `/api/user/update`);

        xhr.send(data);
    }
};

// Remove account
const removeAccount = function() {

    if (confirm("Danger!\n\nYou are about to remove your account.\nThis action will also delete all your previous statistics but your previously submitted quizzes will still be available.\n\nAre you sure to remove your account?")) {

        const xhr = new XMLHttpRequest();

        xhr.onload = function () {
            let responseObject = JSON.parse(this.responseText);

            if (this.status == 200) {
                alert("Your account was successfully removed!\n\nYou will be navigated back to the login page.");
                location.assign("/login?logout");
            } else {
                alert("Failed to remove account.\n\nThe server responded:\n\n" + responseObject.message);
            }
        };

        let data = new FormData();
        data.append("password", passwordField.value);

        xhr.open('delete', `/api/user/delete`);

        xhr.send(data);
    }
};


// Add event listeners

updateButton.addEventListener('click', updateAccount);
removeAccountButton.addEventListener('click', removeAccount);