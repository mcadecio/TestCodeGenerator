console.log("Hi There");
document.getElementById("main-form").addEventListener("submit", (e) => {
    let client = document.getElementById("client").value;
    let base = document.getElementById("base").value;
    let startingNumber = document.getElementById("startingNumber").value;
    let passwordLength = document.getElementById("passwordLength").value;
    let quantity = document.getElementById("quantity").value;

    console.log("client == " + client);
    console.log("base == " + base);
    console.log("startingNumber == " + startingNumber);
    console.log("passwordLength == " + passwordLength);
    console.log("quantity == " + quantity);

    fetch(
        "/api/generate/csv?base=" + base
        + "&startingNumber=" + startingNumber
        + "&passwordLength=" + passwordLength
        + "&quantity=" + quantity)
        .then((result) => result.json())
        .then(json => console.log(json))
    e.preventDefault();
})