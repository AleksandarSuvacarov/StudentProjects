// Nikola Babić 2020/0363


// Funkcionalnost prikazivanja i sakrivanja popup ekrana
$(document).ready(function() {
    $(".popup").hide();

    $(".prikazi_ekipu").click(function() {
        var url = $(this).data("prikaz-prijava");
        window.location.href = url;
    });

    if (window.location.href.includes("/kvizovi/prikaz_ekipe/")){
        $(".popup").show();
    }

    $(".popup").click(function (e){
        var popupContent = $(".popup_content")
            if (!popupContent.is(e.target) && popupContent.has(e.target).length === 0){
                $(".popup").hide();
                var url = $(this).data("povratak");
                window.location.href = url;
            }

    })
});