/* Aleksandar Suvačarov 2020/0355 */
/* ================================ JS ZA PRIJAVU EKIPE=================================== */
$(document).ready(function(){

    $(".drzac_prijave").hide()

    $(".prijavi_klasa").click(function (){

        let id = $(this).attr("id")
        id = /\d+/.exec(id)

        $("#drzac_prijave_"+id).toggle()



    })
})