<template>
  <div class="row totalni_omotac">

    <h1> {{$t("O nama")}} </h1>

    <div class="col-lg-7 col-12 drzac_teksta_levi order-lg-1 order-1">
        <h2> {{$t("opis_galerija_levi_naslov")}} </h2>
        <br>
        {{$t("opis_galerija_levi_1")}}
        <br>
        <br>
        {{$t("opis_galerija_levi_2")}}<br>
        <br>
        {{$t("opis_galerija_levi_3")}}<br>
        <br>
        {{$t("opis_galerija_levi_4")}}<br>
        <br>
        {{$t("opis_galerija_levi_5")}}<br>
        <br>
        {{$t("opis_galerija_levi_6")}}<br>
        <br>
        <br>
    </div>

    <div class="row col-lg-5 col-12 drzac_slike_desni order-lg-2 order-2">
        <img src="/images/galerija_1.jpeg" alt="">
    </div>

    <div class="row col-lg-5 col-12 drzac_slike_levi order-lg-3 order-4">
        <img src="/images/galerija_2.png" alt="">
    </div>

    <div class="col-lg-7 col-12 drzac_teksta_desni order-lg-4 order-3">
        <h2> {{$t("opis_galerija_desni_naslov")}} </h2>
        {{$t("opis_galerija_desni_1")}}
        <br>
        <br>
        {{$t("opis_galerija_desni_2")}}
        <br>
        <br>
        {{$t("opis_galerija_desni_3")}}
        <br>
        <br>
        {{$t("opis_galerija_desni_4")}}
        <br>
        <br>
    </div>


    <div class="row order-5 drzac_kontakta_i_mape" >
        <div class="col-lg-7 col-12 drzac_kontakta">
            
            <h2 style="padding-top: 5%; margin-bottom: 4%">  {{$t("kontaktirajte_nas")}}  </h2>
            <div class="contact-info">
                <div class="contact-item">
                    <i class="fas fa-name"><i> {{$t("kontakt_naziv")}} </i></i>
                    <p>Mystique {{$t("Galerija")}} </p>
                </div>
                <div class="contact-item">
                    <i class="fas fa-map-marker-alt"></i>
                    <p> {{$t("kontakt_adresa")}} </p>
                </div>
                <div class="contact-item">
                    <i class="fas fa-phone"></i>
                    <p> {{$t("kontakt_telefon")}} </p>
                </div>
                <div class="contact-item">
                    <i class="fas fa-envelope"></i>
                    <p> {{$t("kontakt_email")}} </p>
                </div>
            </div>
            <div class="row sveSlike">
                <div class="col-3 slikeDr"><img class="slikice" src="../assets/ig.png" alt=""></div>
                <div class="col-3 slikeDr"><img class="slikice" src="../assets/fb.png" alt=""></div>
                <div class="col-3 slikeDr"><img class="slikice" src="../assets/tw.png" alt=""></div>

            </div>

        </div>

        <div class="drzac_mape col-lg-5 col-12">
            <h2> {{$t("kontakt_mapa")}} </h2>
            <div class="map-container">
                <l-map :zoom="zoom" :center="center">
                    <l-tile-layer :url="tileLayerUrl"></l-tile-layer>
                    <l-marker :lat-lng="markerLatLng">
                        <l-popup>
                            <b>Marker</b><br />
                        </l-popup>
                    </l-marker>
                </l-map>
            </div>
        </div>

        
        
    </div>



  </div>
</template>


<style lang='less' scoped>

@import "../globalniStilovi.less";

.sveSlike{
    margin-top: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.slikeDr{
    width: 6vw;
}

.slikice{
    width: 60%;
}

#app {
  height: 400px;
}

.map-container {
    width: 500px;
    height: 500px;
    margin: 0 auto;
    overflow: hidden;
    border: solid @crvena;
    
}

.totalni_omotac{
    background-color: @bg;
    padding-left: 1%;
    color: @fontBraon;
    font-family: @font;
}

.drzac_teksta_levi{
    display: flex;
    flex-direction: column;
    text-align: left;
    vertical-align: center;
    padding: 5%;
    background-color: @roze;
    border-radius: 5%;
}

h1, h2{
    font-family: @naslovFont;
}

.drzac_teksta_desni{
    text-align: right;
    padding: 5%;
    background-color: @roze;
    border-radius: 5%;
}

.drzac_slike_desni{
    display: flex;
    align-items: center;
}

.drzac_slike_desni img{
    width: 80%;
    margin: auto;
    padding-top: 2%;
    padding-bottom: 2%;
    border-radius: 10%;
}

.drzac_slike_levi img{
    width: 60%;
    margin: auto;
    padding-top: 2%;
    padding-bottom: 2%;
    border-radius: 10%;
    
}

.drzac_kontakta{
    background-color: @roze;
    padding-bottom: 3%;
    border-radius: 5%;
}

.drzac_mape{
    margin-top: 1%;
    margin-bottom: 2%;
}

h1{
    margin-bottom: 4%;
}



</style>


<script>
// Uvoz potrebnih komponenti
import { LMap, LTileLayer, LMarker, LPopup } from 'vue3-leaflet';

export default {
    name: 'ONamaView',
    components: {
        LMap,
        LTileLayer,
        LMarker,
        LPopup,
    },
    data() {
        return {
            zoom: 13,
            center: [44.79852511073951, 20.455275250899668],
            tileLayerUrl: 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
            markerLatLng: [44.79852511073951, 20.455275250899668],
        };
    },
    created(){
        document.title = "Mystique Galirija - O nama"
    }
};
</script>
