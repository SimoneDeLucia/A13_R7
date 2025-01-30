// Funzione per impostare un cookie
function setCookie(name, value, time) {
    const d = new Date();
    d.setTime(d.getTime() + time);
    const expires = "expires=" + d.toUTCString();
    document.cookie = name + "=" + value + ";" + expires + ";path=/";
}

const getCookie = (name) => {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(";").shift();
    return null;
};

const parseJwt = (token) => {
    try {
        return JSON.parse(atob(token.split(".")[1]));
    } catch (e) {
        return null;
    }
};

function insertStringInUsernameElement(username) {
    // Trova l'elemento con ID "username"
    var usernameElement = document.getElementById("Username");

    // Verifica se l'elemento esiste
    if (usernameElement) {
        // Inserisce la stringa nell'elemento
        usernameElement.textContent = username.split("@")[0];
    } else {
        console.log("L'elemento con id 'username' non esiste.");
    }
}

const jwtData = parseJwt(getCookie("jwt"));
const username = jwtData.sub;
console.log(username);
const userId = jwtData.userId;
console.log(userId);
insertStringInUsernameElement(username);