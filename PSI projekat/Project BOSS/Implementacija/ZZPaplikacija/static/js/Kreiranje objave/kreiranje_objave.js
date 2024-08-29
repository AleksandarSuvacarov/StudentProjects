/* Aleksandar Suvačarov 2020/0355 */
/* ================================ JS ZA KREIRANJE OBJAVE=================================== */
$(document).ready(function(){

    document.getElementById("objava_forma").addEventListener("submit", function(event) {
        event.preventDefault(); // Sprječava osvježavanje stranice prilikom slanja forme
        
        // Provjera valjanosti forme
        if (validateForm()) {
            // Ako je forma valjana, izvrši AJAX zahtjev
            sendFormData();
        }
    });
        
    function validateForm() {

        var regex = /^[0-9]+$/;
        
        kapacitet = $("#kapacitet_kviza").val();
        adresa = $("#adresa_kviza").val();
        naziv = $("#naziv_kviza").val();
        opis = $("#opis_kviza").val();
        kotizacija = $("#kotizacija_kviza").val();
        datum_vreme = $("#datum_vreme_kviza").val();
        slika = $("#slika_kviza").val();



        //$(".poruka").text(slika)

        if(slika == false || datum_vreme == "" || naziv == "" ||
            kapacitet == "" || kotizacija == "" || adresa == "" || opis == ""){
           
            $(".poruka").text("Pogresno popunjena forma. Morate popuniti sva polja")
            return false;
        }
        

        if(regex.test(kapacitet) == false){
            $(".poruka").text("Pogresno popunjena forma. Polje kapacitet moze sadrzati samo brojeve")
            return false;
        }

        if(regex.test(kotizacija) == false){
            $(".poruka").text("Pogresno popunjena forma. Polje kotizacija moze sadrzati samo brojeve")
            return false;
        }


        $(".poruka").text("")
        return true;
    }
    
    
    function sendFormData() {
        var xhr = new XMLHttpRequest();
        var form = document.getElementById("objava_forma");
        var formData = new FormData(form);
        
        xhr.open("POST", "http://127.0.0.1:8000/kreiranje_objave/"); // Zamijenite "your-endpoint" s pravim Django endpointom
        
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                // Uspješno obrađen odgovor od servera
                //console.log(xhr.responseText);
                window.location.href="http://127.0.0.1:8000/kvizovi/"
            } else {
                // Greška prilikom slanja zahtjeva na server
                console.error(xhr.status);
            }
            }
        };
        
        xhr.send(formData);
    }
})
