document.getElementById("submit-form").addEventListener("click", (e) => {
    let allocatedTo = document.getElementById("allocatedTo").value;
    let loginBase = document.getElementById("loginBase").value;
    let loginStartingNumber = document.getElementById("loginStartingNumber").value;
    let passwordLength = document.getElementById("passwordLength").value;
    let quantity = document.getElementById("quantity").value;

    console.log("allocatedTo == " + allocatedTo);
    console.log("loginBase == " + loginBase);
    console.log("loginStartingNumber == " + loginStartingNumber);
    console.log("passwordLength == " + passwordLength);
    console.log("quantity == " + quantity);
    let url = "/api/generate/simple/csv?loginBase=" + loginBase
        + "&loginStartingNumber=" + loginStartingNumber
        + "&passwordLength=" + passwordLength
        + "&quantity=" + quantity
        + "&allocatedTo=" + allocatedTo;
    window.location.replace(url);

    e.preventDefault();

    document.getElementById("submit-form").reset();
});

document.getElementById('side-bar').addEventListener('click', function () {
    document.getElementById('side-bar-activate').click();
})