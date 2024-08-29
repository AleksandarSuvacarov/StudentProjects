//Jovana Simić 2020/0360
//Andrija Ognjanovic 2020/0261
$(document).ready(function() {

    $("#poljaOrg").hide();
    $("#zaRecenzije").hide();
    $("#sl1").hide();
    $("#pivo2").hide();
    $("#pivo3").hide();
    $("#pivo4").hide();
    $("#pivo5").hide();
    $("#nevidljiv").hide().val(1);



    //dodavanje dodatnih polja za organizatora da se generise odgovarajuca stranica
    $('input[type=radio][name=vrstaKorisnika]').change(function() {
        //alert(this.value);

        if (this.value == 'korisnikNov') {
            $("#poljaOrg").hide();
        }
        else {
            //ovde dodajemo polja ako smo organizator
            $("#poljaOrg").show();
        }

    });

    //dinamicko prikazivanje polja za recenziju i ocene
    $("#ostaviRec").click(function () {
        $("#zaRecenzije").toggle();
    });

    $("#okaciRec").click(function () {
        $("#zaRecenzije").hide();
    });

    $("#1").click(function () {
        $("#sl1").hide();
        $("#sl2").show();
        $("#sl3").show();
        $("#sl4").show();
        $("#sl5").show();
        $("#pivo1").show();
        $("#pivo5").hide();
        $("#pivo4").hide();
        $("#pivo3").hide();
        $("#pivo2").hide();
        $("#nevidljiv").val(1);
    });

    $("#2").click(function () {
        $("#sl1").hide();
        $("#sl2").hide();
        $("#sl3").show();
        $("#sl4").show();
        $("#sl5").show();
        $("#pivo1").show();
        $("#pivo5").hide();
        $("#pivo4").hide();
        $("#pivo3").hide();
        $("#pivo2").show();
        $("#nevidljiv").val(2);
    });

    $("#3").click(function () {
        $("#sl1").hide();
        $("#sl2").hide();
        $("#sl3").hide();
        $("#sl4").show();
        $("#sl5").show();
        $("#pivo1").show();
        $("#pivo5").hide();
        $("#pivo4").hide();
        $("#pivo3").show();
        $("#pivo2").show();
        $("#nevidljiv").val(3);
    });

    $("#4").click(function () {
        $("#sl1").hide();
        $("#sl2").hide();
        $("#sl3").hide();
        $("#sl4").hide();
        $("#sl5").show();
        $("#pivo1").show();
        $("#pivo5").hide();
        $("#pivo4").show();
        $("#pivo3").show();
        $("#pivo2").show();
        $("#nevidljiv").val(4);
    });

    $("#5").click(function () {
        $("#sl1").hide();
        $("#sl2").hide();
        $("#sl3").hide();
        $("#sl4").hide();
        $("#sl5").hide();
        $("#pivo1").show();
        $("#pivo5").show();
        $("#pivo4").show();
        $("#pivo3").show();
        $("#pivo2").show();
        $("#nevidljiv").val(5);
    });






});