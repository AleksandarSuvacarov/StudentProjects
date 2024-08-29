//Nikola Babić 2020/0363
function LogIn() {
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;

    // proverava da li je popunjeno polje za korisnicko ime
    if (username == "") {
      alert("Polje za korisničko ime ne sme da bude prazno!");
      return false;
    }
    // proverava da li je popunjeno polje za lozinku
    if (password == "") {
      alert("Polje za lozinku ne sme da bude prazno!");
      return false;
    }

    if (username == "admin123" && password == "admin123") {
      window.location.href = "./Administrator/index_administrator.html";
    }
    else if (username == "moderator123" && password == "moderator123") {
        window.location.href = "./Moderator/index_moderator.html";
      }
    else if (username == "organizator123" && password == "organizator123") {
      window.location.href = "./Organizator/index_oraganizator.html";
    }
    else if (username == "korisnik123" && password == "korisnik123") {
        window.location.href = "./Registrovani_korisnik/index_registrovani.html";
    }
    else {
      alert("Kredencijali su neispravni!");
      return false;
    }

    return true;
  }