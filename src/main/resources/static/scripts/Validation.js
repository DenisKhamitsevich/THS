const email = document.getElementById("email");
const password = document.getElementById("password");
const length = document.getElementById("invalid_length");
const letter = document.getElementById("invalid_letter");
const number = document.getElementById("invalid_number");
const capital = document.getElementById("invalid_upper");



//2 section
email.addEventListener("input", function () {
    if (email.validity.typeMismatch) {
        email.setCustomValidity("Введите правильный адрес электронной почты!");
    } else {
        email.setCustomValidity("");
    }

});



password.onfocus = function() {
    document.getElementById("invalid_password_message").style.display = "block";
}




password.onkeyup = function() {
    const lowerCaseLetters = /[a-z]/g;
    const upperCaseLetters = /[A-Z]/g;
    const numbers = /[0-9]/g;
    let cases=[lowerCaseLetters,upperCaseLetters,numbers];
    let wValues=["w_letter_set","w_upper_set","w_number_set"];
    let rValues=["r_letter_set","r_upper_set","r_number_set"];
    let elements=[letter,capital,number];
    let prev="",next="",wtemp="",rtemp="";
    for (let i = 0; i <3 ; i++) {
        wtemp = document.getElementById(wValues[i]);
        rtemp = document.getElementById(rValues[i]);
        if(password.value.match(cases[i])) {
            elements[i].classList.remove("invalid");
            elements[i].classList.add("valid");
            prev="wrong_set";
            next="right_set";
        } else {
            elements[i].classList.remove("valid");
            elements[i].classList.add("invalid");
            prev="right_set";
            next="wrong_set";
        }
        wtemp.classList.remove(prev);
        wtemp.classList.add(next);
        rtemp.classList.remove(next);
        rtemp.classList.add(prev);
    }

    // Validate length
    wtemp = document.getElementById("w_length_set");
    rtemp = document.getElementById("r_length_set");
    if((password.value.length >= 8)&&(password.value.length<=16)) {
        length.classList.remove("invalid");
        length.classList.add("valid");
        prev="wrong_set";
        next="right_set";
    } else {
        length.classList.remove("valid");
        length.classList.add("invalid");
        prev="right_set";
        next="wrong_set";
    }
    wtemp.classList.remove(prev);
    wtemp.classList.add(next);
    rtemp.classList.remove(next);
    rtemp.classList.add(prev);

}