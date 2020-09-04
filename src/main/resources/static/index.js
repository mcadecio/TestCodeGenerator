function submitForm(e) {
    e.preventDefault();

    let allocatedTo = document.getElementById("allocatedTo").value;
    let loginBase = document.getElementById("loginBase").value;
    let loginStartingNumber = document.getElementById("loginStartingNumber").value;
    let passwordLength = document.getElementById("passwordLength").value;
    let quantity = document.getElementById("quantity").value;

    let url = "/api/generate/simple/csv?loginBase=" + loginBase
        + "&loginStartingNumber=" + loginStartingNumber
        + "&passwordLength=" + passwordLength
        + "&quantity=" + quantity
        + "&allocatedTo=" + allocatedTo;
    window.location.replace(url);

    document.querySelector('form').reset();
}

document.getElementById('side-bar').addEventListener('click', function () {
    document.getElementById('side-bar-activate').click();
})